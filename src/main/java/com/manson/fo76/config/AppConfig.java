package com.manson.fo76.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.domain.config.MongoDbConfig;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.unit.DataSize;

@Configuration
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class AppConfig {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
    objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    objectMapper.enable(Feature.IGNORE_UNKNOWN);
    objectMapper.setSerializationInclusion(Include.NON_EMPTY);
  }

  @Autowired
  private MongoDbConfig mongoDbConfig;

  @Bean
  public static ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    PropertySourcesPlaceholderConfigurer propsConfig
        = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocation(new ClassPathResource("git.properties"));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create(mongoDbConfig.getFullUrl());
  }

  @Bean
  public MultipartConfigElement multipartConfigElement() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofBytes(512000000L));
    factory.setMaxRequestSize(DataSize.ofBytes(512000000L));
    return factory.createMultipartConfig();
  }

}
