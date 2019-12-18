import ActionTypeKeys from '../actions/ActionTypeKeys';
import ActionTypes from '../actions/ActionTypes';
import NotificationUtils from '../utils/NotificationUtils';
import initialState from './initialState';

export default function notificationReducer(
  state = initialState.notifications,
  action: ActionTypes
) {
  switch (action.type) {
  case ActionTypeKeys.NOTIFICATION_POST:
    state.set(NotificationUtils.getNotificationKey(action.payload.notification), action.payload.notification);
    return new Map(state);
  case ActionTypeKeys.NOTIFICATION_DISMISS:
    state.delete(NotificationUtils.getNotificationKey(action.payload.dismissNotificationPayload));
    return new Map(state);
  default:
    return state;
  }
}
