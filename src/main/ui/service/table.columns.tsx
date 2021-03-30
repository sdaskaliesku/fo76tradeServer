import {Item} from "./domain";
import React from "react";

export interface ColumnOptions {
  filter: boolean,
  sort: boolean
}

export interface SimpleColumn {
  header: string;
  field: string;
  visible?: boolean;
  isRating?: boolean;
  isBool?: boolean;
}

export interface ColumnDefinition extends SimpleColumn {
  getValue?: (object: Item) => any;
  options?: ColumnOptions
}

const getObjectValue = (path: string, obj: any) => {
  if (!obj) {
    return '';
  }
  let paths = path.split('.')
      , current = obj
      , i;

  for (i = 0; i < paths.length; ++i) {
    if (current[paths[i]] == undefined) {
      return undefined;
    } else {
      current = current[paths[i]];
    }
  }
  return current;
};

export const createColumnDef = ({header, field, visible = true, isRating = false, isBool = false}: ColumnDefinition): ColumnDefinition => {
  return {
    header,
    field,
    visible,
    isRating,
    isBool,
    getValue(obj: any) {
      return getObjectValue(field, obj);
    },
    options: {
      filter: true,
      sort: true,
    }
  }
};

const getLegModField = (index: number, field: string) => {
  return `itemDetails.legendaryMods.${index}.${field}`
};

export const columns = [
  createColumnDef({header: 'Name', field: 'text'}),
  createColumnDef({header: 'Name converted', field: 'itemDetails.name', visible: false}),
  createColumnDef({header: 'Account', field: 'itemDetails.ownerInfo.accountName'}),
  createColumnDef({header: 'Character', field: 'itemDetails.ownerInfo.characterName'}),
  createColumnDef({header: 'Stars', field: 'numLegendaryStars', isRating: true}),
  createColumnDef({header: 'Abbr', field: 'itemDetails.abbreviation'}),
  createColumnDef({header: 'Type', field: 'filterFlag'}),
  createColumnDef({
    header: 'Armor Grade',
    field: 'itemDetails.armorConfig.armorGrade',
    visible: false
  }),
  createColumnDef({header: 'Level', field: 'itemLevel'}),
  createColumnDef({header: 'Count', field: 'count'}),
  createColumnDef({header: '1 star', field: getLegModField(0, 'value'), visible: false}),
  createColumnDef({header: '2 star', field: getLegModField(1, 'value'), visible: false}),
  createColumnDef({header: '3 star', field: getLegModField(2, 'value'), visible: false}),
  createColumnDef({header: 'Prefix', field: getLegModField(0, 'text')}),
  createColumnDef({header: 'Major', field: getLegModField(1, 'text')}),
  createColumnDef({header: 'Minor', field: getLegModField(2, 'text')}),
  createColumnDef({header: 'Fed76 Price', field: 'priceCheckResponse.price'}),
  createColumnDef({
    header: 'Fed76 Description',
    field: 'priceCheckResponse.description',
    visible: false
  }),
  createColumnDef({
    header: 'Fed76 Value',
    field: 'priceCheckResponse.reviewDescription',
    visible: false
  }),
  createColumnDef({
    header: 'Fed76 Plan verdict',
    field: 'priceCheckResponse.verdict',
    visible: false
  }),
  createColumnDef({
    header: 'Fed76 min price',
    field: 'priceCheckResponse.minPrice',
    visible: false
  }),
  createColumnDef({
    header: 'Fed76 max price',
    field: 'priceCheckResponse.maxPrice',
    visible: false
  }),
  createColumnDef({header: 'Description', field: 'description', visible: false}),
  createColumnDef({header: 'Tradable', field: 'isTradable', visible: false, isBool: true}),
  createColumnDef({header: 'Legendary', field: 'isLegendary', visible: false, isBool: true}),
  createColumnDef({
    header: 'Learned recipe',
    field: 'isLearnedRecipe',
    visible: false,
    isBool: true
  }),
  createColumnDef({header: 'Source', field: 'itemDetails.itemSource'}),
  createColumnDef({header: 'Weight', field: 'weight', visible: false}),
  createColumnDef({header: 'Total weight', field: 'itemDetails.totalWeight', visible: false}),
  createColumnDef({header: 'Vendor price', field: 'vendingData.price', visible: false})
];