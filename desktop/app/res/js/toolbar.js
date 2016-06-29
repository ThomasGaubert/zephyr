var mixpanel = require('electron').remote.require('mixpanel').init('6cae86bf1da092b800b30b27689bd665')

$('#minimize').click(function() {
	mixpanel.track('toolbar-minimize')
    require('electron').remote.getCurrentWindow().minimize()
})

$('#close').click(function() {
	mixpanel.track('toolbar-close')
    require('electron').remote.getCurrentWindow().close()
})