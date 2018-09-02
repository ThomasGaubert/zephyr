import React, { Component } from 'react';
import { connect } from 'react-redux';
import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Collapse from '@material-ui/core/Collapse';
import InfoIcon from '@material-ui/icons/Info';
import HelpIcon from '@material-ui/icons/Help';
import UpdateIcon from '@material-ui/icons/Update';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import DeveloperMode from '@material-ui/icons/DeveloperMode';
import DeviceInfo from '@material-ui/icons/PermDeviceInformation';
import ActionTypeKeys from '../../actions/ActionTypeKeys';
import HelpUtils from '../../utils/HelpUtils';
import { Container, Title } from '../primitives';
import ConfigUtils from '../../utils/ConfigUtils';

class Settings extends React.Component<any, any> {
  state = {
    aboutDialogOpen: false,
    devOptionsOpen: false
  };

  onClickInfo = () => {
    this.setState({ aboutDialogOpen: true });
  };

  onClickHelp = () => {
    HelpUtils.openHelp();
  };

  onClickCheckForUpdates = () => {
    if (!ConfigUtils.isStandalone()) {
      this.props.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
        message: 'Build not eligible for updates.',
        type: 'error',
        duration: 2000
      }});
      return;
    }

    this.props.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
      message: 'Checking for updates...',
      type: 'info',
      duration: 2000
    }});
  };

  onClickDeveloper = () => {
    this.setState(state => ({ devOptionsOpen: !state.devOptionsOpen }));
  };

  onClickDevTools = () => {
    require('electron').remote.getCurrentWindow().webContents.toggleDevTools();
  };

  handleClose = () => {
    this.setState({ aboutDialogOpen: false });
  };

  checkForUpdatesButton() {
    if (ConfigUtils.isStandalone()) {
      return (
        <ListItem button onClick={this.onClickCheckForUpdates}>
          <ListItemIcon>
            <UpdateIcon />
          </ListItemIcon>
          <ListItemText inset primary="Check for Updates" />
        </ListItem>
      );
    } else {
      return null;
    }
  }

  devOptions() {
    if (ConfigUtils.isDev()) {
      return (
        <div>
          <Divider/>
          <ListItem button onClick={this.onClickDeveloper}>
            <ListItemIcon>
              <DeveloperMode />
            </ListItemIcon>
            <ListItemText inset primary="Developer" />
            {this.state.devOptionsOpen ? <ExpandLess /> : <ExpandMore />}
          </ListItem>
          <Collapse in={this.state.devOptionsOpen} timeout="auto" unmountOnExit>
            <List component="div" disablePadding>
              <ListItem button className={this.props.nested} onClick={this.onClickDevTools}>
                <ListItemIcon>
                  <DeviceInfo />
                </ListItemIcon>
                <ListItemText inset primary="Toggle Developer Tools" />
              </ListItem>
            </List>
          </Collapse>
        </div>
      );
    } else {
      return <Component/>
    }
  }

  render() {
    return (
      <Container>
        <Title>Settings</Title>
        <div className={this.props.root}>
          <List
            component="nav">
            <ListItem button onClick={this.onClickInfo}>
              <ListItemIcon>
                <InfoIcon />
              </ListItemIcon>
              <ListItemText inset primary="About" />
            </ListItem>
            <ListItem button onClick={this.onClickHelp}>
              <ListItemIcon>
                <HelpIcon />
              </ListItemIcon>
              <ListItemText inset primary="Help" />
            </ListItem>
            {this.checkForUpdatesButton()}
            {this.devOptions()}
          </List>
        </div>
        <Dialog
          open={this.state.aboutDialogOpen}
          onClose={this.props.root}
          aria-labelledby="alert-dialog-title"
          aria-describedby="alert-dialog-description">
          <DialogTitle id="alert-dialog-title">{"Zephyr"}</DialogTitle>
          <DialogContent>
            <DialogContentText id="alert-dialog-description">
              Version {ConfigUtils.getAppVersion()} ({ConfigUtils.getBuildType()})<br/>&copy; 2018 Thomas Gaubert
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color="primary" autoFocus>
              Close
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    );
  }
}

export default connect()(Settings);
