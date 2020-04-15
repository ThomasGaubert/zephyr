import Logger from 'electron-log';
import path from 'path';
import ConfigUtils from './ConfigUtils';

export default class LogUtils {

  static getLogger(): any {
    Logger.transports.file.fileName = this.getLogFileName();
    Logger.transports.file.resolvePath = (vars: any) => {
      return path.join(this.getLogPath(), vars.fileName);
    };
    return Logger;
  }

  static silly(tag: string, message: string) {
    this.getLogger().silly(this.formatLog(tag, message));
  }

  static debug(tag: string, message: string) {
    this.getLogger().debug(this.formatLog(tag, message));
  }

  static verbose(tag: string, message: string) {
    this.getLogger().verbose(this.formatLog(tag, message));
  }

  static info(tag: string, message: string) {
    this.getLogger().info(this.formatLog(tag, message));
  }

  static warn(tag: string, message: string) {
    this.getLogger().warn(this.formatLog(tag, message));
  }

  static error(tag: string, message: string) {
    this.getLogger().error(this.formatLog(tag, message));
  }

  static formatLog(tag: string, message: string): string {
    return `[${tag}] ${message}`;
  }

  static getLogPath(): string {
    return ConfigUtils.getAppRootDirectory();
  }

  static getLogFileName(): string {
    return 'zephyr.log';
  }
}
