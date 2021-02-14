import Button from '@material-ui/core/Button';
import Collapse from '@material-ui/core/Collapse';
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
import DeveloperMode from '@material-ui/icons/DeveloperMode';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import HelpIcon from '@material-ui/icons/Help';
import DeviceInfo from '@material-ui/icons/PermDeviceInformation';
import ShopIcon from '@material-ui/icons/Shop';
import UpdateIcon from '@material-ui/icons/Update';
import { ipcRenderer } from 'electron';
import React from 'react';
import { connect } from 'react-redux';
import ActionTypeKeys from '../../actions/ActionTypeKeys';
import ConfigUtils from '../../utils/ConfigUtils';
import HelpUtils from '../../utils/HelpUtils';
import { Container, Title } from '../primitives';
import AboutDialog from './AboutDialog';

class Settings extends React.Component<any, any> {
  state = {
    aboutDialogOpen: false,
    devOptionsOpen: false
  };

  onClickInfo = () => {
    this.setState({ aboutDialogOpen: true });
  }

  onClickHelp = () => {
    HelpUtils.openHelp();
  }

  onClickDownloadAndroidApp = () => {
    HelpUtils.openPlayStore();
  }

  onClickCheckForUpdates = () => {
    if (!ConfigUtils.updatesEnabled()) {
      this.props.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
        message: 'Build not eligible for updates.',
        type: 'error',
        duration: 2000,
        dismissable: false
      }});
      return;
    }

    this.props.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
      message: 'Checking for updates...',
      type: 'info',
      duration: 2000,
      dismissable: false
    }});

    ipcRenderer.send('check-for-updates');
  }

  onClickDeveloper = () => {
    this.setState(state => ({ devOptionsOpen: !state.devOptionsOpen }));
  }

  onClickDevTools = () => {
    require('electron').remote.getCurrentWindow().webContents.toggleDevTools();
  }

  handleClose = () => {
    this.setState({ aboutDialogOpen: false });
  }

  checkForUpdatesButton() {
    if (ConfigUtils.updatesEnabled()) {
      return (
        <ListItem button onClick={this.onClickCheckForUpdates}>
          <ListItemIcon>
            <UpdateIcon />
          </ListItemIcon>
          <ListItemText primary='Check for Updates' />
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
            <ListItemText primary='Developer' />
            {this.state.devOptionsOpen ? <ExpandLess /> : <ExpandMore />}
          </ListItem>
          <Collapse in={this.state.devOptionsOpen} timeout='auto' unmountOnExit>
            <List disablePadding>
              <ListItem button className={this.props.nested} onClick={this.onClickDevTools}>
                <ListItemIcon>
                  <DeviceInfo />
                </ListItemIcon>
                <ListItemText primary='Toggle Developer Tools' />
              </ListItem>
            </List>
          </Collapse>
        </div>
      );
    } else {
      return null;
    }
  }

  render() {
    return (
      <Container>
        <Title>Settings</Title>
        <div className={this.props.root}>
          <List
            component='nav'>
            <AboutDialog/>
            <ListItem button onClick={this.onClickHelp}>
              <ListItemIcon>
                <HelpIcon />
              </ListItemIcon>
              <ListItemText primary='Help' />
            </ListItem>
            <ListItem button onClick={this.onClickDownloadAndroidApp}>
              <ListItemIcon>
                <ShopIcon />
              </ListItemIcon>
              <ListItemText primary='Get Android app' />
            </ListItem>
            {this.checkForUpdatesButton()}
            {this.devOptions()}
          </List>
        </div>
        <Dialog
          open={this.state.aboutDialogOpen}
          onClose={this.props.root}
          aria-labelledby='alert-dialog-title'
          aria-describedby='alert-dialog-description'>
          <DialogTitle id='alert-dialog-title'>{'Zephyr β'}</DialogTitle>
          <DialogContent>
            <DialogContentText id='alert-dialog-description'>
              Version {ConfigUtils.getAppVersion()} ({ConfigUtils.getBuildType()})<br/>&copy; Thomas Gaubert
            </DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color='primary' autoFocus>
              Close
            </Button>
          </DialogActions>
        </Dialog>
      </Container>
    );
  }
}

export default connect()(Settings);
