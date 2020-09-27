package com.vraj.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vraj.library.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	static final String FIELDS = "SELECT LIBRARY_ID, SCARD_ID, USERNAME, FIRSTNAME, LASTNAME, PHONE, COLLEGE FROM USER_TBL WHERE USERNAME=?1";
	static final String INACTIVE_STATUS = "SELECT * FROM USER_TBL WHERE ACTIVE = FALSE";
	static final String UPDATE_PASSWORD = "UPDATE USER_TBL SET PASSWORD = ?1 WHERE USERNAME = ?2";
	
	Optional<User> findByUsername(String username);
	long deleteByUsername(String username);
	@Query(value = FIELDS, nativeQuery = true)
	List<Object[]> getDetails(String username);
	@Query(value = INACTIVE_STATUS, nativeQuery = true)
	List<User> findByActive();
	@Modifying
	@Query(value = UPDATE_PASSWORD, nativeQuery = true)
	void updatePassword(@Param("password") String password, @Param("username") String username);
	
}
