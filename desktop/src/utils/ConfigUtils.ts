import { app, remote } from 'electron';
import fs from 'fs';
import os from 'os';

declare var __dirname: string;

export default class ConfigUtils {

  private static config;

  static getConfig() {
    if (!ConfigUtils.config) {
      ConfigUtils.config = JSON.parse(fs.readFileSync(`${__dirname}/assets/config/config.json`) as any);
    }

    return this.config;
  }

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
    return ConfigUtils.getConfig().type === 'dev';
  }

  static isSteam(): boolean {
    return ConfigUtils.getConfig().type === 'steam';
  }

  static isStandalone(): boolean {
    return ConfigUtils.getConfig().type === 'standalone';
  }

  static isRenderer(): boolean {
    return (process && process.type === 'renderer' && remote !== undefined); // tslint:disable-line
  }
}
