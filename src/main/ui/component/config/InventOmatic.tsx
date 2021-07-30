import React from "react";
import "./BaseInventOmatic.scss";
import {materialCells, materialRenderers} from "@jsonforms/material-renderers";
import {JsonForms} from "@jsonforms/react";
import {Button} from "@material-ui/core";
import {createAjv} from "@jsonforms/core";
import {Utils} from "../../service/utils";
import {inventOmaticPipboySchema} from "./pipboySchema";
import {inventOmaticStashSchema} from "./stashSchema";
import {Dropdown, DropdownChangeParams} from "primereact/dropdown";


const schemaConfigs = [
  inventOmaticPipboySchema,
  inventOmaticStashSchema
];

const ajv = createAjv({useDefaults: true});

export class InventOmatic extends React.Component<any, any> {

  state = {
    schema: schemaConfigs[0],
    data: schemaConfigs[0].defaultConfig
  }

  fileReader: FileReader = new FileReader();

  constructor(props: any, context?: any) {
    super(props, context);
    this.setData = this.setData.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleFileChosen = this.handleFileChosen.bind(this);
    this.handleFileRead = this.handleFileRead.bind(this);
    this.handleSchemaChange = this.handleSchemaChange.bind(this);
  }

  private setData({errors, data}: { errors: any, data: any }) {
    this.setState({errors, data});
  }

  private onSubmit() {
    const {data} = this.state;
    const jsonString: string = JSON.stringify(data, null, 10);
    Utils.downloadString(jsonString, 'text/json', this.state.schema.fileName);
    console.log(data);
  }

  handleFileRead() {
    // @ts-ignore
    const content = JSON.parse(this.fileReader.result);
    console.log(content);
    this.setState({data: content});
  };

  handleFileChosen(e: any) {
    this.fileReader = new FileReader();
    this.fileReader.onloadend = this.handleFileRead;
    this.fileReader.readAsText(e.target.files[0]);
  }

  handleSchemaChange(e: DropdownChangeParams) {
    this.setState({schema: e.value});
    this.setState({data: e.value.defaultConfig});
  }

  render() {
    const {data, schema} = this.state;

    return (
        <div className={"wrapper"}>
          <div className={'config-btns'}>
            <div className={'mod-selector'}>
              <Dropdown value={schema} options={schemaConfigs} optionLabel={'name'}
                        placeholder="Select mod to generate/update config"
                        tooltip={'Select mod to generate/update config'}
                        onChange={this.handleSchemaChange}
              />
            </div>
            <div>
              <Button variant="contained" component="label">
                Upload File
                <input type="file" hidden onChange={this.handleFileChosen}/>
              </Button>
              <span className={'space'}/>
              <Button variant="contained" color={'secondary'} onClick={this.onSubmit}>Get
                config!</Button>
            </div>
          </div>
          <div className={'title'}>
            {schema.name}
          </div>
          <div className={"mod-config-form"}>
            <JsonForms
                schema={schema.schema}
                uischema={schema.uischema}
                data={data}
                renderers={materialRenderers}
                cells={materialCells}
                ajv={ajv}
                validationMode={"ValidateAndShow"}
                onChange={this.setData}
            />
          </div>
        </div>
    );
  }
}

