import {Item} from "./domain";

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

  public static isEligibleForPriceCheck(item: Item): boolean {
    const filterFlag: string = item.filterFlag;
    return (item.isLegendary || filterFlag === NOTES) && item.isTradable && priceCheckFilterFlags.includes(filterFlag);
  }

  public static uuid(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
      const r = parseFloat(
          '0.' + Math.random().toString().replace('0.', '') + new Date().getTime()) * 16 | 0,
          v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
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
}