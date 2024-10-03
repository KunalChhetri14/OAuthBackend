/*---------------- progress bar, animated numbers,  ----------------*/
    $(document).ready(function() {

        //Animated Number
        $.fn.animateNumbers = function(stop, commas, duration, ease) {
            return this.each(function() {
                var $this = $(this);
                var start = parseInt($this.text().replace(/,/g, ""));
                commas = (commas === undefined) ? true : commas;
                $({
                    value: start
                }).animate({
                    value: stop
                }, {
                    duration: duration == undefined ? 1000 : duration,
                    easing: ease == undefined ? "swing" : ease,
                    step: function() {
                        $this.text(Math.floor(this.value));
                        if (commas) {
                            $this.text($this.text().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
                        }
                    },
                    complete: function() {
                        if (parseInt($this.text()) !== stop) {
                            $this.text(stop);
                            if (commas) {
                                $this.text($this.text().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,"));
                            }
                        }
                    }
                });
            });
        };

        $('.counting-number').bind('inview', function(event, visible, visiblePartX, visiblePartY) {
            var $this = $(this);
            if (visible) {

                $this.animateNumbers($this.data('digit'), false, $this.data('duration'));
                $this.unbind('inview');
            }
        });


function isEmail(email) {
  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
  return regex.test(email);
}


$('.contact').click(function(){  
var error = 0;
var phoneValue = $("#phone").val();

if($("#name").val()==""){
  $("#name").addClass("error");
  error =1;
}else
{
   $("#name").removeClass("error");
}

if($("#email").val()=="")
{
  $("#email").addClass("error");
  error =1;
}
else if(!isEmail($("#email").val()))
{
  $("#email").addClass("error");
  error =2;
}
else
{
   $("#email").removeClass("error");
}

if($("#phone").val()=="")
{
  $("#phone").addClass("error");
  error =1;
}
else if(isNaN(parseInt(phoneValue)) || phoneValue.length != 10)
{
  $("#phone").addClass("error");
  error =3;
}
else
{
   $("#phone").removeClass("error");
}

if($("#message").val()=="")
{
  $("#message").addClass("error");
  error =1;
}else
{
   $("#message").removeClass("error");
}

if(error)
{
   $(".alert").removeClass("successmsg");
  $(".alert").addClass("errmsg");

  if(error==2){
    $(".alert").html("Please enter a valid email address.");
    $("#email").css("border", "solid 2px red");
  }
  else if(error==3){
    $(".alert").html("Please enter a valid contact number.");
    $("#phone").css("border", "solid 2px red");
  }
  else{
    $(".alert").html("All fields are mandatory.");
  }
   $(".alert").fadeIn();

  return false;
}
else
{

  $('.contact').fadeOut();



$.ajax({
  method: "GET",
  url: "ajax.php",
  data: { req:"sendDataToContact",  frmName: $("#name").val(),  frmEmail: $("#email").val(),  frmPhone: $("#phone").val(),  frmSubs: $("#sel1").val(),  frmMessage: $("#message").val()}
  })


.done(function( msg ){
 
    if(msg){

      if(msg=="1")
      {
      
       /*
      $(".alert").addClass("successmsg");
      $(".alert").removeClass("errmsg");
      $(".alert").html("Message sent successfully.");
       $(".alert").fadeIn();

        setTimeout(hideMessageBox, 3000);

        $(".modal-title").css("display","none");
        $("#modal-body").html("<h1 style='color:#fff; text-align:center;'>\" Thank you for contacting us, we will get in touch with you soon. Meanwhile, you can shop from our online partners. \"</h1><br/><div style='text-align:center;'> <a href='http://www.flipkart.com' target='_blank' style='padding-top:25px;'><img src='images/flipkart.png' alt='flipkart'/></a> <a href='http://www.amazon.in' target='_blank'  style='padding-top:25px;'><img src='images/amazon.png' alt='amazon'/></a> </div>")*/

        window.location.href = 'thanku1.html';

      }
      else
      {
        
        $(".alert").addClass("errmsg");
        $(".alert").removeClass("successmsg");
        $(".submit").fadeIn();
        $(".alert").html("Can't send message. Please try again.");  
      }

       
            }
         

            });




}

 
});


$("#phone").focus(function(){
    $("#phone").css("border" , "solid 1px #fbcf0a");
});

$("#email").focus(function(){
    $("#email").css("border" , "solid 1px #fbcf0a");
});

var hideMessageBox = function(){

$(".enqf-in input[type=text]").val("");
$(".enqf-in textarea").val("");
  $(".contact").fadeIn();
  $(".academy").fadeIn();
    $(".alert").fadeOut();
  $(".alert").html("");
};


var hideNewsLetter = function(){

$("#subscriptionname").val("");
$("#subscriptionemail").val("");
$("#subscriptionemail").attr("placeholder","Enter Email");

 $(".alert2").fadeOut(); 
  $(".alert2").html("");
};






    });


 

