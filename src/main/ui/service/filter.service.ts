import {gameApiService} from "./game.api.service";
import {Item} from "./domain";

export declare interface FilterFlag {
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
}

export enum FilterMode {
  "startsWith", "contains", "endsWidth", "equals", "notEquals", "in", "lt", "lte", "gt", "gte", "custom"
}

export interface FilterOption {
  value: any,
  field: string,
  mode: keyof typeof FilterMode
}

export declare interface TableFilter {
  checked: boolean
  name: string

  predicate(item: Item): boolean;

  filterOptions: Array<FilterOption>
}

export declare interface UploadFileFilters {
  filterFlags?: Array<string>
}

export const defaultTableFilters: Array<TableFilter> = [
  {
    name: 'Legendaries',
    checked: false,
    predicate(item: Item): boolean {
      return item.isLegendary;
    },
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
    checked: false,
    predicate(item: Item): boolean {
      return item.isTradable;
    },
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
    checked: false,
    predicate(item: Item): boolean {
      return item.filterFlag === 'NOTES' && !item.isLearnedRecipe;
    },
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
    checked: false,
    predicate(item: Item): boolean {
      // @ts-ignore
      return item.vendingData && item.vendingData.price && item.vendingData.price !== 0;
    },
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
      id: '',
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

  private static isSameFilter(filter: UploadFilter, input: string): boolean {
    return filter.text === input || filter.id === input || filter.value === input;
  }

  getUploadFileFilters(input: Array<string>): Promise<UploadFileFilters> {
    return this.getFilterFlags().then(filterFlags => {
      let uploadFilters = this.buildUploadFilters(filterFlags);
      const uploadFileFilter: UploadFileFilters = {
        filterFlags: []
      };
      input.forEach(selectedFilter => {
        uploadFilters.forEach(filter => {
          if (FilterService.isSameFilter(filter, selectedFilter)) {
            uploadFileFilter.filterFlags?.push(filter.text);
          }
        });
      });
      return Promise.resolve(uploadFileFilter);
    });
  }

  private buildTableFilters(filterFlags: Array<FilterFlag>): Array<TableFilter> {
    let filters: Array<TableFilter> = [];
    filters.push(...defaultTableFilters);
    // filterFlags.forEach(filterFlag => {
    //   filters.push({
    //     name: filterFlag.value,
    //     checked: false,
    //     predicate: filterFlagPredicate(filterFlag.name)
    //   })
    // });
    return filters;
  }

  getTableFilters(): Promise<Array<TableFilter>> {
    if (this.filterFlagsReady) {
      return Promise.resolve(this.buildTableFilters(this.filterFlags));
    }
    return this.getFilterFlags().then(filterFlags => {
      return Promise.resolve(this.buildTableFilters(filterFlags));
    })
  }
}

const filterFlagPredicate = (filterFlag: string) => {
  return (item: Item) => {
    return item.filterFlag === filterFlag;
  }
}

export const filterService = new FilterService();

