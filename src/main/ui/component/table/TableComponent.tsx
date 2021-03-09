import {DataTable, DataTableProps} from "primereact/datatable";
import {ColumnDefinition, columns} from "../../service/table.columns";
import {MultiSelect} from "primereact/multiselect";
import {InputText} from "primereact/inputtext";
import React from "react";
import {Rating} from "primereact/rating";
import {Column} from "primereact/column";
import {Item, PriceCheckResponse} from "../../service/domain";
import {Button} from "primereact/button";
import {Menubar} from "primereact/menubar";
import {MenuItem} from "primereact/components/menuitem/MenuItem";
import {RogueService} from "../../service/rogue.service";
import {Utils} from "../../service/utils";
import ReactJson from "react-json-view";
import './TableComponent.scss';
import {InfoDialog} from "../dialog/InfoDialog";
import {ItemStatsDialog} from "../dialog/ItemStatsDialog";
import {gameApiService} from "../../service/game.api.service";
import {Toast} from "primereact/toast";
import {Prompt} from "react-st-modal";
import {defaultTableFilters, TableFilter} from "../../service/filter.service";
import {itemService} from "../../service/item.service";

enum ExportType {
  JSON, CSV, ROGUE_TRADER
}

const defaultTableProps: DataTableProps = {
  rows: 100,
  className: "p-datatable-sm p-datatable-gridlines",
  paginator: true,
  paginatorTemplate: "CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown",
  rowsPerPageOptions: [20, 50, 100, 500, 1000, 5000, 50000],
  currentPageReportTemplate: "Showing {first} to {last} of {totalRecords}",
  resizableColumns: true,
  autoLayout: true,
  reorderableColumns: true,
  sortMode: 'single',
  sortOrder: -1,
  scrollable: true,
  removableSort: true,
  paginatorPosition: 'both',
  dataKey: 'id',
};

const filterOptions = {
  filter: true,
  filterMatchMode: 'contains',
  filterPlaceholder: 'Search'
}

export interface TableState {
  globalFilter?: any,
  selectedColumns: Array<any>,
  selectedItems: Array<Item>,
  currentTableData: Array<Item>,
  expandedRows: Array<any>,
  itemStatsDialog: boolean,
  tableData: Array<Item>,
  filterFlagsItems: any,
  exportFilename: string,
  sortField: string,
  tableFilters: any
}

export interface TableProps extends DataTableProps {
  value: Array<Item>
}


export class TableComponent extends React.Component<TableProps, TableState> {
  state: TableState = {
    globalFilter: null,
    selectedColumns: [],
    selectedItems: [],
    currentTableData: [],
    expandedRows: [],
    itemStatsDialog: false,
    tableData: this.props.value,
    filterFlagsItems: {},
    exportFilename: 'dump',
    sortField: 'filterFlag',
    tableFilters: {}
  }
  private table: any = React.createRef<DataTable>();
  private infoDialog: any = React.createRef();
  private toast: any = React.createRef();
  private itemStatsDialog: any = React.createRef();
  private menuItems: Array<MenuItem> = [];


  private onTableValueChange(data: Array<Item>) {
    if (!data || data.length < 1) {
      this.setCurrentTableData(this.state.tableData);
    } else {
      this.setCurrentTableData(data);
    }
    const filterFlagStats = this.calculateFilterFlagItems(this.getItemsToUse());
    this.setState({filterFlagsItems: filterFlagStats});
  }

  private calculateFilterFlagItems(items: Array<Item>) {
    let filterFlags = {};
    if (!items || items.length < 1) {
      return filterFlags;
    }
    items.forEach((item: Item) => {
      // @ts-ignore
      if (!filterFlags[item.filterFlag]) {
        // @ts-ignore
        filterFlags[item.filterFlag] = 0;
      }
      // @ts-ignore
      filterFlags[item.filterFlag] += 1;
    });
    return filterFlags;
  }

