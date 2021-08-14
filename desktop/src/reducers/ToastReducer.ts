import ActionTypeKeys from '../actions/ActionTypeKeys';
import ActionTypes from '../actions/ActionTypes';
import { IToastState } from '../store/IStoreState';
import initialState from './initialState';

export default function toastReducer(
  state = initialState.toast,
  action: ActionTypes
): IToastState | null {
  switch (action.type) {
  case ActionTypeKeys.TOAST_SHOW:
    return action.payload;
  case ActionTypeKeys.TOAST_HIDE:
    return null;
  default:
    return state;
  }
}
