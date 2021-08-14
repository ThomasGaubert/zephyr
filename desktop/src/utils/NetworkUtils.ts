import os from 'os';

export default class NetworkUtils {
  static foundIpAddress(): boolean {
    return this.getAllIpAddresses().length > 0;
  }

  static getPrimaryIpAddress(): string | undefined {
    const ipAddresses = this.getAllIpAddresses();
    if (ipAddresses.length > 0) {
      for (let i = 0; i < ipAddresses.length; i++) {
        const parts: String[] = ipAddresses[i].split('.');
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
    const primaryIpAddress = this.getPrimaryIpAddress();
    if (primaryIpAddress !== undefined) {
      return NetworkUtils.getShortAddress(primaryIpAddress);
    } else {
      return undefined;
    }
  }

  static getPrimaryIpAddressShortFormatted(): string | undefined {
    const primaryIpAddress = this.getPrimaryIpAddress();
    if (primaryIpAddress !== undefined) {
      const primaryIpAddressShort = NetworkUtils.getShortAddress(primaryIpAddress);
      if (primaryIpAddressShort === primaryIpAddress) {
        return 'IP: ' + primaryIpAddressShort;
      } else {
        return 'Join code: ' + primaryIpAddressShort;
      }
    } else {
      return undefined;
    }
  }

  static getAllIpAddresses(): string[] {
    const interfaces = os.networkInterfaces();
    const ipAddresses = new Array<string>();
    for (const devName in interfaces) {
      if (devName.includes('VirtualBox')) {
        continue;
      }

      const iface = interfaces[devName];

      if (iface === undefined) {
        return [];
      }

      for (let i = 0; i < iface.length; i++) {
        const alias = iface[i];
        if (alias.family === 'IPv4' && alias.address !== '127.0.0.1' && !alias.internal) {
          ipAddresses.push(alias.address);
        }
      }
    }

    return ipAddresses;
  }

  static getShortAddress(ipAddress: string): string {
    let shortIp: string = '';
    const parts: string[] = ipAddress.split('.');

    // Shorten 192.168.x.y to x.y, otherwise return original
    if (parts.length === 4 && parts[0] === '192' && parts[1] === '168') {
      if (parts[2] !== '0') {
        shortIp += `${parts[2]}.`;
      }
      return `${shortIp}${parts[3]}`;
    } else {
      return ipAddress;
    }
  }
}
