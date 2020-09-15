import {localStorageService} from "./localStorage.service";

export class BaseService {
  protected readonly baseEndPoint: string;

  constructor(baseEndPoint: string) {
    this.baseEndPoint = baseEndPoint;
  }

  protected performRequest = ({url, method, data}: { url: string, method: string, data: any }): any => {
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
      console.error(e);
    });
  };
}