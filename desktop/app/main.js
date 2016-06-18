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

// Socket stuff
var bodyParser = require('body-parser')
var web = require('express')()
web.use(bodyParser.urlencoded({ extended: false }))
web.use(bodyParser.json())
var http = require('http').Server(web)
var io = require('socket.io')(http)
var serverId = 'nodejs-server'

// Preferences
const Config = require('electron-config')
const conf = new Config()

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
    mainWindow.loadURL('file://' + __dirname + '/welcome.html')
  } else {
    mainWindow.loadURL('file://' + __dirname + '/index.html')
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
})

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
function startServer() {
  web.post('/api/notification', function(req, res) {
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(handleNotification(JSON.stringify(req.body))))
  })

  web.get('/api/version', function(req, res) {
    res.setHeader('Content-Type', 'application/json')
    res.send(JSON.stringify(handleVersionRequest(JSON.stringify(req.body))))
  })

  web.get('*', function(req, res) {
    res.status(404).sendFile(__dirname + '/404.html')
  })

  io.on('connection', function(socket) {
    socket.on('notification', function(msg) {
      var n = JSON.parse(msg)
      io.emit(n.metadata.from, JSON.stringify(handleNotification(msg)))
    })

    socket.on('version', function(msg) {
      var v = JSON.parse(msg)
      io.emit(v.metadata.from, JSON.stringify(handleVersionRequest(msg)))

      broadcastPing()
    })

    socket.on('broadcast', function(msg) {
      var b = JSON.parse(msg)
      if (b.metadata.type == 'broadcast-pong') {
        console.log('Received response from ' + b.metadata.from)

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
      }
    })

    socket.on('disconnect', function(){
      console.log('Client disconnected, checking on other clients...')
      broadcastPing()
    })
  })

  http.listen(3753, function() {
    console.log('Listening on *:3753')
  })
}

function startOverlay() {
  if(process.platform == 'win32') {
    const execFile = require('child_process').execFile
    const child = execFile('./build/overlay/zephyr.exe', (error, stdout, stderr) => {
      console.log('Overlay not running! (died)')
      broadcastOverlayNotRunning()
    })
  } else {
    console.log('Overlay not running! (requires win32)')
    broadcastOverlayNotRunning()
  }
}

function broadcastOverlayNotRunning() {
  io.emit('broadcast', JSON.stringify({
    metadata: {
      version: 1,
      type: 'broadcast-overlay-not-running',
      from: serverId,
      to: ''
    },
    payload: {
      message: 'Overlay is no longer running.'
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
  console.log('Notification: ' + msg)

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
      id: n.payload.id
    }
  }
}

function handleVersionRequest(msg) {
  console.log('Client requested version info, sending...')
  console.log('Client info: ' + msg)

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
      console.log("Update available!")
      io.emit('updates', JSON.stringify({
        metadata: {
          version: 1,
          type: 'update-available',
          from: serverId,
          to: ''
        },
        payload: {}
      }))
    })

    autoUpdater.addListener("update-downloaded", function(event, releaseNotes, releaseName, releaseDate, updateURL) {  
        console.log("Update donwloaded!")
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
    })

    autoUpdater.addListener("error", function(err) {  
        console.log("Error while checking for updates: " + err)
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
    })

    autoUpdater.addListener("checking-for-update", function(event) {  
        console.log("Checking for update...")
        io.emit('updates', JSON.stringify({
          metadata: {
            version: 1,
            type: 'update-checking',
            from: serverId,
            to: ''
          },
          payload: {}
        }))
    })

    autoUpdater.addListener("update-not-available", function(event) {  
        console.log("Update not available!")
        io.emit('updates', JSON.stringify({
          metadata: {
            version: 1,
            type: 'update-not-available',
            from: serverId,
            to: ''
          },
          payload: {}
        }))
    })

    const feedURL = 'https://zephyr-updates.herokuapp.com/update/' + process.platform + '/' + app.getVersion() 
    console.log('Checking for updates at ' + feedURL + '...')
    autoUpdater.setFeedURL(feedURL)
    autoUpdater.checkForUpdates()
  }
}