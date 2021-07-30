import {ItemTypes, MatchModes, PipboyActions} from "./configs";
import {JsonSchema} from "@jsonforms/core";
import {as3KeyCodes, getAs3CharCode, LayoutTypes, Type} from "./schema";

const Actions = PipboyActions;

const schema: JsonSchema = {
  type: 'object',
  properties: {
    debug: {
      type: 'boolean',
      title: 'Debug mode',
    },
    showRealItemName: {
      type: 'boolean',
      title: 'Show pop-up with real item name',
      description: 'Once you select(highlight) item, it will show a message with full real item name'
    },
    delayConfig: {
      type: 'object',
      title: 'Delay configuration',
      default: {
        drop: {
          initialDelay: 100,
          step: 20
        },
        consume: {
          initialDelay: 100,
          step: 20
        },
      },
      properties: {
        drop: {
          type: 'object',
          properties: {
            initialDelay: {
              type: 'number',
              title: 'Initial delay',
              default: 100
            },
            step: {
              type: 'number',
              title: 'Delay increase step (per each item)',
              default: 20
            }
          }
        },
        consume: {
          type: 'object',
          properties: {
            initialDelay: {
              type: 'number',
              title: 'Initial delay',
              default: 100
            },
            step: {
              type: 'number',
              title: 'Delay increase step (per each item)',
              default: 20
            }
          }
        }
      }
    },
    configs: {
      type: 'array',
      title: 'Configurations',
      items: {
        type: 'object',
        properties: {
          name: {
            type: "string",
            title: 'Config name',
            default: 'some config name'
          },
          enabled: {
            type: "boolean",
            title: 'Enabled',
            default: true,
            description: 'Enables specific configuration section'
          },
          hotkey: {
            type: "string",
            enum: Object.keys(as3KeyCodes),
            title: 'Hotkey',
            description: 'Hotkey that will trigger listed actions',
          },
          charConfig: {
            type: 'object',
            properties: {
              enabled: {
                type: "boolean",
                title: 'Verify character name',
                description: 'If enabled - mod will check whether current character name matches specified'
              },
              name: {
                type: "string",
                title: 'Character name',
              },
            }
          },
          teenoodleTragedyProtection: {
            type: 'object',
            title: 'Teenoodle tragedy protection',
            properties: {
              ignoreConfig: {
                type: 'object',
                properties: {
                  legendary: {
                    title: 'Prevent any mod actions on legendary items',
                    type: "boolean",
                    default: true
                  },
                  nonTradable: {
                    title: 'Prevent any mod actions on non-tradable items',
                    type: "boolean",
                    default: true
                  },
                }
              },
              excludedItems: {
                default: [
                  {
                    name: 'some item name',
                    matchMode: 'CONTAINS'
                  }
                ],
                items: {
                  type: 'object',
                  properties: {
                    name: {
                      type: "string",
                      title: 'Item name to ignore (prevent any mod actions)',
                      default: 'item name'
                    },
                    matchMode: {
                      type: "string",
                      enum: MatchModes,
                      title: 'Match mode',
                      description: 'Used to match specified item name and item name in game',
                      default: 'CONTAINS'
                    },
                  }
                }
              }
            }
          },
          itemConfigs: {
            type: "array",
            title: 'Item actions',
            items: {
              type: 'object',
              title: '',
              properties: {
                name: {
                  title: 'Item name',
                  description: 'Item name to perform action on',
                  default: 'some item name',
                  type: 'string'
                },
                enabled: {
                  type: "boolean",
                  title: 'Enabled',
                  default: true,
                  description: 'Enables specific configuration'
                },
                action: {
                  type: 'string',
                  enum: Actions,
                  default: 'CONSUME',
                  title: 'Action',
                  description: 'Action that should be performed on item'
                },
                type: {
                  type: 'string',
                  title: 'Item type',
                  description: 'Item type to perform action on',
                  default: 'JUNK',
                  enum: ItemTypes,
                },
                quantity: {
                  type: 'number',
                  default: 1,
                  title: 'Amount of items to perform action on'
                },
                matchMode: {
                  type: "string",
                  enum: MatchModes,
                  title: 'Match mode',
                  description: 'Used to match specified item name and item name in game',
                  default: 'EXACT'
                },
              }
            },
            default: [
              {
                name: 'some item name',
                type: 'AMMO',
                quantity: 1,
                matchMode: 'EXACT',
                action: 'DROP',
                enabled: true
              }
            ]
          },
        }
      }
    }
  },
};

const uischema: any = {
  type: LayoutTypes.VerticalLayout,
  scope: '',
  elements: [
    {
      type: Type.Control,
      scope: '#/properties/debug',
    },
    {
      type: Type.Control,
      scope: '#/properties/showRealItemName',
    },
    {
      type: Type.Control,
      scope: '#/properties/configs',
      options: {
        detail: {
          type: LayoutTypes.VerticalLayout,
          elements: [
            {
              type: Type.Control,
              scope: '#/properties/enabled'
            },
            {
              type: Type.Control,
              scope: '#/properties/name'
            },
            {
              type: Type.Control,
              scope: '#/properties/hotkey'
            },
            {
              type: Type.Control,
              scope: '#/properties/charConfig',
              options: {
                detail: {
                  type: LayoutTypes.HorizontalLayout,
                  elements: [
                    {
                      type: Type.Control,
                      scope: '#/properties/enabled'
                    },
                    {
                      type: Type.Control,
                      scope: '#/properties/name'
                    },
                  ]
                }
              }
            },
            {
              type: Type.Control,
              scope: '#/properties/teenoodleTragedyProtection',
              options: {
                detail: {
                  type: LayoutTypes.VerticalLayout,
                  elements: [
                    {
                      type: Type.Control,
                      scope: '#/properties/ignoreConfig',
                      options: {
                        detail: {
                          type: LayoutTypes.HorizontalLayout,
                          elements: [
                            {
                              type: Type.Control,
                              scope: '#/properties/legendary'
                            },
                            {
                              type: Type.Control,
                              scope: '#/properties/nonTradable'
                            },
                          ]
                        }
                      }
                    },
                    {
                      type: Type.Control,
                      scope: '#/properties/excludedItems'
                    },
                  ]
                }
              }
            },
            {
              type: Type.Control,
              scope: '#/properties/itemConfigs'
            },
          ]
        }
      }
    },
    {
      type: Type.Control,
      scope: '#/properties/delayConfig',
      options: {
        detail: {
          type: LayoutTypes.HorizontalLayout,
          elements: [
            {
              type: "Control",
              scope: "#/properties/drop"
            },
            {
              type: "Control",
              scope: "#/properties/consume"
            },
          ]
        }
      }
    }
  ]
};


export const inventOmaticPipboySchema = {
  uischema: uischema,
  schema: schema,
  defaultConfig: {
    debug: false,
    showRealItemName: false,
    configs: [
      {
        name: 'Sample config',
        hotkey: getAs3CharCode("79"),
        itemConfigs: [
          {
            name: 'Lunchbox',
            type: 'AID',
            quantity: 1,
            matchMode: 'EXACT',
            action: 'CONSUME',
            enabled: true
          }
        ],
        enabled: true,
        charConfig: {
          enabled: false,
          name: '',
        },
        teenoodleTragedyProtection: {
          ignoreConfig: {
            legendary: true,
            nonTradable: true
          },
          excludedItems: [
            {
              name: 'bloodied',
              matchMode: 'CONTAINS'
            }
          ]
        }
      }
    ],
  },
  name: 'Invent-O-Matic-Pipboy',
  fileName: 'inventOmaticPipboyConfig.json',
}