import Avatar from '@material-ui/core/Avatar';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { withStyles } from '@material-ui/core/styles';
import NotificationIcon from '@material-ui/icons/Notifications';
import React from 'react';
import { connect } from 'react-redux';
import ScrollArea from 'react-scrollbar';
import ZephyrNotification from '../../models/ZephyrNotification';
import IStoreState from '../../store/IStoreState';
import { Container, VRTitle } from '../primitives';

const styles = (_): any => ({
  avatar: {
    width: 100,
    height: 200
  },
  listItemTextPrimary: {
    fontSize: '3em'
  },
  listItemTextSecondary: {
    fontSize: '2.5em'
  }
});

class BigHistory extends React.Component<any, any> {

  state = {
    notifications: new Array<ZephyrNotification>()
  };

  listItems(): any {
    return (
      <div>
        {this.props.notifications.reverse().map((notification) => {
          return (
            <ListItem key={notification.id}>
              <Avatar style={{ width: 75, height: 75, marginRight: '20px' }} src={notification.icon ? 'data:image/png;base64, ' + notification.icon : ''}>
                <NotificationIcon style={{ fontSize: '40px' }} />
              </Avatar>
              <ListItemText
                classes={{ primary: this.props.classes.listItemTextPrimary, secondary: this.props.classes.listItemTextSecondary }}
                primary={notification.title}
                secondary={notification.body} />
            </ListItem>
          );
        })}
      </div>);
  }

  emptyList(): any {
    return (
      <ListItem key='notifications-none'>
        <ListItemText
          classes={{ primary: this.props.classes.listItemTextPrimary, secondary: this.props.classes.listItemTextSecondary }}
          primary='No notifications'
          secondary='Notifications from your current session will be shown here.' />
      </ListItem>
    );
  }

  render(): any {
    return (
      <Container>
        <VRTitle>Zephyr β</VRTitle>
        <ScrollArea
            speed={0.8}
            className='area'
            contentClassName='content'
            style={{ width: '100%', height: '775px' }}
            horizontal={false}>
            <List>
              {this.props.notifications.length > 0 ? this.listItems() : this.emptyList()}
            </List>
          </ScrollArea>
      </Container>
    );
  }
}

function mapStatesToProps(state: IStoreState): any {
  return {
    notifications: Array.from(state.notifications.values()).sort((a, b) => a.timestamp < b.timestamp ? -1 : a.timestamp > b.timestamp ? 1 : 0)
  };
}

export default connect(mapStatesToProps)(withStyles(styles)(BigHistory));
