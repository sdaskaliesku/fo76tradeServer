import {BaseService} from "./base.service";
import {PriceCheckResponse} from "./domain";
import {FilterFlag} from "./filter.service";

class GameApiService extends BaseService {

  constructor() {
    super('game/');
  }

  priceCheck(item: any): Promise<PriceCheckResponse> {
    const finalUrl = `${this.baseEndPoint}priceCheck`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: item,
    });
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