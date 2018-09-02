import React from 'react';
import { connect } from 'react-redux';
import socketIOClient from "socket.io-client";
import ActionTypeKeys from '../../actions/ActionTypeKeys';
import LogUtils from '../../utils/LogUtils';

class ZephyrClient extends React.Component<any, any> {
  constructor(props: any) {
    super(props);
    this.state = {
      response: false,
      endpoint: "http://127.0.0.1:3753"
    };
  }

  onConnect (client: ZephyrClient) {
    LogUtils.verbose('ZephyrClient', 'Connected to ZephyrServer.');
    client.props.dispatch({type: ActionTypeKeys.SERVER_CONNECTED});
  }

  componentDidMount() {
    const { endpoint } = this.state;
    const socket = socketIOClient(endpoint);

    // Connection
    socket.on('connect', () => this.onConnect(this));

    // Upon receiving a notification
    socket.on('event-notification', notification => this.props.dispatch({type: ActionTypeKeys.NOTIFICATION_POST, payload: {
      notification: notification
    }}));
  }

  render() {
    return null;
  }
}

export default connect()(ZephyrClient);
