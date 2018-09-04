import {
  INavigateHistory,
  INavigateHome,
  INavigateSettings
} from './navigation';
import { INotificationPost } from './notification';
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
  | INotificationPost;

export default ActionTypes;
