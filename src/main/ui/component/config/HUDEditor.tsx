import React from "react";
import {Utils} from "../../service/utils";
import {createMuiTheme, CssBaseline, ThemeProvider} from "@material-ui/core";
import {Button} from "primereact/button";
import "./HUDEditor.scss";
import {InputNumber} from "primereact/inputnumber";
import {toXML} from "jstoxml";
import {ColorPicker} from "primereact/colorpicker";
import {Checkbox} from "primereact/checkbox";

interface HudEditorSettings {
  Elements: any
}

interface ElementProps {
  label: string
  description: string
  onDataChange: Function
  additionalElements?: Array<any>
}

export class ColorElement extends React.Component<ElementProps, any> {
  htmlElements: any;
  state = {
    RGB: '881111',
    Brightness: 0,
    Contrast: 0,
    Saturation: 0,
  };

  elements = [
    {
      label: 'RGB',
      color: true
    },
    {
      label: 'Brightness',
      min: -10000,
      max: 10000,
      default: 0,
      step: 0.1
    },
    {
      label: 'Contrast',
      min: -10000,
      max: 10000,
      default: 0,
      step: 0.1
    },
    {
      label: 'Saturation',
      min: -10000,
      max: 10000,
      default: 0,
      step: 0.1
    }
  ];

  createElement(props: any) {
    if (props.color) {
      return this.createColorInput(props.label, Utils.uuidv4());
    } else {
      // @ts-ignore
      return this.createInput(props.label, props.step, props.min, props.max, Utils.uuidv4());
    }
  }

  componentDidMount() {
    this.elements.forEach(el => {
      this.setState({[el.label]: el.default});
    });
    this.htmlElements = this.elements.map((el) => this.createElement(el));
    if (this.props && this.props.additionalElements) {
      this.props.additionalElements.forEach(el => {
        this.htmlElements.push(this.createElement(el));
      })
    }
    this.onDataChange(this.state);
  }

  onDataChange(data: any) {
    this.setState(data, () => {
      this.props.onDataChange({
        [this.props.label]: this.state
      });
    });
  }

  createColorInput(label: string, key: string) {
    // @ts-ignore
    let val = this.state[label];
    return (<div className="p-field p-col-12 p-md-3 hud-element" key={key}>
      <label className={'element-label'} htmlFor={label + key}>{label}</label>
      <ColorPicker inputId={label + key} format="hex" value={val}
                   onChange={(e) => this.onDataChange({[label]: e.value})}/>
    </div>)
  }

  createInput(label: string, step: number, min: number, max: number, key: string) {
    // @ts-ignore
    let val = this.state[label];
    return (
        <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
          <label className={'element-label'} htmlFor={label + key}>{label}</label>
          <InputNumber inputId={label + key} value={val}
                       onValueChange={(e) => this.onDataChange({[label]: e.value})} showButtons
                       step={step}
                       min={min} max={max}
                       mode="decimal"/>
        </div>
    )
  }

  render() {
    return (
        <div className={'hud-elements'}>
          <label className={'title'}>{this.props.label}</label>
          <label className={'description'}>{this.props.description}</label>
          {this.htmlElements}
        </div>
    );
  }
}

export class Element extends React.Component<ElementProps, any> {
  state = {
    Scale: 1,
    X: 0,
    Y: 0
  };
  elements = [
    {
      label: 'Scale',
      min: 0,
      max: 10,
      default: 1,
      step: 0.01
    },
    {
      label: 'X',
      min: -10000,
      max: 10000,
      default: 1,
      step: 1
    },
    {
      label: 'Y',
      min: -10000,
      max: 10000,
      default: 1,
      step: 1
    }
  ];
  htmlElements: any;


  componentDidMount() {
    this.elements.forEach(el => {
      this.setState({[el.label]: el.default});
    });
    this.htmlElements = this.elements.map((el) => this.createInput(el.label, el.step, el.min, el.max, Utils.uuidv4()));
    this.onDataChange(this.state);
  }

  onDataChange(data: any) {
    this.setState(data, () => {
      this.props.onDataChange({
        [this.props.label]: this.state
      });
    });
  }

  createInput(label: string, step: number, min: number, max: number, key: string) {
    // @ts-ignore
    let val = this.state[label];
    return (
        <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
          <label className={'element-label'} htmlFor={label + key}>{label}</label>
          <InputNumber inputId={label + key} value={val}
                       onValueChange={(e) => this.onDataChange({[label]: e.value})} showButtons
                       step={step}
                       min={min} max={max}
                       mode="decimal"/>
        </div>
    )
  }

  render() {
    return (
        <div className={'hud-elements'}>
          <label className={'title'}>{this.props.label}</label>
          <label className={'description'}>{this.props.description}</label>
          {this.htmlElements}
        </div>
    );
  }
}

