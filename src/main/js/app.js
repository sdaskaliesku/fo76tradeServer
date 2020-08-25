$(document).ready(function() {
  $('.check_button').click(function() {
    const check_box = $(this).find('input');
    const checked = $(check_box).data('checked');
    if (checked) {
      $(check_box).data('checked', false);
    } else {
      $(check_box).data('checked', true);
    }
  });
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
    table.clearFilter();
  });
  filterValue.on('change', () => updateFilter());
  filterValue.on('keyup', () => updateFilter());

  const updateFilter = () => {
    table.setFilter('text', 'like', filterValue.val());
  };

  let getFilters = () => {
    let itemFilters = {
      filterFlags: [],
      legendaryOnly: false,
      tradableOnly: true,
    };
    let filterFlags = [];
    let els = $('.filter');
    for (let i = 0; i < els.length; i++) {
      let el = $(els[i]);
      if (el.is(':checked')) {
        let filterAttr = el.data('filter');
        if (typeof filterAttr !== 'number' && filterAttr.includes(',')) {
          let filters = el.data('filter').split(',');
          filterFlags = filterFlags.concat(filters);
        } else {
          filterFlags.push(filterAttr);
        }
      }
    }
    itemFilters.filterFlags = filterFlags;
    itemFilters.legendaryOnly = $('#legendaries').is(':checked');
    itemFilters.tradableOnly = $('#tradableOnly').is(':checked');
    return itemFilters;
  };

  let table = new Tabulator('#output', {
    layout: 'fitColumns',
    placeholder: 'No Data Set',
    tooltips: true,
    pagination: 'local',
    paginationSize: 50,
    groupBy: 'filterFlag',
    paginationSizeSelector: [5, 10, 20, 50, 100, 500, 1000, 5000],
    columns: [
      {
        formatter: 'rowSelection',
        titleFormatter: 'rowSelection',
        width: 10,
        hozAlign: 'center',
        headerSort: false,
        download: false,
        cellClick: function(e, cell) {
          cell.getRow().toggleSelect();
        },
      },
      {title: 'Name', field: 'text', sorter: 'string'},
      {
        title: 'Stars',
        field: 'numLegendaryStars',
        formatter: 'star',
        width: 90,
      },
      {title: 'Type', field: 'filterFlag', sorter: 'string', width: 180},
      {title: 'Level', field: 'itemLevel', sorter: 'number', width: 90},
      {title: 'Count', field: 'count', sorter: 'number', width: 100},
      {title: '1 star', field: 'legendaryModsTemp.0', sorter: 'string'},
      {title: '2 star', field: 'legendaryModsTemp.1', sorter: 'string'},
      {title: '3 star', field: 'legendaryModsTemp.2', sorter: 'string'},
      {title: 'Description', field: 'description', sorter: 'string'},
      {
        formatter: 'buttonCross',
        width: 10,
        hozAlign: 'center',
        headerSort: false,
        download: false,
        cellClick: function(e, cell) {
          cell.getRow().delete().then(() => {
          });
        },
      },
    ],
  });

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
        alert('Malformed file, please send the file to author(manson_ew2) to fix the issue!');
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
    filterValue.val('');
    table.clearFilter();
    modDataRequest.filters = getFilters();
    fetch('items/prepareModData', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(modDataRequest),
    }).then((resp) => resp.json(), () => alert('Malformed file, please send the file to author(manson_ew2) to fix the issue!')).then(data => {
      if (data === null || data === undefined || data.length < 1) {
        alert('Malformed file, please send the file to author(manson_ew2) to fix the issue!');
        return;
      }
      table.setData(data);
      $('#output-wrapper').show();
    }, () => alert('Malformed file, please send the file to author(manson_ew2) to fix the issue!'));
  });
});