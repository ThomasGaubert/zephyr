import { app, BrowserWindow } from 'electron';
import { VRWindow } from 'electron-openvr';
import { ZephyrServer } from './server/ZephyrServer';
import ConfigUtils from './utils/ConfigUtils';
import LogUtils from './utils/LogUtils';

declare var __dirname: string;
let mainWindow: Electron.BrowserWindow;
let vrWindow: VRWindow;

const installExtensions = () => {
  if (process.env.NODE_ENV === 'development') {
    process.env.ZEPHYR_BUILD_TYPE = 'dev';
    require('electron-debug')({
      showDevTools: false
    });

    const installer = require('electron-devtools-installer'); // eslint-disable-line global-require
    const extensions = [
      'REACT_DEVELOPER_TOOLS',
      'REDUX_DEVTOOLS'
    ];
    const forceDownload = !!process.env.UPGRADE_EXTENSIONS;
    return Promise.all(extensions.map(name => installer.default(installer[name], forceDownload)));
  } else {
    process.env.ZEPHYR_BUILD_TYPE = 'Standalone';
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
  mainWindow.on('ready-to-show', () => mainWindow.show());
  mainWindow.on('close', () => app.quit());

  vrWindow = new BrowserWindow({
    width: 1920,
    height: 1080,
    backgroundColor: '#0D253A'
  });

  // vrWindow.overlay.position = { x: 0, y: 0, z: -1 };
  vrWindow.loadURL(`file://${__dirname}/overlay.html`);
}

LogUtils.info('Zephyr', `v${ConfigUtils.getAppVersion()} (${ConfigUtils.getBuildType()})`);
app.on('ready', () => installExtensions().then(() => onReady()));
app.on('window-all-closed', () => app.quit());
