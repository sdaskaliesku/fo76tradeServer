package com.manson.fo76.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.internal.MultiPartWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseRestClient {

  protected Client client;
  protected ObjectMapper objectMapper;

  @Autowired
  public BaseRestClient(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.register(new JacksonJsonProvider(objectMapper));
    clientConfig.register(MultiPartFeature.class);
    clientConfig.register(MultiPartWriter.class);
    this.client = ClientBuilder.newClient(clientConfig);
  }
}
