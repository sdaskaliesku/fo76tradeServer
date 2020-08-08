package com.manson.fo76;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	private static final String MONGO_URL_FORMAT = "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority";

	@Value("#{systemProperties['mongo.user']}")
	private String mongoUser;

	@Value("#{systemProperties['mongo.password']}")
	private String mongoPassword;

	@Value("#{systemProperties['mongo.db']}")
	private String mongoDb;

	@Value("#{systemProperties['mongo.url']}")
	private String mongoUrl;

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create(getMongoUrl());
	}

	public String getMongoUrl() {
		return String.format(MONGO_URL_FORMAT, mongoUser, mongoPassword, mongoUrl, mongoDb);
	}

}
