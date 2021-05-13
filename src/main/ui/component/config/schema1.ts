import {Actions, ItemTypes, MatchModes} from "./configs";
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

const itemNameConfigSchema = {
  type: "object",
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
        enum: MatchModes
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
        type: "number"
      },
      enabled: {
        type: "boolean",
        default: true
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
            type: {
              type: 'string',
              enum: ItemTypes,
            },
            quantity: {
              type: 'string'
            },
            matchMode: {
              type: "string",
              enum: MatchModes
            },
            action: {
              type: 'string',
              enum: Actions
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
const dropSection = {
  type: 'array',
  ...sectionConfigSchema
}

const consumeSection = {
  type: 'array',
  ...sectionConfigSchema
}

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
    configs: {
      type: 'array',
      items: {
        type: 'object',
        properties: {
          name: {
            type: "string"
          },
          hotkey: {
            type: "integer",
          },
          enabled: {
            type: "boolean",
            default: true
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
                type: {
                  type: 'string',
                  enum: ItemTypes,
                },
                quantity: {
                  type: 'number'
                },
                matchMode: {
                  type: "string",
                  enum: MatchModes,
                  default: 'EXACT'
                },
                action: {
                  type: 'string',
                  enum: Actions,
                  default: 'CONSUME'
                },
                enabled: {
                  type: "boolean",
                  default: true
                },
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
          }
        }
      }
    }
  }
};

export const uischema = {
  // type: 'HorizontalLayout',
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
      scope: '#/properties/drop',
    },
    // {
    //   type: 'Control',
    //   scope: '#/properties/consume'
    // }
  ]
};


export const schema1 = {
  uischema: uischema,
  schema: schema
}