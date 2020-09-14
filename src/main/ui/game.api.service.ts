import {BaseService} from "./base.service";

class GameApiService extends BaseService {

  constructor() {
    super('game/');
  }

  priceCheck(item: any) {
    const finalUrl = `${this.baseEndPoint}priceCheck`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: item,
    });
  }
}

export const gameApiService = new GameApiService();