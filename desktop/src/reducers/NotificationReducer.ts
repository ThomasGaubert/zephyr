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
  default:
    return state;
  }
}
