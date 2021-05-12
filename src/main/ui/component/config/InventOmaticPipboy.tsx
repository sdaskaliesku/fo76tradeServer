import React from "react";
import {BaseInventOmatic} from './BaseInventOmatic';
// import Form from "@rjsf/core";
import Form from "@rjsf/material-ui";
import {ThemeProvider} from "@material-ui/styles";
import {createMuiTheme, CssBaseline} from "@material-ui/core";
import {JSONSchema7} from "json-schema";
import {UiSchema} from "@rjsf/core";
import {ItemTypes, MatchModes} from "./configs";
import {Utils} from "../../service/utils";

export class InventOmaticPipboy extends BaseInventOmatic<any> {

  state = {
    data: {
      debug: false,
      showRealItemName: false,
      drop: [],
      consume: []
    }
  }

  constructor(props: any);
  constructor(props: any, context: any);
  constructor(props: any, context?: any) {
    super(props, context);
    this.setData = this.setData.bind(this);
  }

  private setData(errors: any, data: any) {
    console.log(errors);
    console.log(data);
    if (data) {
      this.setState({...data});
    }
  }

  render() {
    const {data} = this.state;

    const uiSchema: UiSchema = {
      debug: {
        type: 'boolean',
        title: 'Debug mode'
      },
      showRealItemName: {
        type: 'boolean',
        title: 'Show pop-up with real item name',
        description: 'Once you select item in pipboy, it will show a message with full real item name'
      },
    };

    const enabledSchema: JSONSchema7 = {
      title: 'Enabled?',
      type: 'boolean',
      default: true
    };

    const schema: JSONSchema7 = {
      title: "Invent-O-matic-Pipboy configuration",
      type: "object",
      properties: {
        debug: {
          type: 'boolean',
          title: 'Debug mode',
          default: false
        },
        showRealItemName: {
          type: 'boolean',
          title: 'Show pop-up with real item name',
          description: 'Once you select item in pipboy, it will show a message with full real item name',
          default: false
        },
        drop: {
          title: 'Drop section configuration',
          type: 'array',
          items: {
            type: 'object',
            properties: {
              name: {
                title: 'Name of the drop section',
                type: 'string'
              },
              hotkey: {
                title: 'Hotkey',
                type: "integer"
              },
              enabled: enabledSchema,
              checkCharacterName: {
                title: 'Perform character name check',
                type: "boolean"
              },
              characterName: {
                title: 'Character name',
                type: "string"
              },
              itemNames: {
                title: 'Item configuration',
                type: 'array',
                items: {
                  type: 'object',
                  properties: {
                    name: {
                      title: 'Item name to drop',
                      type: 'string'
                    },
                    quantity: {
                      title: 'Amount of items to drop',
                      type: 'string'
                    },
                    matchMode: {
                      title: 'Match mode',
                      type: "string",
                      enum: MatchModes
                    },
                    type: {
                      title: 'Item type',
                      type: 'string',
                      enum: ItemTypes
                    },
                    enabled: enabledSchema
                  }
                }
              },
              teenoodleTragedyProtection: {
                type: 'object',
                title: 'Teenoodle tragedy protection',
                properties: {
                  ignoreLegendaries: {
                    title: 'Ignore legendaries items (do not drop them)',
                    type: "boolean",
                    default: true
                  },
                  ignoreNonTradable: {
                    title: 'Ignore non-tradable items (do not drop them)',
                    type: "boolean",
                    default: true
                  }
                }
              },
            }
          }
        }
      }
    };
    const onSubmit = (e: any) => {
      const jsonString: string = JSON.stringify(e.formData, null, 10);
      Utils.downloadString(jsonString, 'text/json', 'inventOmaticPipboyConfig.json');
    }
    const log = (type: any) => console.log.bind(console, type);
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
              <Form
                  schema={schema}
                  uiSchema={uiSchema}
                  onSubmit={onSubmit}
                  onError={log("errors")}
              />
            </div>
          </div>
        </ThemeProvider>
    );
  }
}

