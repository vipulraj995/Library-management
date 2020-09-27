package com.vraj.library.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.vraj.library.entity.User;
import com.vraj.library.entity.UserRole;
import com.vraj.library.vo.UserVo;

@Service
public class ProfileMapper {

	private static final String EMPLOYEE = "EMPLOYEE";
	private static final String EARN_LEARN = "EARN & LEARN";
	
	public List<UserVo> map(List<User> users, String username) {
		return users.stream()
				.filter(user -> !user.getUsername().equals(username))
				.map(user -> mapUser(user))
				.collect(Collectors.toList());
	}

	private UserVo mapUser(User user) {
		return UserVo.builder()
				.libraryId(user.getLibraryId())
				.scardId(user.getScardId())
				.username(user.getUsername())
				.firstname(user.getFirstname())
				.lastname(user.getLastname())
				.phone(user.getPhone())
				.college(user.getCollege())
				.active(user.isActive())
				.roles(mapRole(user.getRoles()))
				.build();
	}

	private String mapRole(String roles) {
		return UserRole.ROLE_ADMIN.name().equals(roles) ? EMPLOYEE : EARN_LEARN;
	}
}
