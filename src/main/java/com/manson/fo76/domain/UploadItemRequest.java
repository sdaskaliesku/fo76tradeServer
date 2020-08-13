package com.manson.fo76.domain;

import com.manson.fo76.domain.dto.ItemDTO;
import java.util.List;

public class UploadItemRequest {

  private User user;
  private List<ItemDTO> items;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<ItemDTO> getItems() {
    return items;
  }

  public void setItems(List<ItemDTO> items) {
    this.items = items;
  }
}
