import React from "react";
import {FileUpload} from "primereact/fileupload";
import {Utils} from "../../service/utils";
import {ModDataRequest} from "../../service/domain";
import {
  filterService,
  ItemContext,
  UploadFileFilters,
  UploadFilter
} from "../../service/filter.service";
import {ToggleButton} from "primereact/togglebutton";
import {itemService} from "../../service/item.service";
import './FileUploader.scss';
import {ProgressSpinner} from "primereact/progressspinner";

interface FileUploaderState {
  files: Array<File>
  filters: Array<UploadFilter>,
  status: FileUploaderStatus
}

export interface FileUploaderProps {
  onDataReceived(name: string): any;

  onDataError(name: string): any;

  onStatusUpdate(name: FileUploaderStatus): any;
}

export enum FileUploaderStatus {
  LOADING,
  LOADED,
  NONE,
  ERROR
}

export class FileUploader extends React.Component<FileUploaderProps, FileUploaderState> {
  state = {
    files: [],
    filters: [],
    status: FileUploaderStatus.NONE
  }

  private dialogRef: any;

  componentDidMount() {
    filterService.getUploadFilters().then(filters => {
      this.setState({filters: filters});
    });
    this.uploadFile = this.uploadFile.bind(this);
    this.onSelectFile = this.onSelectFile.bind(this);
    this.dialogRef = React.createRef();
  }

  private setStatus(status: FileUploaderStatus) {
    this.setState({status: status});
    this.props.onStatusUpdate(status);
  }

  private uploadFile() {
    this.setStatus(FileUploaderStatus.LOADING);
    this.state.files.forEach((file) => {
      Utils.readFile(file).then(data => {
        const itemContext: ItemContext = {
          priceCheck: false,
          shortenResponse: true,
          fed76Enhance: true
        }
        const filters: UploadFileFilters = {
          filterFlags: this.state.filters.filter((f: UploadFilter) => !f.isContext).filter((f: UploadFilter) => f.checked).map((f: UploadFilter) => f.text)
        };
        this.state.filters.filter((f: UploadFilter) => f.isContext).filter((f: UploadFilter) => f.checked).forEach((f: UploadFilter) => {
          // @ts-ignore
          itemContext[f.id] = f.checked;
        });
        const request: ModDataRequest = {
          modData: data,
          filters: filters
        };
        itemService.prepareModData(request, itemContext).then((e: any) => {
          this.setStatus(FileUploaderStatus.LOADED);
          this.props.onDataReceived(e);
        }).catch((e: any) => {
          this.setStatus(FileUploaderStatus.ERROR);
          this.props.onDataError(e);
        })
      }).catch((e) => {
        this.setStatus(FileUploaderStatus.ERROR);
        this.props.onDataError(e);
        console.error(e);
      })
    });
  }

  private onSelectFile(event: any) {
    this.setState({files: [...event.files]}, () => {
      // TODO: remove this, once multiple files will be supported on backend
      this.uploadFile();
    });
  }

  private setFilterChecked(e: any, filter: UploadFilter) {
    let newFilters: Array<UploadFilter> = []
    this.state.filters.forEach((f: UploadFilter) => {
      if (f.id === filter.id) {
        f.checked = e;
      }
      newFilters.push(f);
    })
    this.setState({filters: newFilters});
  }

  private renderFilters() {
    return this.state.filters.map((filter: UploadFilter) => {
      if (filter.isSeparator) {
        return (<div style={{paddingRight: '5px'}} key={'separator' + filter.id}/>);
      }
      const onChange = (e: any) => {
        this.setFilterChecked(e.value, filter);
      }
      return (
          <ToggleButton onLabel={filter.text}
                        offLabel={filter.text}
                        onIcon="pi pi-check"
                        offIcon="pi pi-times"
                        checked={filter.checked}
                        key={filter.id}
                        onChange={onChange}
                        tooltip={filter.tooltipText?filter.tooltipText: filter.text}
                        tooltipOptions={{position: 'bottom'}}
                        className={'p-button-lg filter-button'}
          />);
    });
  }

  render() {
    const {status} = this.state;
    let spinner: any = '';
    if (status === FileUploaderStatus.LOADING) {
      spinner = <ProgressSpinner/>
    }
    const filters = this.renderFilters();
    return (
        <React.Fragment>
          <div className={'filter-wrapper'}>{filters}</div>
          <FileUpload className={'file-upload'}
                      customUpload={true}
                      multiple={false}
                      onSelect={this.onSelectFile}
                      uploadHandler={this.uploadFile}
          />
          {spinner}
        </React.Fragment>
    );
  }
}