  private applyFilter(filter: TableFilter) {
    return () => {
      const additionalFilters = new Set(this.state.selectedColumns);
      const filterFields = {};
      filter.filterOptions.forEach(filterOption => {
        columns.forEach(col => {
          if (col.field === filterOption.field) {
            additionalFilters.add(col);
          }
        });
        // @ts-ignore
        filterFields[filterOption.field] = {
          value: filterOption.value,
          matchMode: filterOption.mode
        };
      });
      this.setState({tableFilters: filterFields});
      this.setSelectedColumns(Array.from(additionalFilters));
    };
  }

  private buildFilterButtons(): Array<MenuItem> {
    let buttons: Array<MenuItem> = [];
    defaultTableFilters.forEach((filter: TableFilter) => {
      const filterButton: MenuItem = {
        label: filter.name,
        command: this.applyFilter(filter)
      };
      buttons.push(filterButton);
    });
    return buttons;
  }

  componentDidMount() {
    this.onFilter = this.onFilter.bind(this);
    this.headerTemplate = this.headerTemplate.bind(this);
    this.onSelectedRow = this.onSelectedRow.bind(this);
    this.onColumnToggle = this.onColumnToggle.bind(this);
    this.applyFilter = this.applyFilter.bind(this);
    this.actionsColumn = this.actionsColumn.bind(this);
    this.createHeader = this.createHeader.bind(this);
    this.menuStart = this.menuStart.bind(this);
    this.onGlobalSearch = this.onGlobalSearch.bind(this);
    this.onTableValueChange = this.onTableValueChange.bind(this);
    this.setItemStatsDialog = this.setItemStatsDialog.bind(this);
    this.onItemsStats = this.onItemsStats.bind(this);
    this.onBulkPriceCheck = this.onBulkPriceCheck.bind(this);
    this.clearFilters = this.clearFilters.bind(this);
    this.onSelectedPriceCheck = this.onSelectedPriceCheck.bind(this);
    this.onDeleteSelectedItems = this.onDeleteSelectedItems.bind(this);
    this.onReportSelectedItems = this.onReportSelectedItems.bind(this);
    this.onReportItem = this.onReportItem.bind(this);
    this.setSelectedColumns(columns.filter(col => col.visible));
    this.menuItems.push({
      label: 'Filters',
      icon: 'pi pi-fw pi-filter',
      items: [
        ...this.buildFilterButtons(),
        {
          separator: true
        },
        {
          label: 'Clear all filters',
          command: this.clearFilters,
        },
      ],
    });
    this.menuItems.push({
      label: 'Download',
      icon: 'pi pi-fw pi-download',
      items: [
        {
          label: 'CSV',
          command: this.export(ExportType.CSV),
        },
        {
          label: 'Rogue trader',
          command: this.export(ExportType.ROGUE_TRADER),
        },
        {
          label: 'JSON',
          command: this.export(ExportType.JSON),
        },
      ],
    });
    this.menuItems.push({
      label: 'Actions',
      icon: 'pi pi-fw pi-pencil',
      items: [
        {
          label: 'Delete selected',
          command: this.onDeleteSelectedItems,
          icon: 'pi pi-fw pi-trash',
        },
        {
          label: 'Report selected',
          command: this.onReportSelectedItems,
          icon: 'pi pi-fw pi-exclamation-triangle',
        },
        {
          label: 'Price check selected',
          command: this.onSelectedPriceCheck,
          icon: 'pi pi-fw pi-money-bill',
        },
      ],
    });
    this.menuItems.push({
      label: 'Items stats',
      command: this.onItemsStats,
      icon: 'pi pi-fw pi-book',
    });
    this.menuItems.push({
      label: 'Price check',
      command: this.onBulkPriceCheck,
      icon: 'pi pi-fw pi-money-bill',
    });
  }

