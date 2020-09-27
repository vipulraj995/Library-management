package com.vraj.library.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {

	private LocalDateTime timeStamp;
	private int errorCode;
	private String message;
}
