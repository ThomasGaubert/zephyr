import React from 'react';
import { connect } from 'react-redux';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Avatar from '@material-ui/core/Avatar';
import NotificationIcon from '@material-ui/icons/Notifications';
import ScrollArea from 'react-scrollbar';
import IStoreState from '../../store/IStoreState';
import { Container, VRTitle } from '../primitives';
import ZephyrNotification from '../../models/ZephyrNotification';

const styles = _ => ({
  avatar: {
    width: 100,
    height: 200,
  },
  listItemTextPrimary: {
    fontSize: "3em"
  },
  listItemTextSecondary: {
    fontSize: "2.5em"
  }
});

class BigHistory extends React.Component<any, any> {

  state = {
    notifications: new Array<ZephyrNotification>()
  };

  listItems() {
    return (
      <div>
        {this.props.notifications.reverse().map((notification) => {
          return (
            <ListItem key={notification.id}>
              <Avatar style={{width: 75, height: 75, marginRight: '20px'}}>
                <NotificationIcon style={{ fontSize: '40px' }} />
              </Avatar>
              <ListItemText
                classes={{ primary: this.props.classes.listItemTextPrimary, secondary: this.props.classes.listItemTextSecondary }}
                primary={notification.title}
                secondary={notification.message} />
            </ListItem>
          );
        })}
      </div>);
  }

  emptyList() {
    return (
      <ListItem key='notifications-none'>
        <ListItemText
          classes={{ primary: this.props.classes.listItemTextPrimary, secondary: this.props.classes.listItemTextSecondary }}
          primary='No notifications'
          secondary='Notifications from your current session will be shown here.' />
      </ListItem>
    );
  }

  render() {
    return (
      <Container>
        <VRTitle>Zephyr</VRTitle>
        <ScrollArea
            speed={0.8}
            className="area"
            contentClassName="content"
            style={{width: '100%', height: '775px'}}
            horizontal={false}>
            <List>
              {this.props.notifications.length > 0 ? this.listItems() : this.emptyList()}
            </List>
          </ScrollArea>
      </Container>
    );
  }
}

function mapStatesToProps (state: IStoreState) {
  return {
    notifications: state.notifications
  };
}

export default connect(mapStatesToProps)(withStyles(styles)(BigHistory));
