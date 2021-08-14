import ActionTypeKeys from '../actions/ActionTypeKeys';
import ActionTypes from '../actions/ActionTypes';
import { ConnectionStatus } from '../store/IStoreState';
import initialState from './initialState';

export default function navigationReducer(
  state = initialState.connectionStatus,
  action: ActionTypes
): ConnectionStatus {
  switch (action.type) {
  case ActionTypeKeys.SERVER_CONNECT:
    return ConnectionStatus.CONNECTING;
  case ActionTypeKeys.SERVER_CONNECTED:
    return ConnectionStatus.CONNECTED;
  case ActionTypeKeys.SERVER_DISCONNECT:
    return ConnectionStatus.DISCONNECTED;
  case ActionTypeKeys.SERVER_ERROR:
    return ConnectionStatus.ERROR;
  default:
    return state;
  }
}
