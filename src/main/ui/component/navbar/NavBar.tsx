import React from "react";
import {Menubar} from 'primereact/menubar';

import {Button} from "primereact/button";
import { AppInfo, UrlConfig } from "../../service/domain";
import './NavBar.scss';
import {gameApiService} from "../../service/game.api.service";
import {routes} from "../../service/Routes";
import {MenuItem} from "primereact/menuitem";

export class NavBar extends React.Component<any, any> {

  private static renderButtons(buttons: Array<MenuItem>) {
    return () => {
      if (!buttons || buttons.length < 1) {
        return '';
      }
      return buttons.map((el: MenuItem) => {
        return (
            <a href={el.url} target={el.target} key={el.url} className={'button-link'}>
              <Button icon={el.icon} className={el.className}
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
    const websites: Array<any> = [];
    const tools: Array<any> = [];
    const currentWebsite = '(current)';
    gameApiService.appInfo().then((appInfo: AppInfo) => {
      appInfo.sites.forEach((site: UrlConfig) => {
        let siteName = site.name;
        if (window.location.href.includes(site.name)) {
          siteName += currentWebsite;
        }
        websites.push({
          label: siteName,
          url: site.url,
          target: '_blank'
        });
      });
      appInfo.tools.forEach((site: UrlConfig) => {
        tools.push({
          label: site.name,
          icon: 'pi pi-fw pi-cog',
          url: site.url,
          target: '_blank'
        });
      });
      let label = `${appInfo.name} v${appInfo.version}`;
      if (appInfo.gitConfig.buildTimestamp && appInfo.gitConfig.buildTimestamp.length > 0) {
        label += ` (${appInfo.gitConfig.buildTimestamp})`;
      }
      const {discord, github, commitUrl } = appInfo;
      const newState = {
        items: [
          {
            label: label,
            disabled: true
          },
          {
            label: 'Home',
            icon: 'pi pi-fw pi-home',
            command: (event: any) => {
              window.location.hash = routes.HOME;
            }
          },
          {
            label: 'Website',
            icon: 'pi pi-fw pi-globe',
            items: websites
          },
          {
            separator: true
          },
          {
            label: 'Get tools',
            icon: 'pi pi-fw pi-download',
            items: tools
          },
          {
            separator: true
          },
          {
            label: 'Settings',
            icon: 'pi pi-fw pi-cog',
            command: (event: any) => {
              window.location.hash = routes.SETTINGS;
            }
          },
          {
            label: 'Mod configurations',
            icon: 'pi pi-fw pi-cog',
            items: [
              {
                label: 'InventOmatic',
                icon: 'pi pi-fw pi-cog',
                command: (event: any) => {
                  window.location.hash = routes.InventOmatic;
                }
              },
              {
                label: 'HUDEditor',
                icon: 'pi pi-fw pi-cog',
                command: (event: any) => {
                  window.location.hash = routes.HUDEditor;
                }
              },
            ]
          },
        ],
        end: [
          {
            label: 'Discord',
            icon: 'pi pi-fw pi-discord',
            url: discord,
            className: 'p-button-outlined p-button-success',
            target: '_blank'
          },
          {
            label: 'Source code',
            icon: 'pi pi-fw pi-github',
            url: github,
            className: 'p-button-outlined p-button-primary',
            target: '_blank'
          },
          {
            label: 'Current website build',
            icon: 'pi pi-fw pi-github',
            url: commitUrl,
            className: 'p-button-outlined p-button-primary',
            target: '_blank'
          }
        ]
      };
      this.setState({...newState});
    });
    this.state = {
      items: [
        {
          label: 'FO76 Trade hub',
          disabled: true
        }
      ]
    };
  }

}
