<template>
  <div>
    <div>
      <div class="align-items-xl-center d-flex flex-column justify-content-center mb-3 mt-5"
           v-if="isLoading">
        <b-spinner label="Loading..."></b-spinner>
        <h2>
          <b-badge variant="warning">Price estimates are loading, this may take a while...Stay
            tuned
          </b-badge>
        </h2>
      </div>
    </div>
    <div class="toolbar mb-2 mt-2" v-show="!isLoading">
      <b-button-group>
        <b-button class="my-2 my-sm-0" variant="info" type="submit" @click="priceCheck">
          Fed76 price check
        </b-button>
        <b-button class="my-2 my-sm-0" variant="danger" type="submit" @click="deleteSelected">
          Delete
        </b-button>
        <b-dropdown right text="Table options">
          <template v-for="col in tableColumns">
            <b-form-checkbox v-model="col.visible" name="check-button" switch v-if="col.title">
              {{ col.title }}
            </b-form-checkbox>
          </template>
          <b-dropdown-divider></b-dropdown-divider>
          <b-form-checkbox v-model="useGrouping" name="check-button" switch>Use grouping
          </b-form-checkbox>
        </b-dropdown>
        <b-dropdown right text="Export options">
          <template v-for="exportOption in exportOptions">
            <b-dropdown-item class="export-option" :data-type="exportOption" @click="exportClick">
              {{ exportOption }}
            </b-dropdown-item>
          </template>
        </b-dropdown>
        <b-input-group right class="w-auto">
          <b-input-group-prepend is-text>
            <b-icon icon="search"/>
          </b-input-group-prepend>
          <b-form-input v-model="searchText" type="search" placeholder="Search..."></b-form-input>
          <b-input-group-append is-text>
            <b-icon icon="x" @click="clearSearch"/>
          </b-input-group-append>
        </b-input-group>
        <b-dropdown right text="Filters">
          <template v-for="filter in filters">
            <b-form-checkbox v-model="filter.checked" name="filter check-button" switch>
              {{ filter.name }}
            </b-form-checkbox>
          </template>
        </b-dropdown>
      </b-button-group>
    </div>
    <div ref="table" class="table-bordered table-dark table-striped table-sm"
         v-show="!isLoading"></div>
    <b-modal size="xl" scrollable ok-only centered v-if="selectedItem" id="itemDetailsModal"
             :title="selectedItem.text">
      <template v-for="field in modalFields">
        <span class="modal-row"
              v-if="!isEmpty(getObjectValue(selectedItem, field.field))"><b>{{ field.name }}: </b>{{
            getObjectValue(selectedItem, field.field)
          }}</span>
      </template>
      <template v-if="!isEmpty(selectedItem.stats)">
        <b>Additional parameters:</b>
        <b-list-group v-for="stat in selectedItem.stats" v-bind:key="randomstring()">
          <b-list-group-item class="modal-row" v-if="!isEmpty(stat)">
            Text: {{ stat.text }},
            DamageType: {{ stat.damageType }},
            Value: {{ stat.value }}
          </b-list-group-item>
        </b-list-group>
      </template>
      <template
          v-if="!isEmpty(selectedItem.legendaryMods) && !isEmpty(selectedItem.legendaryMods[0]) && !isEmpty(selectedItem.legendaryMods[0].value)">
        <b>Legendary mods:</b>
        <b-list-group v-for="mod in selectedItem.legendaryMods" v-bind:key="randomstring()">
          <b-list-group-item class="modal-row" v-if="!isEmpty(mod) && !isEmpty(mod.value)">
            {{ mod.value }}
          </b-list-group-item>
        </b-list-group>
      </template>
    </b-modal>
    <b-toast id="fed76" variant="info">
      <template v-slot:toast-title>
        <div class="d-flex flex-grow-1 align-items-baseline">
          <strong class="mr-auto">Thanks to <a href="https://fed76.info/"
                                               target="_blank">imprezobus</a>!</strong>
        </div>
      </template>
      Price estimates powered by <a href="https://fed76.info/pricing/"
                                    target="_blank">PriceCheck</a> tool
    </b-toast>
  </div>
</template>

<script>
import {columns, modalFields} from '../table.columns';
import Tabulator from 'tabulator-tables';
import {filters} from '../domain';
import {gameApiService} from '../game.api.service';
import Vue from 'vue';

const tableConfig = {
  layout: 'fitColumns',
  placeholder: 'No Data Set',
  tooltips: true,
  pagination: 'local',
  paginationSize: 50,
  movableColumns: true,
  groupBy: 'filterFlag',
  paginationSizeSelector: [5, 10, 20, 50, 100, 500, 1000, 5000, 50000],
};
const priceCheckFilterFlags = ['WEAPON', 'ARMOR', 'WEAPON_RANGED', 'WEAPON_MELEE'];

const tableFilters = () => {
  const tFilters = [];
  filters.forEach((filter) => {
    if (filter.types && filter.types.length > 0) {
      filter.types.forEach(type => {
        tFilters.push({
          checked: false,
          name: type.replace('_', ' '),
          filterGroup: type,
        });
      });
    } else {
      tFilters.push({
        checked: false,
        name: filter.name,
        type: filter.id,
      });
    }
  });
  return tFilters;
};

let shouldDisplayFed76Toast = true;

