import React, {useCallback, useState} from "react";
import {InputNumber} from "primereact/inputnumber";
import {Utils} from "../../service/utils";
import {ColorPicker} from "primereact/colorpicker";
import {Checkbox, FormControlLabel} from "@material-ui/core";

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
  const [value, setValue] = useState(defaultValue);
  const key = Utils.uuidv4();

  const onChange = (e: any) => {
    // setValue(e.value);
    onDataChange(e.value);
  };

  return (
      <div className="p-field p-col-12 p-md-3 hud-element" key={key}>
        <label className={'element-label'} htmlFor={label + key}>{label}</label>
        <ColorPicker inputId={label + key} format="hex" value={value}
                     onChange={(e) => onChange(e)}/>
      </div>
  )
}

export const CheckboxComponent = (props: any) => {
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
            control={<Checkbox checked={value}
                               value={value}
                               onChange={(e) => onChange(e)}/>}
            label={label}
        />
      </div>
  )
}