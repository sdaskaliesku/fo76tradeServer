import {BaseService} from "./base.service";
import {PriceCheckResponse} from "./domain";

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
}

export const gameApiService = new GameApiService();