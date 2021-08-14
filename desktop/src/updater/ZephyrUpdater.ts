import { ipcMain } from 'electron';
import { autoUpdater } from 'electron-updater';
import { Store } from 'redux';
import ActionTypeKeys from '../actions/ActionTypeKeys';
import IStoreState from '../store/IStoreState';
import ConfigUtils from '../utils/ConfigUtils';
import LogUtils from '../utils/LogUtils';

export default class ZephyrUpdater {

  private static instance: ZephyrUpdater;
  private readonly store: Store<IStoreState>;

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
      LogUtils.info('ZephyrUpdater', `Update not available: ${String(info)}`);
    });
    autoUpdater.on('error', (err) => this.onUpdateError(err, this.store));
    autoUpdater.on('download-progress', (progressObj) => {
      LogUtils.info('ZephyrUpdater', `Download speed: ${String(progressObj.bytesPerSecond)} - Downloaded ${String(progressObj.percent)}% (${String(progressObj.transferred)}/${String(progressObj.total)})`);
    });
    autoUpdater.on('update-downloaded', (info) => {
      LogUtils.info('ZephyrUpdater', `Update downloaded: ${String(info)}`);
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

  checkForUpdates(): void {
    if (!ConfigUtils.updatesEnabled()) {
      LogUtils.info('ZephyrUpdater', 'Updates disabled.');
      return;
    }

    autoUpdater.checkForUpdatesAndNotify().then(result => {
      LogUtils.info('ZephyrUpdater', `Update result: ${String(result)}`);
    }).catch(error => {
      LogUtils.error('ZephyrUpdater', `Error when checking for updates: ${String(error)}`);
    });
  }

  private onUpdateDownloaded(info, store: Store<IStoreState>): void {
    LogUtils.info('ZephyrUpdater', `Update available: ${String(info)}`);
    store.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
      message: 'Update ' + info.version + ' downloaded.',
      type: 'info',
      duration: 5000,
      dismissable: false
    }});
  }

  private onUpdateError(err, store: Store<IStoreState>): void {
    LogUtils.info('ZephyrUpdater', `Error in auto-updater: ${String(err)}`);
    store.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
      message: 'Failed to check for updates.',
      type: 'error',
      duration: 5000,
      dismissable: false
    }});
  }
}
