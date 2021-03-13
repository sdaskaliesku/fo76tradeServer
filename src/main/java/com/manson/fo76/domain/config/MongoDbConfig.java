package com.manson.fo76.domain.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "mongo-config")
public class MongoDbConfig {
  @Value("${user}")
  private String user;
  @Value("${cluster}")
  private String cluster;
  @Value("${db}")
  private String db;
  @Value("${password}")
  private String password;
  @Value("${fullUrl}")
  private String fullUrl;

}
