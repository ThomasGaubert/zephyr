import { combineReducers } from 'redux';
import IStoreState from '../store/IStoreState';
import NavigationReducer from './NavigationReducer';
import NotificationReducer from './NotificationReducer';
import ServerReducer from './ServerReducer';
import ToastReducer from './ToastReducer';

const RootReducer = combineReducers<IStoreState>({
  navigationTarget: NavigationReducer,
  connectionStatus: ServerReducer,
  toast: ToastReducer,
  notifications: NotificationReducer
});

export default RootReducer;
