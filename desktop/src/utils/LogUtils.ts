import Logger from 'electron-log';

export default class LogUtils {

  static getLogger(): any {
    return Logger;
  }

  static silly(tag: string, message: string) {
    Logger.silly(this.formatLog(tag, message));
  }

  static debug(tag: string, message: string) {
    Logger.debug(this.formatLog(tag, message));
  }

  static verbose(tag: string, message: string) {
    Logger.verbose(this.formatLog(tag, message));
  }

  static info(tag: string, message: string) {
    Logger.info(this.formatLog(tag, message));
  }

  static warn(tag: string, message: string) {
    Logger.warn(this.formatLog(tag, message));
  }

  static error(tag: string, message: string) {
    Logger.error(this.formatLog(tag, message));
  }

  static formatLog(tag: string, message: string): string {
    return `[${tag}] ${message}`;
  }
}
