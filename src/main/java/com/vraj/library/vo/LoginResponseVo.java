package com.vraj.library.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseVo {

	private String username;
	private String token;
	private String role;
}