export class HUDConfig extends React.Component<ElementProps, any> {
  state = {
    CrosshairOpacity: 1,
    EnableRecoloring: true,
    ThirstHungerPercentShow: true,
    AlwaysShowThirstHunger: false,
    EditMode: false,
    CustomCrosshair: true,
    TZMapMarkers: false
  };
  elements = [
    {
      label: 'CrosshairOpacity',
      min: -10,
      max: 10,
      default: 1,
      step: 0.01
    },
    {
      label: 'EnableRecoloring',
      isBool: true,
      default: true,
    },
    {
      label: 'ThirstHungerPercentShow',
      isBool: true,
      default: true,
    },
    {
      label: 'AlwaysShowThirstHunger',
      isBool: true,
      default: true,
    },
    {
      label: 'EditMode',
      isBool: true,
      default: true,
    },
    {
      label: 'CustomCrosshair',
      isBool: true,
      default: true,
    },
    {
      label: 'TZMapMarkers',
      isBool: true,
      default: true,
    },
  ];
  htmlElements: any;


  componentDidMount() {
    this.elements.forEach(el => {
      this.setState({[el.label]: el.default});
    });
    this.htmlElements = this.elements.map((el) => this.createElement(el));
    this.onDataChange({}, this.state);
  }

  onDataChange(e: any, data: any) {
    this.setState(data, () => {
      this.props.onDataChange({
        [this.props.label]: this.state
      });

    });
  }

  createCheckBox(label: string, key: string) {
    // @ts-ignore
    let val = this.state[label];
    return (
        <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
          <label className={'element-label'} htmlFor={label + key}>{label}</label>
          <Checkbox inputId={label + key}
                    onChange={(e) => this.onDataChange(e, {[label]: e.checked})}
                    checked={val}/>
        </div>
    )
  }

  createInput(label: string, step: number, min: number, max: number, key: string) {
    // @ts-ignore
    let val = this.state[label];
    return (
        <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
          <label className={'element-label'} htmlFor={label + key}>{label}</label>
          <InputNumber inputId={label + key} value={val}
                       onValueChange={(e) => this.onDataChange(e, {[label]: e.value})}
                       showButtons
                       step={step}
                       min={min} max={max}
                       mode="decimal"/>
        </div>
    )
  }

  createElement(props: any) {
    if (props.isBool) {
      return this.createCheckBox(props.label, Utils.uuidv4());
    } else {
      // @ts-ignore
      return this.createInput(props.label, props.step, props.min, props.max, Utils.uuidv4());
    }
  }

  render() {
    return (
        <div className={'hud-elements'}>
          <label className={'title'}>{this.props.label}</label>
          <label className={'description'}>{this.props.description}</label>
          {this.htmlElements}
        </div>
    );
  }
}

