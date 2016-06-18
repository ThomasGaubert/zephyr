var os = require('os');
if (os.platform() != 'win32') {
  $('#reveal-check-for-updates').hide()
}

$('.app-version').text(require('electron').remote.app.getVersion)

const reveal = document.getElementById('reveal-effect')
document.getElementById('menu').addEventListener('click', toggleReveal)

function toggleReveal() {
  if (reveal.scaled) {
    $("body").css("background-color", "#FFFFFF")
    reveal.style.width = '0px'
    reveal.style.height = '0px'
    reveal.scaled = false

    $('body').unbind('mousewheel')
    
    setTimeout(
      function() {
        if(!reveal.scaled) {
          $(".app-icon").css('visibility','hidden')
        }
      }, 50)

    setTimeout(
      function() {
        if(!reveal.scaled) {
          $(".reveal-container").hide()
        }
      }, 150)
  } else {
    const centerX = document.documentElement.clientWidth / 2
    const centerY = document.documentElement.clientHeight / 2
    const fullDiameter = Math.sqrt(Math.pow(centerX, 2) + Math.pow(centerY, 2)) * 2
    reveal.style.width = `${fullDiameter}px`
    reveal.style.height = `${fullDiameter}px`
    reveal.scaled = true

    $('body').on({
        'mousewheel': function(e) {
            if (e.target.id == 'el') return
            e.preventDefault()
            e.stopPropagation()
         }
    })

    $(".reveal-container").show()

    setTimeout(
      function() {
        if(reveal.scaled) {
          $(".app-icon").css('visibility','visible')
        }
      }, 45)

    setTimeout(
      function() {
        if(reveal.scaled) {
          $("body").css("background-color", "#0D253A")
        }
      }, 250)
  }
}

document.getElementById('reveal-open-dev-tools').addEventListener('click', function() {
  toggleReveal()
  setTimeout(
      function() {
        $(".dev-tools").toggle()
      }, 250)
})

document.getElementById('reveal-check-for-updates').addEventListener('click', function() {
  toggleReveal()
  setTimeout(
      function() {
        require('electron').remote.autoUpdater.checkForUpdates()
      }, 250)
})

document.getElementById('reveal-close').addEventListener('click', function() {
  toggleReveal()
})