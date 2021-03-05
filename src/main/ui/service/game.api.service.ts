import {BaseService} from "./base.service";
import {Item, PriceCheckResponse, ReportItem} from "./domain";
import {FilterFlag} from "./filter.service";

class GameApiService extends BaseService {

  constructor() {
    super('game/');
  }

  priceCheck(item: Item): Promise<PriceCheckResponse> {
    const finalUrl = `${this.baseEndPoint}priceCheck`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: item,
    });
  }

  reportItem(item: ReportItem): Promise<any> {
    const finalUrl = `${this.baseEndPoint}report`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: item
    })
  }

  filterFlags(): Promise<Array<FilterFlag>> {
    const finalUrl = `${this.baseEndPoint}filterFlags`;
    return this.performRequest({
      url: finalUrl,
      method: 'GET',
    });
  }
}

export const gameApiService = new GameApiService();