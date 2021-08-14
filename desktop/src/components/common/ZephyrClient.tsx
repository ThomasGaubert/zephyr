import React from 'react';
import { connect } from 'react-redux';
import { io } from 'socket.io-client';
import ActionTypeKeys from '../../actions/ActionTypeKeys';
import SocketChannels from '../../models/SocketChannels';
import LogUtils from '../../utils/LogUtils';

class ZephyrClient extends React.Component<any, any> {
  constructor(props: any) {
    super(props);
    this.state = {
      response: false,
      endpoint: 'http://127.0.0.1:3753'
    };
  }

  onConnect (client: ZephyrClient): void {
    LogUtils.verbose('ZephyrClient', 'Connected to ZephyrServer.');
    client.props.dispatch({ type: ActionTypeKeys.SERVER_CONNECTED });
  }

  componentDidMount(): void {
    const { endpoint } = this.state;
    const socket = io(endpoint);

    // Connection
    socket.on('connect', () => this.onConnect(this));

    // Upon receiving a notification
    socket.on(SocketChannels.EVENT_NOTIFICATION_POSTED, notification => this.props.dispatch({type: ActionTypeKeys.NOTIFICATION_POST, payload: {
      notification: notification
    }}));

    // Dismiss notification
    socket.on(SocketChannels.EVENT_NOTIFICATION_DISMISSED, dismissNotificationPayload => this.props.dispatch({type: ActionTypeKeys.NOTIFICATION_DISMISS, payload: {
      dismissNotificationPayload: dismissNotificationPayload
    }}));
  }

  render(): any {
    return null;
  }
}

export default connect()(ZephyrClient);
