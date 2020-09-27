package com.vraj.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vraj.library.dao.UserDaoImpl;
import com.vraj.library.entity.User;
import com.vraj.library.exceptions.UserNotFoundException;
import com.vraj.library.mapper.ProfileMapper;
import com.vraj.library.model.AuthRequest;
import com.vraj.library.util.JwtUtil;
import com.vraj.library.vo.UserVo;

@Service
public class AdminServiceImpl {

	@Autowired
	private UserDaoImpl userDao;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserAuthService userAuthService;
	 
	@Autowired
	private ProfileMapper profileMapper;
	
	public long deleteEmployee(AuthRequest user, String usernameToDelete, String authToken) {
		String username = null;
		if (authToken != null && authToken.startsWith("Bearer ")) {
			String token = authToken.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		if(!username.equals(user.getUsername())) {
			throw new UserNotFoundException("The profile which you are currently using does not belong to you.");
		}
		userAuthService.authenticate(user);
		Optional<User> userOptional =  userDao.findByUsername(usernameToDelete);
		if(!userOptional.isPresent()) {	
			throw new UserNotFoundException(usernameToDelete+" not found");
		}
		return userDao.deleteByUsername(usernameToDelete);
	}

	public List<UserVo> getAllDetails(String authToken) {
		String token = extractTokenFromAuthHeader(authToken);
		String username = jwtUtil.extractUsername(token);
		List<User> users = userDao.findAll();
		return profileMapper.map(users,username);
	}

	public List<UserVo> getDetailsBasedOnActiveStatus(String authToken) {
		String token = extractTokenFromAuthHeader(authToken);
		String username = jwtUtil.extractUsername(token);
		List<User> users = userDao.findByActiveStatus();
		return profileMapper.map(users, username);
	}

	public void verifyEmployee(User user) {
		Optional<User> userOptional = userDao.findByUsername(user.getUsername());
		if(!userOptional.isPresent()) {	
			throw new UserNotFoundException(user.getUsername()+" not found");
		}
		if(!user.isActive()) {
			userDao.deleteByUsername(user.getUsername());
		}
		else {
			User user1 = userOptional.get();
			user1.setActive(user.isActive());
			userDao.save(user1);
		}
	}
	
	private String extractTokenFromAuthHeader(String authToken) {
		if (authToken != null && authToken.startsWith("Bearer ")) {
			String token = authToken.substring(7);
			return token;
		}
		return null;
	}
}
