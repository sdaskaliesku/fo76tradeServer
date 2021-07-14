import React from "react";
import "./BaseInventOmatic.scss";

export enum MatchMode {
  CONTAINS, EXACT, ALL, STARTS
}

export class BaseInventOmatic<State> extends React.Component<any, State> {


  constructor(props: Readonly<any> | any);
  constructor(props: any, context: any);
  constructor(props: any, context?: any) {
    super(props, context);
  }

  render() {
    return (
        <div>
          <h1>THIS IS WORK IN PROGRESS!</h1>
        </div>
    );
  }

}

