import {LOCAL_STORAGE_KEYS, localStorageService} from "./localStorage.service";
import {ColumnDefinition, columns, createColumnDef, SimpleColumn,} from "./table.columns";

class TableSettingsService {

  private columnsInUse: Array<ColumnDefinition> = [...columns];

  TableSettingsService() {
    this.getAllColumns();
  }

  public saveNewColumn(obj: SimpleColumn) {
    const savedColumns = JSON.parse(localStorageService.get(LOCAL_STORAGE_KEYS.TABLE_COLUMNS));
    savedColumns.push(obj);
    localStorageService.set(LOCAL_STORAGE_KEYS.TABLE_COLUMNS, JSON.stringify(savedColumns));
  }

  private getAllSavedColumns(): Array<ColumnDefinition> {
    const savedColumns = JSON.parse(localStorageService.get(LOCAL_STORAGE_KEYS.TABLE_COLUMNS));
    const newColumns: Array<ColumnDefinition> = [];

    if (savedColumns && savedColumns.length > 0) {
      savedColumns.forEach((col: ColumnDefinition) => {
        if (!columns.some(e => e.field === col.field)) {
          newColumns.push(createColumnDef({...col}));
        }
      });
    }
    return newColumns;
  }


  public getAllColumns(): Array<ColumnDefinition> {
    const newColumns: Array<ColumnDefinition> = this.getAllSavedColumns();
    this.columnsInUse = [...columns, ...newColumns];
    return this.columnsInUse;
  }

  public getColumns(): Array<ColumnDefinition> {
    return this.columnsInUse;
  }

}

export const tableSettingsService = new TableSettingsService();
tableSettingsService.getAllColumns();