package com.vraj.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.vraj.library.exceptions.UserNotFoundException;
import com.vraj.library.model.AuthRequest;
import com.vraj.library.util.JwtUtil;
import com.vraj.library.vo.LoginResponseVo;

import io.jsonwebtoken.lang.Collections;

@Service
public class UserAuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public String generateToken(String username) {
			return jwtUtil.generateToken(username);
	}
	
	public LoginResponseVo authenticate(AuthRequest authRequest) {
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			String token = jwtUtil.generateToken(authRequest.getUsername());
			return LoginResponseVo.builder()
					.username(auth.getName())
					.token(token)
					.role(auth.getAuthorities().stream().findFirst().get().getAuthority())
					.build();
		}catch(Exception e) {
			throw new UserNotFoundException();
		}
	}
}
