import React from 'react';
import { connect } from 'react-redux';
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import HomeIcon from '@material-ui/icons/Home';
import HistoryIcon from '@material-ui/icons/History';
import SettingsIcon from '@material-ui/icons/Settings';
import IStoreState from '../../store/IStoreState';
import ActionTypeKeys from '../../actions/ActionTypeKeys';

class NavigationBar extends React.Component<any, any> {
  handleChange = (_, value) => {
    let type: ActionTypeKeys = ActionTypeKeys.NAVIGATE_HOME;
    switch (value) {
      case 0:
        type = ActionTypeKeys.NAVIGATE_HOME;
        break;
      case 1:
        type = ActionTypeKeys.NAVIGATE_HISTORY;
        break;
      case 2:
        type = ActionTypeKeys.NAVIGATE_SETTINGS;
        break;
    }

    this.props.dispatch({type: type});
  };

  render() {
    return (
      <BottomNavigation
        value={this.props.currentTab}
        onChange={this.handleChange}
        showLabels
        className={this.props.root}
        style={{position: 'fixed', bottom: 0, width: '100%'}}>
        <BottomNavigationAction label="Home" icon={<HomeIcon />} />
        <BottomNavigationAction label="History" icon={<HistoryIcon />} />
        <BottomNavigationAction label="Settings" icon={<SettingsIcon />} />
      </BottomNavigation>
    );
  }
}

function mapStatesToProps (state: IStoreState) {
  return {
    currentTab: state.navigationTarget
  };
}

export default connect(mapStatesToProps)(NavigationBar);
