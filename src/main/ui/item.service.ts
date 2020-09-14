import {ModDataRequest} from "./domain";
import {BaseService} from "./base.service";

class ItemService extends BaseService {

  constructor() {
    super('items/');
  }

  prepareModData(modDataRequest: ModDataRequest) {
    const finalUrl = `${this.baseEndPoint}prepareModData`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: modDataRequest,
    });
  }
}

export const itemService = new ItemService();