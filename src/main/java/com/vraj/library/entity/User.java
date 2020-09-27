package com.vraj.library.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USER_TBL")
public class User {

	@Id
	@Column(name = "library_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String libraryId;
	@Column(name = "scard_id")
	private String scardId;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String phone;
	private String college;
	private boolean active;
	private String roles;
	
}
