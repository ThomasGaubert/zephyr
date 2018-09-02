import os from 'os';

export default class NetworkUtils {
  static foundIpAddress(): boolean {
    return this.getAllIpAddresses().length > 0;
  }

  static getPrimaryIpAddress(): string {
    let ipAddresses = this.getAllIpAddresses();
    if (ipAddresses.length > 0) {
      return ipAddresses[0];
    } else {
      return 'Unable to determine local IP';
    }
  }

  static getPrimaryIpAddressShort(): string {
    let ipAddresses = this.getAllIpAddresses();
    if (ipAddresses.length > 0) {
      return NetworkUtils.getShortAddress(ipAddresses[0]);
    } else {
      return 'Unable to determine local IP';
    }
  }

  static getPrimaryIpAddressShortFormatted(): string {
    let ipAddresses = this.getAllIpAddresses();
    if (ipAddresses.length > 0) {
      let ipAddressShort = NetworkUtils.getShortAddress(ipAddresses[0]);
      if (ipAddressShort === ipAddresses[0]) {
        return 'IP: ' + ipAddressShort;
      } else {
        return 'Join code: ' + ipAddressShort;
      }
    } else {
      return 'Unable to determine local IP';
    }
  }

  static getAllIpAddresses(): Array<string> {
    let interfaces = os.networkInterfaces();
    let ipAddresses = new Array<string>();
    for (let devName in interfaces) {
      if (devName.includes('VirtualBox')) {
        continue;
      }

      let iface = interfaces[devName];

      for (let i = 0; i < iface.length; i++) {
        let alias = iface[i];
        if (alias.family === 'IPv4' && alias.address !== '127.0.0.1' && !alias.internal) {
          ipAddresses.push(alias.address);
        }
      }
    }

    return ipAddresses;
  }

  static getShortAddress(ipAddress: string): string {
    let shortIp: string = '';
    let parts: Array<String> = ipAddress.split('.');

    // Shorten 192.168.x.y to x.y, otherwise return original
    if (parts.length === 4 && parts[0] === '192' && parts[1] === '168') {
      if (parts[2] !== '0') {
        shortIp += parts[2] + '.';
      }
      return shortIp + parts[3];
    } else {
      return ipAddress;
    }
  }
}
