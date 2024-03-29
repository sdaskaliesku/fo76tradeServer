import React, {useCallback, useState} from "react";
import {Utils} from "../../../service/utils";
import {InputNumber} from "primereact/inputnumber";
import {Checkbox, FormControlLabel} from "@material-ui/core";
import {Color, ColorPicker, createColor} from 'material-ui-color';

export interface CheckboxComponentProps {
  label: string
  onDataChange: Function,
  defaultValue: any
}

export const CheckboxComponent = (props: CheckboxComponentProps) => {
  const {label, onDataChange, defaultValue} = props;
  const [value, setValue] = useState(defaultValue);
  const key = Utils.uuidv4();

  const onChange = useCallback(
      (e) => {
        setValue(e.target.checked);
        onDataChange(e.target.checked);
      },
      [value]
  );

  return (
      <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
        <FormControlLabel
            control={
              <Checkbox checked={value} value={value} onChange={(e) => onChange(e)}/>
            }
            label={label}
        />
      </div>
  )
}

export const InputNumberComponent = (props: any) => {
  const {label, step, min, max, onDataChange, defaultValue} = props;
  const [value, setValue] = useState(defaultValue);
  const key = Utils.uuidv4();

  const onChange = (e: any) => {
    // setValue(e.value);
    onDataChange(e.value);
  };
  return (
      <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
        <label className={'element-label'} htmlFor={label + key}>{label}</label>
        <InputNumber inputId={label + key} value={value}
                     onChange={(e) => onChange(e)}
                     showButtons
                     step={step}
                     min={min} max={max}
                     mode="decimal"/>
      </div>
  )
}

export const ColorPickerComponent = (props: any) => {
  const {label, onDataChange, defaultValue} = props;
  const [value, setValue] = useState('#' + defaultValue);
  const key = Utils.uuidv4();

  const onChange = useCallback((e: any) => {
    if (e.hex) {
      onDataChange(e.hex);
    }
    setValue(e);
  }, []);

  return (
      <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
        <label className={'element-label'} htmlFor={label + key}>{label}</label>
        <ColorPicker value={value} onChange={onChange} deferred={true}/>
      </div>
  )
}