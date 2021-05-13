import {JSONSchema7} from "json-schema";

export const schema2 = {

}

const enabledSchema: JSONSchema7 = {
  title: 'Enabled?',
  type: 'boolean',
  default: true
};

const schema: any = {
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
          // itemNames: {
          //   title: 'Item configuration',
          //   type: 'array',
          //   items: {
          //     type: 'object',
          //     properties: {
          //       name: {
          //         title: 'Item name to drop',
          //         type: 'string'
          //       },
          //       quantity: {
          //         title: 'Amount of items to drop',
          //         type: 'string'
          //       },
          //       matchMode: {
          //         title: 'Match mode',
          //         type: "string",
          //         enum: MatchModes
          //       },
          //       type: {
          //         title: 'Item type',
          //         type: 'string',
          //         enum: ItemTypes
          //       },
          //       enabled: enabledSchema
          //     }
          //   }
          // },
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