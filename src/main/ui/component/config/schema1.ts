import {Actions, ItemTypes, MatchModes} from "./configs";
import {JsonSchema, Rule, Scopable} from "@jsonforms/core";

const as3KeyCodes: { [key: string]: string } = {
  "0": "48",
  "1": "49",
  "2": "50",
  "3": "51",
  "4": "52",
  "5": "53",
  "6": "54",
  "7": "55",
  "8": "56",
  "9": "57",
  "Numpad 0": "96",
  "Numpad 1": "97",
  "Numpad 2": "98",
  "Numpad 3": "99",
  "Numpad 4": "100",
  "Numpad 5": "101",
  "Numpad 6": "102",
  "Numpad 7": "103",
  "Numpad 8": "104",
  "Numpad 9": "105",
  "Numpad *": "106",
  "Numpad +": "107",
  "Numpad Enter": "13",
  "Numpad -": "109",
  "Numpad /": "111",
  "A": "65",
  "B": "66",
  "C": "67",
  "D": "68",
  "E": "69",
  "F": "70",
  "G": "71",
  "H": "72",
  "I": "73",
  "J": "74",
  "K": "75",
  "L": "76",
  "M": "77",
  "N": "78",
  "O": "79",
  "P": "80",
  "Q": "81",
  "R": "82",
  "S": "83",
  "T": "84",
  "U": "85",
  "V": "86",
  "W": "87",
  "X": "88",
  "Y": "89",
  "Z": "90",
  "Backspace": "8",
  "Tab": "9",
  "Shift": "16",
  "Control": "17",
  "Caps Lock": "20",
  "Esc": "27",
  "Spacebar": "32",
  "Page Up": "33",
  "Page Down": "34",
  "End": "35",
  "Home": "36",
  "Left Arrow": "37",
  "Up Arrow": "38",
  "Right Arrow": "39",
  "Down Arrow": "40",
  "Insert": "45",
  "Delete": "46",
  "Num Lock": "144",
  "ScrLk": "145",
  "Pause/Break": "19",
  ":": "186",
  ";": "186",
  "=": "187",
  "+": "187",
  "-": "189",
  "_": "189",
  "/": "191",
  "?": "191",
  "~": "192",
  "`": "192",
  "[": "219",
  "{": "219",
  "\\": "220",
  "|": "220",
  "}": "221",
  "]": "221",
  "\"": "222",
  "'": "222",
  ",": "188",
  ".": "190",
  "F1": "112",
  "F2": "113",
  "F3": "114",
  "F4": "115",
  "F5": "116",
  "F6": "117",
  "F7": "118",
  "F8": "119",
  "F9": "120",
  "F11": "122",
  "F12": "123",
  "F13": "124",
  "F14": "125",
  "F15": "126"
};

const as3Keys = Object.keys(as3KeyCodes);

export const getAs3CharCode = (keyCode: string) => {
  for (let i = 0; i < as3Keys.length; i++) {
    const char = as3Keys[i];
    const charCode = as3KeyCodes[char];
    if (charCode === keyCode) {
      return char;
    }
  }
}

export const schema: JsonSchema = {
  type: 'object',
  properties: {
    debug: {
      type: 'boolean',
      title: 'Debug mode',
    },
    showRealItemName: {
      type: 'boolean',
      title: 'Show pop-up with real item name',
      description: 'Once you select item in pipboy, it will show a message with full real item name'
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
            default: 'item name'
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
          checkCharacterName: {
            type: "boolean",
            title: 'Verify character name',
            description: 'If enabled - mod will check whether current character name matches specified'
          },
          characterName: {
            type: "string",
            title: 'Character name',
          },
          teenoodleTragedyProtection: {
            type: 'object',
            title: 'Teenoodle tragedy protection',
            properties: {
              ignoreLegendaries: {
                title: 'Prevent any mod actions on legendary items',
                type: "boolean",
                default: true
              },
              ignoreNonTradable: {
                title: 'Prevent any mod actions on non-tradable items',
                type: "boolean",
                default: true
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
  }
};

const enum LayoutTypes {
  HorizontalLayout = "HorizontalLayout",
  VerticalLayout = "VerticalLayout"
}

export interface UISchemaElement extends Scopable {
  type: string;
  rule?: Rule;
  options?: { [key: string]: any };
  properties?: { [key: string]: any };
}

interface Layout extends UISchemaElement {
  elements: UISchemaElement[];
}

export const uischema: any = {
  type: LayoutTypes.VerticalLayout,
  scope: '',
  elements: [
    {
      type: 'Control',
      scope: '#/properties/debug',
      properties: {}
    },
    {
      type: 'Control',
      scope: '#/properties/showRealItemName'
    },
    {
      type: 'Control',
      scope: '#/properties/configs',
    },
  ]
};


export const inventOmaticPipboySchema = {
  uischema: uischema,
  schema: schema
}