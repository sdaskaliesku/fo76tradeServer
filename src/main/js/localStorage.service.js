const LOCAL_STORAGE_TOKEN_KEY = 'fo76marketToken';

class LocalStorageService {
  get(key) {
    return localStorage.getItem(key);
  }

  set(key, value) {
    localStorage.setItem(key, value);
  }

  delete(key) {
    localStorage.removeItem(key);
  }

  getToken() {
    return this.get(LOCAL_STORAGE_TOKEN_KEY);
  }
}

export const localStorageService = new LocalStorageService();