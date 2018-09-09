import { ipcMain } from 'electron';
import { autoUpdater } from 'electron-updater';
import { Store } from 'redux';
import ActionTypeKeys from '../actions/ActionTypeKeys';
import IStoreState from '../store/IStoreState';
import ConfigUtils from '../utils/ConfigUtils';
import LogUtils from '../utils/LogUtils';

export default class ZephyrUpdater {

  private static instance: ZephyrUpdater;
  private store: Store<IStoreState>;

  private constructor(store: Store<IStoreState>) {
    this.store = store;

    // Configure logger
    autoUpdater.logger = LogUtils.getLogger();

    // Configure auto updater events
    autoUpdater.on('checking-for-update', () => {
      LogUtils.info('ZephyrUpdater', 'Checking for update...');
    });
    autoUpdater.on('update-available', (info) => this.onUpdateDownloaded(info, this.store));
    autoUpdater.on('update-not-available', (info) => {
      LogUtils.info('ZephyrUpdater', 'Update not available: ' + info);
    });
    autoUpdater.on('error', (err) => this.onUpdateError(err, this.store));
    autoUpdater.on('download-progress', (progressObj) => {
      let logMessage = 'Download speed: ' + progressObj.bytesPerSecond;
      logMessage = logMessage + ' - Downloaded ' + progressObj.percent + '%';
      logMessage = logMessage + ' (' + progressObj.transferred + '/' + progressObj.total + ')';
      LogUtils.info('ZephyrUpdater', logMessage);
    });
    autoUpdater.on('update-downloaded', (info) => {
      LogUtils.info('ZephyrUpdater', 'Update downloaded: ' + info);
    });

    // Configure IPC events
    ipcMain.on('check-for-updates', () => function (autoUpdater) {
      autoUpdater.checkForUpdatesAndNotify();
    });
  }

  static getInstance(store: Store<IStoreState>): ZephyrUpdater {
    if (!ZephyrUpdater.instance) {
      ZephyrUpdater.instance = new ZephyrUpdater(store);
    }
    return ZephyrUpdater.instance;
  }

  checkForUpdates() {
    if (!ConfigUtils.updatesEnabled()) {
      LogUtils.info('ZephyrUpdater', 'Updates disabled.');
      return;
    }

    autoUpdater.checkForUpdatesAndNotify().then(result => {
      LogUtils.info('ZephyrUpdater', 'Update result: ' + result);
    }).catch(error => {
      LogUtils.error('ZephyrUpdater', 'Error when checking for updates: ' + error);
    });
  }

  private onUpdateDownloaded(info, store: Store<IStoreState>) {
    LogUtils.info('ZephyrUpdater', 'Update available: ' + info);
    store.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
      message: 'Update ' + info.version + ' downloaded.',
      type: 'info',
      duration: 5000
    }});
  }

  private onUpdateError(err, store: Store<IStoreState>) {
    LogUtils.info('ZephyrUpdater', 'Error in auto-updater: ' + err);
    store.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
      message: 'Failed to check for updates.',
      type: 'error',
      duration: 5000
    }});
  }
}
