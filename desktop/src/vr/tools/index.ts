import OpenVR from 'node-openvr';
import ProjectionMatrix from './projectionMatrix';

// ovr.fetchEnums(['ETrackedDeviceProperty'])

class VRVec3 {

  x;
  y;
  z;

  constructor (x, y, z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  getHmdMatrix34 (): number[][] {
    return VRVec3.HmdMatrix34(this.x, this.y, this.z);
  }

  static HmdMatrix34 (x, y, z): number[][] {
    return [
      [1, 0, 0, x],
      [0, 1, 0, y],
      [0, 0, 1, z]
    ];
  }
}

export default {
  agent (VRSystem) {
    // HMD
    const tracking = VRSystem.GetStringTrackedDeviceProperty(0, OpenVR.ETrackedDeviceProperty.Prop_TrackingSystemName_String);
    const model = VRSystem.GetStringTrackedDeviceProperty(0, OpenVR.ETrackedDeviceProperty.Prop_ModelNumber_String);
    const serial = VRSystem.GetStringTrackedDeviceProperty(0, OpenVR.ETrackedDeviceProperty.Prop_SerialNumber_String);

    return { tracking, model, serial };
  },

  printAgent (VRSystem) {
    console.log(this.agent(VRSystem));
  },

  VRVec3,

  ProjectionMatrix
};
