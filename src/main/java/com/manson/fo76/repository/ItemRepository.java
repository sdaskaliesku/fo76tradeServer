package com.manson.fo76.repository;

import com.manson.fo76.domain.User;
import com.manson.fo76.domain.dto.ItemDTO;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemDTO, String> {

	List<ItemDTO> findAllByOwnerId(String ownerId);

	List<ItemDTO> findAllByOwnerName(String ownerName);

	ItemDTO findByIdAndOwnerId(String id, String ownerId);
	ItemDTO findByIdAndOwnerName(String id, String ownerName);

	default List<ItemDTO> findAllByOwnerId(User user) {
		return findAllByOwnerId(user.getId());
	}

	default List<ItemDTO> findAllByOwnerName(User user) {
		return findAllByOwnerName(user.getName());
	}

}
