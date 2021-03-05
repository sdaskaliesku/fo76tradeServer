import {ModDataRequest, ReportItem} from "./domain";
import {BaseService} from "./base.service";

class ItemService extends BaseService {

  constructor() {
    super('items/');
    this.prepareModData = this.prepareModData.bind(this);
    this.reportItem = this.reportItem.bind(this);
  }

  prepareModData(modDataRequest: ModDataRequest) {
    const finalUrl = `${this.baseEndPoint}prepareModData`;
    return this.performRequest({
      url: finalUrl,
      method: 'POST',
      data: modDataRequest,
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
}

export const itemService = new ItemService();