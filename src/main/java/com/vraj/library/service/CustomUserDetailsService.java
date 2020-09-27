package com.vraj.library.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vraj.library.entity.User;
import com.vraj.library.exceptions.UserNotFoundException;
import com.vraj.library.model.CustomUserDetails;
import com.vraj.library.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Optional<User> userOptional = userRepo.findByUsername(username);
		userOptional.orElseThrow(() -> new UserNotFoundException());
		System.out.println(userOptional.get());
		return userOptional.map(CustomUserDetails::new).get();
	}

}
