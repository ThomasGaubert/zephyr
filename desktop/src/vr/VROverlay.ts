import vr from 'node-openvr';
import VRVec3 from './tools';

export default class VROverlay {

  system: any;
  name: string;
  key: string;
  iconPath: string;
  handle: any;
  renderer: any;
  pos: any;

  constructor ({ system, key, name, iconPath, skipChecks = false, handle = null }) {
    if (!skipChecks) {
      if (system == null || (name == null && key == null)) {
        throw new TypeError('VROverlay needs a IVRSystem and a name and/or key argument.');
      }
    }

    this.system = system;
    this.name = name || key;
    this.key = key || name;
    this.iconPath = iconPath;
    this.handle = handle;
    this.renderer = vr.overlay.Internals();

    this.pos = VRVec3.VRVec3.HmdMatrix34(0, 0, 0);

    if (!skipChecks) this.init();
  }

  init () {
    if (!vr.overlay.Check()) {
      throw new Error("Couldn't initialize a VROverlay. Are you in Scene or Overlay mode from VR_Init?");
    }
    this.handle = vr.overlay.CreateDashboardOverlay(this.key, this.name, this.iconPath);
  }

  createNotification(message: string, iconBuf, { width = 100, height = 100, depth = 8 } = {}) {
    if (iconBuf.length !== width * height * 4) {
      console.warn(`Texture is not the correct number of elements for the size given. Got ${iconBuf.length} bytes, looking for ${width * height * 4}.`);
    }

    vr.notifications.CreateNotification(this.handle, message, iconBuf, width, height, depth);
  }

  show () {
    // vr.overlay.ShowOverlay(this.handle)
  }

  get alpha () {
    return vr.overlay.GetOverlayAlpha(this.handle);
  }

  set alpha (_) {
    vr.overlay.SetOverlayAlpha(this.handle);
  }

  setTextureRaw (tex, { width, height, depth = 8 }) {
    // if depth isn't defined, but it is a typed array, we can use the typed array bitness
    // otherwise, just 8 (0-255)
    vr.overlay.SetOverlayRaw(this.handle, tex, width, height, depth);
  }

  setTextureFromBuffer (tex, { width = 100, height = 100, depth = 8 } = {}) {
    // if depth isn't defined, but it is a typed array, we can use the typed array bitness
    // otherwise, just 8 (0-255)
    // tex = Uint8ClampedArray.from(tex)

    if (tex.length !== width * height * 4) {
      console.warn(`Texture is not the correct number of elements for the size given. Got ${tex.length} bytes, looking for ${width * height * 4}.`);
    }

    // tex = Uint8Array.from(tex.map(x => x * 255))

    vr.overlay.SetOverlayTextureFromBuffer(this.handle, tex, width, height, depth);
  }

  setTextureFromFile (path) {
    vr.overlay.SetOverlayFromFile(this.handle, path);
  }

  transformTrackedDeviceRelative (trackedDevice, { x, y, z }) {
    vr.overlay.SetOverlayTransformTrackedDeviceRelative(this.handle, trackedDevice, VRVec3.VRVec3.HmdMatrix34(x, y, z));
  }

  setOverlayWidthInMeters(w) {
    vr.overlay.SetOverlayWidthInMeters(this.handle, w);
  }

  setOverlayMouseScale(width, height) {
    vr.overlay.SetOverlayMouseScale(this.handle, width, height);
  }

  /*
  notes on how this matrix works
  [
    [ Sx, Rx, A, X ],
    [ Ry, Sy, ?, Y ],
    [ Rz, Rz, Sz, Z ],
  ]

  Sx) Scale
  Rx) Rotation

  A) around -30 or +30, this will pick which eye this renders to?
  B)
  */
  transformTrackedDeviceRelativeMatrix (trackedDevice, matrix) {
    vr.overlay.SetOverlayTransformTrackedDeviceRelative(this.handle, trackedDevice, matrix);
  }

  transformAbsolute ({ x, y, z }) {
    this.pos = VRVec3.VRVec3.HmdMatrix34(x, y, z);
    vr.overlay.SetOverlayTransformAbsolute(this.handle, this.pos);
  }

  // set position (v) {
  //   this.transformTrackedDeviceRelative(0, VRVec3.ProjectionMatrix.HmdMatrix34(v));
  // }

  get position () {
    return this.pos;
  }

  set rotation (_) {
    this.transformAbsolute(this.pos);
  }

  get width () {
    return vr.overlay.GetOverlayWidthInMeters(this.handle);
  }

  set width (v) {
    vr.overlay.SetOverlayWidthInMeters(this.handle, v);
  }
}
