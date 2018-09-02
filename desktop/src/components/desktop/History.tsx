import React from 'react';
import { connect } from 'react-redux';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Avatar from '@material-ui/core/Avatar';
import NotificationIcon from '@material-ui/icons/Notifications';
import Tooltip from '@material-ui/core/Tooltip';
import ScrollArea from 'react-scrollbar';
import IStoreState from '../../store/IStoreState';
import { Container, Title } from '../primitives';
import ZephyrNotification from '../../models/ZephyrNotification';

class History extends React.Component<any, any> {

  state = {
    notifications: new Array<ZephyrNotification>()
  };

  listItems() {
    return (
      <div>
        {this.props.notifications.reverse().map((notification) => {
          return (
            <ListItem key={notification.id}>
              <Tooltip title={notification.timestamp} placement='right'>
                <Avatar>
                  <NotificationIcon />
                </Avatar>
              </Tooltip>
              <ListItemText primary={notification.title} secondary={notification.message} />
            </ListItem>
          );
        })}
      </div>);
  }

  emptyList() {
    return (
      <ListItem key="notifications-none">
        <ListItemText primary="No notifications" secondary="Notifications from your current session will be shown here." />
      </ListItem>
    );
  }

  render() {
    return (
      <Container>
        <Title>History</Title>
        <ScrollArea
            speed={0.8}
            className="area"
            contentClassName="content"
            style={{width: '100%', height: '429px', padidngBottom: '70px'}}
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

export default connect(mapStatesToProps)(History);
