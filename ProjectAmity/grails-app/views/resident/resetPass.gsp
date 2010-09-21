<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <g:javascript library="scriptaculous" />
  <g:javascript library="prototype" />

  <script type="text/javascript">
checkEmptyEmail = function ()
{
    if($F('email')=="" )
    {
       // $('emailField').innerHTML = '<p><FONT COLOR="red">Email cannot be blank</FONT></p>'
        Modalbox.resizeToContent();

    }
}
checkValidEmail = function()
{
  var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
   var address = $F('email')
   if(reg.test(address) == true) {

      $('emailField').innerHTML = "<img src=\"./images/amity/green_tick.png\" id=\"greenTick\"/> Valid email."
      Modalbox.resizeToContent();
   }else if(reg.test(address) == false)
       {
          $('emailField').innerHTML =  "<img src=\"./images/amity/red_cross.png\" id=\"redCross\"/> Invalid email."
          Modalbox.resizeToContent();
       }
}

 checkBeforeSubmit = function()
  {
    var errors="";
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
   var address = $F('email')
   if(reg.test(address) == false) {

      errors+="Invalid Email Address.\n"
   }

    if($F('nricField')=="" )
    {
        errors += "NRIC cannot be blank!\n";

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

checkNRIC = function()
{
if($F('nric')=="" )
  {
    $('nricField').innerHTML = '<p><FONT COLOR="red">NRIC cannot be blank</FONT></p>'
    return
  }

  var url = '<g:createLink action="checkNRIC"/>'
  url += '?nric=' + $F('nric')

  new Ajax.Request(url,
  {
      method: 'post',
      onSuccess: function(response)
      {
          var content = response.responseText
          if(content == 'F')
          {
              $('nricField').innerHTML = '<FONT COLOR="red">NRIC does not exist.</FONT>'
              Modalbox.resizeToContent();

          } else if (content == 'T')
            {
              $('nricField').innerHTML='&nbsp;'
              Modalbox.resizeToContent();
            }
      },
      onFailure: function(response)
{

}
}
);
}

resetSuccess = function(response)
{
var temp = response.responseText
var splittedString = temp.toString().split("|")
if(splittedString[0] == "1")
{
  
   var temp2=""
        $('loading').hide();
        var splittedString2 = temp.toString().split("|")
        for(var i=1; i<splittedString.length; i++)
        {
            temp2+=splittedString2[i]+"<div id=\""+i+ "\""+"></div>"
        }
        $('result').innerHTML = "<center>"+ temp2 +"</center>"
//alert(splittedString[1])
} else if (splittedString[0]=="2")
{
$('mainForm').hide()
 $('loading').hide();
$('result').innerHTML = splittedString[1]
$('result').show()
}
    Modalbox.resizeToContent();
}
onLoading = function()
{
  $('mainForm').hide();
  $('loading').show();
  Modalbox.resizeToContent();
}

onFailSubmit = function()
{ 
        $('mainForm').hide()
        $('loading').hide();
        $('result').innerHTML = "<center>We are currently unable to fulfill your request.</center>"
        S('result').show()
        Modalbox.resizeToContent();
}
</script>

<title></title>
</head>
<body>
        <script type="text/javascript">
       Modalbox.resizeToContent();
  $('loading').hide();
</script>
<div id="mainForm">

<g:form controller="resident">
    

                <table border="0">

    <tr>
    <td width="10%"><b>Email: </b></td>
    <td> <g:textField name="email" onblur="checkEmptyEmail(),checkValidEmail();" onfocus="" onkeyup="" /></td>
    </tr>

    <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%"><div id = "emailField">&nbsp;</div></td>
    </tr>
              <tr>
                
    <td width="10%"><b>NRIC: </b></td>
                <td><g:textField name="nric" onblur="checkNRIC()" onfocus="" onkeyup="" /></td>
              </tr>

              <tr>
    <td width="10%">&nbsp;</td>
    <td width="10%"><div id = "nricField"></div></td>
</tr>
    </table>
<g:submitToRemote value="Reset Password" url="[controller:'resident', action:'resetPassword']" onLoading="onLoading();" onFailure="onFailSubmit()" onSuccess="resetSuccess(e)" />
</g:form>
    
</div><div id="loading"><center><img id="spinner1" src="${resource(dir:'images', file:'spinner.gif')}" alt="Loading" /> Password is being reset..</center></div><div id="result"></div>
</body>
</html>
