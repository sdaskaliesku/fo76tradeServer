import {ModDataRequest} from "./domain";
import {BaseService} from "./base.service";

class ItemService extends BaseService {

  constructor() {
    super('items/');
    this.prepareModData = this.prepareModData.bind(this);
    this.prepareFedModData = this.prepareFedModData.bind(this);
  }

  prepareModData(modDataRequest: ModDataRequest) {
    const finalUrl = `${this.baseEndPoint}prepareModData`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: modDataRequest,
    });
  }

  prepareFedModData(modDataRequest: ModDataRequest) {
    const finalUrl = `${this.baseEndPoint}prepareFedModData`;
    const fedModDataRequest = {
      version: modDataRequest.modData.version,
      characterInventories: modDataRequest.modData.characterInventories
    };
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: fedModDataRequest,
    });
  }
}

export const itemService = new ItemService();