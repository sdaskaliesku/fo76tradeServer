package com.manson.fo76;

import com.manson.fo76.domain.ItemsUploadFilters;
import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.domain.dto.StatsDTO;
import com.manson.fo76.domain.items.ItemDescriptor;
import com.manson.fo76.domain.items.enums.FilterFlag;
import com.manson.fo76.service.ItemService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
//public class Main implements CommandLineRunner {

	//	@Autowired
//	private UserService userService;
//	@Autowired
//	private ItemRepository itemRepository;
	@Autowired
	private ItemService itemService;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	//	@Override
	public void run(String... args) throws Exception {
		File file = new File("D:\\workspace\\fo76tradeServer\\all_inventory.json");
		ItemsUploadFilters itemsUploadFilters = new ItemsUploadFilters();
		itemsUploadFilters.setLegendaryOnly(false);
		for (FilterFlag filterFlag : FilterFlag.values()) {
			itemsUploadFilters.getFilterFlags().addAll(filterFlag.getFlags());
		}
//		itemsUploadFilters.getFilterFlags().addAll(FilterFlag.WEAPON.getFlags());
		Pair<User, List<ItemDescriptor>> pair = itemService
				.processModDataItems(file, itemsUploadFilters);
		List<ItemDescriptor> itemDescriptors = pair.getValue();
//		ModData modData = JsonParser.parse(file);
//		System.out.println(modData);
//		List<ItemDescriptor> inventoryList = modData.getInventoryList();
//		List<ItemDescriptor> playerInventory = modData.getPlayerInventory();
//		List<ItemDescriptor> stashInventory = modData.getStashInventory();
//		Map<Long, ItemDescriptor> allItems = new HashMap<>();
//		processItems(inventoryList, allItems, "inventoryList");
//		processItems(playerInventory, allItems, "playerInventory");
//		processItems(stashInventory, allItems, "stashInventory");
		List<ItemDTO> itemDTOS = new ArrayList<>();
		for (ItemDescriptor itemDescriptor : itemDescriptors) {
			ItemDTO itemDTO = Utils.convertItem(itemDescriptor, pair.getKey());
			itemDTOS.add(itemDTO);
			FilterFlag flag = itemDTO.getFilterFlag();
			for (StatsDTO stats : itemDTO.getStats()) {
				if (stats.getTextDecoded() == null && StringUtils.isNotBlank(stats.getText())) {
					System.out.println();
				}
				if (stats.getDamageType() == null) {
					System.out.println();
				}
			}
		}
		System.out.println();
//		userService.deleteAll();
//		User user = new User();
//		user.setName("veron_baron1");
//		user.setPassword("534534534");
//		userService.createNewUser(user);
//
//		List<User> users = userService.findAll();
//		System.out.println(users);
//		List<ItemDTO> allByOwnerId = itemRepository.findAllByOwnerName("manson");
//		System.out.println();
//		itemRepository.deleteAll();
//		File file = new File("D:\\workspace\\fo76tradeServer\\saveeverything2.ini");
////		Set<Integer> damageTypes = new HashSet<>();
//		List<ItemDescriptor> descriptors = JsonParser.parse(file);
//		User user = userService.findAll().get(0);
//		for (ItemDescriptor item : descriptors) {
//			ItemDTO itemDTO = Utils.convertItem(item, user);
//			itemRepository.save(itemDTO);
//		}
//			if (CollectionUtils.isNotEmpty(item.getItemCardEntries())) {
//				for (ItemCardEntry itemCardEntry : item.getItemCardEntries()) {
//					damageTypes.add(itemCardEntry.getDamageType());
//				}
//			}
//		}
//		System.out.println(damageTypes);
	}
}
