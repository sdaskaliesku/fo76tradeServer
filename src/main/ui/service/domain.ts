import {RogueService} from "./rogue.service";
import {Utils} from "./utils";
import {UploadFileFilters} from "./filter.service";

export const DISCORD_LINK = 'https://discord.gg/7fef733';
export const NEXUS_LINK = 'https://www.nexusmods.com/fallout76/mods/698';
export const GH_LINK = 'https://github.com/sdaskaliesku/fo76tradeServer';
export const COMPANION_LINK = 'https://www.nexusmods.com/fallout76/mods/744';
export const APP_VERSION = '0.94';
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
  name?: string;
  price?: number;
  review?: ReviewResponse;
  timestamp?: string;
  path?: string;
  description?: string;
}

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
      Utils.downloadString(RogueService.toCSV(rawData, character), 'text/csv', `rogue_trader_${character}.csv`);
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

export interface UnknownFields {
}

export interface VendingData {
  unknownFields?: UnknownFields;
  price: number;
  machineType: number;
  vendedOnOtherMachine: boolean;
}

export interface OwnerInfo {
  unknownFields?: UnknownFields;
  accountName: string;
  characterName: string;
}

export interface ItemDetails {
  unknownFields?: UnknownFields;
  itemSource?: string;
  filterFlag?: string;
  ownerInfo?: OwnerInfo;
  totalWeight?: number;
  name?: string;
  abbreviation?: string;
  abbreviationId?: string;
  config?: any;
  armorConfig?: any;
  legendaryMods?: Array<any>;
  stats?: Array<Stat>;
}

export interface Stat {
  unknownFields?: UnknownFields;
  itemCardText?: string;
  value?: string;
  damageType?: string;
}

export interface ReportItem {
  item: Item,
  reason: string
}

export interface Item {
  unknownFields?: UnknownFields;
  id: string;
  text: string;
  serverHandleId: number;
  count: number;
  itemValue: number;
  weight: number;
  itemLevel: number;
  numLegendaryStars: number;
  isTradable: boolean;
  isSpoiled: boolean;
  isSetItem: boolean;
  isQuestItem: boolean;
  isLegendary: boolean;
  vendingData?: VendingData;
  filterFlag: string;
  itemDetails?: ItemDetails | undefined;
  isWeightless: boolean;
  scrapAllowed: boolean;
  isAutoScrappable: boolean;
  canGoIntoScrapStash: boolean;
  isLearnedRecipe: boolean;
  priceCheckResponse?: PriceCheckResponse;
}
