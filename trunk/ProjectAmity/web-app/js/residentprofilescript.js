
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
        Modalbox.resizeToContent();
    } else if (strongRegex.test(pwd.value)) {
        strength.innerHTML = '<span style="color:green">Strong</span>';
        Modalbox.resizeToContent();
    } else if (mediumRegex.test(pwd.value)) {
        strength.innerHTML = '<span style="color:orange">Medium</span>';
        Modalbox.resizeToContent();
    } else {
        strength.innerHTML = '<span style="color:red">Weak</span>';
        Modalbox.resizeToContent();
    }


    if($F('password2') != "")
    {
        if($F('password') == $F('password2'))
        {   
            $('checkPass').innerHTML= "<img src=\"../images/amity/green_tick.png\" id=\"greenTick\"/> Password matched."
            Modalbox.resizeToContent();
        }
        else if ($F('password') != $F('password2'))
        {
            
            $('checkPass').innerHTML = "<img src=\"../images/amity/red_cross.png\" id=\"redCross\"/> Password does not match."
            Modalbox.resizeToContent();
        }

    }
}

function identicalPassword()
{
    if($F('password') == $F('password2'))
    {
       
        $('checkPass').innerHTML= "<img src=\"../images/amity/green_tick.png\" id=\"greenTick\"/> Password matched."
        Modalbox.resizeToContent();
    }
    else if ($F('password') != $F('password2'))
    {
       
        $('checkPass').innerHTML = "<img src=\"../images/amity/red_cross.png\" id=\"redCross\"/> Password does not match."
        Modalbox.resizeToContent();
    }

}

function checkBeforeSubmit()
{
    var errors="";
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    var address = $F('email')
    if(reg.test(address) == false) {

        errors+="Invalid Email Address.\n"
    }
    if($F('password')=="" || $F('password2')=="" )
    {
        errors += "Password cannot be blank!\n";

    }

    if($('checkPass').innerHTML == "<img src=\"" + "../images/amity/red_cross.png\"" + " id="+"\"redCross\"" + "> Password does not match.")
    {
        errors+="Password does not match.\n"
    }

    if($F('userid')=="" )
    {
        errors += "UserID cannot be blank!\n";

    }
    if($F('email')=="" )
    {
        errors += "Email cannot be blank!\n";

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
        Modalbox.resizeToContent();
    }
}
function checkEmptySecondPassword()
{
    if($F('password2')=="" )
    {
        $('checkPass').innerHTML = '<p><FONT COLOR="red">Password cannot be blank</FONT></p>'
        Modalbox.resizeToContent();
    }
}
function checkEmptyEmail()
{
    if($F('email')=="" )
    {
        $('emailField').innerHTML = '<p><FONT COLOR="red">Email cannot be blank</FONT></p>'
        Modalbox.resizeToContent();

    }
}


function changePassSuccess(response)
{
    var temp = response.responseText
    var temp2=""
    if(temp.toString() =="T")
    {
        $('mainForm').hide()
        $('loading').hide();
        $('result').innerHTML = "<center> Your account has been successfully updated.</center>"
        S('result').show()
        Modalbox.resizeToContent();

    }
    else
    {
        $('loading').hide();
        var splittedString = temp.toString().split("|")
        for(var i=0; i<splittedString.length; i++)
        {
            temp2+=splittedString[i]+"<div id=\""+i+ "\""+"></div>"
        }
        $('result').innerHTML = "<center>"+ temp2 +"</center>"

    //$('result').innerHTML = "<center>"+ temp +"</center>"
    //   alert (temp)
    }
}

function onFailVerify()
{ //resident/index
    $('resultVerification').innerHTML = "<img src=\"" + "../images/amity/red_cross.png\"" + " id="+"\"redCross\"" + "> Unable to resent."
}

function onLoadVerify()
{//resident/index
    $('resultVerification').innerHTML = "<img src=\"" + "../images/spinner.gif\"" + " id="+"\"spinner\"" + ">"
}

function onSuccessVerify()
{//resident/index
    $('resultVerification').innerHTML = "<img src=\"../images/amity/green_tick.png\" id=\"greenTick\"/>"
}

function onFailSubmit()
{ //resident/update
        $('mainForm').hide()
        $('loading').hide();
        $('result').innerHTML = "<center>We are currently unable to fulfill your request.</center>"
        S('result').show()
        Modalbox.resizeToContent();
}