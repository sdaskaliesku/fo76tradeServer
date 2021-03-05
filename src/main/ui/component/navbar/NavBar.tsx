import React from "react";
import {Menubar} from 'primereact/menubar';
import {MenuItem} from "primereact/api";
import {Button} from "primereact/button";
import {APP_VERSION, COMPANION_LINK, DISCORD_LINK, GH_LINK, NEXUS_LINK} from "../../service/domain";
import './NavBar.scss';

export class NavBar extends React.Component<any, any> {

  private static renderButtons(buttons: Array<MenuItem>) {
    return () => {
      if (!buttons || buttons.length < 1) {
        return '';
      }
      return buttons.map((el: MenuItem) => {
        return (
            <a href={el.url} target={el.target} key={el.url}>
              <Button href={el.url} icon={el.icon} className={el.className}
                      label={el.label}/>
            </a>
        );
      });
    }
  }

  render() {
    return (
        <Menubar model={this.state.items} end={NavBar.renderButtons(this.state.end)}
                 start={NavBar.renderButtons(this.state.start)}/>
    );
  }

  constructor(props: any) {
    super(props);
    this.state = {
      items: [
        {
          label: 'FO76 Trade hub v' + APP_VERSION,
          disabled: true
        },
        {
          label: 'Website',
          icon: 'pi pi-fw pi-globe',
          items: [
            {
              label: 'Stage',
              url: 'https://fo76market.herokuapp.com/',
              target: '_blank'
            },
            {
              label: 'Production',
              url: 'https://fo76market.online/',
              target: '_blank'
            }
          ]
        },
        {
          separator: true
        },
        {
          label: 'Get tools',
          icon: 'pi pi-fw pi-download',
          items: [
            {
              label: 'Item Extractor Mod',
              icon: 'pi pi-fw pi-cog',
              url: NEXUS_LINK,
              target: '_blank'
            },
            {
              label: 'Mod companion app',
              icon: 'pi pi-fw pi-cog',
              url: COMPANION_LINK,
              target: '_blank'
            }
          ]
        }
      ],
      end: [
        {
          label: 'Discord',
          icon: 'pi pi-fw pi-discord',
          url: DISCORD_LINK,
          className: 'p-button-outlined p-button-success',
          target: '_blank'
        },
        {
          label: 'Source code',
          icon: 'pi pi-fw pi-github',
          url: GH_LINK,
          className: 'p-button-outlined p-button-primary',
          target: '_blank'
        },
      ]
    };
  }

}