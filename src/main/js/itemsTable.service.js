import {columns} from './table.columns';

const $table = $('#output');

export class ItemsTableService {

  table;

  constructor(data) {
    this.table = $table.bootstrapTable({
      columns,
      pagination: true,
      search: true,
      data,
    });
  }

}