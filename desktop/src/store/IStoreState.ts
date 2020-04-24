import ZephyrNotification from '../models/ZephyrNotification';

export interface IToastState {
  readonly message: string;
  readonly type: string;
  readonly duration: number;
  readonly dismissable: boolean;
}

export enum ConnectionStatus {
  CONNECTING = 'CONNECTING',
  CONNECTED = 'CONNECTED',
  DISCONNECTED = 'DISCONNECTED',
  ERROR = 'ERROR'
}

export default interface IStoreState {
  readonly navigationTarget: number;
  readonly connectionStatus: ConnectionStatus;
  readonly toast: IToastState | null;
  readonly notifications: Map<string, ZephyrNotification>;
}
