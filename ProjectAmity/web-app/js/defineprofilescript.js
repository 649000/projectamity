
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
        strength.innerHTML = '<span style="color:green">Strong</span>';
    } else if (mediumRegex.test(pwd.value)) {
        strength.innerHTML = '<span style="color:orange">Medium</span>';
    } else {
        strength.innerHTML = '<span style="color:red">Weak</span>';
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

function checkBeforeSubmit()
{
    var errors="";
    if($F('password')=="" || $F('password2')=="" )
    {
        errors += "Password cannot be blank!\n";

    }

    if($F('userid')=="" )
    {
        errors += "UserID cannot be blank!\n";

    }
    if($F('photo')=="" )
    {
        errors += "Image cannot be blank!\n";

    }
    if(errors =="")
        return true;
    else {
        alert(errors);
        return false;
    }
}

function checkEmptyFirstPassword()
{
    if($F('password')=="" )
    {
        $('strength').innerHTML = '<p><FONT COLOR="red">Password cannot be blank</FONT></p>'
    }
}
function checkEmptySecondPassword()
{
   if($F('password2')=="" )
    {
        $('checkPass').innerHTML = '<p><FONT COLOR="red">Password cannot be blank</FONT></p>'
    }
}
function checkEmptyPhoto()
{
    if($F('photo')=="" )
    {
        $('photoField').innerHTML = '<p><FONT COLOR="red">Image cannot be blank</FONT></p>'

    }
}