  private export(type: ExportType) {
    return () => {
      switch (type) {
        case ExportType.CSV:
          Prompt(
              'Please, provide file name',
              {
                isRequired: true
              }
          ).then((result) => {
            if (!result || result.length < 1) {
              this.toast.current.show({
                severity: 'error',
                summary: 'Invalid file name!',
                detail: `Provided value: ${result}`
              });
              return;
            }
            this.setState({exportFilename: result}, () => {
              this.table.current.exportCSV();
            });
          });
          return;
        case ExportType.JSON:
          Prompt(
              'Please, provide file name',
              {
                isRequired: true
              }
          ).then((result) => {
            if (!result || result.length < 1) {
              this.toast.current.show({
                severity: 'error',
                summary: 'Invalid file name!',
                detail: `Provided value: ${result}`
              });
              return;
            }
            const json = JSON.stringify(this.getItemsToUse());
            Utils.downloadString(json, 'text/json', `${result}.json`);
          });
          return;
        case ExportType.ROGUE_TRADER:
          Prompt(
              'Please, provide your rogue trader character name',
              {
                isRequired: true
              }
          ).then((result) => {
            if (!result || result.length < 1) {
              this.toast.current.show({
                severity: 'error',
                summary: 'Invalid nick name!',
                detail: `Provided value: ${result}`
              });
              return;
            }
            const csv = RogueService.toCSV(this.getItemsToUse(), result);
            Utils.downloadString(csv, 'text/csv', `RogueTrader_${result}.csv`);
          });
          return;
      }
    }
  }

  private bulkPriceCheck(items: Array<Item>, maxItems: number = 99999) {
    let i = 0;
    let promises: Array<Promise<any>> = [];
    this.toast.current.show({
      severity: 'info',
      summary: 'Bulk price check starts now...',
      detail: 'You will get new message, once we will get all the prices'
    });
    items.forEach((item: Item) => {
      if (i < maxItems) {
        if (!Utils.shouldPriceCheck(item)) {
          console.warn('Ignoring price check', item);
          return;
        }
        promises.push(this.onPriceCheck(item, true));
        i++;
      }
    });
    console.log(`Price check executed for ${i} items`);
    Promise.all(promises).finally(() => {
      console.log('Bulk price check done!')
      this.toast.current.show({
        severity: 'info',
        summary: 'Bulk price check done!',
        sticky: true
      });
    });
  }

  private onSelectedPriceCheck() {
    this.bulkPriceCheck(this.state.selectedItems);
  }

  private onBulkPriceCheck() {
    Prompt(
        'Please, provide max amount of items for price check',
        {
          isRequired: true,
          defaultValue: 900
        }
    ).then((result) => {
      if (result === undefined) {
        return;
      }
      let maxItems = Number(result);
      if (Number.isNaN(maxItems) || maxItems <= 0) {
        this.toast.current.show({
          severity: 'error',
          summary: 'Invalid amount of items!',
          detail: `Provided value: ${result}`
        });
      } else {
        this.bulkPriceCheck(this.getItemsToUse(), maxItems);
      }
    });
  }

  private getItemsToUse(): Array<Item> {
    let itemsToUse: Array<Item> = this.state.tableData;
    if (this.state.currentTableData && this.state.currentTableData.length > 0) {
      itemsToUse = this.state.currentTableData;
    }
    return itemsToUse;
  }

  private onItemsStats() {
    this.itemStatsDialog.current.show(this.getItemsToUse());
  }

  private setExpandedRows(data: any) {
    this.setState({expandedRows: data});
  }

  private setGlobalFilter(data: any) {
    this.setState({globalFilter: data});
  }

  private setSelectedColumns(data: any) {
    this.setState({selectedColumns: data});
  }

  private setSelectedItems(data: any) {
    this.setState({selectedItems: data});
  }

  private onGlobalSearch(e: any) {
    setTimeout(() => {
      this.setGlobalFilter(e.target.value);
    }, 100);
  }

  private onColumnToggle(event: any) {
    let selected = event.value as Array<ColumnDefinition>;
    let orderedSelectedColumns = columns.filter(col => selected.some(sCol => sCol.field === col.field));
    this.setSelectedColumns(orderedSelectedColumns);
  }

  private clearFilters() {
    try {
      Object.keys(this.table.current.state.filters).forEach(key => {
        delete this.table.current.state.filters[key];
      });
    } catch (_) {

    }
    this.setState({tableFilters: {}});
    this.table.current.filter();
  }

  private setCurrentTableData(data: Array<Item>) {
    this.setState({currentTableData: data});
  }

