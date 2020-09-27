package com.vraj.library.vo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserVo implements Serializable{

	private static final long serialVersionUID = -5907070479709687424L;
	private String libraryId;
	private String scardId;
	private String username;
	private String firstname;
	private String lastname;
	private String phone;
	private String college;
	private boolean active;
	private String roles;
}
