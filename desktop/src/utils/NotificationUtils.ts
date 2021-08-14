import DismissNotificationPayload from '../models/DismissNotificationPayload';
import ZephyrNotification from '../models/ZephyrNotification';

export default class NotificationUtils {
  static getNotificationKey(item: ZephyrNotification | DismissNotificationPayload): string {
    return `${item.packageName}.${item.id}`;
  }
}
