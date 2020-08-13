package com.manson.fo76.web.api;

import com.manson.fo76.domain.JwtResponse;
import com.manson.fo76.domain.User;
import com.manson.fo76.helper.JwtTokenUtil;
import com.manson.fo76.service.ItemService;
import com.manson.fo76.service.UserService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UsersController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserService userService;
  private final ItemService itemService;

  @Autowired
  public UsersController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
      UserService userService, ItemService itemService) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userService = userService;
    this.itemService = itemService;
  }

  @PostMapping(
      value = "/create", consumes = "application/json", produces = "application/json")
  public User createNewUser(@RequestBody User user) {
    return userService.createNewUser(user);
  }

  @PostMapping(
      value = "/forgot", consumes = "application/json", produces = "application/json")
  public User forgot(@RequestBody User user) {
    return userService.findByIdOrName(user);
  }

  @GetMapping("/findAll")
  public List<User> findAll() {
    return userService.findAll();
  }

  @GetMapping("/findByIdOrName")
  public User findByIdOrName(String idOrName) {
    return userService.findByIdOrName(idOrName);
  }

  @DeleteMapping(value = "/delete", consumes = "application/json", produces = "application/json")
  public boolean delete(@RequestBody User user) {
    User userInDb = userService.delete(user);
    if (Objects.nonNull(userInDb)) {
      return itemService.deleteAllByOwnerId(userInDb.getId());
    }
    return false;
  }

  @PostMapping(value = "/authenticate", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> generateAuthenticationToken(@RequestBody User user) {

    final UserDetails userDetails = userService.loadUser(user);
    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    authenticate(user.getName(), user.getPassword());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(token));
  }

  private void authenticate(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
