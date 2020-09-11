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
    cellClick: function (e: any, cell: any) {
      e.preventDefault();
      e.stopPropagation();
    },
  },
  createColumnDef({title: 'Name', field: 'text'}),
  createColumnDef({title: 'Account', field: 'ownerInfo.accountOwner'}),
  createColumnDef({title: 'Character', field: 'ownerInfo.characterOwner'}),
  {
    ...createColumnDef({title: 'Stars', field: 'numLegendaryStars'}),
    formatter: 'star',
  },
  createColumnDef({title: 'Abbr', field: 'abbreviation'}),
  createColumnDef({title: 'Type', field: 'filterFlag'}),
  createColumnDef({title: 'ArmorGrade', field: 'armorGrade', visible: false}),
  createColumnDef({title: 'Level', field: 'itemLevel'}),
  createColumnDef({title: 'Count', field: 'count'}),
  createColumnDef({title: '1 star', field: 'legendaryMods.0.value'}),
  createColumnDef({title: '2 star', field: 'legendaryMods.1.value'}),
  createColumnDef({title: '3 star', field: 'legendaryMods.2.value'}),
  createColumnDef({title: 'Vendor Price', field: 'tradeOptions.vendorPrice', visible: false}),
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