function setup () {
  var log = require('electron-log')
  log.appName = 'zephyr'

  return log
}


module.exports = {
  setup: setup
}