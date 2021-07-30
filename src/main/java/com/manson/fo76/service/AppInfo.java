package com.manson.fo76.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manson.fo76.domain.config.GitConfig;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class AppInfo {

  private String name;

  private String version;

  @JsonIgnore
  private String commitUrlFormat;

  private List<UrlConfig> sites;

  private List<UrlConfig> tools;

  private String discord;

  private String github;

  private String commitUrl;

  @Autowired
  private GitConfig gitConfig;

  @PostConstruct
  public void init() {
    this.commitUrl = this.commitUrlFormat + this.gitConfig.getGitCommitId();
  }
}
