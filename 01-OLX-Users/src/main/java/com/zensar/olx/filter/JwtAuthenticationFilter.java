package com.zensar.olx.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

//Spring MVC,REST internally use servlet API
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.zensar.olx.DB.TokenStorage;
import com.zensar.olx.util.JwtUtil;




public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
	// Authorization is predefined HTTP header
	private String authorizationHeader = "Authorization";
	private final String BEARER = "Bearer ";

	/*
	 * This is custom filter You will need to add this filter in spring security
	 * filter chain otherwise it is not excuted
	 */
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		/*
		 * 1 Check if user have passed token, we do that by fetching value from
		 * authorization header 2 if token not present ask use to login 3 if token
		 * present fetch it and validate it
		 */
		JwtUtil jwtutil = new JwtUtil();
		// 1
		System.out.println("------------------IN doFilterInternal------------------------------------------");
		// Bearer- Is a predefined token value,this is predefined
		String authorizationHeaderValue = request.getHeader(authorizationHeader);

		/*
		 * If Token is not passed or if it does not start with bearer Don't do anything
		 * proceed to next filter in chain
		 */

		// 2 token not found
		if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(BEARER)) {
			chain.doFilter(request, response);
			return;
		}

		//Following Statement removes "Bearer " from token so that we can only get token without the space next to barer
			String token = authorizationHeaderValue.substring(7).trim();
			System.out.println("token="+token);
			
			//check if this token exist in cache
			String tokenExiste=TokenStorage.getCache(token);
			//If token exists  
			if (tokenExiste == null) {
				// Authorization Bearer token
				chain.doFilter(request, response);
				return ;
			}

			//3 if token is valid
			try {
				//when the token is valid
				String encodedPayload = jwtutil.validateToken(token);
				// token is valid
				String payload = new String(Base64.getDecoder().decode(encodedPayload));

				//If token is valid-->try 
				//From this payload we need to fetch username
				JsonParser jsonParser=JsonParserFactory.getJsonParser();
				Map<String,Object> parseMap=jsonParser.parseMap(payload);
				String username=(String) parseMap.get("username");
				//Create userNamePasswordAuthenticationToken 
				UsernamePasswordAuthenticationToken authenticateToken;
				authenticateToken=new UsernamePasswordAuthenticationToken(username, null,
						AuthorityUtils.createAuthorityList("ROLE_USER") );
				
				// AuthenticateUser
				SecurityContextHolder.getContext().setAuthentication(authenticateToken);
			} catch (Exception e) {

				e.printStackTrace();
			}
		chain.doFilter(request, response);
	}

}
