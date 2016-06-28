function setup () {
  var log = require('electron-log')
  log.appName = 'zephyr'

  console.log = function (msg) {
    process.stdout.write(msg + require('os').EOL)
  }

  return log
}

module.exports = {
  setup: setup
}