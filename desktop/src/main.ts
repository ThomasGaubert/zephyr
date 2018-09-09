import { app, BrowserWindow } from 'electron';
import { ZephyrServer } from './server/ZephyrServer';
import ZephyrUpdater from './updater/ZephyrUpdater';
import ConfigUtils from './utils/ConfigUtils';
import LogUtils from './utils/LogUtils';
import VRWindow from './vr/VRWindow';

declare var __dirname: string;
let mainWindow: Electron.BrowserWindow;
let vrWindow: VRWindow;

const installExtensions = () => {
  if (ConfigUtils.isDev()) {
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

  vrWindow = new VRWindow({
    width: 7920,
    height: 4320,
    backgroundColor: '#0D253A',
    vr: {
      key: 'com.texasgamer.zephyr',
      name: 'Zephyr',
      fps: 45,
      iconPath: `${__dirname}/assets/images/icon.png`
    }
  });
  vrWindow.loadURL(`file://${__dirname}/overlay.html`);

  LogUtils.info('Zephyr', `${__dirname}/assets/images/icon.png`);

  if (ConfigUtils.isStandalone()) {
    ZephyrUpdater.getInstance().checkForUpdates();
  }
}

LogUtils.info('Zephyr', `v${ConfigUtils.getAppVersion()} (${ConfigUtils.getBuildType()})`);
app.on('ready', () => installExtensions().then(() => onReady()));
app.on('window-all-closed', () => app.quit());
