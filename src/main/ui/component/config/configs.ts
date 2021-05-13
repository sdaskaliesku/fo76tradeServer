const stringIsNumber = (value: any) => !isNaN(Number(value));

const toArray = (enumme: any) => Object.keys(enumme).filter(stringIsNumber).map(key => enumme[key]);

export enum ItemType {
  ALL,
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

export enum MatchMode {
  ALL,
  EXACT,
  CONTAINS,
  STARTS
}

export enum Action {
  CONSUME,
  DROP
}

export interface TeenoodleTragedyProtection {
  ignoreLegendaries: boolean,
  ignoreNonTradable: boolean
}

export interface ItemNameConfig {
  name: string,
  quantity: number,
  matchMode: keyof MatchMode,
  enabled: boolean,
  type: keyof ItemType,
  action: keyof Action
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
  actions: Array<ActionSectionConfig>
}

export const MatchModes = toArray(MatchMode);
export const ItemTypes = toArray(ItemType);
export const Actions = toArray(Action);