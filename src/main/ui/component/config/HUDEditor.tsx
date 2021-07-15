import React from "react";
import {Utils} from "../../service/utils";
import {Button, CssBaseline, ThemeProvider} from "@material-ui/core";
import "./HUDEditor.scss";
import {theme} from "../../index";

import {
  CheckboxComponent,
  ColorPickerComponent,
  InputNumberComponent
} from "./HUDComponents";

interface HUDEditorProps {
  schema: HUDEditorSchema
}

interface HUDElement {
  [name: string]: HudConfigElement
}

interface HudConfigElement {
  label: string
  id: string
  description?: string
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


export class HUDEditor extends React.Component<HUDEditorProps, any> {
  htmlElements: Array<any> = [];
  HUDEditor: any = {};
  fileReader:FileReader = new FileReader();

  constructor(props: HUDEditorProps, context: any) {
    super(props, context);
    this.onSubmit = this.onSubmit.bind(this);
    this.handleFileChosen = this.handleFileChosen.bind(this);
    this.handleFileRead = this.handleFileRead.bind(this);
  }

  processHudElement(hudElement: HUDElement, stateObj: any, elements: Array<any>, path: string) {
    const schemaElKeys = Object.keys(hudElement);
    for (const schemaElKey of schemaElKeys) {
      const newPath = path + '.' + schemaElKey;
      const configEl: HudConfigElement = hudElement[schemaElKey];
      this.processHudConfigElement(configEl, stateObj, elements, newPath);
    }
  }

  processHudConfigElement(configEl: HudConfigElement, stateObj: any, elements: Array<any>, path: string) {
    stateObj[configEl.id] = {};
    if (configEl.fields && configEl.fields.length > 0) {
      const fieldsElements = configEl.fields.map((hudField) => {
        stateObj[configEl.id][hudField.id] = hudField.defaultValue;
        const newPath = path + '.' + hudField.id;
        return this.createHtmlElement(hudField, stateObj[configEl.id], newPath);
      });
      elements.push(
          <div className={'hud-elements'} key={Utils.uuidv4()}>
            <label className={'title'}>{configEl.label}</label>
            <label className={'description'}>{configEl.description}</label>
            {fieldsElements}
          </div>
      );
    }
    if (configEl.additionalConfigs && configEl.additionalConfigs.length > 0) {
      const els: Array<any> = [];

      configEl.additionalConfigs.forEach(hudConfigEl => {
        const newPath = path + '.' + hudConfigEl.id;
        this.processHudConfigElement(hudConfigEl, stateObj[configEl.id], els, newPath);
      });
      elements.push(...els);
    }
    if (configEl.additionalElements && configEl.additionalElements.length > 0) {
      const els: Array<any> = [];
      configEl.additionalElements.forEach(hudEl => {
        const newPath = path + '.' + hudEl.id;
        this.processHudElement(hudEl, stateObj[configEl.id], els, newPath);
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
    HUDEditor.downloadConfig(this.HUDEditor);
    console.log(this.HUDEditor);
  }

  createHtmlElement(hudField: HUDField, stateObj: any, path: string) {
    // const newPath = path + '.' + hudField.id;
    const newPath = path;
    const onDataChange = (data: any) => {
      stateObj[hudField.id] = data;
      console.log(newPath + ' = ' + data);
      console.log(stateObj);
    }
    switch (hudField.type) {
      case "COLOR":
        return this.createColorInput(hudField, stateObj, onDataChange, newPath);
      case "BOOLEAN":
        return this.createCheckBox(hudField, stateObj, onDataChange, newPath);
      case "NUMERIC":
        return this.createInput(hudField, stateObj, onDataChange, newPath);
    }
  }

  createColorInput(hudField: HUDField, stateObj: any, onDataChange: Function, path: string) {
    const key = Utils.uuidv4();
    const {label, defaultValue} = hudField;
    return (
        <ColorPickerComponent label={label} defaultValue={defaultValue} onDataChange={onDataChange}
                              key={key}/>)
  }

  createInput(hudField: HUDField, stateObj: any, onDataChange: Function, path: string) {
    const key = Utils.uuidv4();
    const {label, step, min, max, defaultValue} = hudField;
    return (
        <InputNumberComponent onDataChange={onDataChange}
                              defaultValue={defaultValue}
                              label={label}
                              step={step}
                              min={min} max={max} key={key}/>
    )
  }

  createCheckBox(hudField: HUDField, stateObj: any, onDataChange: Function, path: string) {
    let val = stateObj[hudField.id];
    const key = Utils.uuidv4();
    const {label, defaultValue} = hudField;
    return (
        <CheckboxComponent checked={val} onDataChange={onDataChange} label={label} key={key}
                           defaultValue={defaultValue}/>
    )
  }

  handleFileRead(e: any) {
    const content = this.fileReader.result;
    console.log(content)
  };

  handleFileChosen(file: any) {
    this.fileReader = new FileReader();
    this.fileReader.onloadend = this.handleFileRead;
    this.fileReader.readAsText(file);
  }

  render() {

    const {schema} = this.props;
    const schemaKeys = Object.keys(schema);
    for (const key of schemaKeys) {
      const path = key;
      this.HUDEditor[key] = {};
      const elements: Array<any> = [];
      const schemaElement: HUDElement = schema[key];
      this.processHudElement(schemaElement, this.HUDEditor[key], elements, path);
      this.htmlElements.push(
          <div className={'elements-row'} key={Utils.uuidv4()}>
            {elements}
          </div>
      )
    }

    return (
        <ThemeProvider theme={theme}>
          <CssBaseline/>
          <div className={"wrapper"}>
            <div className={"hud-editor-form"}>
              <h1>THIS IS WORK IN PROGRESS!</h1>
              {/*<Button variant="contained" component="label">*/}
              {/*  Upload File*/}
              {/*  <input type="file"*/}
              {/*         hidden*/}
              {/*         onChange={e => this.handleFileChosen(e.target.files[0])}*/}
              {/*  />*/}
              {/*</Button>*/}
              <Button variant="contained" color={'secondary'} onClick={this.onSubmit}>Get
                config!</Button>
              {this.htmlElements}
            </div>
          </div>
        </ThemeProvider>
    );
  }
}