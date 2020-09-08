export const DISCORD_LINK = 'https://www.nexusmods.com/fallout76/mods/698';
export const NEXUS_LINK = 'https://www.nexusmods.com/fallout76/mods/698';
export const GH_LINK = 'https://github.com/sdaskaliesku/fo76tradeServer';
export const APP_VERSION = 0.5;
export const MIN_MOD_SUPPORTED_VERSION = 0.4;

export declare interface ModDataRequest {
  modData: ModData
  filters: UploadFileFilters
}

export declare interface ModData {
  version: number
  characterInventories: {
    string: any
  }
  user: {
    user: string,
    password: string,
    id?: string,
    append?: boolean
  }
}

export declare interface UploadFileFilters {
  filterFlags: Array<number>,
  legendaryOnly: boolean,
  tradableOnly: boolean,
}

export declare interface Filter {
  id?: string,
  name: string,
  checked?: boolean,
  hide?: boolean,
  filters?: string
  types?: Array<string>
}

export const filters: Array<Filter> = [
  {
    id: 'tradableOnly',
    name: 'Tradable',
    checked: true,
    hide: true,
  },
  {
    id: 'legendaries',
    name: 'legendaries',
    checked: true,
  },
  {
    name: 'WEAPON',
    filters: '2,3',
    checked: true,
    types: [
      'WEAPON_MELEE',
      'WEAPON_RANGED',
      'WEAPON_THROWN',
    ],
  },
  {
    name: 'ARMOR',
    filters: '4',
    checked: true,
    types: [
      'ARMOR',
      'ARMOR_OUTFIT',
    ],
  },
  {
    name: 'AID',
    filters: '8,9',
    checked: true,
    types: ['AID'],
  },
  {
    name: 'HOLO',
    filters: '512',
    checked: true,
    types: ['HOLO'],
  },
  {
    name: 'AMMO',
    filters: '4096',
    checked: true,
    types: ['AMMO'],
  },
  {
    name: 'NOTES',
    filters: '128,8192',
    checked: true,
    types: ['NOTES'],
  },
  {
    name: 'MISC',
    filters: '33280',
    checked: true,
    types: ['MISC'],
  },
  {
    name: 'MODS',
    filters: '2048',
    checked: true,
    types: ['MODS'],
  },
  {
    name: 'JUNK',
    filters: '33792, 1024',
    checked: true,
    types: ['JUNK'],
  },
];