  componentDidUpdate(prevProps: DataTableProps) {
    if (!this.state.currentTableData) {
      this.setCurrentTableData(this.table.current.props.value);
    }
  }

  private menuStart(selectedColumns: any) {
    return () => (
        <React.Fragment>
          <span>Columns to display&nbsp;</span>
          <MultiSelect value={selectedColumns} options={columns} optionLabel="header"
                       onChange={this.onColumnToggle} style={{width: '20em'}}/>
        </React.Fragment>)
  }

  private menuEnd(onGlobalSearch: any) {
    return () => {
      return (
          <span className="p-input-icon-left">
            <i className="pi pi-search"/>
            <InputText type="search" onInput={onGlobalSearch}
                       placeholder="Global Search"/>
          </span>
      );
    }
  }

  private createHeader() {
    const {selectedColumns} = this.state;
    return (
        <div style={{textAlign: 'left'}}>
          <Menubar model={this.menuItems}
                   start={this.menuStart(selectedColumns)}
                   end={this.menuEnd(this.onGlobalSearch)}/>
        </div>
    )
  }

  private onSelectedRow(e: any) {
    this.setSelectedItems(e.value);
  }

  private static ratingBodyTemplate(rowData: any) {
    return <Rating value={rowData.numLegendaryStars} readOnly={true} stars={5} cancel={false}/>;
  }

  private static booleanBodyTemplate(col: ColumnDefinition) {

    return (rowData: Item) => {
      // @ts-ignore
      const value: any = col.getValue(rowData);
      if (value) {
        return '✔️';
      }
      return '❌'
    }
  }

  private static rowExpansionTemplate(data: any) {
    return <ReactJson src={data}
                      enableClipboard={true}
                      collapseStringsAfterLength={false}
                      collapsed={1}
                      theme={'summerfruit'}
                      displayDataTypes={false}
                      name={false}
                      iconStyle={'square'}
    />;
  }

  private headerTemplate(data: any) {
    if (!this || !this.state) {
      return;
    }
    const numItems = this.state.filterFlagsItems[data.filterFlag];
    return (
        <React.Fragment>
          <b>{data.filterFlag + `(${numItems})`}</b>
        </React.Fragment>
    );
  }

  private static footerTemplate() {
    return (<React.Fragment/>);
  }

  private triggerItemUpdate(item: Item) {
    this.setExpandedRows({
      [item.id]: true
    });
    const expanded = this.state.expandedRows;
    // @ts-ignore
    delete expanded[item.id]
    this.setExpandedRows(expanded);
  }

  private onPriceCheck(data: Item, disableToast: boolean = false) {
    if (!Utils.shouldPriceCheck(data)) {
      console.warn(`Item cannot be price checked`, data);
      if (!disableToast) {
        this.toast.current.show({
          severity: 'warn',
          summary: 'Item cannot be price checked!',
          detail: `Item name: ${data.text}.`
        });
      }
      return Promise.resolve();
    }
    return gameApiService.priceCheck(data).then((response: PriceCheckResponse) => {
      this.getItemsToUse()?.forEach((item: Item) => {
        if (item.id === data.id) {
          item.priceCheckResponse = response;
          if (!item.priceCheckResponse || !item.priceCheckResponse.price || item.priceCheckResponse.price < 0) {
            item.priceCheckResponse.price = -1;
          }
          this.triggerItemUpdate(data);
        }
      });
      return Promise.resolve();
    }).catch(e => {
      console.error(`Error price checking item ${data}`, e);
      this.getItemsToUse()?.forEach((item: Item) => {
        if (item.id === data.id) {
          item.priceCheckResponse = {
            price: -1
          };
          this.triggerItemUpdate(data);
        }
      });
      return Promise.resolve();
    });
  }

  private onReportItem(data: any) {
    Prompt(
        'Please, provide reason for report',
        {
          isRequired: true
        }
    ).then((result) => {
      if (result && result.length > 0) {
        itemService.reportItem({
          item: data,
          reason: result
        }).then(() => {
          this.toast.current.show({
            severity: 'success',
            summary: 'Item successfully reported!',
            detail: `Item name: ${data.text}. Reason: ${result}`
          });
        }).catch((e) => {
          console.error(`Error reporting item`, data, e);
        })
      }
    });
  }

