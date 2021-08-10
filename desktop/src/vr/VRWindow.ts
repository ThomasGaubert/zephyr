import { app, BrowserWindow, nativeImage, NativeImage } from 'electron';
import vr from 'node-openvr';
import ZephyrNotification from '../models/ZephyrNotification';
import EventUtils from '../utils/EventUtils';
import LogUtils from '../utils/LogUtils';
import VROverlay from './VROverlay';

app.disableHardwareAcceleration();

export default class VRWindow extends BrowserWindow {

  vrHasNewFrame: boolean;
  overlay: any;
  vrSystem: any;

  constructor (opts) {
    opts.frame = false;
    opts.transparent = true;
    opts.show = false;
    opts.webPreferences = {
      ...(opts.webPreferences || {}),
      offscreen: opts.vr.devMode || true
    };

    const vrOpts = opts.vr;
    opts.vr = undefined;

    super(opts);

    this.vrHasNewFrame = true;

    this.setupOverlay(vrOpts);
  }

  setupOverlay ({ key, name = 'Zephyr', fps = 60, iconPath, system = null }) {
    this.vrSystem = system || vr.system.VR_Init(vr.EVRApplicationType.VRApplication_Overlay);
    this.overlay = new VROverlay({ system: this.vrSystem, key: key, name: name, iconPath: iconPath });
    this.webContents.setFrameRate(fps);

    this.overlay.width = 3;
    this.overlay.setOverlayMouseScale(this.getSize()[0], this.getSize()[1]);

    this.webContents.on('paint', (..._) => {
      this.vrDraw();
    });

    this.webContents.on('dom-ready', () => {
      this.overlay.show();

      this.vrDraw();

      setTimeout(() => {
        this.vrDraw();
      }, 1000);
    });

    EventUtils.getInstance().on('vr-show-notification', (payload: any) => {
      let notification: ZephyrNotification = payload[0];
      if (notification) {
        LogUtils.verbose('VRWindow', 'Creating notification: ' + JSON.stringify(notification));

        const iconImg = nativeImage.createFromDataURL('data:image/png;base64, ' + notification.icon);
        const iconBuf = iconImg.toBitmap();

        this.overlay.createNotification(notification.title + '\n' + notification.body, iconBuf, { ...iconImg.getSize() });
      }
    });
  }

  vrDraw (force = false) {
    this.webContents.capturePage().then((image: NativeImage) => {
      const buf = image.toBitmap();

      if (!force && buf.length === 0) {
        return;
      }

      this.overlay.setTextureFromBuffer(buf, { ...image.getSize() });
    }).catch((error: any) => {
      LogUtils.error('VRWindow', 'Error when capturing page: ' + error);
    });
  }
}
