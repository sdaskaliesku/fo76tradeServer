<template>
  <div>
    <DataTable :value="tableData" class="p-datatable-sm p-datatable-gridlines p-datatable-striped"
               :paginator="true" :rows="50"
               paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
               :rowsPerPageOptions="[5, 10, 20, 50, 100, 500, 1000, 5000, 50000]"
               currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
               resizableColumns
               ref="dt"
               paginatorPosition="both"

               autoLayout
               reorderableColumns
               :expandableRowGroups="false"
               v-model:filters="filters"
               filterDisplay="menu"
               v-model:expandedRows="expandedRows"

               sortMode="single"
               sortField="filterFlag"
               :sortOrder="1"
               scrollable
               removableSort
    >
      <template #header>
        <div class="p-d-flex p-jc-between p-ai-center">
          <span class="p-input-icon-left">
                <i class="pi pi-search" />
                <InputText v-model="filters['global'].value" placeholder="Keyword Search" />
            </span>
        </div>
        <div style="text-align:left">
          <div style="text-align:left">
            <MultiSelect :modelValue="selectedColumns" :options="tableColumns" optionLabel="header"
                         @update:modelValue="onToggle"
                         placeholder="Select Columns" style="width: 20em"/>
            <Button icon="pi pi-external-link" label="Export" @click="exportCSV($event)"/>
          </div>
        </div>
      </template>
      <Column :expander="true"></Column>
      <Column header="#">
        <template #body="slotProps">
          {{slotProps.index + 1}}
        </template>
      </Column>
<!--      <Column header="#">-->
<!--        <template #body="slotProps">-->
<!--          {{slotProps.index + 1}}-->
<!--        </template>-->
<!--      </Column>-->
<!--      <Column header="test"></Column>-->
<!--      <template v-for="column of selectedColumns" :key="column.field">-->
<!--        <Column :field="column.field" :header="column.header" sortable>-->
<!--          <template #body="slotProps" v-if="column.field === 'numLegendaryStars'">-->
<!--            <Rating :modelValue="slotProps.data.numLegendaryStars" :readonly="true" :cancel="false"-->
<!--                    :stars="5"/>-->
<!--          </template>-->
<!--        </Column>-->
<!--      </template>-->
      <template #expansion="slotProps">
        <json-viewer :value="slotProps.data" copyable  />
      </template>
    </DataTable>
  </div>
</template>

<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import MultiSelect from 'primevue/multiselect';
import Rating from 'primevue/rating';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import {items} from '../../sample';
import {columns} from '../../table.columns';
import {FilterMatchMode} from 'primevue/api';

export default {
  name: 'TableV2',
  components: {DataTable, Column, MultiSelect, Button, Rating, InputText},
  data() {
    return {
      // TODO: add default columns
      selectedColumns: [],
      expandedRowGroups: null,
      tableData: [],
      tableColumns: [],
      expandedRows: [],
      filters: {
        'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
      },
    };
  },
  methods: {
    onToggle(value) {
      this.selectedColumns = this.tableColumns.filter(col => value.includes(col));
    },
    exportCSV() {
      this.$refs.dt.exportCSV();
    },
  },
  mounted() {
    columns.forEach(col => {
      const tableCol = {
        field: col.field,
        header: col.title,
      };
      this.tableColumns.push(tableCol);
      if (col.visible) {
        this.selectedColumns.push(tableCol);
      }
    });
    this.tableData = items;
  },
};
</script>

<style scoped>
 * {
   font-size: 10px;
 }


</style>