const headers: Array<String> = ["Prefix", "Type", "Major", "Minor", "Level", "Notes", "Value", "Character"];
const separator = ",";
const eof = "\r\n";

declare interface RogueCSVLine {
  prefix?: string,
  type: string,
  major?: string,
  minor?: string,
  level: Number,
  notes?: string,
  value?: Number,
  character: string,
}

const legendaryFilter = (object: any) => {
  return object && object.isLegendary && object.isTradable;
}

export class RogueService {

  private static createRogueObject(item: any, character: string): RogueCSVLine {
    return {
      prefix: item.legendaryMods[0]?.value,
      type: item.itemDetails.name,
      major: item.legendaryMods[1]?.value,
      minor: item.legendaryMods[2]?.value,
      level: item.itemLevel,
      notes: '',
      value: 0,
      character,
    }
  }

  private static toRogueObject(data: Array<any>, character: string): Array<RogueCSVLine> {
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