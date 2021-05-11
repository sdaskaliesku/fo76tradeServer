import React from "react";
import {BaseInventOmatic} from "./BaseInventOmatic";
import {JsonForms} from '@jsonforms/react';
import {vanillaCells, vanillaRenderers} from '@jsonforms/vanilla-renderers';
import {JsonSchema, JsonSchema4} from "@jsonforms/core";

const commonSchema = {
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

const matchModes = ["ALL", "EXACT", "CONTAINS", "STARTS"];

const itemNameConfigSchema: JsonSchema = {
  type: "array",
  items: {
    type: 'object',
    properties: {
      name: {
        type: 'string'
      },
      quantity: {
        type: 'string'
      },
      matchMode: {
        type: "string",
        enum: matchModes
      },
      enabled: {
        type: "boolean"
      },
    }
  }
}

const sectionConfigSchema: JsonSchema4 = {
  items: {
    type: 'object',
    properties: {
      name: {
        type: "string"
      },
      hotkey: {
        type: "integer"
      },
      enabled: {
        type: "boolean"
      },
      checkCharacterName: {
        type: "boolean"
      },
      characterName: {
        type: "string"
      },
      itemNames: {
        type: "array",
        items: {
          type: 'object',
          properties: {
            name: {
              type: 'string'
            },
            quantity: {
              type: 'string'
            },
            matchMode: {
              type: "string",
              enum: matchModes
            },
            enabled: {
              type: "boolean"
            },
          }
        }
      }
    }
  }
};

export const schema: JsonSchema = {
  type: 'object',
  properties: {
    debug: {
      type: 'boolean',
      title: 'Debug mode'
    },
    showRealItemName: {
      type: 'boolean',
      title: 'Show pop-up with real item name',
      description: 'Once you select item in pipboy, it will show a message with full real item name'
    },
    drop: {
      type: 'array',
      ...sectionConfigSchema
    },
    consume: {
      type: 'array',
      ...sectionConfigSchema
    }
  }
};

export const uischema = {
  type: 'VerticalLayout',
  elements: [
    {
      type: 'Control',
      scope: '#/properties/debug'
    },
    {
      type: 'Control',
      scope: '#/properties/showRealItemName'
    },
    {
      type: 'Control',
      scope: '#/properties/drop'
    },
    {
      type: 'Control',
      scope: '#/properties/consume'
    }
  ]
};

// const uischema = {};

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
    // @ts-ignore
    return (
        <div className={"wrapper"}>
          <div className={"p-text-center"}>Invent-O-matic-Pipboy configuration</div>
          <div className={"pipboy-form"}>
            <JsonForms
                schema={schema}
                uischema={uischema}
                data={data}
                renderers={vanillaRenderers}
                cells={vanillaCells}
                onChange={({errors, data}: any) => this.setData(errors, data)}
            />
          </div>
        </div>
    );
  }
}

