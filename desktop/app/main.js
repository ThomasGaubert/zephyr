// Handle any Squirrel events first
if(require('electron-squirrel-startup')) return;

const electron = require('electron')
// Module to control application life.
const app = electron.app

// Module to create native browser window.
const BrowserWindow = electron.BrowserWindow

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let mainWindow

// Preferences
const Config = require('electron-config')
const conf = new Config()

// Analytics
var mixpanel = require('mixpanel').init('6cae86bf1da092b800b30b27689bd665')
if (!conf.has('uuid')) {
  conf.set('uuid', require('node-uuid').v1())
}
const uuid = conf.get('uuid')

// Networking
var bodyParser = require('body-parser')
var web = require('express')()
web.use(bodyParser.urlencoded({ extended: false }))
web.use(bodyParser.json())
var http = require('http').Server(web)
var io = require('socket.io')(http)
var serverId = 'nodejs-server'

// Logging
var log = require('./res/js/console').setup()
var quitting

log.info('Zephyr v' + app.getVersion())

// Setup auto-updater
const autoUpdater = electron.autoUpdater
setupAutoUpdater()

function createWindow () {
  // Create the browser window.
  mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    minWidth: 400,
    minHeight: 600,
    frame: false,
    icon: __dirname + '/res/img/icon.ico'
  })

  // Start onboarding if needed, otherwise load app
  if (!conf.has('firstRun') || conf.get('firstRun')) {
    log.info('Opening onboarding window...')
    mainWindow.loadURL('file://' + __dirname + '/welcome.html')
    mixpanel.track('first-run', {uuid: uuid})
  } else {
    log.info('Opening main window...')
    mainWindow.loadURL('file://' + __dirname + '/index.html')
    mixpanel.track('repeat-run', {uuid: uuid})
  }

  // Emitted when the window is closed.
  mainWindow.on('closed', function () {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    mainWindow = null
  })

  startServer()
  startOverlay()
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow)

// Quit when all windows are closed.
app.on('window-all-closed', function () {
  // On OS X it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', function () {
  // On OS X it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (mainWindow === null) {
    createWindow()
  }
})

app.on('will-quit', function () {
  quitting = true
  io.emit('broadcast', JSON.stringify({
    metadata: {
      version: 1,
      type: 'broadcast-shutdown',
      from: serverId,
      to: ''
    },
    payload: {
      message: 'Server is shutting down...'
    }
  }))
  mixpanel.track('quitting', {uuid: uuid})
  log.info('Quitting Zephyr v' + app.getVersion())
  log.info('------------------------------------')
})

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
function startServer() {
  web.post('/api/notification', function(req, res) {
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(handleNotification(JSON.stringify(req.body))))
    mixpanel.track('api-http-notification', req.body.payload)
  })

  web.get('/api/version', function(req, res) {
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(handleVersionRequest(JSON.stringify(req.body))))
    req.body.payload.set('uuid', uuid)
    mixpanel.track('api-http-version', req.body.payload)
  })

  web.get('*', function(req, res) {
    res.status(404).sendFile(__dirname + '/404.html')
    req.body.payload.set('uuid', uuid)
    mixpanel.track('api-http-404', req.body.payload)
  })

  io.on('connection', function(socket) {
    socket.on('notification', function(msg) {
      var n = JSON.parse(msg)
      io.emit(n.metadata.from, JSON.stringify(handleNotification(msg)))
      n.payload.set('uuid', uuid)
      mixpanel.track('api-ws-notification', n.payload)
    })

    socket.on('version', function(msg) {
      var v = JSON.parse(msg)
      io.emit(v.metadata.from, JSON.stringify(handleVersionRequest(msg)))

      broadcastPing()
      v.payload.set('uuid', uuid)
      mixpanel.track('api-ws-version', v.payload)
    })

    socket.on('broadcast', function(msg) {
      var b = JSON.parse(msg)
      if (b.metadata.type == 'broadcast-pong') {
        log.info('Received response from ' + b.metadata.from)

        io.emit('devices', JSON.stringify({
          metadata: {
            version: 1,
            type: 'devices-add',
            from: serverId,
            to: ''
          },
          payload: {
            name: b.metadata.from
          }
        }))
        mixpanel.track('api-ws-pong', {id: uuid})
      }
    })

    socket.on('disconnect', function() {
      if (!quitting) {
        log.info('Client disconnected, checking on other clients...')
        mixpanel.track('api-ws-disconnect', {id: uuid})
        broadcastPing()
      }
    })
  })

  http.listen(3753, function() {
    log.info('Listening on *:3753')
  })
}

