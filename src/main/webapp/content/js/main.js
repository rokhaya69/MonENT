$(document).ready(function () {
  $nav = $('.nav');
  $toggleCollapse = $('.toggle-collapse');

  /**Click event on toggle menu */
  $toggleCollapse.click(function () {
    $nav.toggleClass('collapse');
  });

  //Owl Carousel for blog
  $owl = $('.owl-carousel');
  $owl.owlCarousel({
    loop: true,
    autoplay: false,
    autoplayTimeout: 3000,
    dots: false,
    nav: true,
    navText: [$('.owl-navigation .owl-nav-prev'), $('.owl-navigation .owl-nav-next')],
  });

  //click to scroll top
  $('.mo span').click(function () {
    $('html, body').animate(
      {
        scrollTop: 0,
      },
      1000
    );
  });
});
