import React from "react";
import {BaseInventOmatic} from './BaseInventOmatic';
import {createMuiTheme, CssBaseline, ThemeProvider} from "@material-ui/core";
import {Utils} from "../../service/utils";
import {JsonForms} from "@jsonforms/react";
import {materialCells, materialRenderers} from "@jsonforms/material-renderers";
import {schema1} from "./schema1";
import {Button} from "primereact/button";

export class InventOmaticPipboy extends BaseInventOmatic<any> {

  state = {
    data: {
      debug: false,
      showRealItemName: false,
      configs: [
        {
          name: 'Sample config',
          hotkey: 49,
          itemNames: [
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

  constructor(props: any);
  constructor(props: any, context: any);
  constructor(props: any, context?: any) {
    super(props, context);
    this.setData = this.setData.bind(this);
  }

  private setData(e: any) {
    if (e.errors && e.errors.length > 0) {
      console.error(e.errors);
    }
    this.setState({...e.data});
  }

  render() {
    const {data} = this.state;

    const onSubmit = () => {
      const jsonString: string = JSON.stringify(data, null, 10);
      Utils.downloadString(jsonString, 'text/json', 'inventOmaticPipboyConfig.json');
    };
    const theme = createMuiTheme({
      palette: {
        type: "dark"
      }
    });
    return (
        <ThemeProvider theme={theme}>
          <CssBaseline/>
          <div className={"wrapper"}>
            <div className={"pipboy-form"}>
              <h1>THIS IS WORK IN PROGRESS!</h1>
              <Button className={'p-button-success'} onClick={onSubmit}>Get config!</Button>
              <JsonForms
                  schema={schema1.schema}
                  data={data}
                  renderers={materialRenderers}
                  cells={materialCells}
                  validationMode={"ValidateAndShow"}
                  onChange={(e: any) => this.setData(e)}
              />
            </div>
          </div>
        </ThemeProvider>
    );
  }
}

