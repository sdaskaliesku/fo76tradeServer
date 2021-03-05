import React from "react";
import {Dialog} from "primereact/dialog";
import {Button} from "primereact/button";

export class InfoDialog extends React.Component<any, any> {

  state = {
    visible: false,
    content: '',
    header: ''
  }

  private setVisible(value: boolean) {
    this.setState({visible: value});
  }

  private setContent(value: any) {
    this.setState({content: value});
  }

  private setHeader(value: any) {
    this.setState({header: value});
  }

  public show(header: any, content: any) {
    this.setContent(content);
    this.setHeader(header);
    this.setVisible(true);
  }

  private renderFooter() {
    return (
        <div>
          <Button label="Close" icon="pi pi-check" onClick={() => {
            this.setVisible(false)
          }} autoFocus/>
        </div>
    );
  }

  render() {
    const {header, content, visible} = this.state;
    return (
        <Dialog header={header}
                visible={visible}
                style={{width: '50vw'}}
                onHide={() => {
                  this.setVisible(false)
                }}
                footer={this.renderFooter()}>
          {content}
        </Dialog>
    );
  }
}