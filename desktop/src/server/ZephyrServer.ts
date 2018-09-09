import BodyParser from 'body-parser';
import express from 'express';
import SocketIO from 'socket.io';
import ZephyrNotification from '../models/ZephyrNotification';
import ConfigUtils from '../utils/ConfigUtils';
import EventUtils from '../utils/EventUtils';
import LogUtils from '../utils/LogUtils';

export class ZephyrServer {

  static API_VERSION: number = 1;
  static DEFAULT_PORT: number = 3753;

  notifications: Array<ZephyrNotification>;

  constructor() {
    const app = express();
    this.notifications = new Array<ZephyrNotification>();

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
      config: ConfigUtils.getConfig()
    }));
  }

  serveNotifications (_, res, server: ZephyrServer) {
    res.send(JSON.stringify({
      notifications: server.notifications
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
    socket.on('post-notification', (msg) => server.onPostNotification(io, msg, server));

    // Disconnect
    socket.on('disconnect', this.onSocketDisconnect);
  }

  onPostNotification(io: any, msg: any, server: ZephyrServer) {
    let notification = server.deserializeNotification(msg);

    if (notification !== undefined) {
      LogUtils.verbose('ZephyrServer', 'Notification posted.');
      server.notifications.push(notification);
      io.emit('event-notification', notification);

      EventUtils.getInstance().emit('vr-show-notification', notification);
    } else {
      LogUtils.warn('ZephyrServer', 'Invalid notification posted.');
    }
  }

  onSocketDisconnect() {
    LogUtils.verbose('ZephyrServer', 'Client disconnected.');
  }

  deserializeNotification (notification: any): ZephyrNotification | undefined {
    let notificationJson = JSON.parse(notification);

    // Notifications must have an id and title
    if (notificationJson['id'] === undefined || notificationJson['title'] === undefined) {
      return undefined;
    }

    return {
      id: notificationJson['id'],
      timestamp: notificationJson['timestamp'] !== undefined ? notificationJson['timestamp'] : Date.now(),
      title: notificationJson['title'],
      message: notificationJson['message'],
      icon: notificationJson['icon']
    } as ZephyrNotification;
  }
}
