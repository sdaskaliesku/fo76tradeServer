import {RogueService} from "./rogue.service";
import {Utils} from "./utils";

export const DISCORD_LINK = 'https://discord.gg/7fef733';
export const NEXUS_LINK = 'https://www.nexusmods.com/fallout76/mods/698';
export const GH_LINK = 'https://github.com/sdaskaliesku/fo76tradeServer';
export const COMPANION_LINK = 'https://www.nexusmods.com/fallout76/mods/744';
export const APP_VERSION = '0.9 (WORK IN PROGRESS, USE https://fo76market.online/ for stable version)';
export const MIN_MOD_SUPPORTED_VERSION = 0.4;
export const MIN_FED_MOD_SUPPORTED_VERSION = 0.1;

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

export declare interface AuthorResponse {
  name: string
  logo: string
  description: string
  url: string
}

export declare interface ReviewRatingResponse {
  bestRating: string
  ratingValue: string
  worstRating: string
}

export declare interface ReviewResponse {
  author: AuthorResponse
  dateCreated: string
  description: string
  name: string
  reviewRating: ReviewRatingResponse
  url: string
}

export declare interface PriceCheckResponse {
  name: string
  price: Number
  review: ReviewResponse
  timestamp: string
  path: string
  description: string

  isEmpty(): boolean
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
    id: 'priceCheckOnly',
    name: 'Price Check Only',
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

export const downloadOptions = [
  {
    title: 'Raw json',
    type: 'raw_json',
    handler: function ({rawData}: { rawData: any }) {
      Utils.downloadString(JSON.stringify(rawData), 'text/json', 'full_data.json');
    },
  },
  {
    title: 'RogueTrader CSV (extremely experimental)',
    type: 'rogue_csv',
    handler: function ({rawData, character}: { rawData: any, character: string }) {
      Utils.downloadString(RogueService.toCSV(rawData, character), 'text/csv', 'rogue_trader.csv');
    },
  },
  {
    title: 'json',
    type: 'json',
    handler: function ({tabulator}: { tabulator: any }) {
      tabulator.download(this.type, `data.${this.type}`, {style: true});
    },
  },
  {
    title: 'csv / excel',
    type: 'csv',
    handler: function ({tabulator}: { tabulator: any }) {
      tabulator.download(this.type, `data.${this.type}`, {style: true});
    },
  },
  {
    title: 'html',
    type: 'html',
    handler: function ({tabulator}: { tabulator: any }) {
      tabulator.download(this.type, `data.${this.type}`, {style: true});
    },
  },
];