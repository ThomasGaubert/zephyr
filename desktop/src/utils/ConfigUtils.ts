import { app, remote } from 'electron';
import os from 'os';

export default class ConfigUtils {
  /* Versions */
  static getAppVersion(): string {
    if (this.isRenderer()) {
      return remote.app.getVersion();
    } else {
      return app.getVersion();
    }
  }

  static getNodeVersion(): string {
    if (this.isRenderer()) {
      return remote.process.version;
    } else {
      return process.version;
    }
  }

  static getNodeVersions(): NodeJS.ProcessVersions {
    if (this.isRenderer()) {
      return remote.process.versions;
    } else {
      return process.versions;
    }
  }

  static getPlatform(): string {
    return os.platform();
  }

  static getPlatformRelease(): string {
    return os.release();
  }

  /* Build types */
  static getBuildType(): string {
    if (this.isDev()) {
      return 'Dev';
    } else if (this.isSteam()) {
      return 'Steam';
    } else if (this.isStandalone()) {
      return 'Standalone';
    } else {
      return 'Unknown';
    }
  }

  static isDev(): boolean {
    if (this.isRenderer()) {
      return remote.getGlobal('process').env.ZEPHYR_BUILD_TYPE === 'dev';
    } else {
      return process.env.ZEPHYR_BUILD_TYPE === 'dev';
    }
  }

  static isSteam(): boolean {
    return false;
  }

  static isStandalone(): boolean {
    return false;
  }

  static isRenderer(): boolean {
    return (process && process.type === 'renderer' && remote !== undefined); // tslint:disable-line
  }
}
