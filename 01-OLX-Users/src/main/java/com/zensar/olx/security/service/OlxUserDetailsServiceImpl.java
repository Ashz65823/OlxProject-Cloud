package com.zensar.olx.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zensar.olx.DB.OlxUserDAO;
import com.zensar.olx.bean.OlxUser;



@Service
public class OlxUserDetailsServiceImpl implements UserDetailsService {

	@Autowired // Dependence injection
	private OlxUserDAO repo;

	@Override
	// UserDetails is an interface given by spring security import line 3
	// We are free to implement the interface but for simplicity Spring security has
	// given a class
	// Which implements user detail interface
	// Name of the class is User
	// So in this method we need to create object of User and return it
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Talk to DB and fetch UserDetails
		System.out.println("-------------------------------------");
		OlxUser foundUser=repo.findByUserName(username);//findBy is the conversion made by springtool
		if(foundUser==null)
		{
			throw new UsernameNotFoundException(username);//if username not found exception 
		}
		System.out.println("In loadUserByUsername");
		System.out.println("User logged in successfully");
		/*if (username.equals("zensar")) {*/
		
		String roles=foundUser.getRoles();
			User authenticatedUser = new User(foundUser.getUserName(),foundUser.getPassword(),
					AuthorityUtils.createAuthorityList(roles));

		return authenticatedUser;
		//}
		//throw new UsernameNotFoundException(username);
	}

}
