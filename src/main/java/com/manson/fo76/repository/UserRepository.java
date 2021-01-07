package com.manson.fo76.repository;

import com.manson.fo76.domain.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

  User findByName(String name);
}
