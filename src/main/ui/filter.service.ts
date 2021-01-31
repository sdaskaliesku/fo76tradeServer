import {gameApiService} from "./game.api.service";

export declare interface FilterFlag {
  value: string
  flags: Array<Number>
  hasStarMods: boolean
  subtypes: Array<FilterFlag>
}

declare interface UploadFilter {
  id: string
  text: string
  value: string
  checked: boolean
}

declare interface TableFilter {
  checked: boolean
  name: string
  type: string
  filterGroup?: string
}

export declare interface UploadFileFilters {
  filterFlags?: Array<String>
}

class FilterService {

  private filterFlags: Array<FilterFlag> = [];
  private filterFlagsReady: boolean = false;
  private defaultTableFilters: Array<TableFilter> = [
    {
      name: 'Legendaries',
      type: 'legendaries',
      checked: false
    },
    {
      name: 'Tradable',
      type: 'tradableOnly',
      checked: false
    },
    {
      name: 'Unknown plans',
      type: 'unknownPlans',
      checked: false
    }
  ];
  private defaultUploadFilters: Array<UploadFilter> = [
    {
      id: 'tradableOnly',
      text: 'Tradable',
      value: 'Tradable',
      checked: true,
    },
    {
      id: 'legendaries',
      text: 'Legendaries',
      value: 'legendaries',
      checked: true,
    },
    {
      id: 'priceCheckOnly',
      text: 'Price Check Only',
      value: 'Price Check Only',
      checked: true,
    }
  ];

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
    filterFlags.forEach(filter => {
      filters.push({
        id: filter.value,
        text: filter.value,
        value: filter.value,
        checked: true,
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
    filters.push(...this.defaultTableFilters);
    filterFlags.forEach(filterFlag => {
      filters.push({
        type: filterFlag.value,
        name: filterFlag.value,
        filterGroup: filterFlag.value.toUpperCase(),
        checked: false
      })
    });
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

  isLegendaryTableFilter(filter: TableFilter) {
    return filter.type === this.defaultTableFilters[0].type;
  }

  isTradableTableFilter(filter: TableFilter) {
    return filter.type === this.defaultTableFilters[1].type;
  }

  isUnknownPlansTableFilter(filter: TableFilter) {
    return filter.type === this.defaultTableFilters[2].type;
  }

  getLegendaryTableFilter() {
    return {
      field: 'numLegendaryStars',
      type: '>',
      value: 0,
    };
  }

  getTradableTableFilter() {
    return {
      field: 'isTradable',
      type: '=',
      value: true,
    };
  }

  getUnknownPlansTableFilters() {
    return [
      {
        field: 'isLearnedRecipe',
        type: '=',
        value: false,
      },
      {
        field: 'filterFlag',
        type: '=',
        value: 'NOTES',
      },
    ];
  }

  getFilterFlagTableFilter(filterFlags: any) {
    return {
      field: 'filterFlag',
      type: 'in',
      value: filterFlags,
    };
  }
}

export const filterService = new FilterService();

