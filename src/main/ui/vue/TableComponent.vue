<template>
  <div>
    <div class="toolbar mb-2 mt-2">
      <b-button-group>
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
    <div ref="table" class="table-bordered table-dark table-striped table-sm"></div>
    <b-modal ok-only centered v-if="selectedItem" id="itemDetailsModal" :title="selectedItem.text">
      <template v-for="field in modalFields">
        <span class="modal-row" v-if="!isEmpty(getObjectValue(selectedItem, field.field))"><b>{{field.name}}: </b>{{ getObjectValue(selectedItem, field.field) }}</span>
      </template>
      <template v-if="!isEmpty(selectedItem.stats)">
        <b>Additional parameters:</b>
        <b-list-group v-for="stat in selectedItem.stats" v-bind:key="randomstring()">
          <b-list-group-item class="modal-row" v-if="!isEmpty(stat)">
            Text: {{ stat.text }},
            DamageType: {{stat.damageType}},
            Value: {{stat.value}}
          </b-list-group-item>
        </b-list-group>
      </template>
      <template v-if="selectedItem.legendaryMods && selectedItem.legendaryMods[0].value" >
        <b>Legendary mods:</b>
        <b-list-group v-for="mod in selectedItem.legendaryMods" v-bind:key="randomstring()">
          <b-list-group-item class="modal-row" v-if="!isEmpty(mod)">{{mod.value}}</b-list-group-item>
        </b-list-group>
      </template>
    </b-modal>
  </div>
</template>

<script>
import {columns} from '../table.columns';
import Tabulator from 'tabulator-tables';
import {filters} from '../domain';
const randomstringFunc = require('randomstring');

const modalFields = [
  {
    name: 'Account',
    field: 'ownerInfo.accountOwner',
  },
  {
    name: 'Character',
    field: 'ownerInfo.characterOwner',
  },
  {
    name: 'Description',
    field: 'description',
  },
  {
    name: 'Type',
    field: 'filterFlag',
  },
  {
    name: 'Legendary',
    field: 'isLegendary',
  },
  {
    name: 'Tradable',
    field: 'isTradable',
  },
  {
    name: 'Level',
    field: 'itemLevel',
  },
  {
    name: 'Count',
    field: 'count',
  },
  {
    name: 'Game Price',
    field: 'itemValue',
  },
  {
    name: 'Legendary',
    field: 'isLegendary',
  },
  {
    name: 'Legendary stars',
    field: 'numLegendaryStars',
  },
  {
    name: 'Weight',
    field: 'weight',
  },
  {
    name: 'Abbreviation',
    field: 'abbreviation',
  }
];

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
      let isEmptyString = false;
      const type = typeof input;
      if (type === String || type === Array) {
        isEmptyString = input.length < 0;
      }
      return isUndef ||isNull || isEmptyString;
    },
    randomstring: function(params) {
      return randomstringFunc.generate(params);
    },
    getObjectValue: function(object, field) {
      return field.split('.').reduce((p, c) => p && p[c] || null, object);
    }
  },
  mounted: function() {
    this.tabulator = new Tabulator(this.$refs.table, {
      data: this.tableData,
      columns: this.getColumnsToDisplay(),
      rowClick: this.rowClick,
      ...tableConfig,
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
      modalFields: modalFields
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