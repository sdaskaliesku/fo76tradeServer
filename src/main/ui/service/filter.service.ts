import {gameApiService} from "./game.api.service";

export interface FilterFlag {
  name: string
  value: string
  flags: Array<number>
  hasStarMods: boolean
  subtypes: Array<FilterFlag>
  parent: boolean
}

export interface UploadFilter {
  id: string
  text: string
  value: string
  checked: boolean
  isSeparator?: boolean
  tooltipText?: string
  isContext?: boolean
}

export enum FilterMode {
  "startsWith",
  "contains",
  "endsWidth",
  "equals",
  "notEquals",
  "in",
  "lt",
  "lte",
  "gt",
  "gte",
  "custom"
}

export interface FilterOption {
  value: any,
  field: string,
  mode: keyof typeof FilterMode
}

export interface TableFilter {
  name: string
  filterOptions: Array<FilterOption>
}

export interface UploadFileFilters {
  filterFlags?: Array<string>
}

export interface ItemContext {
  priceCheck?: boolean
  fed76Enhance?: boolean
  shortenResponse?: boolean
}

export const defaultTableFilters: Array<TableFilter> = [
  {
    name: 'Legendaries',
    filterOptions: [
      {
        value: true,
        mode: 'contains',
        field: 'isLegendary'
      }
    ]
  },
  {
    name: 'Tradable',
    filterOptions: [
      {
        value: true,
        mode: 'contains',
        field: 'isTradable'
      }
    ]
  },
  {
    name: 'Unknown plans',
    filterOptions: [
      {
        value: false,
        mode: 'contains',
        field: 'isLearnedRecipe'
      },
      {
        value: 'NOTES',
        mode: 'contains',
        field: 'filterFlag'
      },
    ]
  },
  {
    name: 'Listed in vendor',
    filterOptions: [
      {
        value: 0,
        mode: 'notEquals',
        field: 'vendingData.price'
      }
    ]
  }
];

class FilterService {

  private filterFlags: Array<FilterFlag> = [];
  private filterFlagsReady: boolean = false;

  private defaultUploadFilters: Array<UploadFilter> = [
    {
      id: 'tradableOnly',
      text: 'Tradable',
      value: 'Tradable',
      checked: false,
    },
    {
      id: 'legendaries',
      text: 'Legendaries',
      value: 'legendaries',
      checked: false,
    },
    {
      id: 'priceCheckOnly',
      text: 'Price Check Only',
      value: 'Price Check Only',
      checked: false,
    },
    {
      id: '1',
      text: '',
      value: '',
      checked: false,
      isSeparator: true
    },
    {
      id: 'priceCheck',
      text: 'Auto price check',
      value: 'priceCheck',
      checked: false,
      isContext: true,
      tooltipText: 'Automatically get price estimates(WARNING: this may take a lot of processing time)'
    },
    {
      id: 'shortenResponse',
      text: 'Shorten response',
      value: 'shortenResponse',
      checked: true,
      isContext: true,
      tooltipText: 'Get rids of some unused fields, so website performance is better'
    },
    {
      id: 'fed76Enhance',
      text: 'Enhance FED76',
      value: 'fed76Enhance',
      checked: true,
      isContext: true,
      tooltipText: 'If item is listed in vendor - item name, leg. mods is sent to FED76 service to enhance price check estimates and make them more realistic'
    },
    {
      id: '2',
      text: '',
      value: '',
      checked: false,
      isSeparator: true
    }
  ];

  constructor() {
    this.getFilterFlags().then(() => {
    });
  }

  private getFilterFlags(): Promise<Array<FilterFlag>> {
    if (this.filterFlagsReady) {
      return Promise.resolve(this.filterFlags);
    }
    return gameApiService.filterFlags().then(filterFlags => {
      this.filterFlags = filterFlags;
      this.filterFlagsReady = true;
      return Promise.resolve(this.filterFlags);
    }).catch((e) => {
      console.error("Error getting filter flags", e);
      this.filterFlags = [];
      this.filterFlagsReady = true;
      return Promise.reject(e);
    })
  }

  private buildUploadFilters(filterFlags: Array<FilterFlag>): Array<UploadFilter> {
    let filters: Array<UploadFilter> = [];
    filters.push(...this.defaultUploadFilters);
    filterFlags.filter(filterFlag => filterFlag.parent).forEach(filter => {
      filters.push({
        id: filter.value,
        text: filter.value,
        value: filter.value,
        checked: false,
      })
    })
    return filters;
  }

  getUploadFilters(): Promise<Array<UploadFilter>> {
    if (this.filterFlagsReady) {
      return Promise.resolve(this.buildUploadFilters(this.filterFlags));
    }
    return this.getFilterFlags().then(filterFlags => {
      return Promise.resolve(this.buildUploadFilters(filterFlags));
    })
  }

}

export const filterService = new FilterService();

