package com.zensar.olx.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zensar.olx.filter.JwtAuthenticationFilter;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//UserDetailsService is an interface given by spring security\
	//This interface has only 1 method loadUserByUserName(String userName)
	//The above method is responsible for loading the user object from DB
	//If user object not found in DB this method should throw UserNameNotFoundException
	//It is responsibility of developer to give implementation to this interface
	
	@Autowired
	private UserDetailsService userDetailsService;// interface by spring security

//http status code 401 is the standard http code which specifies that user is not passing right user name and password..

	// Authentication-->user name & password
	// Prove whatever user is claiming
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth

				/*
				 * .inMemoryAuthentication()//We are storing credentials in memory -->we
				 * actually have to fatchcfrom DB .withUser("zensar")//userName
				 * .password("$2a$10$A9gVtuNsDpFM3LnCd0SKv.7G/0cKN61BLWHjmuqw61jRXX9PdX/m2")//No
				 * operation{noop}-->password--removing {noop} to make it password
				 * encrypted//password here should be in encoded form //This is bad to stored
				 * password in plain test //we MUST store password encoded form
				 * //BCryptPasswordEncoder is recommended for password encoding .roles("USER")
				 * //role .and()
				 */
				.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());// this line tells spring
																								// security to use
																								// BCryptPasswordEncoder

	}

	// What are you allowed to do?
	// To use PC,Chair-->At office level
	// Authorization->Specifying access rights to resource
	// Access based on Roles(Authorization access with restriction

	// http status code 403(forbidden) specifies user is authenticated but not
	// authorized to access this resource
	
	//Authentication
	//access based on roles
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers("/user/authenticate","/token/validate")
		.permitAll()//This url must be public so that user can login(permitAll is public)
		.antMatchers(HttpMethod.OPTIONS, "/**")
		.permitAll().anyRequest()
		.authenticated().and()
		.addFilter(new JwtAuthenticationFilter(authenticationManager()))
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);// Ask to enter user name and password using prompt
		// any request should be authenticated and should use httpBasic
	}
	@Override
	@Bean
		protected AuthenticationManager authenticationManager() throws Exception {
			// TODO Auto-generated method stub
			return super.authenticationManager();
		}
	// Following bean is used for password encording
	@Bean // -->object we are returning as bean(method)--same as @component(class)
	public PasswordEncoder getPasswordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}

}