function startOverlay() {
  if(process.platform == 'win32') {
    const execFile = require('child_process').execFile
    const child = execFile('./build/overlay/zephyr.exe', (error, stdout, stderr) => {
      log.info('Overlay no longer running!')
      log.info('Overlay error: ' + error)
      log.info('Overlay stdout: ' + stdout)
      log.info('Overlay stderr: ' + stderr)
      broadcastOverlayNotRunning(error)
      mixpanel.track('overlay-error', {
        uuid: uuid,
        error: error
      })
    })
  } else {
    log.info('Overlay not running! (requires win32)')
    broadcastOverlayNotRunning('Windows is required.')
    mixpanel.track('overlay-error-win32', {
      uuid: uuid,
      platform: process.platform
    })
  }
}

function broadcastOverlayNotRunning(error) {
  io.emit('broadcast', JSON.stringify({
    metadata: {
      version: 1,
      type: 'broadcast-overlay-not-running',
      from: serverId,
      to: ''
    },
    payload: {
      message: 'Overlay is no longer running.',
      error: error
    }
  }))
}

function broadcastPing() { 
  io.emit('broadcast', JSON.stringify({
    metadata: {
      version: 1,
      type: 'broadcast-ping',
      from: serverId,
      to: ''
    },
    payload: {}
  }))

  io.emit('devices', JSON.stringify({
    metadata: {
      version: 1,
      type: 'devices-clear',
      from: serverId,
      to: ''
    },
    payload: {}
  }))
}

function handleNotification(msg) {
  log.info('Notification: ' + msg)

  io.emit('notification', msg)

  var n = JSON.parse(msg)
  return {
    metadata: {
      version: 1,
      type: 'notification-response',
      from: serverId,
      to: n.metadata.from
    },
    payload: {
      result: true,
      resultCode: 0,
      resultMessage: 'Success',
      uuid: n.payload.id
    }
  }
}

function handleVersionRequest(msg) {
  log.info('Client requested version info, sending...')
  log.info('Client info: ' + msg)

  var v = JSON.parse(msg)
  var t = v.metadata && v.metadata.from ? v.metadata.from : ''

  return {
    metadata: {
      version: 1,
      type: 'version',
      from: serverId,
      to: t
    },
    payload: {
      name: 'Zephyr Server',
      version: app.getVersion(),
      versionCode: 1,
      versions: [
        {
          name: 'platform',
          value: process.platform
        },
        {
          name: 'node',
          value: process.version
        }
      ]
    }
  }
}

function setupAutoUpdater() {
  if(process.platform == 'win32') {
    autoUpdater.addListener("update-available", function(event) {  
      log.info("Update available!")
      io.emit('updates', JSON.stringify({
        metadata: {
          version: 1,
          type: 'update-available',
          from: serverId,
          to: ''
        },
        payload: {}
      }))
      mixpanel.track('update-available', {uuid: uuid})
    })

    autoUpdater.addListener("update-downloaded", function(event, releaseNotes, releaseName, releaseDate, updateURL) {  
        log.info("Update downloaded!")
        io.emit('updates', JSON.stringify({
          metadata: {
            version: 1,
            type: 'update-downloaded',
            from: serverId,
            to: ''
          },
          payload: {
            name: releaseName,
            date: releaseDate,
            changelog: releaseNotes
          }
        }))
        mixpanel.track('update-downloaded', {
          uuid: uuid,
          releaseName: releaseName
        })
    })

    autoUpdater.addListener("error", function(err) {  
        log.info("Error while checking for updates: " + err)
        io.emit('updates', JSON.stringify({
          metadata: {
            version: 1,
            type: 'update-error',
            from: serverId,
            to: ''
          },
          payload: {
            error: err
          }
        }))
        mixpanel.track('update-error', {
          uuid: uuid,
          error: error
        })
    })

    autoUpdater.addListener("checking-for-update", function(event) {  
        log.info("Checking for update...")
        io.emit('updates', JSON.stringify({
          metadata: {
            version: 1,
            type: 'update-checking',
            from: serverId,
            to: ''
          },
          payload: {}
        }))
        mixpanel.track('update-checking', {uuid: uuid})
    })

    autoUpdater.addListener("update-not-available", function(event) {  
        log.info("Update not available!")
        io.emit('updates', JSON.stringify({
          metadata: {
            version: 1,
            type: 'update-not-available',
            from: serverId,
            to: ''
          },
          payload: {}
        }))
        mixpanel.track('update-not-available', {uuid, uuid})
    })

    const feedURL = 'https://zephyr-updates.herokuapp.com/update/' + process.platform + '/' + app.getVersion() 
    log.info('Checking for updates at ' + feedURL + '...')
    autoUpdater.setFeedURL(feedURL)
    autoUpdater.checkForUpdates()
  }
}