export class HUDEditor extends React.Component<any, HudEditorSettings> {
  state = {
    Elements: {}
  };
  data = {
    elements: [
      {
        label: 'QuickLoot',
        description: 'Menu that shows when you roll over a container. Also includes some container interact prompts (eg Stash box).'
      },
      {
        label: 'Frobber',
        description: 'Includes lock prompts (like unlocking a door or interacting with NPCs)'
      },
      {
        label: 'RollOver',
        description: 'Includes the interact prompts for most other things.'
      },
      {
        label: 'Compass',
        description: 'Compass that is normally centered at the bottom.'
      },
      {
        label: 'QuestTracker',
        description: 'Quest tracker that is normally in the upper right.'
      },
      {
        label: 'Notification',
        description: 'Includes messages like "You lack the requirements to make this". Also includes public event notifications.'
      },
      {
        label: 'LeftMeter',
        description: 'Health bar and rads indicator/taking rads text.'
      },
      {
        label: 'StealthMeter',
        description: 'The meter that appears when you crouch (Hidden/Danger etc).'
      },
      {
        label: 'Announce',
        description: 'Quest announcements/rewards.'
      },
      {
        label: 'TeamPanel',
        description: 'The list of teammates shown when on a team.The list of teammates shown when on a team.'
      },
      {
        label: 'FusionCoreMeter',
        description: 'Fusion core meter when the power armor gauges UI is disabled.'
      }
    ],
    rightMeter: {
      parts: [
        {
          label: 'APMeter',
          description: 'Shows your current AP.'
        },
        {
          label: 'ActiveEffects',
          description: 'Shows your current acive effects (Diseased etc).'
        },
        {
          label: 'HungerMeter',
          description: 'The hunger bar.'
        },
        {
          label: 'ThirstMeter',
          description: 'The thirst bar.'
        },
        {
          label: 'AmmoCount',
          description: 'Your current/total ammo counts.'
        },
        {
          label: 'ExplosiveAmmoCount',
          description: 'Your current grenade/mine total.'
        },
        {
          label: 'FlashLightWidget',
          description: 'Unused for now. Would be the indicator that displays when flashlight is on.'
        },
        {
          label: 'Emote',
          description: 'Shows the animation of your last used emote.'
        }
      ]
    },
    colors: [
      {
        label: 'HitMarkerTint',
        description: 'Some description'
      },
      {
        label: 'RightMeters',
        description: 'Some description'
      },
      {
        label: 'LeftMeters',
        description: 'Some description',
        additionalElements: [
          {
            label: 'RadsBarRGB',
            color: true
          }
        ]
      },
      {
        label: 'Notifications',
        description: 'Some description'
      },
      {
        label: 'HudFrobber',
        description: 'Some description'
      },
      {
        label: 'QuestTracker',
        description: 'Some description'
      },
      {
        label: 'BottomCenter',
        description: 'Some description',
        additionalElements: [
          {
            label: 'CompassRGB',
            color: true
          },
          {
            label: 'CompassBrightness',
            min: -10000,
            max: 10000,
            default: 0,
            step: 0.1
          },
          {
            label: 'CompassContrast',
            min: -10000,
            max: 10000,
            default: 0,
            step: 0.1
          },
          {
            label: 'CompassSaturation',
            min: -10000,
            max: 10000,
            default: 0,
            step: 0.1
          }
        ]
      },
      {
        label: 'Announce',
        description: 'Some description'
      },
      {
        label: 'Center',
        description: 'Some description'
      },
      {
        label: 'Team',
        description: 'Some description',
        additionalElements: [
          {
            label: 'RadsBarRGB',
            color: true
          }
        ]
      },
      {
        label: 'Floating',
        description: 'Some description'
      },
    ]
  }
  elementsHolder = {
    Elements: {
      RightMeter: {
        Parts: {}
      }
    },
    Colors: {}
  };


  constructor(props: any, context: any) {
    super(props, context);
    this.onElementsDataChange = this.onElementsDataChange.bind(this);
    this.onElementsPartsDataChange = this.onElementsPartsDataChange.bind(this);
    this.onColorElementDataChange = this.onColorElementDataChange.bind(this);
  }

  onColorElementDataChange(data: any) {
    this.elementsHolder = {...this.elementsHolder, ...{Colors: {...this.elementsHolder.Colors, ...data}}};
  }

  onElementsPartsDataChange(data: any) {
    this.elementsHolder.Elements.RightMeter.Parts = {...this.elementsHolder.Elements.RightMeter.Parts, ...data};
  }

  onElementsDataChange(data: any) {
    this.elementsHolder = {...this.elementsHolder, ...{Elements: {...this.elementsHolder.Elements, ...data}}};
  }

  render() {
    const onSubmit = () => {
      const finalObject = {
        HUDEditor: this.elementsHolder
      };
      const jsonString: string = toXML(finalObject, {
        indent: '       '
      });
      Utils.downloadString(jsonString, 'text/xml', 'HUDEditor.xml');
    };
    const theme = createMuiTheme({
      palette: {
        type: "dark"
      }
    });
    return (
        <ThemeProvider theme={theme}>
          <CssBaseline/>
          <div className={"wrapper"}>
            <div className={"hud-editor-form"}>
              <h1>THIS IS WORK IN PROGRESS!</h1>
              <Button className={'p-button-success'} onClick={onSubmit}>Get config!</Button>
              <div className={'elements-row'}>
                {this.data.elements.map(el => <Element label={el.label} description={el.description}
                                                       key={Utils.uuidv4()}
                                                       onDataChange={this.onElementsDataChange}/>)}
              </div>
              <h3>Right meters</h3><br/>
              <div className={'elements-row'}>
                {this.data.rightMeter.parts.map(el => <Element label={el.label}
                                                               description={el.description}
                                                               key={Utils.uuidv4()}
                                                               onDataChange={this.onElementsPartsDataChange}/>)}
              </div>
              <h3>Colors</h3><br/>
              <div className={'elements-row'}>
                {this.data.colors.map(el => <ColorElement label={el.label}
                                                          description={el.description}
                                                          key={Utils.uuidv4()}
                                                          onDataChange={this.onColorElementDataChange}
                                                          additionalElements={el.additionalElements}/>)}
              </div>
              <div className={'elements-row hud'}>
                <HUDConfig label={'HUD'} description={'some description'}
                           onDataChange={this.onColorElementDataChange}/>
              </div>
            </div>
          </div>
        </ThemeProvider>
    );
  }
}