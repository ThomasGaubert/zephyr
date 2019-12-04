import { app, remote } from 'electron';
import fs from 'fs';
import os from 'os';
import path from 'path';
import SocketChannels from '../models/SocketChannels';
import ZephyrFeatures from '../models/ZephyrFeatures';

declare var __dirname: string;

interface IZephyrConfig {
  type: string;
  enableUpdates: boolean;
  port: number;
  enableOverlay: boolean;
}

interface IZephyrSocketChannels {
  actions: IZephyrSocketActionChannels;
  events: IZephyrSocketEventChannels;
}

interface IZephyrSocketActionChannels {
  postNotification: string;
  dismissNotification: string;
  disconnect: string;
}

interface IZephyrSocketEventChannels {
  notificationPosted: string;
  notificationDismissed: string;
}

export default class ConfigUtils {

  private static config: IZephyrConfig;

  static getConfig(): IZephyrConfig {
    if (!ConfigUtils.config) {
      ConfigUtils.config = JSON.parse(fs.readFileSync(`${__dirname}/assets/config/config.json`) as any) as IZephyrConfig;
    }

    return this.config;
  }

  static getAppFeatures(): Array<string> {
    return [ZephyrFeatures.POST_NOTIFICATIONS, ZephyrFeatures.DISMISS_NOTIFICATIONS];
  }

  static getSocketChannels(): IZephyrSocketChannels {
    return {
      actions: {
        postNotification: SocketChannels.ACTION_POST_NOTIFICATION,
        dismissNotification: SocketChannels.ACTION_DISMISS_NOTIFICATION,
        disconnect: SocketChannels.ACTION_DISCONNECT
      },
      events: {
        notificationPosted: SocketChannels.EVENT_NOTIFICATION_POSTED,
        notificationDismissed: SocketChannels.EVENT_NOTIFICATION_DISMISSED
      }
    };
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

  /* Paths */
  /**
   * Returns path to current script/package being executed.
   */
  static getAppPath(): string {
    return __dirname;
  }

  /**
   * Returns path to packaged resources directory.
   */
  static getResourcesDirectory(): string {
    return this.getAppPath() + '/assets';
  }

  /**
   * Returns path to packaged images directory.
   */
  static getImagesDirectory(): string {
    return this.getResourcesDirectory() + '/images';
  }

  /**
   * Returns path to root of installation.
   */
  static getInstallDirectory(): string {
    let appPath = path.dirname(this.getAppPath()).split(path.sep);

    if (!this.isRunner()) {
      appPath.pop();
      appPath.pop();
    }

    return path.join(...appPath);
  }

  /**
   * Returns path to external resources directory.
   */
  static getExternalResourcesDirectory(): string {
    return path.join(this.getInstallDirectory(), 'resources');
  }

  /**
   * Returns path to external images directory.
   */
  static getExternalImagesDirectory(): string {
    return path.join(this.getExternalResourcesDirectory(), 'images');
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

  /**
   * Returns if running in "runner mode" (from `yarn start`).
   */
  static isRunner(): boolean {
    return (process.defaultApp || /node_modules[\\/]electron[\\/]/.test(process.execPath));
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

  /**
   * Returns if running in renderer thread.
   */
  static isRenderer(): boolean {
    return (process && process.type === 'renderer' && remote !== undefined); // tslint:disable-line
  }

  /* Features */
  static updatesEnabled(): boolean {
    return ConfigUtils.getConfig().enableUpdates;
  }

  /* Configuration */
  static getPort(): number {
    return ConfigUtils.getConfig().port;
  }

  static overlayEnabled(): boolean {
    return ConfigUtils.getConfig().enableOverlay;
  }
}
