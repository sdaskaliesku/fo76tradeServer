const LOCAL_STORAGE_TOKEN_KEY: string = 'fo76marketToken';

class LocalStorageService {
  get(key: string): string {
    return <string>localStorage.getItem(key);
  }

  set(key: string, value: string): void {
    localStorage.setItem(key, value);
  }

  delete(key: string): void {
    localStorage.removeItem(key);
  }

  getToken(): string {
    return this.get(LOCAL_STORAGE_TOKEN_KEY);
  }
}

export const localStorageService = new LocalStorageService();