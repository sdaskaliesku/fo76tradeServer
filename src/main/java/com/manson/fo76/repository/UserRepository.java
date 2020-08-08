package com.manson.fo76.repository;

import com.manson.fo76.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

	User findByName(String name);

}
