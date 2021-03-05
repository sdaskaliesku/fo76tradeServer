import React from "react";
import {InfoDialog} from "./InfoDialog";
import {Item} from "../../service/domain";

export class ItemStatsDialog extends InfoDialog {

  header = 'Item statistics';

  private static buildStatsObject(items: Array<Item>) {
    const statsObject = {
      filters: {},
      legendaryStats: {},
    };
    // @ts-ignore
    for (let item of items) {
      // @ts-ignore
      let filterStat = statsObject.filters[item.filterFlag];
      // @ts-ignore
      statsObject.filters[item.filterFlag] = ItemStatsDialog.buildStatObject(filterStat, item);

      if (item.isLegendary) {
        // @ts-ignore
        let legendaryStats = statsObject.legendaryStats[item.numLegendaryStars];
        // @ts-ignore
        statsObject.legendaryStats[item.numLegendaryStars] = ItemStatsDialog.buildStatObject(legendaryStats, item);
      }
    }
    return statsObject;
  }

  private static buildStatObject(statObject: any, item: Item) {
    if (!statObject) {
      statObject = {
        count: 0,
        items: 0,
        weight: 0,
      };
    }
    statObject.count += 1;
    statObject.items += item.count;
    if (item.itemDetails) {
      statObject.weight += item.itemDetails.totalWeight;
    }
    return statObject;
  }

  private static renderContent(items: Array<Item>) {
    const itemStats = ItemStatsDialog.buildStatsObject(items);
    const legendaryStats = [];
    for (let [key, value] of Object.entries(itemStats.legendaryStats)) {
      legendaryStats.push(ItemStatsDialog.renderStatObject(`# of stars ${key}`, value));
    }
    const weightStats = [];
    for (let [key, value] of Object.entries(itemStats.filters)) {
      weightStats.push(ItemStatsDialog.renderStatObject(key, value));
    }

    return (
        <React.Fragment>
          <div>
            <h4>Legendary items stats</h4>
            {legendaryStats}
          </div>

          <div>
            <h4>Weight stats</h4>
            {weightStats}
          </div>
        </React.Fragment>
    );

  }

  private static renderStatObject(title: string, stat: any) {
    return (
        <React.Fragment key={title + new Date().getMilliseconds()}>
          <b>{title}</b><br/>
          <span>Total count(distinct): {stat.count}</span><br/>
          <span>Total weight: {stat.weight}</span><br/>
          <span>Total items: {stat.items}</span><br/><br/>
        </React.Fragment>
    );
  }

  show(items: Array<Item>) {
    super.show(this.header, ItemStatsDialog.renderContent(items));
  }
}