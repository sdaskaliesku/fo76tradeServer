import React from "react";
import {Accordion, AccordionTab} from "primereact/accordion";
import {tableSettingsService} from "../../service/tableSettings.service";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {InputText} from "primereact/inputtext";
import {ToggleButton} from "primereact/togglebutton";
import {Button} from "primereact/button";
import {SimpleColumn} from "../../service/table.columns";
import './SettingsPage.scss';

export class SettingsPage extends React.Component<any, any> {

  state = {
    tableData: tableSettingsService.getAllSavedColumns(),
    selected: null,
    field: '',
    header: '',
    visible: true,
    isBool: false
  }

  render() {
    const {tableData, visible, header, field, isBool, selected} = this.state;
    const setVisible = (value: boolean) => {
      this.setState({visible: value});
    }
    const setIsBool = (value: boolean) => {
      this.setState({isBool: value});
    }
    const setHeader = (e: any) => {
      this.setState({header: e.target.value});
    }
    const setField = (e: any) => {
      this.setState({field: e.target.value});
    }
    const clearNewColumn = () => {
      setVisible(true);
      setIsBool(false);
      setHeader({target: {value: ''}});
      setField({target: {value: ''}});
    };
    const addNewColumn = () => {
      if (!field || field.length < 1 || !header || header.length < 1) {
        console.error('Missing required fields');
        return;
      }
      const col: SimpleColumn = {
        field: field,
        header: header,
        visible: visible,
        isBool: isBool,
        isRating: false
      };
      setTableDataAndSave([...tableData, col]);
      clearNewColumn();
    }
    const clearAll = () => {
      setTableDataAndSave([]);
    }

    const setTableDataAndSave = (arr: Array<SimpleColumn>) => {
      this.setState({tableData: arr}, () => {
        tableSettingsService.save(arr);
      });
    }

    const deleteSelected = () => {
      if (this.state.selected) {
        // @ts-ignore
        this.state.selected.forEach((col: SimpleColumn) => {
          let columns = this.state.tableData.filter(val => val.field !== col.field);
          setTableDataAndSave(columns);
        });
      }
    }

    return (
        <Accordion multiple>
          <AccordionTab header="Custom table columns">
            <DataTable value={tableData}
                       emptyMessage={'No custom columns'}
                       rows={100}
                       className={"p-datatable-sm p-datatable-gridlines"}
                       selection={selected}
                       onSelectionChange={e => this.setState({selected: e.value})}
                       selectionMode="multiple"
            >
              <Column field="header" header="Header"/>
              <Column field="field" header="Field"/>
              <Column field="visible" header="Visible by default" body={(e: any) => {
                return String(e.visible)
              }}/>
              <Column field="isBool" header="isBoolean" body={(e: any) => {
                return String(e.isBool)
              }}/>
            </DataTable>
            <div className='divider'>
              <h2>Add new custom column</h2>
              <div className="card">
                <div className="p-grid p-fluid">
                  <div className="p-col-12 p-md-4 divider">
                    <div className="p-inputgroup">
                      <InputText placeholder="Field path" value={field}
                                 onChange={e => setField(e)}/>
                    </div>
                  </div>

                  <div className="p-col-12 p-md-4 divider">
                    <div className="p-inputgroup">
                      <InputText placeholder="Header name" value={header}
                                 onChange={e => setHeader(e)}/>
                    </div>
                  </div>

                  <div className="p-col-12 p-md-4 divider">
                    <div className="p-inputgroup">
                      <ToggleButton onChange={e => setVisible(e.value)}
                                    checked={visible}
                                    offLabel={'Visible by default'}
                                    onLabel={'Visible by default'}
                                    onIcon="pi pi-check" offIcon="pi pi-times"/>
                      <ToggleButton onChange={e => setIsBool(e.value)}
                                    checked={isBool}
                                    offLabel={'Is boolean type?'}
                                    onLabel={'Is boolean type?'}
                                    onIcon="pi pi-check" offIcon="pi pi-times"/>
                    </div>
                  </div>

                  <div className="p-col-12 p-md-4 divider">
                    <div className="p-inputgroup">
                      <Button className={'p-button-success'}
                              onClick={() => addNewColumn()}>Add</Button>
                      <Button className={'p-button-warning'}
                              onClick={() => clearNewColumn()}>Clear fields</Button>
                    </div>
                  </div>

                  <div className="p-col-12 p-md-4 divider">
                    <div className="p-inputgroup">
                      <Button className={'p-button-danger'} onClick={() => clearAll()}>Delete all
                        custom columns</Button>
                      <Button className={'p-button-danger'} onClick={() => deleteSelected()}>Delete
                        selected columns</Button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </AccordionTab>
        </Accordion>
    );
  }
}