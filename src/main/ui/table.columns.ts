const createColumnDef = ({title, field, visible = true}: { title: string, field: string, visible?: boolean }) => {
  return {
    title,
    field,
    visible,
    hozAlign: "center"
  }
}

export const columns = [
  {
    formatter: 'rowSelection',
    titleFormatter: 'rowSelection',
    width: 10,
    hozAlign: 'center',
    headerSort: false,
    download: false,
    visible: true,
    cellClick: function (e: any) {
      e.preventDefault();
      e.stopPropagation();
    },
  },
  createColumnDef({title: 'Name', field: 'text'}),
  createColumnDef({title: 'Name converted', field: 'itemDetails.name', visible: false}),
  createColumnDef({title: 'Account', field: 'itemDetails.ownerInfo.accountName'}),
  createColumnDef({title: 'Character', field: 'itemDetails.ownerInfo.characterName'}),
  {
    ...createColumnDef({title: 'Stars', field: 'numLegendaryStars'}),
    formatter: 'star',
  },
  createColumnDef({title: 'Abbr', field: 'itemDetails.abbreviation'}),
  createColumnDef({title: 'Type', field: 'filterFlag'}),
  createColumnDef({title: 'Armor Grade', field: 'itemDetails.armorConfig.armorGrade', visible: false}),
  createColumnDef({title: 'Level', field: 'itemLevel'}),
  createColumnDef({title: 'Count', field: 'count'}),
  createColumnDef({title: '1 star', field: 'itemDetails.legendaryMods.0.value'}),
  createColumnDef({title: '2 star', field: 'itemDetails.legendaryMods.1.value'}),
  createColumnDef({title: '3 star', field: 'itemDetails.legendaryMods.2.value'}),
  createColumnDef({title: 'Prefix', field: 'itemDetails.legendaryMods.0.text', visible: false}),
  createColumnDef({title: 'Major', field: 'itemDetails.legendaryMods.1.text', visible: false}),
  createColumnDef({title: 'Minor', field: 'itemDetails.legendaryMods.2.text', visible: false}),
  // createColumnDef({title: 'Vendor Price', field: 'tradeOptions.vendorPrice', visible: false}),
  createColumnDef({title: 'Fed76 Price', field: 'priceCheckResponse.price'}),
  // createColumnDef({
  //   title: 'Fed76 Description',
  //   field: 'priceCheckResponse.description'
  // }),
  // // createColumnDef({
  // //   title: 'Fed76 Value',
  // //   field: 'priceCheckResponse.review.description'
  // // }),
  createColumnDef({title: 'Description', field: 'description', visible: false}),
  {
    ...createColumnDef({title: 'Tradable', field: 'isTradable', visible: false}),
    formatter: 'tickCross'
  },
  {
    ...createColumnDef({title: 'Legendary', field: 'isLegendary', visible: false}),
    formatter: 'tickCross'
  },
  createColumnDef({title: 'Source', field: 'itemDetails.itemSource'}),
  createColumnDef({title: 'Weight', field: 'weight', visible: false}),
  createColumnDef({title: 'Total weight', field: 'itemDetails.totalWeight', visible: false})
];