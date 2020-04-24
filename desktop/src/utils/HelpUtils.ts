import { shell } from 'electron';
import LogUtils from './LogUtils';

export default class HelpUtils {
  static openHelp() {
    LogUtils.verbose('HelpUtils', 'Opening help...');
    shell.openExternal('https://zephyrvr.gitbook.io/docs/');
  }

  static openConnectionHelp() {
    LogUtils.verbose('HelpUtils', 'Opening connection help...');
    shell.openExternal('https://zephyrvr.gitbook.io/docs/troubleshooting/connection-issues');
  }

  static openPlayStore() {
    LogUtils.verbose('HelpUtils', 'Opening Play Store...');
    shell.openExternal('https://play.google.com/store/apps/details?id=com.texasgamer.zephyr');
  }
}
