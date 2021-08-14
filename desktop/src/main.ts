import { app, BrowserWindow } from 'electron';
import electronDebug from 'electron-debug';
import installExtension, { REACT_DEVELOPER_TOOLS, REDUX_DEVTOOLS } from 'electron-devtools-installer';
import { forwardToRenderer, replayActionMain, triggerAlias } from 'electron-redux';
import Path from 'path';
import { applyMiddleware, createStore, Store } from 'redux';
import ActionTypeKeys from './actions/ActionTypeKeys';
import RootReducer from './reducers/RootReducer';
import { ZephyrServer } from './server/ZephyrServer';
import IStoreState from './store/IStoreState';
import ZephyrUpdater from './updater/ZephyrUpdater';
import ConfigUtils from './utils/ConfigUtils';
import LogUtils from './utils/LogUtils';
import VRWindow from './vr/VRWindow';

declare let __dirname: string; // eslint-disable-line @typescript-eslint/naming-convention
let mainWindow: Electron.BrowserWindow;
let vrWindow: VRWindow;
let mainWindowReady: boolean;
let launchError: string | undefined;
let zephyrServer: ZephyrServer;

const store: Store<IStoreState> = createStore(
  RootReducer,
  applyMiddleware(
    triggerAlias,
    forwardToRenderer
  )
);

replayActionMain(store);

const installExtensions = async (): Promise<void> => {
  if (ConfigUtils.isDev()) {
    electronDebug({
      showDevTools: false
    });

    return await installExtension([REACT_DEVELOPER_TOOLS, REDUX_DEVTOOLS]).then((_) => {
      LogUtils.debug('Zephyr Beta', 'Successfully installed debug extensions.');
    }).catch((err) => {
      LogUtils.error('Zephyr Beta', 'Error when installing debug extensions: ' + err);
    });
  }

  return await Promise.resolve();
};

function onReady(): void {
  zephyrServer = new ZephyrServer();

  mainWindow = new BrowserWindow({
    width: 400,
    height: 600,
    frame: false,
    resizable: false,
    show: false,
    backgroundColor: '#0D253A',
    webPreferences: {
      nodeIntegration: true,
      enableRemoteModule: true
    }
  });
  mainWindow.loadURL(`file://${__dirname}/index.html`).catch(error => {
    LogUtils.error('Zephyr Beta', 'Main window content failed to load: ' + JSON.stringify(error));
  });
  mainWindow.on('ready-to-show', () => {
    mainWindow.show();
    if (launchError !== undefined && launchError.length > 0) {
      dispatchError();
    }
    mainWindowReady = true;
  });
  mainWindow.on('close', () => quit());

  if (ConfigUtils.overlayEnabled()) {
    vrWindow = new VRWindow({
      width: 1920,
      height: 1080,
      transparent: true,
      vr: {
        key: 'texasgamer.zephyr',
        name: 'Zephyr Beta',
        fps: 45,
        iconPath: `${ConfigUtils.getExternalImagesDirectory()}/icon.png`
      },
      webPreferences: {
        nodeIntegration: true,
        enableRemoteModule: true
      }
    });
    vrWindow.loadURL(`file://${__dirname}/overlay.html`).catch(error => {
      LogUtils.error('Zephyr Beta', 'VR dashboard content failed to load: ' + JSON.stringify(error));
    });
  } else {
    LogUtils.warn('Zephyr Beta', 'Overlay disabled!');
    const overlayWindow = new BrowserWindow({
      width: 1375,
      height: 750,
      frame: false,
      resizable: false,
      show: false,
      transparent: true,
      webPreferences: {
        nodeIntegration: true,
        enableRemoteModule: true
      }
    });
    overlayWindow.loadURL(`file://${__dirname}/overlay.html`).catch(error => {
      LogUtils.error('Zephyr Beta', 'Dashboard window content failed to load: ' + JSON.stringify(error));
    });
    overlayWindow.on('ready-to-show', () => overlayWindow.show());
  }

  ZephyrUpdater.getInstance(store).checkForUpdates();
}

function onError(errorObject: any): void {
  let errorString = 'Unknown error';
  try {
    const errorParts = errorObject.toString().split(' ');
    let errorCode = -1;
    if (errorParts.length === 2) {
      errorCode = parseInt(errorParts[1], 10);
      if (errorCode === 108) {
        errorString = 'No headset detected. Restart Zephyr after connecting a headset.';
      }
    }
    launchError = errorString + ' (' + errorCode + ')';
    LogUtils.error('Zephyr Beta', 'Error when initializing: ' + errorObject);

    if (mainWindowReady) {
      dispatchError();
    }
  } catch (e) {
    LogUtils.error('Zephyr Beta', 'Unable to parse error: ' + errorObject);
    errorString = 'Unknown error - failed to parse error';
    if (mainWindowReady) {
      dispatchError();
    }
  }
}

function dispatchError(): void {
  store.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
    message: launchError,
    type: 'error',
    duration: null,
    dismissable: false
  }});
}

function init(): void {
  if (ConfigUtils.isDev()) {
    LogUtils.info('Zephyr Beta', 'Live reload enabled!');
    /* eslint-disable @typescript-eslint/no-var-requires */
    require('electron-reload')(__dirname, {
      electron: Path.join(__dirname, '..', 'node_modules', '.bin', 'electron'),
      ignored: /.*\.log/
    });
  }

  const gotTheLock = app.requestSingleInstanceLock();

  if (!gotTheLock) {
    quit();
  } else {
    app.on('second-instance', () => {
      // Someone tried to run a second instance, we should focus our window.
      if (mainWindow) {
        if (mainWindow.isMinimized()) {
          mainWindow.restore();
        }
        mainWindow.focus();
      }
    });

    LogUtils.info('Zephyr Beta', `v${ConfigUtils.getAppVersion()} (${ConfigUtils.getBuildType()})`);

    app.whenReady().then(installExtensions).then(onReady).catch(onError);
    app.on('window-all-closed', () => quit());
  }
}

function quit(): void {
  LogUtils.info('Zephyr Beta', 'Quitting...');
  zephyrServer.stopDiscoveryBroadcast().then(() => {
    app.quit();
  }).catch(() => {
    LogUtils.error('Zephyr Beta', 'Failed to stop discovery broadcast when quitting!');
    app.quit();
  });
}

init();
