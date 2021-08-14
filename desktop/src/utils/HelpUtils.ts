import { shell } from 'electron';
import LogUtils from './LogUtils';

export default class HelpUtils {
  static openHelp(): void {
    LogUtils.verbose('HelpUtils', 'Opening help...');
    shell.openExternal('https://zephyrvr.gitbook.io/docs/').catch(error => {
      LogUtils.error('HelpUtils', 'Failed to open help: ' + JSON.stringify(error));
    });
  }

  static openConnectionHelp(): void {
    LogUtils.verbose('HelpUtils', 'Opening connection help...');
    shell.openExternal('https://zephyrvr.gitbook.io/docs/troubleshooting/connection-issues').catch(error => {
      LogUtils.error('HelpUtils', 'Failed to open connection help: ' + JSON.stringify(error));
    });
  }

  static openPlayStore(): void {
    LogUtils.verbose('HelpUtils', 'Opening Play Store...');
    shell.openExternal('https://play.google.com/store/apps/details?id=com.texasgamer.zephyr').catch(error => {
      LogUtils.error('HelpUtils', 'Failed to open Play Store: ' + JSON.stringify(error));
    });
  }
}
