package com.manson.fo76.repository;

import com.manson.fo76.domain.dto.ItemDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemDTO, String> {

  List<ItemDTO> findAllByOwnerInfoId(String ownerId);

  Page<ItemDTO> findAllByOwnerInfoId(String ownerId, Pageable pageable);

  List<ItemDTO> findAllByOwnerInfoName(String ownerName);

  Page<ItemDTO> findAllByOwnerInfoName(String ownerName, Pageable pageable);

  ItemDTO findByIdAndOwnerInfoId(String id, String ownerId);

  ItemDTO findByIdAndOwnerInfoName(String id, String ownerName);

  void deleteAllByOwnerInfoId(String ownerId);
}
