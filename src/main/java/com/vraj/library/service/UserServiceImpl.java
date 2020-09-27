package com.vraj.library.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.vraj.library.dao.UserDaoImpl;
import com.vraj.library.entity.User;
import com.vraj.library.entity.UserRole;
import com.vraj.library.exceptions.UserAlreadyPresentException;
import com.vraj.library.exceptions.UserNotFoundException;
import com.vraj.library.model.AuthRequest;
import com.vraj.library.util.JwtUtil;
import com.vraj.library.vo.LoginResponseVo;

import lombok.SneakyThrows;

@Service
public class UserServiceImpl {

	@Autowired
	private UserDaoImpl userDao;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserAuthService userAuthService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@SneakyThrows
	public User save(User user, String role) {
		if (userDao.findByUsername(user.getUsername()).isPresent()) {
			throw new UserAlreadyPresentException();
		}
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		if(Enum.valueOf(UserRole.class, role) != null) {
			user.setRoles(UserRole.valueOf(role).toString());	
		}
		return userDao.save(user);
	}

	public void update(User user, String authToken) {
		String username = null;
		if (authToken != null && authToken.startsWith("Bearer ")) {
			String token = authToken.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		if(!username.equals(user.getUsername())) {
			throw new UserNotFoundException("The profile you want to update does not belong to you.");
		}
		Optional<User> userOptional = userDao.findByUsername(user.getUsername());
		if(!userOptional.isPresent()) {	
			throw new UserNotFoundException("The profile you want to update does not belong to you.");
		}
		User user1 = userOptional.get();
		if(!StringUtils.isEmpty(user.getFirstname()) && !user.getFirstname().matches("\\s*")) {
			user1.setFirstname(user.getFirstname());
		}
		if(!StringUtils.isEmpty(user.getLastname()) && !user.getLastname().matches("\\s*")) {
			user1.setLastname(user.getLastname());
		}
		if(!StringUtils.isEmpty(user.getPhone()) && !user.getPhone().matches("\\s*")) {
			user1.setPhone(user.getPhone());
		}
		userDao.save(user1);
	}

	public User getDetails(String authToken) {
		String username = null;
		if (authToken != null && authToken.startsWith("Bearer ")) {
			String token = authToken.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		Optional<User> userOptional =  userDao.findByUsername(username);
		if(!userOptional.isPresent()) {	
			throw new UserNotFoundException(username+" not found");
		}
//		Object[] values = userRepo.getDetails(username).get(0);
//		String sdf =(String) values[0];
//		Arrays.stream(values).forEach(System.out::println);
		return User.builder()
				.libraryId(userOptional.get().getLibraryId())
				.scardId(userOptional.get().getScardId())
				.username(userOptional.get().getUsername())
				.firstname(userOptional.get().getFirstname())
				.lastname(userOptional.get().getLastname())
				.phone(userOptional.get().getPhone())
				.college(userOptional.get().getCollege())
				.build();
	}
	
	public void changePassword(Map<String, String> updates, String authToken) {
		String username = null;
		if (authToken != null && authToken.startsWith("Bearer ")) {
			String token = authToken.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		AuthRequest user = AuthRequest.builder().username(username).password(updates.get("current password")).build();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		}catch(Exception e) {
			throw new UserNotFoundException();
		}
		userDao.updatePassword(bcryptEncoder.encode(updates.get("new password")), username);
	}
}
