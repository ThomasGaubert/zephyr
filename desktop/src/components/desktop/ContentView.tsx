import React from 'react';
import { connect } from 'react-redux';
import IStoreState from '../../store/IStoreState';
import History from './History';
import Home from './Home';
import Settings from './Settings';
import Unknown from './Unknown';

class ContentView extends React.Component<any, any> {

  homeView = <Home/>;
  historyView = <History/>;
  settingsView = <Settings/>;
  unknownView = <Unknown/>;

  render(): any {
    switch (this.props.currentTab) {
    case 0: return (this.homeView);
    case 1: return (this.historyView);
    case 2: return (this.settingsView);
    default: return (this.unknownView);
    }
  }
}

function mapStatesToProps (state: IStoreState): any {
  return {
    currentTab: state.navigationTarget
  };
}

export default connect(mapStatesToProps)(ContentView);
