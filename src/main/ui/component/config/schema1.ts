import {MatchModes} from "./configs";

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

const sectionConfigSchema = {
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
              enum: MatchModes
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

export const schema = {
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
    drop: dropSection,
    consume: consumeSection
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