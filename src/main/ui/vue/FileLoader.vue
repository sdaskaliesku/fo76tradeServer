<template>
  <div>
    <div v-if="!tableData">
      <b-form-group label="Filters for uploading" label-size="lg">
        <b-form-checkbox-group
            v-model="selected"
            :options="options"
            switches
            size="lg"
        ></b-form-checkbox-group>
      </b-form-group>
    </div>
    <b-form-file v-if="!tableData"
        v-model="file"
        :state="Boolean(file)"
        placeholder="Choose a file or drop it here..."
        drop-placeholder="Drop file here..."
    ></b-form-file>
    <div>
      <div class="d-flex justify-content-center mb-3 mt-5" v-if="loading">
        <b-spinner label="Loading..."></b-spinner>
      </div>
    </div>
    <b-modal title="Error" ok-variant="danger" centered ok-only id="errorModal">
      {{modalText}}
    </b-modal>
    <table-component class="mt-2" v-if="tableData && tableData.length > 0" :table-data="tableData"/>
  </div>
</template>

<script>
import {Utils} from '../utils';
import {filters, MIN_MOD_SUPPORTED_VERSION} from '../domain';
import {itemService} from '../item.service';
import TableComponent from './TableComponent';

const modalTexts = {
  invalidVersion: (version) => {
    return `You are using unsupported mod version ${version}. Minimum supported version of the mod: ${MIN_MOD_SUPPORTED_VERSION}. Please, update the mod, extract your items and try uploading new file again.`;
  },
  emptyResult: () => {
    return `Something went wrong. Please, reach out author of the mod/website and send the file for investigation. You may ask for help in discord (HELP button at the top).`;
  },
};

export default {
  name: 'FileLoader',
  components: {TableComponent},
  data() {
    return {
      file: null,
      selected: [],
      options: [],
      tableData: null,
      loading: false,
      modalText: '',
    };
  },
  beforeMount() {
    filters.forEach((filter) => {
      this.options.push({
        text: this.capitalize(filter.name),
        value: filter.filters ? filter.filters : filter.name,
        checked: filter.checked,
      });
    });
  },
  watch: {
    file: function(val) {
      if (val != null) {
        this.loading = true;
        this.tableData = [];
        Utils.readFile(val).then((data) => {
          const modFileVersion = data.version;
          if (modFileVersion < MIN_MOD_SUPPORTED_VERSION) {
            this.showModal(modalTexts.invalidVersion(modFileVersion));
            this.loading = false;
            this.file = null;
            return Promise.reject();
          }
          return itemService.prepareModData({
            modData: data,
            filters: this.getUploadDataFilters(),
          });
        }).then(
            (items) => {
              if (items && items.length > 0) {
                this.tableData = items;
              } else {
                this.file = null;
                this.showModal(modalTexts.emptyResult());
              }
            },
            e => console.error(e),
        ).finally(() => this.loading = false);
      }
    },
  },
  methods: {
    showModal: function(text) {
      this.modalText = text;
      this.$bvModal.show('errorModal');
    },
    capitalize: (input) => {
      return input.charAt(0).toUpperCase() + input.slice(1).toLowerCase();
    },
    getUploadDataFilters: function() {
      let uploadFileFilters = {
        filterFlags: [],
        legendaryOnly: false,
        tradableOnly: false,
      };
      this.selected.forEach((v) => {
        let valNumber = Number(v);
        if (v.includes(',') || !Number.isNaN(valNumber)) {
          let nums = v.split(',').map(x => Number(x));
          uploadFileFilters.filterFlags = uploadFileFilters.filterFlags.concat(nums);
        } else {
          if (v === filters[0].name) {
            uploadFileFilters.tradableOnly = true;
          } else if (v === filters[1].name) {
            uploadFileFilters.legendaryOnly = true;
          }
        }
      });
      return uploadFileFilters;
    },
  },
};
</script>