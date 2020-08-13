package com.manson.fo76.service;

import static com.manson.fo76.helper.Utils.validatePassword;

import com.manson.fo76.domain.User;
import com.manson.fo76.repository.UserRepository;
import java.util.ArrayList;
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
    if (StringUtils.isNotBlank(id)) {
      return userRepository.findById(id).orElse(null);
    }
    return null;
  }

  public User findByName(String name) {
    if (StringUtils.isNotBlank(name)) {
      return userRepository.findByName(name);
    }
    return null;
  }

  public User findByIdOrName(String idOrName) {
    return Optional.ofNullable(findById(idOrName)).orElseGet(() -> userRepository.findByName(idOrName));
  }

  public User findByIdOrName(User user) {
    if (Objects.nonNull(user)) {
      return Optional.ofNullable(findByIdOrName(user.getId()))
          .orElseGet(() -> findByIdOrName(user.getName()));
    }
    return null;
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public void deleteAll() {
    userRepository.deleteAll();
  }

  public User delete(User user) {
    if (Objects.isNull(user)) {
      return null;
    }
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

  @Override
  public UserDetails loadUserByUsername(String name) {
    User user = findByName(name);
    if (user != null) {
      return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
          new ArrayList<>());
    }
    return null;
  }

  public UserDetails loadUser(User user) {
    if (Objects.isNull(user)) {
      return null;
    }
    User userInDb = findByIdOrName(user);
    if (userInDb != null && validatePassword(user, userInDb)) {
      return new org.springframework.security.core.userdetails.User(userInDb.getName(), userInDb.getPassword(),
          new ArrayList<>());
    }
    return null;
  }
}
