package com.manson.fo76.web.api;

import com.manson.fo76.domain.User;
import com.manson.fo76.service.ItemService;
import com.manson.fo76.service.UserService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "users")
public class UsersController {

	private final UserService userService;
	private final ItemService itemService;

	@Autowired
	public UsersController(UserService userService, ItemService itemService) {
		this.userService = userService;
		this.itemService = itemService;
	}

	@PostMapping(
			value = "/create", consumes = "application/json", produces = "application/json")
	public User createNewUser(@RequestBody User user) {
		return userService.createNewUser(user);
	}

	@GetMapping("/findAll")
	public List<User> findAll() {
		return userService.findAll();
	}

	@GetMapping("/findByIdOrName")
	public User findByIdOrName(String idOrName) {
		return userService.findByIdOrName(idOrName);
	}

	@DeleteMapping("/delete")
	public boolean delete(User user) {
		User userInDb = userService.delete(user);
		if (Objects.nonNull(userInDb)) {
			return itemService.deleteAllByOwnerId(userInDb.getId());
		}
		return false;
	}
}
