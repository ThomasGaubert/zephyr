import IStoreState, { ConnectionStatus } from '../store/IStoreState';
import ZephyrNotification from '../models/ZephyrNotification';

const DefaultState: IStoreState = {
  navigationTarget: 0,
  connectionStatus: ConnectionStatus.DISCONNECTED,
  toast: null,
  notifications: new Array<ZephyrNotification>()
};

export default DefaultState;
