import DismissNotificationPayload from '../../models/DismissNotificationPayload';
import ZephyrNotification from '../../models/ZephyrNotification';
import keys from '../ActionTypeKeys';

export interface INotificationPost {
  readonly type: keys.NOTIFICATION_POST;
  readonly payload: {
    readonly notification: ZephyrNotification;
  };
}

export interface INotificationDismiss {
  readonly type: keys.NOTIFICATION_DISMISS;
  readonly payload: {
    readonly dismissNotificationPayload: DismissNotificationPayload;
  };
}
