import {UploadFileFilters} from "./filter.service";

export const MIN_MOD_SUPPORTED_VERSION = 0.4;

export interface UrlConfig {
  name: string
  url: string
}

export interface GitConfig {
  buildTimestamp: string
  gitCommitId: string
}

export interface AppInfo {
  name: string
  version: string
  sites: Array<UrlConfig>
  tools: Array<UrlConfig>
  discord: string
  github: string
  commitUrl: string
  gitConfig: GitConfig
}

export interface ModDataRequest {
  modData: ModData
  filters: UploadFileFilters
}

export interface ModData {
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

export interface AuthorResponse {
  name: string
  logo: string
  description: string
  url: string
}

export interface ReviewRatingResponse {
  bestRating: string
  ratingValue: string
  worstRating: string
}

export interface ReviewResponse {
  author: AuthorResponse
  dateCreated: string
  description: string
  name: string
  reviewRating: ReviewRatingResponse
  url: string
}

export interface PriceCheckResponse {
  name?: string;
  price?: number;
  review?: ReviewResponse;
  timestamp?: string;
  path?: string;
  description?: string;
}

export interface UnknownFields {
  [key: string]: any
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
  serverHandleId?: number;
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
  itemDetails?: ItemDetails;
  isWeightless: boolean;
  scrapAllowed: boolean;
  isAutoScrappable: boolean;
  canGoIntoScrapStash: boolean;
  isLearnedRecipe: boolean;
  priceCheckResponse?: PriceCheckResponse;
}
