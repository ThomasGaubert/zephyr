import ciao, { CiaoService } from '@homebridge/ciao';
import express from 'express';
import os from 'os';
import { Server } from 'socket.io';
import DismissNotificationPayload from '../models/DismissNotificationPayload';
import SocketChannels from '../models/SocketChannels';
import ZephyrNotification from '../models/ZephyrNotification';
import ConfigUtils from '../utils/ConfigUtils';
import EventUtils from '../utils/EventUtils';
import LogUtils from '../utils/LogUtils';
import NotificationUtils from '../utils/NotificationUtils';

export class ZephyrServer {

  static API_VERSION: number = 1;
  static DEFAULT_PORT: number = 3753;

  notifications: Map<string, ZephyrNotification>;
  service: CiaoService;

  constructor() {
    const app = express();
    this.notifications = new Map<string, ZephyrNotification>();

    let port: number = ConfigUtils.getPort();

    if (!port) {
      port = ZephyrServer.DEFAULT_PORT;
    }

    // Server config
    app.set('port', port);
    app.use(this.setupHeaders);
    app.use(express.urlencoded({ extended: true }));
    app.use(express.json());

    let http = require('http').createServer(app);
    let io = new Server(http);

    // API routes
    app.get('/api/version', this.serveVersion);
    app.get('/api/notifications', (body, res) => this.serveNotifications(body, res, this));
    app.get('*', this.serve404);

    // Socket routes
    io.on('connection', (socket) => this.onSocketConnection(socket, io, this));

    http.listen(app.get('port'), function() {
      LogUtils.info('ZephyrServer', 'Server listening on *:' + app.get('port'));
    });

    // Initialize service info for advertisement
    const responder = ciao.getResponder();
    this.service = responder.createService({
      name: 'Zephyr-' + os.hostname(),
      type: 'zephyr',
      port: ZephyrServer.DEFAULT_PORT,
      txt: {
        apiVersion: ZephyrServer.API_VERSION,
        displayName: os.hostname()
      }
    });

    // Discovery
    if (ConfigUtils.discoveryEnabled()) {
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
    this.service.advertise().then(() => {
      LogUtils.info('ZephyrServer', 'Published discovery broadcast');
    }).catch(() => {
      LogUtils.error('ZephyrServer', 'Error when publishing discovery broadcast');
    });
  }

  stopDiscoveryBroadcast(): Promise<void> {
    LogUtils.info('ZephyrServer', 'Stopping discovery broadcast...');
    return this.service.end().then(() => {
      LogUtils.info('ZephyrServer', 'Stopped discovery broadcast');
    }).catch(() => {
      LogUtils.error('ZephyrServer', 'Error when stopping discovery broadcast');
    });
  }
}
