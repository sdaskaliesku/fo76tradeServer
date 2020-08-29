import {localStorageService} from './localStorage.service';

const performRequest = ({url, method, data}) => {
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
    params.headers['Authorization'] = `Bearer ${token}`;
  }
  return fetch(url, params).
  then((resp) => resp.json(), (e) => {
    console.log(e);
  });
};

class ItemService {

  constructor() {
    this.baseEndPoint = 'items/';
  }

  prepareModData(modDataRequest) {
    const finalUrl = `${this.baseEndPoint}prepareModData`;
    return performRequest({
      url: finalUrl,
      method: 'POST',
      data: modDataRequest,
    });
  }
}

export const itemService = new ItemService();