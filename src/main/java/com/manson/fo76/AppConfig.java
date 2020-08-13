package com.manson.fo76;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class AppConfig {

	private static final ObjectMapper OM = new ObjectMapper();
	private static final String MONGO_URL_FORMAT = "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority";

	static {
		OM.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		OM.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}

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

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofBytes(512000000L));
		factory.setMaxRequestSize(DataSize.ofBytes(512000000L));
		return factory.createMultipartConfig();
	}

	@Bean
	public static ObjectMapper getObjectMapper() {
		return OM;
	}
}
