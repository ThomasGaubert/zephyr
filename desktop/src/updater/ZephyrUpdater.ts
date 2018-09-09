import { autoUpdater } from 'electron-updater';
import ConfigUtils from '../utils/ConfigUtils';
import LogUtils from '../utils/LogUtils';

export default class ZephyrUpdater {

  private static instance: ZephyrUpdater;

  private constructor() {
    // Configure logger
    autoUpdater.logger = LogUtils.getLogger();

    // Configure events
    autoUpdater.on('checking-for-update', () => {
      LogUtils.info('ZephyrUpdater', 'Checking for update...');
    });
    autoUpdater.on('update-available', (info) => {
      LogUtils.info('ZephyrUpdater', 'Update available: ' + info);
    });
    autoUpdater.on('update-not-available', (info) => {
      LogUtils.info('ZephyrUpdater', 'Update not available: ' + info);
    });
    autoUpdater.on('error', (err) => {
      LogUtils.info('ZephyrUpdater', 'Error in auto-updater. ' + err);
    });
    autoUpdater.on('download-progress', (progressObj) => {
      let logMessage = 'Download speed: ' + progressObj.bytesPerSecond;
      logMessage = logMessage + ' - Downloaded ' + progressObj.percent + '%';
      logMessage = logMessage + ' (' + progressObj.transferred + '/' + progressObj.total + ')';
      LogUtils.info('ZephyrUpdater', logMessage);
    });
    autoUpdater.on('update-downloaded', (info) => {
      LogUtils.info('ZephyrUpdater', 'Update downloaded: ' + info);
    });
  }

  static getInstance(): ZephyrUpdater {
    if (!ZephyrUpdater.instance) {
      ZephyrUpdater.instance = new ZephyrUpdater();
    }
    return ZephyrUpdater.instance;
  }

  checkForUpdates() {
    if (ConfigUtils.updatesEnabled()) {
      LogUtils.info('ZephyrUpdater', 'Updates disabled.');
    }

    autoUpdater.checkForUpdatesAndNotify().then(result => {
      LogUtils.info('ZephyrUpdater', 'Update result: ' + result);
    }).catch(error => {
      LogUtils.error('ZephyrUpdater', 'Error when checking for updates: ' + error);
    });
  }
}
