package com.manson.fo76.service;

import com.manson.fo76.domain.AbstractObject;

public class Is extends AbstractObject {
  private boolean Effect;
  private boolean Weapon;
  private boolean Armor;
  private boolean Ranged;
  private boolean Melee;

  public boolean isEffect() {
    return Effect;
  }

  public void setEffect(boolean effect) {
    Effect = effect;
  }

  public boolean isWeapon() {
    return Weapon;
  }

  public void setWeapon(boolean weapon) {
    Weapon = weapon;
  }

  public boolean isArmor() {
    return Armor;
  }

  public void setArmor(boolean armor) {
    Armor = armor;
  }

  public boolean isRanged() {
    return Ranged;
  }

  public void setRanged(boolean ranged) {
    Ranged = ranged;
  }

  public boolean isMelee() {
    return Melee;
  }

  public void setMelee(boolean melee) {
    Melee = melee;
  }
}
