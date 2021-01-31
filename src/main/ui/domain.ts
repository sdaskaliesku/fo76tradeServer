import {RogueService} from "./rogue.service";
import {Utils} from "./utils";
import {UploadFileFilters} from "./filter.service";

export const DISCORD_LINK = 'https://discord.gg/7fef733';
export const NEXUS_LINK = 'https://www.nexusmods.com/fallout76/mods/698';
export const GH_LINK = 'https://github.com/sdaskaliesku/fo76tradeServer';
export const COMPANION_LINK = 'https://www.nexusmods.com/fallout76/mods/744';
export const APP_VERSION = '0.92';
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
  name: string
  price: Number
  review: ReviewResponse
  timestamp: string
  path: string
  description: string

  isEmpty(): boolean
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