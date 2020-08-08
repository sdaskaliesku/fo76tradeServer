package com.manson.fo76.service;

import com.manson.fo76.domain.User;
import com.manson.fo76.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createNewUser(String name, String password) {
		User user = new User();
		user.setName(name);
		user.setPassword(password);
		return createNewUser(user);
	}

	public User createNewUser(User user) {
		User userInDb = userRepository.findByName(user.getName());
		if (Objects.isNull(userInDb)) {
			return userRepository.save(user);
		}
		return null;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void deleteAll() {
		userRepository.deleteAll();
	}
}
