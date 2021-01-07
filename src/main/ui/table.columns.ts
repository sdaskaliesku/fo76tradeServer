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
    ...createColumnDef({title: 'Stars', field: 'legendaryMods.length'}),
    formatter: 'star',
  },
  createColumnDef({title: 'Abbr', field: 'itemDetails.abbreviation'}),
  createColumnDef({title: 'Type', field: 'filterFlag'}),
  createColumnDef({title: 'Armor Grade', field: 'itemDetails.armorGrade', visible: false}),
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
  createColumnDef({
    title: 'Fed76 Description',
    field: 'priceCheckResponse.description'
  }),
  // createColumnDef({
  //   title: 'Fed76 Value',
  //   field: 'priceCheckResponse.review.description'
  // }),
  createColumnDef({title: 'Description', field: 'description', visible: false}),
  {
    ...createColumnDef({title: 'Tradable', field: 'isTradable', visible: false}),
    formatter: 'tickCross'
  },
  {
    ...createColumnDef({title: 'Legendary', field: 'isLegendary', visible: false}),
    formatter: 'tickCross'
  },
  createColumnDef({title: 'Weight', field: 'weight', visible: false}),
  createColumnDef({title: 'Total weight', field: 'itemDetails.totalWeight', visible: false})
];


export const modalFields = [
  {
    name: 'Account',
    field: 'ownerInfo.accountOwner',
  },
  {
    name: 'Character',
    field: 'ownerInfo.characterOwner',
  },
  {
    name: 'Description',
    field: 'description',
  },
  {
    name: 'Name converted',
    field: 'itemDetails.name',
  },
  {
    name: 'Type',
    field: 'itemDetails.filterFlag',
  },
  {
    name: 'Armor Grade',
    field: 'itemDetails.armorGrade',
  },
  {
    name: 'Armor formId',
    field: 'itemDetails.formId',
  },
  {
    name: 'Legendary',
    field: 'isLegendary',
  },
  {
    name: 'Tradable',
    field: 'isTradable',
  },
  {
    name: 'Level',
    field: 'itemLevel',
  },
  {
    name: 'Count',
    field: 'count',
  },
  {
    name: 'Game Price',
    field: 'itemValue',
  },
  {
    name: 'Fed76 Name',
    field: 'priceCheckResponse.name',
  },
  {
    name: 'Fed76 Price',
    field: 'priceCheckResponse.price',
  },
  {
    name: 'Fed76 Description',
    field: 'priceCheckResponse.description',
  },
  {
    name: 'Fed76 Value',
    field: 'priceCheckResponse.review.description',
  },
  {
    name: 'Fed76 Rating Value',
    field: 'priceCheckResponse.review.reviewRating.ratingValue',
  },
  {
    name: 'Legendary stars',
    field: 'numLegendaryStars',
  },
  {
    name: 'Weight',
    field: 'weight',
  },
  {
    name: 'Abbreviation',
    field: 'itemDetails.abbreviation',
  }
];