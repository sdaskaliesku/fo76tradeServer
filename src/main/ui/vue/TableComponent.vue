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
    <!--    TODO: move toolbar to a separate component -->
    <div class="toolbar mb-2 mt-2" v-show="!isLoading">
      <b-button-group>
        <b-input-group right class="w-auto">
          <b-form-input v-model="maxItemsForPriceCheck" type="number"
                        placeholder="Items for price check"></b-form-input>
          <b-button class="my-2 my-sm-0" variant="info" type="submit" @click="priceCheck">
            Fed76 price check
          </b-button>
        </b-input-group>
        <b-button class="my-2 my-sm-0" variant="danger" type="submit" @click="deleteSelected">
          Delete Selected
        </b-button>
        <b-input-group right class="w-auto">
          <b-input-group-prepend is-text>
            <b-icon icon="person-fill"/>
          </b-input-group-prepend>
          <b-form-input class="rogue-char" v-model="rogueCharName" type="search"
                        placeholder="RogueTrader character name"></b-form-input>
        </b-input-group>
        <b-dropdown right text="Download" variant="success">
          <template v-for="downloadOption in downloadOptions">
            <b-dropdown-item v-if="downloadOption" class="export-option"
                             @click="() => createDownloadHandler(downloadOption, tabulator)">
              {{ downloadOption.title }}
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
        <b-dropdown right text="Columns">
          <template v-for="col in tableColumns">
            <b-form-checkbox v-model="col.visible" name="check-button" switch v-if="col.title">
              {{ col.title }}
            </b-form-checkbox>
          </template>
          <b-dropdown-divider></b-dropdown-divider>
          <b-form-checkbox v-model="useGrouping" name="check-button" switch>Use grouping
          </b-form-checkbox>
        </b-dropdown>
        <b-dropdown right text="Filters">
          <template v-for="filter in filters">
            <b-form-checkbox v-model="filter.checked" name="filter check-button" switch>
              {{ filter.name }}
            </b-form-checkbox>
          </template>
        </b-dropdown>
        <b-button class="my-2 my-sm-0" type="submit" @click="$bvModal.show('modal-weight-info')">
          Weight info
        </b-button>
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
    <b-modal id="modal-weight-info" hide-footer>
      <template v-slot:modal-title>
        Weight info
      </template>
      <div class="d-block text-center">
        <span class="d-block"><b>Total weight:</b> {{totalWeight}}</span>
        <template v-for="(value, name) in weightInfo">
          <span class="d-block"><b>{{name}}:</b> {{value}}</span>
        </template>
      </div>
      <b-button class="mt-3" block @click="$bvModal.hide('modal-weight-info')">Close</b-button>
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
import {downloadOptions, filters} from '../domain';
import {gameApiService} from '../game.api.service';
import Vue from 'vue';
import {Utils} from '../utils';
import {tableSettingsService} from '../tableSettings.service';

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
const maxPriceCheckItems = 950;

export default {
  name: 'TableComponent',
  props: {
    tableData: Array,
    config: {
      type: Object,
      required: false,
      default: function() {
        return {
          isFedEnhancer: false,
        };
      },
    },
  },
  methods: {
    getColumnsToDisplay: function() {
      let cols = [];
      columns.forEach(col => {
        if (col.visible) {
          cols.push(col);
        }
        if (col.field) {
          tableSettingsService.set({
            show: col.visible,
            field: col.field,
          });
        }
      });
      return cols;
    },
    priceCheck: async function() {
      this.isLoading = true;
      this.$bvToast.show('fed76');
      let priceCheckedItems = 0;
      let itemsForCheck = maxPriceCheckItems;
      let providedNumber = Number(this.maxItemsForPriceCheck);
      if (!Number.isNaN(providedNumber) || providedNumber > 0) {
        itemsForCheck = providedNumber;
      }
      for (const item of this.tableData) {
        if (Utils.isEligibleForPriceCheck(item) && Utils.isPriceCheckResponseEmpty(
            item.itemDetails.priceCheckResponse)) {
          item.itemDetails.priceCheckResponse = await gameApiService.priceCheck(item);
          if (priceCheckedItems++ >= itemsForCheck) {
            break;
          }
        }
      }
      this.$emit('updateTableData', this.tableData);
      this.isLoading = false;
    },
    singlePriceCheck: async function(e, cell) {
      e.preventDefault();
      e.stopPropagation();
      const cellItem = cell.getData();
      if (Utils.isEligibleForPriceCheck(cellItem)) {
        if (!Utils.isPriceCheckResponseEmpty(cellItem.itemDetails.priceCheckResponse)) {
          return;
        }
        if (shouldDisplayFed76Toast) {
          this.$bvToast.show('fed76');
          shouldDisplayFed76Toast = !shouldDisplayFed76Toast;
        }
        const priceCheckResponse = await gameApiService.priceCheck(cellItem);
        for (const item of this.tableData) {
          if (cellItem.id === item.id) {
            item.itemDetails.priceCheckResponse = priceCheckResponse;
            break;
          }
        }
        this.$emit('updateTableData', this.tableData);
      } else {
        this.$bvToast.toast(`Price check available only for tradable legendary armor and weapon!`, {
          title: 'FED76',
          variant: 'danger',
          autoHideDelay: 1000,
        });
      }
    },
    deleteSingleRow: function(e, cell) {
      e.preventDefault();
      e.stopPropagation();
      cell.getRow().delete().then(() => {
      });
      this.$emit('updateTableData', this.tabulator.getData());
    },
    deleteSelected: function() {
      const rows = this.tabulator.getSelectedRows();
      if (rows && rows.length > 0) {
        for (let i = 0; i < rows.length; i++) {
          rows[i].delete().then(() => {
          });
        }
      }
      this.$emit('updateTableData', this.tabulator.getData());
    },
    createDownloadHandler: function(option, tabulator) {
      return option.handler({tabulator, rawData: this.tableData, character: this.rogueCharName});
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
      return Utils.uuid();
    },
    getObjectValue: function(object, field) {
      return field.split('.').reduce((p, c) => p && p[c] || null, object);
    },
  },
  beforeMount: function() {
    columns.push({
      formatter: function() {
        return new Vue(
            {template: `<b-icon icon="cash" scale="2" variant="info" title="Fed76 Price estimate"/>`}).$mount().$el;
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
      cellClick: this.deleteSingleRow,
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
    this.tableData.forEach(item => {
      if (Number.isNaN(Number(this.weightInfo[item.filterFlag]))) {
        this.weightInfo[item.filterFlag] = 0;
      }
      const itemWeight = item.count * item.weight;
      this.weightInfo[item.filterFlag] += itemWeight;
      this.totalWeight += itemWeight;
    });
  },
  watch: {
    tableData: {
      handler: function(val) {
        this.tabulator.setData(val);
      },
      deep: true,
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
      rogueCharName: '',
      maxItemsForPriceCheck: maxPriceCheckItems,
      downloadOptions: downloadOptions,
      selectedItem: null,
      modalFields: modalFields,
      isLoading: false,
      weightInfo: {},
      totalWeight: 0
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

.rogue-char {
  width: 250px;
}

::v-deep .custom-control-label {
  text-transform: lowercase;
}

::v-deep .custom-control-label:first-letter {
  text-transform: uppercase;
}

</style>