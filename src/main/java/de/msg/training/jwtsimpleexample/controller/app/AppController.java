package de.msg.training.jwtsimpleexample.controller.app;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppController {
	
	@GetMapping("/user")
	//@PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
	public String userAccess() {
		return "User Here.";
	}

	@GetMapping("/admin")
	//@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Here.";
	}
}