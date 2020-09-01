import {columns, filters} from './table.columns';

export const initUploadFilters = () => {
  let filtersWrapper = $('#filters');
  filtersWrapper.empty();
  filters.forEach(filter => {
    let filterEl = $(createToggleButton(filter.name, '', 'filter'),
        filter.checked);
    if (filter.filters && filter.filters.length > 0) {
      filterEl.data('filter', filter.filters);
    }
    if (filter.id) {
      filterEl.attr('id', filter.id);
    }
    filtersWrapper.append(filterEl);
    filterEl.bootstrapToggle({
      on: filter.name,
      off: filter.name,
      width: 120,
      size: 'small',
    });
  });
};

const createToggle = ($wrapper, $el, name, width = 150) => {
  $wrapper.append($el);
  $el.bootstrapToggle({
    on: name,
    off: name,
    width,
    height: 30,
    size: 'small',
  });
};

export const initTableFilters = (table) => {
  let filtersWrapper = $('#table-filters');
  filtersWrapper.empty();
  filters.forEach(filter => {
    if (filter.hide) {
      return;
    }
    if (filter.types && filter.types.length > 0) {
      filter.types.forEach(fil => {
        let filterEl = $(createToggleButton(fil, fil, 'table-filter'));
        createToggle(filtersWrapper, filterEl, fil.replace('_', ' '));
      });
    } else {
      let filterEl = $(
          createToggleButton(filter.name, filter.name, 'table-filter'));
      createToggle(filtersWrapper, filterEl, filter.name);
    }
  });
  let tableFilters = $('.table-filter');
  tableFilters.off('change');
  tableFilters.on('change', () => {
    applyTableFilters(table);
  });
};

export const updateFilterByName = () => {
  const filterValue = $('#filter-value');
  const value = filterValue.val();
  if (value && value.length > 0) {
    return {
      field: 'text',
      type: 'like',
      value: filterValue.val(),
    };
  }
};

export const applyTableFilters = (table) => {
  let tableFilters = $('.table-filter');
  let filters = [];
  let filterGroups = [];
  tableFilters.each(function() {
    const filterEl = $(this);
    const isChecked = filterEl.is(':checked');
    const fieldName = filterEl.data('column');
    if (isChecked) {
      if (fieldName === 'legendaries') {
        filters.push({
          field: 'numLegendaryStars',
          type: '>',
          value: 0,
        });
      } else {
        filterGroups.push(fieldName);
      }
    }
  });
  if (filterGroups.length > 0) {
    filters.push({
      field: 'filterFlag',
      type: 'in',
      value: filterGroups,
    });
  }
  let filterByName = updateFilterByName();
  if (filterByName) {
    filters.push(filterByName);
  }
  table.clearFilter();
  table.setFilter(filters);
};

export const initDownloadFilters = (table) => {
  let $card = $('.card.card-body');
  $card.empty();
  columns.forEach(col => {
    if (col.title) {
      let column = $(
          createToggleButton(col.title, col.field, 'download-column', true));
      $card.append(column);
      column.bootstrapToggle({
        on: col.title,
        off: col.title,
        width: 120,
        size: 'small',
      });
    }
  });
  $('.download-column').on('change', (e) => {
    let column = $(e.target);
    let columnName = column.data('column');
    let isChecked = column.is(':checked');
    table.updateColumnDefinition(columnName, {download: isChecked}).
    then(() => {
    });
  });
};

const createToggleButton = (name, field, claz, checked) => {
  let check = '';
  if (checked) {
    check = 'checked';
  }
  return `<input data-column="${field}" 
           class="${claz}" 
           type="checkbox"
           ${check}/>`;
};

export const getFilters = () => {
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
      if (filterAttr) {
        if (typeof filterAttr !== 'number' && filterAttr.includes(',')) {
          let filters = el.data('filter').split(',');
          filterFlags = filterFlags.concat(filters);
        } else {
          filterFlags.push(filterAttr);
        }
      }
    }
  }
  itemFilters.filterFlags = filterFlags;
  itemFilters.legendaryOnly = $('#legendaries').is(':checked');
  itemFilters.tradableOnly = $('#tradableOnly').is(':checked');
  return itemFilters;
};