import React from 'react';
import { connect } from 'react-redux';
import IStoreState from '../../store/IStoreState';
import Home from './Home';
import History from './History';
import Settings from './Settings';
import Unknown from './Unknown';

class ContentView extends React.Component<any, any> {

  homeView = <Home/>;
  historyView = <History/>;
  settingsView = <Settings/>;
  unknownView = <Unknown/>;

  render() {
    switch (this.props.currentTab) {
      case 0: return (this.homeView);
      case 1: return (this.historyView);
      case 2: return (this.settingsView);
      default: return (this.unknownView);
    }
  }
}

function mapStatesToProps (state: IStoreState) {
  return {
    currentTab: state.navigationTarget
  };
}

export default connect(mapStatesToProps)(ContentView);
