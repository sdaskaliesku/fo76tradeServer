import {MatchMode} from "./BaseInventOmatic";

export enum ItemTypes {
  POWER_ARMOR,
  WEAPON,
  ARMOR,
  APPAREL,
  FOOD_WATER,
  AID,
  NOTES,
  HOLO,
  AMMO,
  MISC,
  MODS,
  JUNK
}

export interface TeenoodleTragedyProtection {
  ignoreLegendaries: boolean,
  ignoreNonTradable: boolean,
  typesToDrop: Array<string>
}

export interface ItemNameConfig {
  name: string,
  quantity: number,
  matchMode: keyof MatchMode,
  enabled: boolean
}

export interface SectionConfig {
  name: string,
  hotkey: number,
  _comment?: string,
  itemNames: Array<ItemNameConfig>,
  enabled: boolean,
  checkCharacterName: boolean,
  characterName?: string,
  teenoodleTragedyProtection?: TeenoodleTragedyProtection
}

export interface ActionSectionConfig {
  enabled: boolean,
  configs: Array<SectionConfig>
}

export interface InventOmaticPipboyConfig {
  debug: boolean,
  showRealItemName: boolean,
  drop: Array<ActionSectionConfig>,
  consume: Array<ActionSectionConfig>
}