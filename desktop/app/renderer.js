// Logging
var log = require('./res/js/console').setup()

setupMenu()

var socket = io('http://localhost:3753')
var shell = require('electron').shell
$(document).on('click', 'a[href^="http"]', function(event) {
  event.preventDefault()
  shell.openExternal(this.href)
})

$('.dev-tools').hide()

var clientId = 'zephyr-desktop-client-' + Math.floor((Math.random() * 100) + 1)

log.info(getVersionInfo().payload.name + ' ' + getVersionInfo().payload.version)
log.info('Client ID: ' + clientId)

requestServerVersion()

$('#btnDevTools').click(function() {
  require('electron').remote.getCurrentWindow().toggleDevTools()
})

$('#btnExNotif').click(function() {
  socket.emit('notification', JSON.stringify(getTestNotification()))
})

$('#btnVersion').click(function() {
  requestServerVersion()
})

socket.on('broadcast', function(msg) {
  var b = JSON.parse(msg)
  if (b.metadata.type == 'broadcast-ping') {
    socket.emit('broadcast', JSON.stringify({
      metadata: {
        version: 1,
        type: 'broadcast-pong',
        from: clientId,
        to: b.metadata.from
      },
      payload: {}
    }))
  } else if (b.metadata.type == 'broadcast-overlay-not-running') {
    log.info('Overlay not running! (' + b.payload.message + ' : ' + b.payload.error + ' )')
    $.snackbar({content: 'Zephyr overlay is not running. Restart Zephyr to try again.', timeout: 0})
    $('#status').html('Zephyr overlay is not running. Restart Zephyr to try again.<br/>Error: ' + b.payload.error + '<br/><br/>If this issue persists, please post in <a href="http://reddit.com/r/zephyr_vr">/r/zephyr_vr</a>.')
  }
})

socket.on('devices', function(msg) {
  var d = JSON.parse(msg)
  if(d.metadata.type == 'devices-add' && d.payload.name != clientId) {
    $('#devices-none').hide()
    $('#devices-list').show()
    $('#devices-list').append('<li>' + d.payload.name + '</li>')
  } else if(d.metadata.type == 'devices-clear') {
    $('#devices-list').empty()
    $('#devices-list').hide()
    $('#devices-none').show()
  }
})

socket.on('notification', function(msg) {
  var n = JSON.parse(msg)
  $.snackbar({content: n.payload.title + ': ' + n.payload.text})
})

socket.on('updates', function(msg) {
  log.info(msg)
  var u = JSON.parse(msg)
  if(u.metadata.type == 'update-downloaded') {
    $.snackbar({content: 'Update downloaded, restart to apply.', timeout: 0})
  } else if(u.metadata.type == 'update-checking') {
    $.snackbar({content: 'Checking for updates...'})
  } else if(u.metadata.type == 'update-not-available') {
    $.snackbar({content: 'Zephyr is up to date.'})
  } else if(u.metadata.type == 'update-available') {
    $.snackbar({content: 'Downloading update...'})
  }
})

socket.on(clientId, function(msg) {
  var m = JSON.parse(msg)

  if(m.metadata.type == 'version') {
    verifyVersion(msg)
  } else if(m.metadata.type == 'notification-response') {
    handleNotificationResponse(msg)
  } else {
    log.info('Unknown private message: ' + m)
  }
})

function getTestNotification() {
  return {
    metadata: {
      version: 1,
      type: 'notification',
      from: clientId,
      to: ''
    },
    payload: {
      id: 0,
      title: 'Test Notification',
      text: 'This is a test notification.',
      device: 'Zephyr Desktop Client'
    }
  }
}

function requestServerVersion() {
  socket.emit('version', JSON.stringify(getVersionInfo()))
}

function getVersionInfo() {
  return {
    metadata: {
      version: 1,
      type: 'version',
      from: clientId,
      to: ''
    },
    payload: {
      name: 'Zephyr Desktop Client',
      version: require('electron').remote.app.getVersion(),
      versionCode: 1,
      versions: []
    }
  }
}

function verifyVersion(msg) {
  var v = JSON.parse(msg)
  if (v.metadata.version == 1) {
    if(v.payload.versionCode == 1) {
      log.info('Connected to ' + v.payload.name + ' ' + v.payload.version + ' (' + v.payload.versionCode + ')')
      $.snackbar({content: 'Connected to server.'})
      $('#status').text('Connected to ' + v.payload.name + ' ' + v.payload.version)
    } else {
      log.info('Server is running incompatible version!')
      $.snackbar({content: 'Unable to connect: Server is running incompatible version.', timeout: 0})
      $('#status').text('Unable to connect: Server is running incompatible version')
    }
  } else {
    log.info('Invalid payload version!')
    $.snackbar({content: 'Unable to connect: Invalid payload version.', timeout: 0})
    $('#status').text('Unable to connect: Invalid payload version')
  }
}

function handleNotificationResponse(msg) {
  var r = JSON.parse(msg)
  if (r.metadata.version == 1) {
    if(r.payload.result) {
      log.info('Notification ' + r.payload.id + ' successfully displayed. (' + r.payload.resultCode + ' : ' + r.payload.resultMessage + ')')
      $.snackbar({content: 'Notification successfully displayed.'})
    } else {
      log.info('Notification ' + r.payload.id + ' failed to display. (' + r.payload.resultCode + ' : ' + r.payload.resultMessage + ')')
      $.snackbar({content: 'Failed to display notification. (' + r.payload.resultCode +')'})
    }
  } else {
    log.info('Invalid payload version!')
    $.snackbar({content: 'Notification response: Invalid payload version.'})
  }
}