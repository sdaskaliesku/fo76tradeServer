package com.manson.fo76;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manson.fo76.domain.ItemType;
import com.manson.fo76.domain.ItemsUploadFilters;
import com.manson.fo76.domain.LegendaryMod;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    List<LegendaryMod> legendaryMods = new ArrayList<>();
    ObjectMapper om = new ObjectMapper();
    File file = new File("D:\\workspace\\fo76tradeServer\\src\\main\\resources\\legendaryMods.config.json");
    legendaryMods = om.readValue(file, new TypeReference<List<LegendaryMod>>() {
    });
    String melee = "Bloodied (Does more damage the lower your health is)\n"
        + "Furious (Damage increased after each consecutive hit on the same target)\n"
        + "Instigating (Double damage if target is full health)\n"
        + "Anti-armor (Ignores 50% of your target's armor)\n"
        + "Vampire's (Gain brief health regeneration when you hit an enemy)\n"
        + "Berserker's (More damage the lower your damage resistance)\n"
        + "Junkie's (More damage the more your withdrawal effects)\n"
        + "Suppressor's (Reduce your target's damage output by 20% for 3s)\n"
        + "Assassin's (10% damage to players)\n"
        + "Nocturnal (Damage increased by the night and reduced by the day)\n"
        + "Mutant's (Damage increased by 10% if you are mutated)\n"
        + "Stalker's (If not in combat, 100% VATS accuracy at 50% increased AP cost)\n"
        + "Executioner's (50% increased damage if target is below 40% health)\n"
        + "Troubleshooter's (30% damage to robots)\n"
        + "Ghoul slayer's (30% damage to ghouls)\n"
        + "Mutant slayer's (30% damage to super mutants)\n"
        + "Hunter's (30% damage to animals)\n"
        + "Exterminator's (30% damage to Mirelurks and bugs)\n"
        + "Zealot's (30% damage to scorched)";
    String[] meleeMajor = melee.split("\n");
//    for (LegendaryMod legendaryMod : legendaryMods) {
//      legendaryMod.setItemTypes(new ArrayList<>());
//      legendaryMod.getItemTypes().add(ItemType.armor);
//      Map<String, String> map = new HashMap<>();
//      map.put("en", "");
//      legendaryMod.setTranslations(map);
//    }
//    om.writeValue(file, legendaryMods);
    System.out.println(legendaryMods);
//    if (!update_db) {
//      return;
//    }
//    File file = new File("D:\\workspace\\fo76tradeServer\\all_inventory.json");
//    ItemsUploadFilters itemsUploadFilters = new ItemsUploadFilters();
//    ModData modData = JsonParser.parse(file);
//    Pair<User, List<ItemDescriptor>> pair = itemService.processModDataItems(modData, itemsUploadFilters);
//    User user = userService.findAll().stream().filter(x -> x.getName().equalsIgnoreCase(pair.getKey().getName()))
//        .collect(
//            Collectors.toList()).get(0);
//    List<ItemDTO> itemDTOS = Utils.convertItems(pair.getValue(), user);
//    if (CollectionUtils.isNotEmpty(itemDTOS)) {
//      itemRepository.deleteAll();
//      itemRepository.saveAll(itemDTOS);
//    }
  }
}
