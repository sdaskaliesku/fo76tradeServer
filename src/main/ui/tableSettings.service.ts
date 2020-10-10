import {LOCAL_STORAGE_KEYS, localStorageService} from "./localStorage.service";
import {columns} from "./table.columns";

class TableSettingsService {

  private columns: Map<string, boolean> = new Map<string, boolean>();


  constructor() {
    this.columns = TableSettingsService.load();
    console.log(this.columns);
    this.loadColumnsToDisplay();
  }

  private static load(): Map<string, boolean> {
    try {
      const data = JSON.parse(localStorageService.get(LOCAL_STORAGE_KEYS.TABLE_SETTINGS));
      if (data) {
        return new Map(data);
      }
    } catch (e) {
      console.log('Error loading table settings', e);
    }
    return new Map<string, boolean>();
  }

  public loadColumnsToDisplay() {
    if (this.columns.size < 1) {
      return;
    }
    columns.forEach(col => {
      // @ts-ignore
      const hasKey = this.columns.has(col.field);
      // @ts-ignore
      const value = this.columns.get(col.field);
      if (hasKey && value) {
        col.visible = true;
      } else {
        col.visible = !hasKey;
      }
    });
  }

  public save() {
    const data = JSON.stringify([...this.columns]);
    localStorageService.set(LOCAL_STORAGE_KEYS.TABLE_SETTINGS, data);
  }

  public set({show, field}: { show: boolean, field: string }) {
    this.columns.set(field, show);
    // console.log(this.columns);
    this.save();
  }

}

export const tableSettingsService = new TableSettingsService();