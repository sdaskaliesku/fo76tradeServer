package com.manson.fo76.web.api;

import com.manson.fo76.domain.dto.ItemDTO;
import com.manson.fo76.service.ItemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemsController {

	private final ItemService itemService;

	@Autowired
	public ItemsController(ItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping("/findAll")
	public List<ItemDTO> findAll() {
		return itemService.findAll();
	}

	@GetMapping("/findAllByOwnerId")
	public List<ItemDTO> findAllByOwnerId(@RequestParam String ownerId) {
		return itemService.findAllByOwnerId(ownerId);
	}

	@GetMapping("/findAllByOwnerName")
	public List<ItemDTO> findAllByOwnerName(@RequestParam String ownerName) {
		return itemService.findAllByOwnerName(ownerName);
	}

	@GetMapping("/findByIdAndOwnerId")
	public ItemDTO findByIdAndOwnerId(@RequestParam String id, @RequestParam String ownerId) {
		return itemService.findByIdAndOwnerId(id, ownerId);
	}

	@GetMapping("/findByIdAndOwnerName")
	public ItemDTO findByIdAndOwnerName(@RequestParam String id, @RequestParam String ownerName) {
		return itemService.findByIdAndOwnerName(id, ownerName);
	}

}
