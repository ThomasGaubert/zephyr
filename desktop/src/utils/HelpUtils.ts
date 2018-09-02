import { shell } from 'electron';
import LogUtils from './LogUtils';

export default class HelpUtils {
  static openHelp() {
    LogUtils.verbose('HelpUtils', 'Opening help...');
    shell.openExternal('https://zephyrvr.gitbook.io/docs/');
  }

  static openConnectionHelp() {
    LogUtils.verbose('HelpUtils', 'Opening connection help...');
    shell.openExternal('https://zephyrvr.gitbook.io/docs/');
  }
}
