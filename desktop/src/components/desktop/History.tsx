import Avatar from '@material-ui/core/Avatar';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Tooltip from '@material-ui/core/Tooltip';
import NotificationIcon from '@material-ui/icons/Notifications';
import React from 'react';
import { connect } from 'react-redux';
import ScrollArea from 'react-scrollbar';
import ZephyrNotification from '../../models/ZephyrNotification';
import IStoreState from '../../store/IStoreState';
import { Container, Title } from '../primitives';

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
                <Avatar src={notification.icon ? 'data:image/png;base64, ' + notification.icon : ''}>
                  <NotificationIcon />
                </Avatar>
              </Tooltip>
              <ListItemText primary={notification.title} secondary={notification.body} />
            </ListItem>
          );
        })}
      </div>);
  }

  emptyList() {
    return (
      <ListItem key='notifications-none'>
        <ListItemText primary='No notifications' secondary='Notifications from your current session will be shown here.' />
      </ListItem>
    );
  }

  render() {
    return (
      <Container>
        <Title>History</Title>
        <ScrollArea
            speed={0.8}
            className='area'
            contentClassName='content'
            style={{ width: '100%', height: '429px', padidngBottom: '70px' }}
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
    notifications: Array.from(state.notifications.values()).sort((a, b) => a.timestamp < b.timestamp ? -1 : a.timestamp > b.timestamp ? 1 : 0)
  };
}

export default connect(mapStatesToProps)(History);
