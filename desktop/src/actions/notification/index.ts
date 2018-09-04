import ZephyrNotification from '../../models/ZephyrNotification';
import keys from '../ActionTypeKeys';

export interface INotificationPost {
  readonly type: keys.NOTIFICATION_POST;
  readonly payload: {
    readonly notification: ZephyrNotification;
  };
}
