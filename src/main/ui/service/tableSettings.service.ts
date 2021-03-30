import {LOCAL_STORAGE_KEYS, localStorageService} from "./localStorage.service";
import {ColumnDefinition, columns, createColumnDef, SimpleColumn,} from "./table.columns";

class TableSettingsService {

  private columnsInUse: Array<ColumnDefinition> = [...columns];

  public save(obj: Array<SimpleColumn>) {
    localStorageService.set(LOCAL_STORAGE_KEYS.TABLE_COLUMNS, JSON.stringify(obj));
  }

  private static load(): Array<SimpleColumn> {
    try {
      return JSON.parse(localStorageService.get(LOCAL_STORAGE_KEYS.TABLE_COLUMNS));
    } catch (e) {
      console.error('Error loading table columns');
    }
    return [];
  }

  public saveNewColumn(obj: SimpleColumn) {
    const savedColumns = TableSettingsService.load();
    savedColumns.push(obj);
    this.save(savedColumns);
  }

  public getAllSavedColumns(): Array<ColumnDefinition> {
    const savedColumns = TableSettingsService.load();
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