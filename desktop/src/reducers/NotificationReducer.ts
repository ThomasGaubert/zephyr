import ActionTypeKeys from '../actions/ActionTypeKeys';
import ActionTypes from '../actions/ActionTypes';
import initialState from './initialState';

export default function notificationReducer(
  state = initialState.notifications,
  action: ActionTypes
) {
  switch (action.type) {
  case ActionTypeKeys.NOTIFICATION_POST:
    state.push(action.payload.notification);
    return state.slice(0);
  case ActionTypeKeys.NOTIFICATION_DISMISS:
    return state.filter((notification) => {
      return notification.id !== action.payload.dismissNotificationPayload.id;
    });
  default:
    return state;
  }
}
