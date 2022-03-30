package com.zensar.olx.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zensar.olx.DB.TokenStorage;
import com.zensar.olx.bean.LoginResponse;
import com.zensar.olx.bean.LoginUser;
import com.zensar.olx.bean.OlxUser;
import com.zensar.olx.service.OlxUserService;
import com.zensar.olx.util.JwtUtil;

@RefreshScope
@RestController
public class OlxUserController {

	@Autowired
	OlxUserService service;
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private JwtUtil util;
	

	/**
	 * 1 This is the rest specification for authenticating token for user details
	 * 
	 * @param user
	 * @return
	 */
	//1 Logins a user with authetication
	@PostMapping("/user/authenticate")
	public LoginResponse login(@RequestBody LoginUser user) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
		try {
			Authentication authentication=this.manager.authenticate(authenticationToken);
			// User is authenticated successfully then generate the token and retuen it back
			String token = util.generateToken(user.getUserName());
			LoginResponse loginResponse = new LoginResponse();
			loginResponse.setJwt(token);
			TokenStorage.storeToken(token, token);
			
			return loginResponse;
		} catch (Exception e) {
			// Authentication is failed

			e.printStackTrace();
			throw e;
		}
	}

	// 3 Registers an user
	@PostMapping("/user")
	public OlxUser addOlxUser(@RequestBody OlxUser olxUser)// this is request to post
	{
		return this.service.addOlxUse(olxUser);
	}

	//4 Return user information based on user id
	@GetMapping("/user/{uid}")
	public OlxUser findOlxUserById(@PathVariable(name = "uid") int id)// this is to get value from uid
	{
		return this.service.findOlxUser(id);
	}

	//5 Find use bases on user name
	@GetMapping("/user/find/{userName}")
	public OlxUser finOlxUserByName(@PathVariable(name = "userName") String name) {
		return this.service.findOlxUserByName(name);
	}
	
	
	//Validating token 5
	@GetMapping("token/validate")
	public ResponseEntity<Boolean> isValidateToken(@RequestHeader("Authorization") String authenToken )
	{
		try {
			String validateToken=util.validateToken(authenToken.substring(7));
			System.out.println("Valid user ");
			return new ResponseEntity<>(true,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		}
	}
	
	//2 Logout 
	@DeleteMapping("user/logout")
	public ResponseEntity<Boolean>Logout(@RequestHeader("Authorization") String authenToken)
	{
		
		String validateToken=authenToken.substring(7);
		try {
			//Delete the storage token from cache
			System.out.println("Logged out");
			TokenStorage.removeToken(validateToken);
			ResponseEntity<Boolean>responseEntity=new ResponseEntity<Boolean>(true,HttpStatus.OK);
			return responseEntity;
		}catch (Exception e) {
			ResponseEntity<Boolean> responseEntity=new ResponseEntity<Boolean>(false,HttpStatus.BAD_REQUEST);
		return responseEntity;
		}
	}
}
