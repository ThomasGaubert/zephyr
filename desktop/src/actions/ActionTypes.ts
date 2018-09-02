import {
  INavigateHome,
  INavigateHistory,
  INavigateSettings
} from "./navigation";
import { IToastShow, IToastHide } from "./toast";
import {
  IServerConnect,
  IServerConnected,
  IServerDisconnect,
  IServerError
} from "./server";
import { INotificationPost } from "./notification";

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
