import BodyParser from 'body-parser';
import dgram, { Socket } from 'dgram';
import express from 'express';
import SocketIO from 'socket.io';
import DiscoveryPacket from '../models/DiscoveryPacket';
import DismissNotificationPayload from '../models/DismissNotificationPayload';
import SocketChannels from '../models/SocketChannels';
import ZephyrNotification from '../models/ZephyrNotification';
import ConfigUtils, { IZephyrDiscoveryConfig } from '../utils/ConfigUtils';
import EventUtils from '../utils/EventUtils';
import LogUtils from '../utils/LogUtils';
import NetworkUtils from '../utils/NetworkUtils';
import NotificationUtils from '../utils/NotificationUtils';

export class ZephyrServer {

  static API_VERSION: number = 1;
  static DEFAULT_PORT: number = 3753;

  notifications: Map<string, ZephyrNotification>;
  discoveryConfig: IZephyrDiscoveryConfig;

  constructor() {
    const app = express();
    this.notifications = new Map<string, ZephyrNotification>();
    this.discoveryConfig = ConfigUtils.getDiscoveryConfig();

    let port: number = ConfigUtils.getPort();

    if (!port) {
      port = ZephyrServer.DEFAULT_PORT;
    }

    // Server config
    app.set('port', port);
    app.use(this.setupHeaders);
    app.use(BodyParser.urlencoded({ extended: false }));
    app.use(BodyParser.json());

    let http = require('http').Server(app);
    let io = SocketIO(http);

    // API routes
    app.get('/api/version', this.serveVersion);
    app.get('/api/notifications', (body, res) => this.serveNotifications(body, res, this));
    app.get('*', this.serve404);

    // Socket routes
    io.on('connection', (socket) => this.onSocketConnection(socket, io, this));

    http.listen(app.get('port'), function() {
      LogUtils.info('ZephyrServer', 'Server listening on *:' + app.get('port'));
    });

    // Discovery
    if (this.discoveryConfig.enabled) {
      this.startDiscoveryBroadcast();
    }
  }

  // HTTP
  setupHeaders (_, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'X-Requested-With');
    res.header('Access-Control-Allow-Headers', 'Content-Type');
    res.header('Access-Control-Allow-Methods', 'PUT, GET, POST, DELETE, OPTIONS');
    res.header('Content-Type', 'application/json');
    res.header('X-Zephyr-Api-Version', ZephyrServer.API_VERSION);
    next();
  }

  serveVersion (_, res) {
    res.send(JSON.stringify({
      api: ZephyrServer.API_VERSION,
      desktop: ConfigUtils.getAppVersion(),
      node: ConfigUtils.getNodeVersion(),
      buildType: ConfigUtils.getBuildType(),
      config: ConfigUtils.getConfig(),
      features: ConfigUtils.getAppFeatures(),
      socketChannels: ConfigUtils.getSocketChannels()
    }));
  }

  serveNotifications (_, res, server: ZephyrServer) {
    res.send(JSON.stringify({
      notifications: Array.from(server.notifications.values()).sort((a, b) => a.timestamp < b.timestamp ? -1 : a.timestamp > b.timestamp ? 1 : 0)
    }));
  }

  serve404 (_, res) {
    res.status(404).send(JSON.stringify({
      error: 404,
      message: 'Not found'
    }));
  }

  // Sockets
  onSocketConnection (socket: any, io: any, server: ZephyrServer) {
    LogUtils.verbose('ZephyrServer', 'Client connected.');

    // Notification
    socket.on(SocketChannels.ACTION_POST_NOTIFICATION, (msg) => server.onPostNotification(io, msg, server));
    socket.on(SocketChannels.ACTION_DISMISS_NOTIFICATION, (msg) => server.onDismissNotification(io, msg, server));

    // Disconnect
    socket.on(SocketChannels.ACTION_DISCONNECT, this.onSocketDisconnect);
  }

  onPostNotification(io: any, msg: any, server: ZephyrServer) {
    let notification = server.deserializeNotification(msg);

    if (notification !== undefined) {
      LogUtils.verbose('ZephyrServer', 'Notification posted.');
      server.notifications.set(NotificationUtils.getNotificationKey(notification), notification);
      io.emit(SocketChannels.EVENT_NOTIFICATION_POSTED, notification);

      EventUtils.getInstance().emit('vr-show-notification', notification);
    } else {
      LogUtils.warn('ZephyrServer', 'Invalid notification posted.');
    }
  }

  onDismissNotification(io: any, msg: any, server: ZephyrServer) {
    let dismissPayload = server.deserializeDismissPayload(msg);

    if (dismissPayload !== undefined) {
      LogUtils.verbose('ZephyrServer', 'Notification dismissed.');
      server.notifications.delete(NotificationUtils.getNotificationKey(dismissPayload));
      io.emit(SocketChannels.EVENT_NOTIFICATION_DISMISSED, dismissPayload);
    } else {
      LogUtils.warn('ZephyrServer', 'Unable to dismiss notification: invalid payload.');
    }
  }

  onSocketDisconnect() {
    LogUtils.verbose('ZephyrServer', 'Client disconnected.');
  }

  deserializeNotification (notification: any): ZephyrNotification | undefined {
    let notificationJson = JSON.parse(notification);

    // Notifications must have package name, id, and title
    if (notificationJson['packageName'] === undefined || notificationJson['id'] === undefined || notificationJson['title'] === undefined) {
      return undefined;
    }

    return {
      packageName: notificationJson['packageName'],
      id: notificationJson['id'],
      timestamp: notificationJson['timestamp'] !== undefined ? notificationJson['timestamp'] : Date.now(),
      title: notificationJson['title'],
      body: notificationJson['body'],
      icon: notificationJson['icon']
    } as ZephyrNotification;
  }

  deserializeDismissPayload (payload: any): DismissNotificationPayload | undefined {
    let payloadJson = JSON.parse(payload);

    // Dismiss payloads must have a package name
    if (payloadJson['packageName'] === undefined || payloadJson['id'] === undefined) {
      return undefined;
    }

    return {
      packageName: payloadJson['packageName'],
      id: payloadJson['id']
    } as DismissNotificationPayload;
  }

  startDiscoveryBroadcast () {
    LogUtils.info('ZephyrServer', 'Starting discovery broadcast...');
    const socket: Socket = dgram.createSocket('udp4');
    const serverInstance: ZephyrServer = this;
    socket.on('listening', function() {
      socket.setBroadcast(true);
      socket.setMulticastTTL(128);
      NetworkUtils.getAllIpAddresses().forEach((ipAddress) => {
        socket.addMembership(serverInstance.discoveryConfig.broadcastAddress, ipAddress);
      });
      setInterval(() => serverInstance.broadcast(serverInstance, socket), serverInstance.discoveryConfig.broadcastIntervalInMs);
      LogUtils.info('ZephyrServer', 'Started discovery broadcast.');
    });
    socket.bind(8000);
  }

  broadcast (server: ZephyrServer, socket: Socket) {
    const discoveryPacket = {
      timestamp: Date.now(),
      apiVersion: ZephyrServer.API_VERSION,
      port: ZephyrServer.DEFAULT_PORT
    } as DiscoveryPacket;
    const message = Buffer.from(JSON.stringify(discoveryPacket), 'utf8');
    socket.send(message, 0, message.length, server.discoveryConfig.broadcastPort, server.discoveryConfig.broadcastAddress);
  }
}
