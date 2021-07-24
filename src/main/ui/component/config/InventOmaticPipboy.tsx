import React from "react";
import "./BaseInventOmatic.scss";
import {getAs3CharCode, inventOmaticPipboySchema} from "./schema1";
import {materialCells, materialRenderers} from "@jsonforms/material-renderers";
import {JsonForms} from "@jsonforms/react";
import {Button} from "@material-ui/core";
import {createAjv} from "@jsonforms/core";
import {Utils} from "../../service/utils";

export enum MatchMode {
  CONTAINS, EXACT, ALL, STARTS
}

export class InventOmaticPipboy extends React.Component<any, any> {

  state = {
    data: {
      debug: false,
      showRealItemName: false,
      configs: [
        {
          name: 'Sample config',
          hotkey: getAs3CharCode("79"),
          itemConfigs: [
            {
              name: 'Mini Nuke',
              type: 'AMMO',
              quantity: 1,
              matchMode: 'EXACT',
              action: 'DROP',
              enabled: true
            }
          ],
          enabled: true,
          checkCharacterName: false,
          characterName: '',
          teenoodleTragedyProtection: {
            ignoreLegendaries: true,
            ignoreNonTradable: true
          }
        }
      ],
    }
  }

  fileReader: FileReader = new FileReader();
  ajv: any;

  constructor(props: any, context?: any) {
    super(props, context);
    this.setData = this.setData.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleFileChosen = this.handleFileChosen.bind(this);
    this.handleFileRead = this.handleFileRead.bind(this);
    this.ajv = createAjv({useDefaults: true});
  }

  private setData({errors, data}: { errors: any, data: any }) {
    this.setState({errors, data});
  }

  private onSubmit() {
    const {data} = this.state;
    const jsonString: string = JSON.stringify(data, null, 10);
    Utils.downloadString(jsonString, 'text/json', 'inventOmaticPipboyConfig.json');
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

  render() {
    const {data} = this.state;

    return (
        <div className={"wrapper"}>
          <Button variant="contained" component="label">
            Upload File
            <input type="file" hidden onChange={this.handleFileChosen}/>
          </Button>
          <Button variant="contained" color={'secondary'} onClick={this.onSubmit}>Get
            config!</Button>
          <div className={"pipboy-form"}>
            <JsonForms
                schema={inventOmaticPipboySchema.schema}
                uischema={inventOmaticPipboySchema.uischema}
                data={data}
                renderers={materialRenderers}
                cells={materialCells}
                ajv={this.ajv}
                validationMode={"ValidateAndShow"}
                onChange={(e: any) => this.setData(e)}

            />
          </div>
        </div>
    );
  }
}

