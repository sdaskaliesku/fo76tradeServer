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
  createColumnDef({title: 'Account', field: 'ownerInfo.accountOwner'}),
  createColumnDef({title: 'Character', field: 'ownerInfo.characterOwner'}),
  {
    ...createColumnDef({title: 'Stars', field: 'numLegendaryStars'}),
    formatter: 'star',
  },
  createColumnDef({title: 'Abbr', field: 'itemDetails.abbreviation'}),
  createColumnDef({title: 'Type', field: 'filterFlag'}),
  createColumnDef({title: 'Armor Grade', field: 'itemDetails.armorGrade', visible: false}),
  createColumnDef({title: 'Level', field: 'itemLevel'}),
  createColumnDef({title: 'Count', field: 'count'}),
  createColumnDef({title: '1 star', field: 'legendaryMods.0.value'}),
  createColumnDef({title: '2 star', field: 'legendaryMods.1.value'}),
  createColumnDef({title: '3 star', field: 'legendaryMods.2.value'}),
  createColumnDef({title: 'Vendor Price', field: 'tradeOptions.vendorPrice', visible: false}),
  createColumnDef({title: 'fed76.info Price', field: 'itemDetails.priceCheckResponse.price'}),
  createColumnDef({title: 'fed76.info description', field: 'itemDetails.priceCheckResponse.description'}),
  createColumnDef({title: 'fed76.info value', field: 'itemDetails.priceCheckResponse.review.description'}),
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
  {
    formatter: 'buttonCross',
    width: 10,
    hozAlign: 'center',
    headerSort: false,
    download: false,
    visible: true,
    cellClick: function (e: any, cell: any) {
      e.preventDefault();
      e.stopPropagation();
      cell.getRow().delete().then(() => {
      });
    },
  },
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
    field: 'filterFlag',
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
    name: 'fed76.info name',
    field: 'itemDetails.priceCheckResponse.name',
  },
  {
    name: 'fed76.info Price',
    field: 'itemDetails.priceCheckResponse.price',
  },
  {
    name: 'fed76.info description',
    field: 'itemDetails.priceCheckResponse.description',
  },
  {
    name: 'fed76.info value',
    field: 'itemDetails.priceCheckResponse.review.description',
  },
  {
    name: 'fed76.info rating value',
    field: 'itemDetails.priceCheckResponse.review.reviewRating.ratingValue',
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