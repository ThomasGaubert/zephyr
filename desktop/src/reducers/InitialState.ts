import ZephyrNotification from '../models/ZephyrNotification';
import IStoreState, { ConnectionStatus } from '../store/IStoreState';

const DefaultState: IStoreState = {
  navigationTarget: 0,
  connectionStatus: ConnectionStatus.DISCONNECTED,
  toast: null,
  notifications: new Map<string, ZephyrNotification>()
};

export default DefaultState;
