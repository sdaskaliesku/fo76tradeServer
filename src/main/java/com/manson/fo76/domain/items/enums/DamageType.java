package com.manson.fo76.domain.items.enums;

import java.util.Arrays;
import java.util.Objects;

public enum DamageType {
	FLAG_0(0, "Nothing"),
	FLAG_1(1, "Ballistic"),
	FLAG_2(2, "Water"),
	FLAG_3(3, ""),
	FLAG_4(4, "Energy"),
	FLAG_5(5, ""),
	FLAG_6(6, "Radiation"),
	FLAG_10(10, "Ammo");
	private final int flag;
	private final String name;

	DamageType(int flag, String name) {
		this.flag = flag;
		this.name = name;
	}

	public static DamageType fromDamageType(Integer dmgType) {
		if (Objects.isNull(dmgType)) {
			return null;
		}
		return Arrays.stream(DamageType.values()).filter(damageType -> damageType.getFlag() == dmgType)
				.findFirst().orElse(null);
	}

	public int getFlag() {
		return flag;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
