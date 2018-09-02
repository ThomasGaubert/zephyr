import ActionTypeKeys from '../actions/ActionTypeKeys';
import ActionTypes from '../actions/ActionTypes';
import initialState from './initialState';

export default function navigationReducer(
  state = initialState.navigationTarget,
  action: ActionTypes
) {
  switch (action.type) {
  case ActionTypeKeys.NAVIGATE_HOME:
    return onNavigateHome();
  case ActionTypeKeys.NAVIGATE_HISTORY:
    return onNavigateHistory();
  case ActionTypeKeys.NAVIGATE_SETTINGS:
    return onNavigateSettings();
  default:
    return state;
  }
}

function onNavigateHome () {
  return 0;
}

function onNavigateHistory () {
  return 1;
}

function onNavigateSettings () {
  return 2;
}
