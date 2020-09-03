export const columns = [
  {
    formatter: 'rowSelection',
    titleFormatter: 'rowSelection',
    width: 10,
    hozAlign: 'center',
    headerSort: false,
    download: false,
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Name', field: 'text', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Account', field: 'ownerInfo.accountOwner', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Character', field: 'ownerInfo.characterOwner', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Stars',
    field: 'numLegendaryStars',
    formatter: 'star',
    width: 90,
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Abbr', field: 'abbreviation', sorter: 'string', width: 150,
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Type', field: 'filterFlag', sorter: 'string', width: 180,
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Level', field: 'itemLevel', sorter: 'number', width: 90,
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Count', field: 'count', sorter: 'number', width: 100,
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: '1 star', field: 'legendaryMods.0.value', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: '2 star', field: 'legendaryMods.1.value', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: '3 star', field: 'legendaryMods.2.value', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Vendor Price', field: 'tradeOptions.vendorPrice', sorter: 'number',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    title: 'Description', field: 'description', sorter: 'string',
    cellClick: function(e, cell) {
      cell.getRow().toggleSelect();
    },
  },
  {
    formatter: 'buttonCross',
    width: 10,
    hozAlign: 'center',
    headerSort: false,
    download: false,
    cellClick: function(e, cell) {
      cell.getRow().delete().then(() => {
      });
    },
  },
];

export const filters = [
  {
    id: 'tradableOnly',
    name: 'Tradable',
    checked: true,
    hide: true,
  },
  {
    id: 'legendaries',
    name: 'legendaries',
  },
  {
    name: 'WEAPON',
    filters: '2,3',
    types: [
      'WEAPON',
      'WEAPON_MELEE',
      'WEAPON_RANGED',
      'WEAPON_THROWN',
    ],
  },
  {
    name: 'ARMOR',
    filters: '4',
    types: [
      'ARMOR',
      'ARMOR_OUTFIT',
    ],
  },
  {
    name: 'AID',
    filters: '8,9',
    types: [],
  },
  {
    name: 'HOLO',
    filters: '512',
    types: [],
  },
  {
    name: 'AMMO',
    filters: '4096',
    types: [],
  },
  {
    name: 'NOTES',
    filters: '128,8192',
    types: [],
  },
  {
    name: 'MISC',
    filters: '33280',
    types: [],
  },
  {
    name: 'MODS',
    filters: '2048',
    types: [],
  },
  {
    name: 'JUNK',
    filters: '33792, 1024',
    types: [],
  },
];