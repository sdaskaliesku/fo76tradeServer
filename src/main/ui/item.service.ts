import {localStorageService} from './localStorage.service';
import {ModDataRequest} from "./domain";

const performRequest = ({url, method, data}: { url: string, method: string, data: any }) => {
  const params = {
    method,
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  };
  const token = localStorageService.getToken();
  if (token !== null && token !== undefined && token.length > 0) {
    // @ts-ignore
    params.headers['Authorization'] = `Bearer ${token}`;
  }
  return fetch(url, params).then((resp) => resp.json(), (e) => {
    console.log(e);
  });
};

class ItemService {
  private readonly baseEndPoint: string;

  constructor() {
    this.baseEndPoint = 'items/';
  }

  prepareModData(modDataRequest: ModDataRequest) {
    const finalUrl = `${this.baseEndPoint}prepareModData`;
    return performRequest({
      url: finalUrl,
      method: 'POST',
      data: modDataRequest,
    });
  }
}

export const itemService = new ItemService();