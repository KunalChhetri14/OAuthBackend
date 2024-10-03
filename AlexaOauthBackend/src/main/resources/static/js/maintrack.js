


/* ---------------------	
		BACK TO TOP JS START
/* --------------------- */	
if ($('#back-to-top').length) {
    var scrollTrigger = 100, // px
        backToTop = function () {
            var scrollTop = $(window).scrollTop();
            if (scrollTop > scrollTrigger) {
                $('#back-to-top').addClass('show');
            } else {
                $('#back-to-top').removeClass('show');
            }
        };
    backToTop();
    $(window).on('scroll', function () {
        backToTop();
    });
    $('#back-to-top').on('click', function (e) {
        e.preventDefault();
        $('html,body').animate({
            scrollTop: 0
        }, 700);
    });
}

/* ---------------------	
		WOW ANIMTED EFFECT START
/* --------------------- */

    new WOW().init();
	
/* ---------------------	
		WOW ANIMTED EFFECT END
/* --------------------- */	

/* ---------------------	
		STICKY NAV BAR TO TOP JS START
/* --------------------- */	
$(document).ready(function() {
var stickyNavTop = $('.menuTop').offset().top;
 
var stickyNav = function(){
var scrollTop = $(window).scrollTop();
      
if (scrollTop > stickyNavTop) { 
    $('.menuTop').addClass('sticky');
} else {
    $('.menuTop').removeClass('sticky'); 
}
};
 
stickyNav();
 
$(window).scroll(function() {
    stickyNav();
});
});





