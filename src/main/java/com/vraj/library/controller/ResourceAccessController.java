package com.vraj.library.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vraj.library.entity.User;
import com.vraj.library.model.AuthRequest;
import com.vraj.library.service.AdminServiceImpl;
import com.vraj.library.service.UserAuthService;
//import com.vraj.library.service.UserAuthService;
import com.vraj.library.service.UserServiceImpl;
import com.vraj.library.vo.LoginResponseVo;
import com.vraj.library.vo.UserVo;

@CrossOrigin
@RestController
@RequestMapping("/v1")
public class ResourceAccessController {
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private AdminServiceImpl adminService;
	
	@Autowired
	private UserAuthService userAuthService;

	@GetMapping("/user/info")
	public String infoPage() {
		return "Information page";
	}

	@GetMapping("/admin/main")
	public String test() {
		return "welcome to LMS";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (Objects.nonNull(auth)) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/";
	}

//	@RequestMapping(value = "/logout")
//	public String logout(HttpServletRequest request) {
//	    HttpSession session = request.getSession(false);
//	    System.out.println(session);
//	    if (session != null) {
//	        session.invalidate();
//	    }
//	    System.out.println(session);
//	    return "redirect:/";  //Where you go after logout here.
//	}
	
	@PostMapping(path = "/authenticate", produces = "application/json")
	public ResponseEntity<LoginResponseVo> authenticate(@RequestBody AuthRequest authRequest) {
		LoginResponseVo loginResponse = userAuthService.authenticate(authRequest);
		//String token = userAuthService.generateToken(authRequest.getUsername());
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}
	
	@PostMapping(path = "/signup", produces = "application/json")
	public ResponseEntity<String> registerUser(@RequestBody User user, @RequestHeader("Role") String role																) {
		User userResponse = userService.save(user, role);
		return new ResponseEntity<>(userResponse.getUsername(), HttpStatus.CREATED);
		
	}
	
	@PutMapping(path = "/update", produces = "application/json")
	public ResponseEntity<String> updateDetails(@RequestBody User user, 
												@RequestHeader(name = "Authorization") String authToken) {
		userService.update(user, authToken);
		return new ResponseEntity<>(user.getUsername(), HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/profile", produces = "application/json")
	public ResponseEntity<User> getSelfDetails(@RequestHeader(name = "Authorization") String authToken) {
		User user = userService.getDetails(authToken);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping(path = "/allprofile", produces = "application/json")
	public ResponseEntity<List<UserVo>> getAllDetails(@RequestHeader(name = "Authorization") String authToken) {
		List<UserVo> users = adminService.getAllDetails(authToken);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete/{username}", produces = "application/json")
	public ResponseEntity<String> deleteStudentEmployee(@RequestBody AuthRequest user,
														@PathVariable("username") String usernameToDelete,
														@RequestHeader(name = "Authorization") String authToken) {
		adminService.deleteEmployee(user, usernameToDelete, authToken);
		return new ResponseEntity<>(usernameToDelete, HttpStatus.OK);
				
	}
	
	@GetMapping(path = "/pending-approvals", produces = "application/json")
	public ResponseEntity<List<UserVo>> pendingApprovals(@RequestHeader(name = "Authorization") String authToken) {
		List<UserVo> users = adminService.getDetailsBasedOnActiveStatus(authToken);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@PutMapping(path = "/approve", produces = "application/json")
	public ResponseEntity<String> verifyEmployee(@RequestBody User user) {
		adminService.verifyEmployee(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PatchMapping(path = "/change-password", produces = "application/json")
	public ResponseEntity<String> updatePassword(@RequestHeader(name = "Authorization") String authToken,
										   @RequestBody Map<String, String> updates){
		userService.changePassword(updates, authToken);
		return new ResponseEntity<>("Password updated", HttpStatus.OK);
	}
	
}
