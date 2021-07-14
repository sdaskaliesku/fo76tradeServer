import React from "react";
import {Utils} from "../../service/utils";
import {createMuiTheme, CssBaseline, ThemeProvider} from "@material-ui/core";
import {Button} from "primereact/button";
import "./HUDEditor.scss";
import {InputNumber} from "primereact/inputnumber";
import {toXML} from "jstoxml";

interface HudEditorSettings {
  Elements: any
}

interface ElementProps {
  label: string,
  description: string
  onDataChange: Function
}

export class Element extends React.Component<ElementProps, any> {
  state = {};
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

export class HUDEditor extends React.Component<any, HudEditorSettings> {
  state = {
    Elements: {}
  };
  data = {
    elements: [
      {
        label: 'QuickLoot',
        description: 'Some description'
      },
      {
        label: 'Frobber',
        description: 'Some description'
      },
      {
        label: 'RollOver',
        description: 'Some description'
      },
      {
        label: 'Compass',
        description: 'Some description'
      },
      {
        label: 'QuestTracker',
        description: 'Some description'
      },
      {
        label: 'Notification',
        description: 'Some description'
      },
      {
        label: 'LeftMeter',
        description: 'Some description'
      },
      {
        label: 'StealthMeter',
        description: 'Some description'
      },
      {
        label: 'Announce',
        description: 'Some description'
      },
      {
        label: 'TeamPanel',
        description: 'Some description'
      },
      {
        label: 'FusionCoreMeter',
        description: 'Some description'
      }
    ],
    rightMeter: {
      parts: [
        {
          label: 'APMeter',
          description: 'Some description'
        },
        {
          label: 'ActiveEffects',
          description: 'Some description'
        },
        {
          label: 'HungerMeter',
          description: 'Some description'
        },
        {
          label: 'ThirstMeter',
          description: 'Some description'
        },
        {
          label: 'AmmoCount',
          description: 'Some description'
        },
        {
          label: 'QuickLoot',
          description: 'Some description'
        },
        {
          label: 'ExplosiveAmmoCount',
          description: 'Some description'
        },
        {
          label: 'FlashLightWidget',
          description: 'Some description'
        },
        {
          label: 'Emote',
          description: 'Some description'
        }
      ]
    }
  }
  elementsHolder = {
    Elements: {
      RightMeter: {
        Parts: {}
      }
    }
  };


  constructor(props: any, context: any) {
    super(props, context);
    this.onElementsDataChange = this.onElementsDataChange.bind(this);
    this.onElementsPartsDataChange = this.onElementsPartsDataChange.bind(this);
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
            </div>
          </div>
        </ThemeProvider>
    );
  }
}