package com.manson.fo76.domain.fed76;

import com.manson.domain.fed76.BasePriceCheckResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class PriceCheckCacheItem {

  @Id
  private String id;
  @Indexed
  private String requestId;
  private BasePriceCheckResponse response;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public BasePriceCheckResponse getResponse() {
    return response;
  }

  public void setResponse(BasePriceCheckResponse response) {
    this.response = response;
  }
}