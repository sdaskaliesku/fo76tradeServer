<template>
  <div>
    <DataTable :value="tableData" class="p-datatable-striped p-datatable-sm p-datatable-gridlines" :paginator="true" :rows="50"
               paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
               :rowsPerPageOptions="[5, 10, 20, 50, 100, 500, 1000, 5000, 50000]"
               currentPageReportTemplate="Showing {first} to {last} of {totalRecords}"
               resizableColumns ref="dt">
      <template #header>
        <div style="text-align:left">
          <MultiSelect :value="selectedColumns" :options="cols" optionLabel="header" @input="onToggle"
                       placeholder="Select Columns" style="width: 20em"/>
          <div style="text-align: left">
            <Button icon="pi pi-external-link" label="Export" @click="exportCSV($event)" />
          </div>
        </div>
      </template>
      <Column v-for="col of cols" :field="col.field" :header="col.header" :key="col.field" sortable></Column>
    </DataTable>
  </div>
</template>

<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import MultiSelect from 'primevue/MultiSelect';
import Button from 'primevue/Button';
import {items} from '../../sample';
import {columns} from '../../table.columns';

export default {
  name: 'TableV2',
  components: {DataTable, Column, MultiSelect, Button},
  data() {
    return {
      selectedColumns: null,
      tableData: [],
      cols: [],
    };
  },
  methods: {
    onToggle(value) {
      this.selectedColumns = this.cols.filter(col => value.includes(col));
    },
    exportCSV() {
      this.$refs.dt.exportCSV();
    }
  },
  mounted() {
    const colls = [];
    columns.forEach(col => {
      colls.push({
        field: col.field,
        header: col.title
      });
    })
    this.tableData = items;
    console.log('mounted');
    this.cols = colls;
  }
};
</script>

<style scoped>

</style>