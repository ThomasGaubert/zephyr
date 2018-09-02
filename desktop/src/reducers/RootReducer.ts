import { combineReducers } from 'redux';
import IStoreState from '../store/IStoreState';
import NavigationReducer from './NavigationReducer';
import ServerReducer from './ServerReducer';
import ToastReducer from './ToastReducer';
import NotificationReducer from './NotificationReducer';

const RootReducer = combineReducers<IStoreState>({
  navigationTarget: NavigationReducer,
  connectionStatus: ServerReducer,
  toast: ToastReducer,
  notifications: NotificationReducer
});

export default RootReducer;
