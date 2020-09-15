import {PriceCheckResponse} from "./domain";

const priceCheckFilterFlags: Array<string> = ['WEAPON', 'ARMOR', 'WEAPON_RANGED', 'WEAPON_MELEE'];

export class Utils {
  public static readFile(file: File): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const fr = new FileReader();
      fr.onerror = reject;
      fr.onload = () => {
        const data: string | ArrayBuffer | null = fr.result;
        resolve(JSON.parse(<string>data));
      }
      fr.readAsText(file);
    });
  }

  public static isEligibleForPriceCheck(item: any): boolean {
    const filterFlag: string = item.filterFlag;
    return item.isLegendary && item.isTradable && priceCheckFilterFlags.includes(filterFlag);
  }

  public static isPriceCheckResponseEmpty(priceCheckResponse: PriceCheckResponse): boolean {
    let values = [];
    values.push(priceCheckResponse.name);
    values.push(priceCheckResponse.description);
    values.push(priceCheckResponse.timestamp);
    values.push(priceCheckResponse.path);
    return values.every(value => value === 'EMPTY');
  }

  public static uuid(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
      const r = parseFloat(
          '0.' + Math.random().toString().replace('0.', '') + new Date().getTime()) * 16 | 0,
          v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }
}