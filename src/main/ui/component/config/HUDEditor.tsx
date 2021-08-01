import React from "react";
import {Utils} from "../../service/utils";
import {Button, Tooltip} from "@material-ui/core";
import "./HUDEditor.scss";

import HelpIcon from '@material-ui/icons/Help';
import {gameApiService} from "../../service/game.api.service";
import {
  CheckboxComponent,
  ColorPickerComponent,
  InputNumberComponent
} from "./schema-form/Components";

interface HUDElement {
  [name: string]: HudConfigElement
}

interface HudConfigElement {
  label: string
  id: string
  description: string
  additionalElements?: Array<HUDElement>
  additionalConfigs?: Array<HudConfigElement>
  fields: Array<HUDField>
}

export interface HUDField {
  label: string
  id: string
  description?: string
  defaultValue: any
  min?: number
  max?: number
  step?: number
  type: "NUMERIC" | "COLOR" | "BOOLEAN"
}

export interface HUDEditorSchema {
  [name: string]: HUDElement
}


export class HUDEditor extends React.Component<any, any> {
  htmlElements: Array<any> = [];
  HUDEditor: any = {};
  fileReader: FileReader = new FileReader();
  data: any = {};
  schema: HUDEditorSchema = {};
  state: any = {
    HUDEditor: {},
    htmlElements: []
  };

  setConfigState(path: string, value: any) {
    const configState = JSON.parse(JSON.stringify(this.HUDEditor));
    Utils.setPropertyByPath(this.HUDEditor, path, value);
    Utils.setPropertyByPath(configState, path, value);
    const prevState = this.state.HUDEditor;
    const newState = {
      ...prevState,
      ...configState
    };
    this.setState({
      HUDEditor: {
        ...this.state.HUDEditor,
        ...newState
      }
    });
  }

  constructor(props: any, context: any) {
    super(props, context);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleFileChosen = this.handleFileChosen.bind(this);
    this.handleFileRead = this.handleFileRead.bind(this);
  }

  processHudElement(hudElement: HUDElement, elements: Array<any>, path: string) {
    const schemaElKeys = Object.keys(hudElement);
    for (const schemaElKey of schemaElKeys) {
      const newPath = path + '.' + schemaElKey;
      const configEl: HudConfigElement = hudElement[schemaElKey];
      this.processHudConfigElement(configEl, elements, newPath);
    }
  }

  getFieldValue(hudField: HUDField, path: string): any {
    const providedValue = Utils.getPropertyByPath(this.data.HUDEditor, path);
    const hudVal = hudField.defaultValue;
    if (providedValue !== undefined) {
      return providedValue;
    }
    return hudVal;
  }

  processHudConfigElement(configEl: HudConfigElement, elements: Array<any>, path: string) {
    this.setConfigState(path, {});
    if (configEl.fields && configEl.fields.length > 0) {
      const fieldsElements = configEl.fields.map((hudField) => {
        const newPath = path + '.' + hudField.id;
        this.setConfigState(newPath, this.getFieldValue(hudField, path));
        return this.createHtmlElement(hudField, newPath);
      });
      elements.push(
          <div className={'hud-elements'} key={Utils.uuidv4()}>
            <Tooltip title={configEl.description}>
              <label className={'title'}>
                {configEl.label}&nbsp;<HelpIcon fontSize={'large'}/>
              </label>
            </Tooltip>
            {fieldsElements}
          </div>
      );
    }
    if (configEl.additionalConfigs && configEl.additionalConfigs.length > 0) {
      const els: Array<any> = [];
      configEl.additionalConfigs.forEach(hudConfigEl => {
        const newPath = path + '.' + hudConfigEl.id;
        this.processHudConfigElement(hudConfigEl, els, newPath);
      });
      elements.push(...els);
    }
    if (configEl.additionalElements && configEl.additionalElements.length > 0) {
      const els: Array<any> = [];
      configEl.additionalElements.forEach(hudEl => {
        this.processHudElement(hudEl, els, path);
      });
      elements.push(...els);
    }
  }

  static downloadConfig(data: any) {
    const finalObject = {
      HUDEditor: data
    };
    const jsonString: string = Utils.toXML(finalObject);
    Utils.downloadString(jsonString, 'text/xml', 'HUDEditor.xml');
  }

  onSubmit() {
    HUDEditor.downloadConfig(this.state.HUDEditor);
  }

  createHtmlElement(hudField: HUDField, path: string) {
    const onDataChange = (data: any) => {
      this.setConfigState(path, data);
    }
    switch (hudField.type) {
      case "COLOR":
        return this.createColorInput(hudField, onDataChange, path);
      case "BOOLEAN":
        return this.createCheckBox(hudField, onDataChange, path);
      case "NUMERIC":
        return this.createInput(hudField, onDataChange, path);
    }
  }

  createColorInput(hudField: HUDField, onDataChange: Function, path: string) {
    const key = Utils.uuidv4();
    const {label} = hudField;
    const defaultValue = String(this.getFieldValue(hudField, path));
    this.setConfigState(path, defaultValue);
    return (
        <ColorPickerComponent label={label} defaultValue={defaultValue} onDataChange={onDataChange}
                              key={key}/>)
  }

  createInput(hudField: HUDField, onDataChange: Function, path: string) {
    const key = Utils.uuidv4();
    const {label, step, min, max} = hudField;
    const defaultValue = this.getFieldValue(hudField, path);
    this.setConfigState(path, defaultValue);
    return (
        <InputNumberComponent onDataChange={onDataChange}
                              defaultValue={defaultValue}
                              label={label}
                              step={step}
                              min={min} max={max} key={key}/>
    )
  }

  createCheckBox(hudField: HUDField, onDataChange: Function, path: string) {
    const key = Utils.uuidv4();
    const {label} = hudField;
    const defaultValue = this.getFieldValue(hudField, path);
    this.setConfigState(path, defaultValue);
    return (
        <CheckboxComponent onDataChange={onDataChange} label={label} key={key}
                           defaultValue={defaultValue}/>
    )
  }

  handleFileRead() {
    const content = this.fileReader.result;
    this.data = Utils.fromXML(content);
    this.buildElements();
  };

  handleFileChosen(e: any) {
    this.fileReader = new FileReader();
    this.fileReader.onloadend = this.handleFileRead;
    this.fileReader.readAsText(e.target.files[0]);
  }

  buildElements() {
    const schemaKeys = Object.keys(this.schema);
    const htmlElements = [];
    for (const key of schemaKeys) {
      const path = key;
      this.setConfigState(path, {});
      const elements: Array<any> = [];
      const schemaElement: HUDElement = this.schema[key];
      this.processHudElement(schemaElement, elements, path);
      htmlElements.push(
          <div className={'elements-row'} key={Utils.uuidv4()}>
            {elements}
          </div>
      )
    }
    this.setState({
      htmlElements
    });
  }

  componentDidMount() {
    gameApiService.hudEditorConfig().then((schema) => {
      this.schema = schema;
      console.log(schema);
      this.buildElements();
    });
  }

  render() {
    return (
        <div className={"wrapper"}>
          <div className={"hud-editor-form"}>
            <h1 className={'title'}>UI config for <a href={'https://www.nexusmods.com/fallout76/mods/953'} target={'_blank'}>HUDEditor</a>!</h1>
            <Button variant="contained" component="label">
              Upload File
              <input type="file" hidden onChange={this.handleFileChosen}/>
            </Button>
            <Button variant="contained" color={'secondary'} onClick={this.onSubmit}>Get
              config!</Button>
            {this.state.htmlElements}
          </div>
        </div>
    );
  }
}