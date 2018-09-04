import keys from '../ActionTypeKeys';

export interface IServerConnect {
  readonly type: keys.SERVER_CONNECT;
}

export interface IServerConnected {
  readonly type: keys.SERVER_CONNECTED;
}

export interface IServerDisconnect {
  readonly type: keys.SERVER_DISCONNECT;
}

export interface IServerError {
  readonly type: keys.SERVER_ERROR;
}
