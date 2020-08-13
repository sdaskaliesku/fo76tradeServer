package com.manson.fo76;

import com.manson.fo76.domain.ItemsUploadFilters;
import com.manson.fo76.domain.ModData;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.helper.Utils;
import com.manson.fo76.repository.ItemRepository;
import com.manson.fo76.service.ItemService;
import com.manson.fo76.service.JsonParser;
import com.manson.fo76.service.UserService;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

//@SpringBootApplication
public class MainConsole implements CommandLineRunner {

  private static boolean update_db = true;
  @Autowired
  private UserService userService;
  @Autowired
  private ItemRepository itemRepository;
  @Autowired
  private ItemService itemService;

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (!update_db) {
      return;
    }
    File file = new File("D:\\workspace\\fo76tradeServer\\all_inventory.json");
    ItemsUploadFilters itemsUploadFilters = new ItemsUploadFilters();
    ModData modData = JsonParser.parse(file);
    Pair<User, List<ItemDescriptor>> pair = itemService.processModDataItems(modData, itemsUploadFilters);
    User user = userService.findAll().stream().filter(x -> x.getName().equalsIgnoreCase(pair.getKey().getName()))
        .collect(
            Collectors.toList()).get(0);
    List<ItemDTO> itemDTOS = Utils.convertItems(pair.getValue(), user);
    if (CollectionUtils.isNotEmpty(itemDTOS)) {
      itemRepository.deleteAll();
      itemRepository.saveAll(itemDTOS);
    }
  }
}
