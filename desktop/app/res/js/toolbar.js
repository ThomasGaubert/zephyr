$('#minimize').click(function() {
    require('electron').remote.getCurrentWindow().minimize()
})

$('#close').click(function() {
    require('electron').remote.getCurrentWindow().close()
})