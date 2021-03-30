package com.manson.fo76.domain.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class GitConfig {

  @Value("${git.build.time:}")
  private String buildTimestamp;

  @Value("${git.commit.id:}")
  private String gitCommitId;
}
