import React from 'react';
import {Menubar} from 'primereact/menubar';
import {Button} from 'primereact/button';

export const Navbar = () => {
  const items = [
    {
      label: 'Website',
      icon: 'pi pi-fw pi-globe',
      items: [
        {
          label: 'Stage',
          url: 'https://fo76market.herokuapp.com/',
          target: '_blank',
        },
        {
          label: 'Stage 2',
          url: 'https://fo76market.azurewebsites.net/',
          target: '_blank',
        },
        {
          label: 'Production',
          url: 'https://fo76market.online/',
          target: '_blank',
        },
      ],
    },
    {
      label: 'Get tools',
      icon: 'pi pi-fw pi-download',
      items: [
        {
          label: 'Internal Extractor Mod',
          url: 'nexusmods.com/fallout76/mods/698',
          target: '_blank',
          icon: 'pi pi-fw pi-cog',
        },
        {
          label: 'Mod Companion App',
          url: 'https://www.nexusmods.com/fallout76/mods/744',
          target: '_blank',
          icon: 'pi pi-fw pi-cog',
        },
        {
          label: 'Mod Companion App (Github)',
          url: 'https://github.com/sdaskaliesku/modCompanionApp/packages/414872',
          target: '_blank',
          icon: 'pi pi-fw pi-cog',
        },
      ],
    },
  ];

  const logo = 'FO76 Trade';
  const end = () => {
    return (
        <>
          <Button label='Discord'
                  onClick={() => window.open(
                      'https://discord.com/invite/7fef733')}
                  icon="pi pi-discord"
                  className='p-button-outlined p-button-success'
          />
          <Button label='Source code'
                  onClick={() => window.open(
                      'https://github.com/sdaskaliesku/fo76tradeServer')}
                  icon="pi pi-github"
                  className='p-button-outlined p-button-success'
          />
          <Button label='Current website build'
                  onClick={() => window.open(
                      'https://github.com/sdaskaliesku/fo76tradeServer/commit/master')}
                  icon="pi pi-github"
                  className='p-button-outlined p-button-success'
          />
        </>
    );
  };

  return <Menubar model={items} start={logo} end={end}/>;
};