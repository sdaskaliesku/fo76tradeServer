import {itemService} from './item.service';
import {columns} from './table.columns';
import {
  applyTableFilters,
  getFilters,
  initDownloadFilters,
  initTableFilters,
  initUploadFilters,
} from './dom.functions';

let table;
$(document).ready(function() {

  initUploadFilters();
  $('#deleteSelected').click(() => {
    let rows = table.getSelectedRows();
    if (rows && rows.length > 0) {
      for (let i = 0; i < rows.length; i++) {
        rows[i].delete().then(() => {
        });
      }
    }
  });

  const filterValue = $('#filter-value');
  $('#clear-filter').click(() => {
    filterValue.val('');
    applyTableFilters(table);
  });
  $('#clear-all-filters').click(() => {
    clearAllFilters();
  });
  filterValue.on('change', () => applyTableFilters(table));
  filterValue.on('keyup', () => applyTableFilters(table));

  const clearAllFilters = () => {
    filterValue.val('');
    initTableFilters(table);
    table.clearFilter();
  };

  $('.download').click(function() {
    const format = $(this).data('format');
    table.download(format, 'data.' + format);
  });

  $('.custom-file-input').on('change', function() {
    var fileName = $(this).val().split('\\').pop();
    $(this).siblings('.custom-file-label').addClass('selected').html(fileName);
  });
  let modDataRequest = {};
  modDataRequest.modData = {};
  $('#file').on('change', function() {
    let fileReader = new FileReader();
    fileReader.onload = () => {
      try {
        modDataRequest.modData = JSON.parse(fileReader.result);
      } catch (e) {
        alert(
            'Malformed file, please send the file to author(manson_ew2) to fix the issue!');
      }
    };
    const files = $(this).prop('files');
    if (files.length > 0) {
      fileReader.readAsText(files[0]);
    }
  });
  $('#prepare').click(() => {
    if ($.isEmptyObject(modDataRequest.modData)) {
      alert('Please, select inventory dump file');
      return;
    }
    table = new Tabulator('#output', {
      layout: 'fitColumns',
      placeholder: 'No Data Set',
      tooltips: true,
      pagination: 'local',
      paginationSize: 50,
      movableColumns: true,
      groupBy: 'filterFlag',
      paginationSizeSelector: [5, 10, 20, 50, 100, 500, 1000, 5000],
      columns: columns,
    });
    initDownloadFilters(table);
    clearAllFilters();
    modDataRequest.filters = getFilters();
    itemService.prepareModData(modDataRequest).then(data => {
      if (data === null || data === undefined || data.length < 1) {
        alert(
            'Malformed file, please send the file to author(manson_ew2) to fix the issue!');
        return;
      }
      table.setData(data);
      $('#output-wrapper').show();
    }, () => alert(
        'Malformed file, please send the file to author(manson_ew2) to fix the issue!'));
  });
});