  private setTableData(items: Array<Item>) {
    this.setState({tableData: items});
  }

  private deleteItem(data: Item) {
    this.setTableData(this.state.tableData.filter(x => x.id !== data.id));
  }

  private onDeleteItem(data: any) {
    this.deleteItem(data);
  }

  private onDeleteSelectedItems() {
    this.state.selectedItems.forEach((item: Item) => {
      setTimeout(() => {
        this.deleteItem(item);
      }, 10);
    })
  }

  private onReportSelectedItems() {
    this.state.selectedItems.forEach((item: Item) => {
      setTimeout(() => {
        this.onReportItem(item);
      }, 10);
    })
  }

  private actionsColumn(item: Item) {
    const disabled = !Utils.shouldPriceCheck(item);
    return (
        <React.Fragment>
          <Button icon="pi pi-money-bill"
                  className="p-button-rounded p-button-icon p-mr-2"
                  tooltip={'Price check'}
                  onClick={() => this.onPriceCheck(item)}
                  disabled={disabled}
                  tooltipOptions={{event: 'focus'}}
          />
          <Button icon="pi pi-exclamation-triangle"
                  className="p-button-rounded p-button-warning p-mr-2"
                  tooltip={'Report item'}
                  onClick={() => this.onReportItem(item)}/>
          <Button icon="pi pi-trash"
                  className="p-button-rounded p-button-danger"
                  tooltip={'Delete item'}
                  onClick={() => this.onDeleteItem(item)}/>
        </React.Fragment>
    );
  }

  private dynamicColumns() {
    return this.state.selectedColumns.map((col: ColumnDefinition) => {
      if (col.isRating) {
        return <Column
            key={col.field}
            columnKey={col.field}
            field={col.field}
            header={col.header}
            body={TableComponent.ratingBodyTemplate}
            sortable={true}
            {...filterOptions}
        />;
      }
      return <Column
          key={col.field}
          body={col.isBool ? TableComponent.booleanBodyTemplate(col) : ''}
          columnKey={col.field}
          field={col.field}
          header={col.header}
          sortable={true}
          {...filterOptions}
      />;
    });
  }

  private setItemStatsDialog(data: any) {
    this.setState({itemStatsDialog: data});
  }

  private onFilter(e: any) {
    setTimeout(() => {
      this.setState({tableFilters: e.filters});
    }, 100)
  }

  render() {
    return (
        <React.Fragment>
          <InfoDialog ref={this.infoDialog}/>
          <ItemStatsDialog ref={this.itemStatsDialog}/>
          <DataTable {...defaultTableProps} value={this.state.tableData}
                     ref={this.table}
                     filters={this.state.tableFilters}
                     onFilter={this.onFilter}
                     header={this.createHeader()}
                     globalFilter={this.state.globalFilter}
                     onSelectionChange={this.onSelectedRow}
                     selection={this.state.selectedItems}
                     rowExpansionTemplate={TableComponent.rowExpansionTemplate}
              // rowGroupMode="subheader"
              // expandableRowGroups={true}
                     groupField="filterFlag"
                     rowGroupHeaderTemplate={this.headerTemplate}
                     rowGroupFooterTemplate={TableComponent.footerTemplate}
                     onValueChange={this.onTableValueChange}
                     expandedRows={this.state.expandedRows}
                     onRowToggle={(e) => this.setExpandedRows(e.data)}
                     exportFilename={this.state.exportFilename}
                     sortField={this.state.sortField}
          >
            <Column selectionMode="multiple" headerStyle={{width: '3em'}}/>
            <Column expander={true} headerStyle={{width: '3em'}}/>
            {this.dynamicColumns()}
            <Column exportable={false} header={'Actions'} body={this.actionsColumn}/>
          </DataTable>
          <Toast ref={this.toast}/>
        </React.Fragment>
    );
  }
}