import React, {useRef, useState} from "react";
import ReactDOM from "react-dom";

import 'primereact/resources/themes/bootstrap4-dark-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import PrimeReact from 'primereact/api';
import {TableComponent} from "./component/table/TableComponent";
import {NavBar} from "./component/navbar/NavBar";
import {FileUploader, FileUploaderStatus} from "./component/fileUploader/FileUploader";
import {InfoDialog} from "./component/dialog/InfoDialog";
import {MIN_MOD_SUPPORTED_VERSION} from "./service/domain";

PrimeReact.ripple = true;

document.documentElement.style.fontSize = '9px';

const modalTexts = {
  invalidVersion: (version: any) => {
    return `You are using unsupported mod version ${version}. Minimum supported version of the mod: ${MIN_MOD_SUPPORTED_VERSION}. Please, update the mod, extract your items and try uploading new file again.`;
  },
  emptyResult: () => {
    return `No data received. Try using different upload filters or disable them at all. If issue is persistent, please, join discord channel and send your file to the author.`
  },
  errorResult: () => {
    return `Something went wrong. Please, reach out author of the mod/website and send the file for investigation. You may ask for help in discord.`;
  },
};

const App = () => {
  const [tableData, setTableData] = useState([]);
  const [fileStatus, setFileStatus] = useState(FileUploaderStatus.NONE);
  const dialogRef: any = useRef(null);

   const showModal = (input: string) => {
    dialogRef.current.show('Ooops', input);
  }

  const onDataError = (e: any) => {
    console.error(e);
    showModal(modalTexts.errorResult());
    setTableData([])
    setFileStatus(FileUploaderStatus.NONE);
  };
  const onDataReceived = (e: any) => {
    if (e.length < 1) {
      showModal(modalTexts.emptyResult());
      setTableData([])
      setFileStatus(FileUploaderStatus.NONE);
      return;
    }
    setFileStatus(FileUploaderStatus.LOADED);
    setTableData(e);
  };
  const onStatusUpdate = (e: FileUploaderStatus) => {
    setFileStatus(e);
  }

  const template = (body: any) => (
      <React.Fragment>
        <NavBar/>
        {body}
        <InfoDialog ref={dialogRef}/>
      </React.Fragment>);

  let body: any;

  switch (fileStatus) {
    case FileUploaderStatus.ERROR:
      body = <p>'modal dialog'</p>;
      break;
    case FileUploaderStatus.LOADED:
      if (tableData.length > 1) {
        body = (<TableComponent value={tableData}/>);
      }
      break;
    default:
      body = (<FileUploader onStatusUpdate={onStatusUpdate} onDataError={onDataError}
                            onDataReceived={onDataReceived}/>);
      break;
  }
  return template(body);
};

ReactDOM.render(
    <App/>,
    document.getElementById("app")
);