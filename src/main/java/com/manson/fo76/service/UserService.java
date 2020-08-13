package com.manson.fo76.service;

import com.manson.fo76.domain.User;
import com.manson.fo76.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createNewUser(User user) {
		User userInDb = userRepository.findByName(user.getName());
		if (Objects.isNull(userInDb)) {
			user.setPassword(UUID.randomUUID().toString());
			return userRepository.save(user);
		}
		return null;
	}

	public User findById(String id) {
		return userRepository.findById(id).orElse(null);
	}

	public User findByName(String id) {
		return userRepository.findByName(id);
	}

	public User findByIdOrName(String idOrName) {
		return Optional.ofNullable(findById(idOrName)).orElseGet(() -> userRepository.findByName(idOrName));
	}

	public User findByIdOrName(User user) {
		return Optional.ofNullable(findByIdOrName(user.getId()))
				.orElseGet(() -> findByIdOrName(user.getName()));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void deleteAll() {
		userRepository.deleteAll();
	}

	public User delete(User user) {
		User userInDb = findByIdOrName(user);
		if (Objects.isNull(userInDb)) {
			return null;
		}
		if (StringUtils.equals(userInDb.getPassword(), user.getPassword())) {
			userRepository.delete(userInDb);
			return userInDb;
		}
		return null;
	}

	public UserDetails loadUserByUsername(String username) {
		return null;
	}
}
