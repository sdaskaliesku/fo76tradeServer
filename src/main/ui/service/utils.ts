import {Item} from "./domain";
import {toXML} from "jstoxml";
import {parse} from "fast-xml-parser";

const NOTES = 'NOTES';
const priceCheckFilterFlags: Array<string> = ['WEAPON', 'ARMOR', 'WEAPON_RANGED', 'WEAPON_MELEE', NOTES];

export class Utils {
  public static readFile(file: File): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      const fr = new FileReader();
      fr.onerror = reject;
      fr.onload = () => {
        const data: string | ArrayBuffer | null = fr.result;
        try {
          resolve(JSON.parse(<string>data));
        } catch (e) {
          reject(e);
        }
      }
      fr.readAsText(file);
    });
  }

  public static shouldPriceCheck(item: Item): boolean {
    if (!item.isTradable) {
      return false;
    }
    if (item.priceCheckResponse && item.priceCheckResponse.price) {
      const price = item.priceCheckResponse.price;
      if (price === -1 || price > 0) {
        return false;
      }
    }
    const filterFlag = item.filterFlag;
    if (filterFlag === NOTES) {
      return true;
    }
    return item.isLegendary && priceCheckFilterFlags.includes(filterFlag);
  }

  public static downloadString(text: string, fileType: string, fileName: string) {
    const blob = new Blob([text], {type: fileType});

    const a = document.createElement('a');
    a.download = fileName;
    a.href = URL.createObjectURL(blob);
    a.dataset.downloadurl = [fileType, a.download, a.href].join(':');
    a.style.display = "none";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    setTimeout(function () {
      URL.revokeObjectURL(a.href);
    }, 1500);
  }

  public static uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      const r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }

  public static getPropertyByPath(obj: any, path: string) {
    try {
      path = path.replace(/\[(\w+)\]/g, '.$1');
      path = path.replace(/^\./, '');
      const a = path.split('.');
      let o = obj;
      while (a.length) {
        const n = a.shift();
        // @ts-ignore
        if (!(n in o)) return;
        // @ts-ignore
        o = o[n];
      }
      return o;
    } catch (e) {
      return undefined;
    }
  }

  public static setPropertyByPath(obj: any, path: string, value: any) {
    try {
      const a = path.split('.');
      let o = obj;
      while (a.length - 1) {
        const n = a.shift();
        // @ts-ignore
        if (!(n in o)) {
          // @ts-ignore
          o[n] = {};
        }
        // @ts-ignore
        o = o[n];
      }
      o[a[0]] = value;
    } catch (e) {

    }
  }

  public static toXML(input: any): string {
    return toXML(input, {
      indent: '       '
    });
  }

  public static fromXML(input: any): any {
    return parse(input);
  }
}