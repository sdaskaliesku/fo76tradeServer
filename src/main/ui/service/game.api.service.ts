import {BaseService} from "./base.service";
import {AppInfo, Item, PriceCheckResponse} from "./domain";
import {FilterFlag} from "./filter.service";
import {HUDEditorSchema} from "../component/config/HUDEditor";

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

  filterFlags(): Promise<Array<FilterFlag>> {
    const finalUrl = `${this.baseEndPoint}filterFlags`;
    return this.performRequest({
      url: finalUrl,
      method: 'GET',
    });
  }

  appInfo(): Promise<AppInfo> {
    const finalUrl = `${this.baseEndPoint}appInfo`;
    return this.performRequest({
      url: finalUrl,
      method: 'GET',
    });
  }

  hudEditorConfig(): Promise<HUDEditorSchema> {
    const finalUrl = `${this.baseEndPoint}hudEditorConfig`;
    return this.performRequest({
      url: finalUrl,
      method: 'GET',
    });
  }
}

export const gameApiService = new GameApiService();