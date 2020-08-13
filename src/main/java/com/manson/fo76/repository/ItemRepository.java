package com.manson.fo76.repository;

import com.manson.fo76.domain.dto.ItemDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemDTO, String> {

  List<ItemDTO> findAllByOwnerId(String ownerId);

  Page<ItemDTO> findAllByOwnerId(String ownerId, Pageable pageable);

  List<ItemDTO> findAllByOwnerName(String ownerName);

  Page<ItemDTO> findAllByOwnerName(String ownerName, Pageable pageable);

  ItemDTO findByIdAndOwnerId(String id, String ownerId);

  ItemDTO findByIdAndOwnerName(String id, String ownerName);

  void deleteAllByOwnerId(String ownerId);
}
