import { app, BrowserWindow } from 'electron';
import vr from 'node-openvr';
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

  setupOverlay ({ key, name = 'Electron VR App', fps = 60, iconPath, system = null }) {
    this.vrSystem = system || vr.system.VR_Init(vr.EVRApplicationType.VRApplication_Overlay);
    this.overlay = new VROverlay({ system: this.vrSystem, key: `electronvr:${Date.now()}:${key}`, name: name, iconPath: iconPath });
    this.webContents.setFrameRate(fps);

    this.overlay.width = 2;

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
  }

  vrDraw (force = false) {
    this.webContents.capturePage((image) => {
      const buf = image.toBitmap();

      if (!force && buf.length === 0) {
        return;
      }

      this.overlay.setTextureFromBuffer(buf, { ...image.getSize() });
    });
  }
}
