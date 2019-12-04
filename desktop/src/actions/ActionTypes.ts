import {
  INavigateHistory,
  INavigateHome,
  INavigateSettings
} from './navigation';
import {
  INotificationDismiss,
  INotificationPost
} from './notification';
import {
  IServerConnect,
  IServerConnected,
  IServerDisconnect,
  IServerError
} from './server';
import { IToastHide, IToastShow } from './toast';

type ActionTypes =
  | INavigateHome
  | INavigateHistory
  | INavigateSettings
  | IToastShow
  | IToastHide
  | IServerConnect
  | IServerConnected
  | IServerDisconnect
  | IServerError
  | INotificationDismiss
  | INotificationPost;

export default ActionTypes;
