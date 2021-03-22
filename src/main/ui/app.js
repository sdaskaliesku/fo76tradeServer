import React, { useState } from 'react';
import ReactDOM from 'react-dom';
import { FileUpload } from 'primereact/fileupload';
import {Utils} from './service/utils';
import {itemService} from './service/item.service';
import 'primereact/resources/themes/bootstrap4-dark-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import { ProgressSpinner } from 'primereact/progressspinner';
import {Navbar} from './component/navbar/Navbar';



const App = () => {
    const [fileData, setFileData] = useState('');
    const [isLoading, setIsLoading] = useState(false);
  const handleChange = e => {
    setIsLoading(true);
    Utils.readFile(e.files[0]).then((data)=> {
      const request = {
        filters: {
          filterFlags: []
        },
        modData: data
      };
      const itemContext = {
        priceCheck: false,
        shortenResponse: true,
        fed76Enhance: true
      }

      itemService.prepareModData(request, itemContext).then((resp)=> {
        setIsLoading(false);
        console.log(resp);
        setFileData(resp);
      })
    })
  };

  return (
      <>
        <Navbar/>
        <FileUpload mode="basic"
                    customUpload={true}
                    onSelect={handleChange}
                    />
                    <div>
                      {isLoading ? <ProgressSpinner style={{width: '50px', height: '50px'}} strokeWidth="8" fill="#EEEEEE" animationDuration=".5s"/> : null}
                    </div>
      </>
  );
};

ReactDOM.render(<App/>, document.getElementById('app'));