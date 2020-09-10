package com.manson.fo76

import com.manson.fo76.repository.ItemRepository
import com.manson.fo76.service.ItemService
import com.manson.fo76.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication

//@SpringBootApplication
class MainConsole : CommandLineRunner {
    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val itemRepository: ItemRepository? = null

    @Autowired
    private val itemService: ItemService? = null
    @Throws(Exception::class)
    override fun run(vararg args: String) {
//    if (!update_db) {
//      return;
//    }
//    File file = new File("D:\\workspace\\fo76tradeServer\\itemsmod.ini");
//    ItemsUploadFilters itemsUploadFilters = new ItemsUploadFilters();
//    ModData modData = JsonParser.parse(file);
//    Pair<User, List<ItemDescriptor>> pair = itemService.processModDataItems(modData, itemsUploadFilters);
////    User user = userService.findAll().stream().filter(x -> x.getName().equalsIgnoreCase(pair.getKey().getName()))
////        .collect(
////            Collectors.toList()).get(0);
//    User user = new User();
//    user.setId("someid");
//    user.setName("manson");
//    List<ItemDTO> itemDTOS = Utils.convertItems(pair.getValue(), user);
//    if (CollectionUtils.isNotEmpty(itemDTOS)) {
//      itemRepository.deleteAll();
//      itemRepository.saveAll(itemDTOS);
//    }
    }

    companion object {
        private const val update_db = true
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Main::class.java, *args)
        }
    }
}