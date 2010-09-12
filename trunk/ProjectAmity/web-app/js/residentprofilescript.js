
function init(response)
{
    $('checkPass').hide
}
function passwordChanged() {
    var strength = document.getElementById('strength');
    var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\W).*$", "g");
    var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
    var enoughRegex = new RegExp("(?=.{6,}).*", "g");
    var pwd = document.getElementById("password");
    if (pwd.value.length==0) {
        strength.innerHTML = '';
    } else if (false == enoughRegex.test(pwd.value)) {
        strength.innerHTML = 'More Characters';
    } else if (strongRegex.test(pwd.value)) {
        strength.innerHTML = '<FONT COLOR="green">Strong!</FONT>';
    } else if (mediumRegex.test(pwd.value)) {
        strength.innerHTML = '<FONT COLOR="orange">Medium!</FONT>';
    } else {
        strength.innerHTML = '<FONT COLOR="red>Weak!</FONT>';
    }
    if($F('password2') != "")
    {
        if($F('password') == $F('password2'))
        {
            $('checkPass').innerHTML= "<img src=\"../images/amity/green_tick.png\" id=\"greenTick\"/> Password matched."
        }
        else if ($F('password') != $F('password2'))
        {

            $('checkPass').innerHTML = "<img src=\"../images/amity/red_cross.png\" id=\"redCross\"/> Password does not match."
        }
    
    }
}

function identicalPassword()
{
    if($F('password') == $F('password2'))
    {
        $('checkPass').innerHTML= "<img src=\"../images/amity/green_tick.png\" id=\"greenTick\"/> Password matched."
    }
    else if ($F('password') != $F('password2'))
    {

        $('checkPass').innerHTML = "<img src=\"../images/amity/red_cross.png\" id=\"redCross\"/> Password does not match."
    }
    
}