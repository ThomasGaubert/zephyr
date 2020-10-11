var scrollThreshold = 0;
var wavesHeaderPinned = false;

$(document).ready(function() {
  calculateScrollThreshold();
});

$(window).scroll(function() {
  onScroll($(window).scrollTop());
});

$(window).resize(function() {
  calculateScrollThreshold();
});

function calculateScrollThreshold() {
  scrollThreshold = $('#waves-container').offset().top;
  onScroll($(document).scrollTop());
}

function onScroll(scrollPosition) {
  var wavesContainer = $('#waves-container');
  var wavesHeader = $('#waves-header');

  if (wavesContainer.css('display') === 'none') {
    // Skip if waves container is disabled.
    return;
  }

  if (scrollPosition >= scrollThreshold) {
    wavesHeader.addClass('waves-container-pinned');
    wavesContainer.height(wavesHeader.height());
    wavesHeaderPinned = true;
  } else {
    wavesHeader.removeClass('waves-container-pinned');
    wavesContainer.height(wavesHeader.height());
    scrollThreshold = wavesContainer.offset().top;
    wavesHeaderPinned = false;
  }

  var fadeInThreshold = scrollThreshold - 100;
  var opacity = 0;

  if (scrollPosition < fadeInThreshold) {
    opacity = 0;
  } else if (scrollPosition >= scrollThreshold) {
    opacity = 1;
  } else {
    opacity = (scrollPosition - fadeInThreshold) / 100;
  }

  // Set opacity of page elements as user scrolls down page
  // 0 - at top of page
  // 1 - past threshold
  if (opacity >= 0 && opacity <= 1) {
    $('#header-content').children().css({ opacity: opacity });
    $('#waves-footer').css({ opacity: 1 - opacity });
    $('#hero-banner').css({ opacity: 1 - opacity });
  }
}