package com.manson.fo76.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
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

  public static final boolean ENABLE_AUTO_PRICE_CHECK = false;
  public static final boolean ENABLE_PRICE_ENHANCE = false;
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String MONGO_URL_FORMAT = "mongodb+srv://%s:%s@%s/%s?retryWrites=true&w=majority";

  static {
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    objectMapper.enable(Feature.IGNORE_UNKNOWN);
    objectMapper.setSerializationInclusion(Include.NON_EMPTY);
  }

  @Value("#{systemProperties['mongo.user']}")
  private String mongoUser;
  @Value("#{systemProperties['mongo.password']}")
  private String mongoPassword;
  @Value("#{systemProperties['mongo.db']}")
  private String mongoDb;
  @Value("#{systemProperties['mongo.url']}")
  private String mongoUrl;

  public String createMongoUrl() {
    return String.format(MONGO_URL_FORMAT, mongoUser, mongoPassword, mongoUrl, mongoDb);
  }

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create(this.createMongoUrl());
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
    return objectMapper;
  }

}
