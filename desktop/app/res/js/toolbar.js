var mixpanel = require('electron').remote.require('mixpanel').init('6cae86bf1da092b800b30b27689bd665')
const Config = require('electron-config')
const conf = new Config()

$('#minimize').click(function() {
  mixpanel.track('toolbar-minimize')
  require('electron').remote.getCurrentWindow().minimize()
})

$('#close').click(function() {
  mixpanel.track('toolbar-close')
  require('electron').remote.getCurrentWindow().close()
})

require('electron').remote.getCurrentWindow().on('close', function () {
  if (!conf.has('trayAlert') || conf.get('trayAlert')) {
    let notification = new Notification('Minimized to Tray', {
      body: 'Zephyr is still running in the background.\nRight click icon to resume.'
    })
    conf.set('trayAlert', false)
  }
})