export default {
  name: 'TableComponent',
  props: {
    tableData: Array,
  },
  methods: {
    getColumnsToDisplay: function() {
      let cols = [];
      columns.forEach(col => {
        if (col.visible) {
          cols.push(col);
        }
      });
      return cols;
    },
    priceCheck: async function() {
      this.isLoading = true;
      this.$bvToast.show('fed76');
      for (const item of this.tableData) {
        if (item.isLegendary && item.isTradable && priceCheckFilterFlags.includes(
            item.filterFlag)) {
          item.itemDetails.priceCheckResponse = await gameApiService.priceCheck(item);
          this.tabulator.setData(this.tableData);
        }
      }
      this.isLoading = false;
    },
    singlePriceCheck: async function(e, cell) {
      e.preventDefault();
      e.stopPropagation();
      const cellItem = cell.getData();
      if (cellItem.isLegendary && cellItem.isTradable && priceCheckFilterFlags.includes(
          cellItem.filterFlag)) {
        if (shouldDisplayFed76Toast) {
          this.$bvToast.show('fed76');
          shouldDisplayFed76Toast = !shouldDisplayFed76Toast;
        }
        const priceCheckResponse = await gameApiService.priceCheck(cellItem);
        for (const item of this.tableData) {
          if (cellItem.id === item.id) {
            item.itemDetails.priceCheckResponse = priceCheckResponse;
            this.tabulator.setData(this.tableData);
            return;
          }
        }
      } else {
        this.$bvToast.toast(`Price check available only for tradable legendary armor and weapon!`, {
          title: 'FED76',
          variant: 'danger',
          autoHideDelay: 1000
        })
      }
    },
    deleteSelected: function() {
      const rows = this.tabulator.getSelectedRows();
      if (rows && rows.length > 0) {
        for (let i = 0; i < rows.length; i++) {
          rows[i].delete().then(() => {
          });
        }
      }
    },
    exportClick: function(e) {
      const type = e.target.dataset['type'];
      this.tabulator.download(type, `data.${type}`, {style: true});
    },
    clearSearch: function() {
      this.searchText = '';
    },
    getAppliedFilters: function() {
      const tableFilters = [];
      const filterGroups = [];
      this.filters.forEach((filter) => {
        if (!filter.checked) {
          return;
        }
        if (filter.filterGroup) {
          filterGroups.push(filter.filterGroup);
        } else {
          if (filter.type === 'legendaries') {
            tableFilters.push({
              field: 'numLegendaryStars',
              type: '>',
              value: 0,
            });
          } else if (filter.type === 'tradableOnly') {
            tableFilters.push({
              field: 'isTradable',
              type: '=',
              value: true,
            });
          }
        }
      });
      if (filterGroups.length > 0) {
        tableFilters.push({
          field: 'filterFlag',
          type: 'in',
          value: filterGroups,
        });
      }
      if (this.searchText.length > 0) {
        tableFilters.push({
          field: columns[1].field,
          type: 'like',
          value: this.searchText,
        });
      }
      return tableFilters;
    },
    rowClick: function(e, row) {
      this.selectedItem = row.getData();
      this.$bvModal.show('itemDetailsModal');
    },
    isEmpty: function(input) {
      const isUndef = input === undefined;
      const isNull = input === null;
      let isEmpty = false;
      const type = typeof input;
      if (type === String || type === Array) {
        isEmpty = input.length <= 0;
      }
      return isUndef || isNull || isEmpty;
    },
    randomstring: function() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
        const r = parseFloat(
            '0.' + Math.random().toString().replace('0.', '') + new Date().getTime()) * 16 | 0,
            v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    },
    getObjectValue: function(object, field) {
      return field.split('.').reduce((p, c) => p && p[c] || null, object);
    },
  },
  beforeMount: function() {
    columns.push({
      formatter: function() {
        return new Vue({template: `<b-icon icon="cash" scale="2" variant="info" title="Fed76 Price estimate"/>`}).$mount().$el;
      },
      width: 10,
      hozAlign: 'center',
      headerSort: false,
      download: false,
      visible: true,
      cellClick: this.singlePriceCheck,
    });
    columns.push({
      formatter: 'buttonCross',
      width: 10,
      hozAlign: 'center',
      headerSort: false,
      download: false,
      visible: true,
      cellClick: function(e, cell) {
        e.preventDefault();
        e.stopPropagation();
        cell.getRow().delete().then(() => {
        });
      },
    });
  },
  mounted: function() {
    this.tabulator = new Tabulator(this.$refs.table, {
      data: this.tableData,
      columns: this.getColumnsToDisplay(),
      rowClick: this.rowClick,
      ...tableConfig,
    });
    this.$root.$on('bv::modal::hidden', function() {
      this.selectedItem = null;
    });
  },
  watch: {
    tableData: {
      handler: function(val) {
        this.tabulator.setData(val);
      },
    },
    useGrouping: {
      handler: function() {
        if (this.useGrouping) {
          this.tabulator.setGroupBy('filterFlag');
        } else {
          this.tabulator.setGroupBy('');
        }
      },
    },
    tableColumns: {
      handler: function() {
        this.tabulator.setColumns(this.getColumnsToDisplay());
      },
      deep: true,
    },
    searchText: {
      handler: function() {
        setTimeout(() => {
          this.tabulator.setFilter(this.getAppliedFilters());
        }, 500);
      },
    },
    filters: {
      handler: function() {
        this.tabulator.setFilter(this.getAppliedFilters());
      },
      deep: true,
    },
  },
  data() {
    return {
      tabulator: null,
      tableColumns: columns,
      useGrouping: true,
      filters: tableFilters(),
      searchText: '',
      exportOptions: ['csv', 'html', 'json'],
      selectedItem: null,
      modalFields: modalFields,
      isLoading: false,
    };
  },
};
</script>

<style scoped>
.modal-row {
  display: block;
}

.toolbar {
  display: flex;
  justify-content: flex-end;
}

.export-option {
  text-transform: uppercase;
}

::v-deep .custom-control-label {
  text-transform: lowercase;
}

::v-deep .custom-control-label:first-letter {
  text-transform: uppercase;
}

</style>