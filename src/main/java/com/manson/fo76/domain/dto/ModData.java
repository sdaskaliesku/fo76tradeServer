package com.manson.fo76.domain.dto;

import com.manson.AbstractObject;
import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ModData extends AbstractObject {

  private Map<String, CharacterInventory> characterInventories;
  private Double version;
  private Date dumpDate;

  public boolean isEmpty() {
    return MapUtils.isEmpty(characterInventories);
  }

  public Map<String, CharacterInventory> getCharacterInventories() {
    return characterInventories;
  }

  public void setCharacterInventories(
      Map<String, CharacterInventory> characterInventories) {
    this.characterInventories = characterInventories;
  }

  public Double getVersion() {
    return version;
  }

  public void setVersion(Double version) {
    this.version = version;
  }

  public Date getDumpDate() {
    return dumpDate;
  }

  public void setDumpDate(Date dumpDate) {
    this.dumpDate = dumpDate;
  }
}
