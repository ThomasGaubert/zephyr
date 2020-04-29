import os from 'os';

export default class NetworkUtils {
  static foundIpAddress(): boolean {
    return this.getAllIpAddresses().length > 0;
  }

  static getPrimaryIpAddress(): string | undefined {
    let ipAddresses = this.getAllIpAddresses();
    if (ipAddresses.length > 0) {
      for (let i = 0; i < ipAddresses.length; i++) {
        let parts: Array<String> = ipAddresses[i].split('.');
        if (parts.length === 4 && parts[0] === '192' && parts[1] === '168') {
          return ipAddresses[i];
        }
      }

      return ipAddresses[0];
    } else {
      return undefined;
    }
  }

  static getPrimaryIpAddressShort(): string | undefined {
    let primaryIpAddress = this.getPrimaryIpAddress();
    if (primaryIpAddress !== undefined) {
      return NetworkUtils.getShortAddress(primaryIpAddress);
    } else {
      return undefined;
    }
  }

  static getPrimaryIpAddressShortFormatted(): string | undefined {
    let primaryIpAddress = this.getPrimaryIpAddress();
    if (primaryIpAddress !== undefined) {
      let primaryIpAddressShort = NetworkUtils.getShortAddress(primaryIpAddress);
      if (primaryIpAddressShort === primaryIpAddress) {
        return 'IP: ' + primaryIpAddressShort;
      } else {
        return 'Join code: ' + primaryIpAddressShort;
      }
    } else {
      return undefined;
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

      if (iface === undefined) {
        return [];
      }

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
