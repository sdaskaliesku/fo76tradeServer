package com.manson.fo76.web.api;

import com.manson.fo76.domain.User;
import com.manson.fo76.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users")
public class UsersController {

	private final UserService userService;

	@Autowired
	public UsersController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/create")
	public User createNewUser(@RequestParam String name, @RequestParam String password) {
		return userService.createNewUser(name, password);
	}

	@GetMapping("/findAll")
	public List<User> findAll() {
		return userService.findAll();
	}
}
