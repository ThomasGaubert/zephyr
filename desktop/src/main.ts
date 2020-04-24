import { app, BrowserWindow } from 'electron';
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

declare var __dirname: string;
let mainWindow: Electron.BrowserWindow;
let vrWindow: VRWindow;
let mainWindowReady: boolean;
let launchError: string | undefined;

const store: Store<IStoreState> = createStore(
  RootReducer,
  applyMiddleware(
    triggerAlias,
    forwardToRenderer
  )
);

replayActionMain(store);

const installExtensions = () => {
  if (ConfigUtils.isDev()) {
    require('electron-debug')({
      showDevTools: false
    });

    return installExtension([REACT_DEVELOPER_TOOLS, REDUX_DEVTOOLS]).then((_) => {
      LogUtils.debug('Zephyr Beta', 'Successfully installed debug extensions.');
    }).catch((err) => {
      LogUtils.error('Zephyr Beta', 'Error when installing debug extensions: ' + err);
    });
  }

  return Promise.resolve([]);
};

function onReady() {
  new ZephyrServer(); // tslint:disable-line

  mainWindow = new BrowserWindow({
    width: 400,
    height: 600,
    frame: false,
    resizable: false,
    show: false,
    backgroundColor: '#0D253A'
  });
  mainWindow.loadURL(`file://${__dirname}/index.html`);
  mainWindow.on('ready-to-show', () => {
    mainWindow.show();
    if (launchError !== undefined && launchError.length > 0) {
      dispatchError();
    }
    mainWindowReady = true;
  });
  mainWindow.on('close', () => app.quit());

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
      }
    });
    vrWindow.loadURL(`file://${__dirname}/overlay.html`);
  } else {
    LogUtils.warn('Zephyr Beta', 'Overlay disabled!');
    let overlayWindow = new BrowserWindow({
      width: 1375,
      height: 750,
      frame: false,
      resizable: false,
      show: false,
      transparent: true
    });
    overlayWindow.loadURL(`file://${__dirname}/overlay.html`);
    overlayWindow.on('ready-to-show', () => overlayWindow.show());
  }

  ZephyrUpdater.getInstance(store).checkForUpdates();
}

function onError(error: any) {
  let errorString = 'Unknown error';
  if (error.code === 108) {
    errorString = 'No HMD connected. Restart Zephyr after connecting a HMD.';
  }
  launchError = errorString + ' (' + error.code + ')';
  LogUtils.info('Zephyr Beta', errorString);

  if (mainWindowReady) {
    dispatchError();
  }
}

function dispatchError() {
  store.dispatch({type: ActionTypeKeys.TOAST_SHOW, payload: {
    message: launchError,
    type: 'error',
    duration: null,
    dismissable: false
  }});
}

function init() {
  if (ConfigUtils.isDev()) {
    LogUtils.info('Zephyr Beta', 'Live reload enabled!');
    require('electron-reload')(__dirname, {
      electron: Path.join(__dirname, '..', 'node_modules', '.bin', 'electron'),
      ignored: /.*\.log/
    });
  }

  const gotTheLock = app.requestSingleInstanceLock();

  if (!gotTheLock) {
    app.quit();
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

    app.on('ready', () => installExtensions().then(() => onReady()).catch(onError));
    app.on('window-all-closed', () => app.quit());
  }
}

init();
