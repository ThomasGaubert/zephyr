import keys from "../ActionTypeKeys";
import ZephyrNotification from "../../models/ZephyrNotification";

export interface INotificationPost {
  readonly type: keys.NOTIFICATION_POST;
  readonly payload: {
    readonly notification: ZephyrNotification;
  }
}
