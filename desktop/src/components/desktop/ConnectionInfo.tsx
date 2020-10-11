import Card from '@material-ui/core/Card';
import Collapse from '@material-ui/core/Collapse';
import Divider from '@material-ui/core/Divider';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { withStyles } from '@material-ui/core/styles';
import ConnectedIcon from '@material-ui/icons/CheckCircle';
import ErrorIcon from '@material-ui/icons/Error';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import LinkIcon from '@material-ui/icons/Phonelink';
import React from 'react';
import { connect } from 'react-redux';
import ScrollArea from 'react-scrollbar';
import IStoreState, { ConnectionStatus } from '../../store/IStoreState';
import HelpUtils from '../../utils/HelpUtils';
import NetworkUtils from '../../utils/NetworkUtils';
import QrDialog from './QrDialog';

const styles = _ => ({
  root: {
    width: '100%',
    maxWidth: 360
  }
});

class ConnectionInfo extends React.Component<any, any> {

  state = {
    ipDropdownOpen: false
  };

  getStatusIcon (connectionStatus: ConnectionStatus) {
    switch (connectionStatus) {
    case ConnectionStatus.CONNECTED:
      return <ConnectedIcon/>;
    case ConnectionStatus.CONNECTING:
      return <ErrorIcon/>;
    case ConnectionStatus.DISCONNECTED:
      return <ErrorIcon/>;
    case ConnectionStatus.ERROR:
      return <ErrorIcon/>;
    }
  }

  getStatusText (connectionStatus: ConnectionStatus) {
    switch (connectionStatus) {
    case ConnectionStatus.CONNECTED:
      return 'Connected';
    case ConnectionStatus.CONNECTING:
      return 'Connecting';
    case ConnectionStatus.DISCONNECTED:
      return 'Disconnected';
    case ConnectionStatus.ERROR:
      return 'Error';
    }
  }

  getAllIps () {
    return (
      <div>
        {NetworkUtils.getAllIpAddresses().map((ip) => {
          return (
            <ListItem key={ip}>
              <ListItemText primary={ip} />
            </ListItem>
          );
        })}
      </div>);
  }

  onClickMoreIps = () => {
    this.setState(state => ({ ipDropdownOpen: !state.ipDropdownOpen }));
  }

  onClickIpHelp = () => {
    HelpUtils.openConnectionHelp();
  }

  render () {
    return (
      <div className={this.props.root}>
        <ScrollArea
            speed={0.8}
            className='area'
            contentClassName='content'
            style={{ width: '100%', height: '429px', padidngBottom: '70px' }}
            horizontal={false}>
          <Card style={{ padding: '1.0rem', paddingBottom: '0', backgroundColor: '#2962ff' }}>
            <div>Thanks for beta testing!</div>
            <p>What's new:</p>
            <ul>
              <li>Updated dependencies</li>
            </ul>
          </Card>
          <List component='nav'>
            <ListItem>
              <ListItemIcon>
                { this.getStatusIcon(this.props.connectionStatus) }
              </ListItemIcon>
              <ListItemText primary={this.getStatusText(this.props.connectionStatus)} />
            </ListItem>
            <Divider/>
            <QrDialog/>
            <ListItem button onClick={this.onClickMoreIps}>
              <ListItemIcon>
                <LinkIcon />
              </ListItemIcon>
              <ListItemText primary={NetworkUtils.getPrimaryIpAddressShortFormatted() !== undefined ? NetworkUtils.getPrimaryIpAddressShortFormatted() : 'Unable to determine local IP'} />
              {this.state.ipDropdownOpen ? <ExpandLess /> : <ExpandMore />}
            </ListItem>
            <Collapse in={this.state.ipDropdownOpen} timeout='auto' unmountOnExit>
              {this.getAllIps()}
              <List disablePadding>
                <ListItem button onClick={this.onClickIpHelp}>
                  <ListItemText primary='Need help?' />
                </ListItem>
              </List>
            </Collapse>
          </List>
        </ScrollArea>
      </div>
    );
  }
}

function mapStatesToProps (state: IStoreState) {
  return {
    connectionStatus: state.connectionStatus
  };
}

export default connect(mapStatesToProps)(withStyles(styles)(ConnectionInfo));
