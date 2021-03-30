export const LOCAL_STORAGE_KEYS = {
  TOKEN: 'fo76marketToken',
  TABLE_SETTINGS: 'fo76tableSettings',
  TABLE_COLUMNS: 'fo76tableColumns'
}

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
    return this.get(LOCAL_STORAGE_KEYS.TOKEN);
  }
}

export const localStorageService = new LocalStorageService();