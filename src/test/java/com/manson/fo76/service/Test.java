package com.manson.fo76.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.domain.LegendaryModDescriptor;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

public class Test {

  @org.junit.jupiter.api.Test
  void name() throws Exception {
    ClientConfig config = new ClientConfig();
    ObjectMapper objectMapper = new ObjectMapper();
    config.register(new JacksonJsonProvider(objectMapper));
    Client client = ClientBuilder.newClient(config);

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("operationName", "gameTaxonomyNodes");
    map.put("query",
        "query gameTaxonomyNodes($systemId: String!, $branches: [String!]) {↵  gameTaxonomyNodes(systemId: $systemId, branches: $branches) {↵    id↵    branch↵    key↵    meta↵    gameId↵    __typename↵  }↵}");
    Map<String, Object> variables = new HashMap<>();
    variables.put("branches", Arrays.asList("Effect.Armor", "Effect.Melee", "Effect.Ranged", "Effect.Weapon"));

    map.put("variables", variables);

    WebTarget webTarget = client.target("https://a.roguetrader.com/").path("graphql");

    String customRequestBody = objectMapper.writeValueAsString(map);
    Entity<String> entity = Entity.json(customRequestBody);
    Invocation post = webTarget.request().accept("application/json").buildPost(entity);
//    System.out.println(post);
    Map invoke = post.invoke(Map.class);
    objectMapper.writeValue(new File("roguetrader.json"), invoke);
  }

  @org.junit.jupiter.api.Test
  void test2() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    List<LegendaryModDescriptor> legendaryModDescriptors = objectMapper.readValue(
        new File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\legendaryMods.config.json"),
        new TypeReference<List<LegendaryModDescriptor>>() {
        });
    List<RogueTraderConfig> configs = objectMapper
        .readValue(new File("roguetrader.json"), new TypeReference<List<RogueTraderConfig>>() {
        });
    for (LegendaryModDescriptor legModDescriptor : legendaryModDescriptors) {
//      if (!legModDescriptor.getItemType().equalsIgnoreCase("weapon")) {
//        continue;
//      }
      Collection<String> texts = legModDescriptor.getTexts().values();
      for (RogueTraderConfig config : configs) {
//        if (config.getMeta().getIs().isArmor()) {
//          continue;
//        }
        for (String txt : texts) {
          if (StringUtils.equalsIgnoreCase(txt, config.getMeta().getName()) || StringUtils.equalsIgnoreCase(txt, config.getKey())) {
            legModDescriptor.setAbbreviation(config.getKey());
            legModDescriptor.getAdditionalAbbreviations().addAll(config.getMeta().getLookup());
            LinkedHashSet<String> strings = new LinkedHashSet<>(legModDescriptor.getAdditionalAbbreviations());
            legModDescriptor.setAdditionalAbbreviations(new ArrayList<>(strings));
            legModDescriptor.setGameId(config.getGameId());
          }
        }
//        System.out.println(config);
      }
    }
    objectMapper.writeValue(new File("legmods.json"), legendaryModDescriptors);
    System.out.println();

  }
}
