var remote = require('electron').remote
var mixpanel = remote.require('mixpanel').init('6cae86bf1da092b800b30b27689bd665')
const Config = require('electron-config')
const conf = new Config()

$('#minimize').click(function() {
  mixpanel.track('toolbar-minimize')
  remote.getCurrentWindow().minimize()
})

$('#tray').click(function() {
  mixpanel.track('toolbar-tray')
  
  if (!conf.has('trayAlert') || conf.get('trayAlert')) {
    let notification = new Notification('Minimized to Tray', {
      body: 'Zephyr is still running in the background.\nRight click icon to resume.'
    })
    conf.set('trayAlert', false)
    closingToTray = false
  }

  remote.getCurrentWindow().close()
})

$('#close').click(function() {
  mixpanel.track('toolbar-close')

  if (process.platform !== 'darwin') {    
    remote.app.quit()
  } else {
    remote.getCurrentWindow().close()
  }
})