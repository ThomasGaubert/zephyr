import ActionTypeKeys from '../actions/ActionTypeKeys';
import ActionTypes from '../actions/ActionTypes';
import initialState from './initialState';

export default function navigationReducer(
  state = initialState.navigationTarget,
  action: ActionTypes
): number {
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

function onNavigateHome(): number {
  return 0;
}

function onNavigateHistory(): number {
  return 1;
}

function onNavigateSettings(): number {
  return 2;
}
