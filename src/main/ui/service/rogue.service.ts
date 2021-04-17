import {Item} from "./domain";

const headers: Array<string> = ["Prefix", "Type", "Major", "Minor", "Level", "Notes", "Value", "Character"];
const separator = ",";
const eof = "\r\n";

interface RogueCSVLine {
  prefix?: string,
  type?: string,
  major?: string,
  minor?: string,
  level: number,
  notes?: string,
  value?: number,
  character: string,
}

const legendaryFilter = (object: Item) => {
  return object && object.isLegendary && object.isTradable;
}

export class RogueService {

  private static getLegModValue(item: Item, index: number) {
    if (item && item.itemDetails && item.itemDetails.legendaryModConfig && item.itemDetails.legendaryModConfig.legendaryMods.length >= index
        && item.itemDetails.legendaryModConfig.legendaryMods[index]) {
      return item.itemDetails.legendaryModConfig.legendaryMods[index].gameId;
    }
  }

  private static getItemName(item: Item) {
    // if (item && item.itemDetails && item.itemDetails.config && item.itemDetails.config.gameId) {
    //   return item.itemDetails.config.gameId;
    // }
    if (item && item.itemDetails) {
      return item.itemDetails.name;
    }
  }

  private static createRogueObject(item: Item, character: string): RogueCSVLine {
    return {
      prefix: this.getLegModValue(item, 0),
      type: this.getItemName(item),
      major: this.getLegModValue(item, 1),
      minor: this.getLegModValue(item, 2),
      level: item.itemLevel,
      notes: '',
      value: item.itemValue,
      character,
    }
  }

  private static toRogueObject(data: Array<Item>, character: string): Array<RogueCSVLine> {
    return data.filter(legendaryFilter).map(v => RogueService.createRogueObject(v, character));
  }

  private static toCSVLine(object: any): string {
    return Object.values(object).join(separator) + eof;
  }


  public static toCSV(data: Array<any>, character: string): string {
    let csv = RogueService.toCSVLine(headers);
    let rogueObjects = RogueService.toRogueObject(data, character);
    rogueObjects.forEach(value => {
      csv += RogueService.toCSVLine(value);
    });
    return csv.replace("’", "'").replace("+", "﹢");
  }
}