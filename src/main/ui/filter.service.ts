import {FilterFlag} from "./domain";
import {gameApiService} from "./game.api.service";

declare interface UploadFilter {
  id: string
  text: string
  value: string
  checked: boolean
}

declare interface UploadFileFilters {
  filterFlags?: Array<String>
  legendaryOnly: boolean
  tradableOnly: boolean
  priceCheckOnly: boolean
}

class FilterService {

  private filterFlags: Array<FilterFlag> = [];
  private filterFlagsReady: boolean = false;
  private defaultFilters: Array<UploadFilter> = [
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
    filters.push(...this.defaultFilters);
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
        tradableOnly: false,
        legendaryOnly: false,
        priceCheckOnly: false,
        filterFlags: []
      };
      input.forEach(selectedFilter => {
        if (!uploadFileFilter.tradableOnly) {
          uploadFileFilter.tradableOnly = FilterService.isSameFilter(this.defaultFilters[0], selectedFilter);
        }
        if (!uploadFileFilter.legendaryOnly) {
          uploadFileFilter.legendaryOnly = FilterService.isSameFilter(this.defaultFilters[1], selectedFilter);
        }
        if (!uploadFileFilter.priceCheckOnly) {
          uploadFileFilter.priceCheckOnly = FilterService.isSameFilter(this.defaultFilters[2], selectedFilter);
        }
        uploadFilters.forEach(filter => {
          if (FilterService.isSameFilter(filter, selectedFilter)) {
            uploadFileFilter.filterFlags?.push(filter.text);
          }
        });
      });
      return Promise.resolve(uploadFileFilter);
    });
  }
}

export const filterService = new FilterService();

