package com.manson.fo76.service;

import com.manson.fo76.domain.dto.User;
import com.manson.fo76.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
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
    return Optional.ofNullable(findById(idOrName)).orElse(findByName(idOrName));
  }

}
