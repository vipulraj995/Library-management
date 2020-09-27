package com.vraj.library.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vraj.library.entity.User;
import com.vraj.library.repository.UserRepository;

import lombok.SneakyThrows;

@Component
@Transactional
public class UserDaoImpl {

	@Autowired
	private UserRepository userRepository;
	
	@SneakyThrows
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@SneakyThrows
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@SneakyThrows
	public long deleteByUsername(String username) {
		return userRepository.deleteByUsername(username);
	}
	
	@SneakyThrows
	public List<User> findAll() {
		return userRepository.findAll();
	}

	public List<User> findByActiveStatus() {
		return userRepository.findByActive();
	}

	public void updatePassword(String newPassword, String username) {
		userRepository.updatePassword(newPassword, username);
	}

}
