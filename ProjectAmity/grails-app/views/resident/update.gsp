<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
  <g:javascript library="scriptaculous" />
  <g:javascript library="prototype" />
  <growler:resources />
  <script type="text/javascript" src="${resource(dir: 'js', file: 'residentprofilescript.js')}" ></script>

  <script type="text/javascript">
checkValidEmail = function()
{
    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
    var address = $F('email')
    var url = '<g:createLink action="checkEmailUpdate"/>'
    url += '?email=' + $F('email')

    if(reg.test(address) == true) {

           new Ajax.Request(url,
    {
        method: 'post',
        onSuccess: function(response)
        {
            var content = response.responseText
            if(content == 'F')
            {
                $('emailField').innerHTML = '<FONT COLOR="red">Email already exist in system.</FONT>'
                Modalbox.resizeToContent();

            } else if (content == 'T')
{
                $('emailField').innerHTML = "<img src=\"../images/amity/green_tick.png\" id=\"greenTick\"/> Valid email."
                Modalbox.resizeToContent();
            }
        },
        onFailure: function(response)
        {

        }
    }
    );


    } else if(reg.test(address) == false)
{
        $('emailField').innerHTML =  "<img src=\"../images/amity/red_cross.png\" id=\"redCross\"/> Invalid email."
    }


}
onLoading = function()
{
  $('mainForm').hide();
  $('loading').show();
  Modalbox.resizeToContent();
}

    </script>
</head>

<body>
    <script type="text/javascript">
  $('loading').hide();
</script>
  <div id="mainForm">
<g:form controller="resident">
  <table border="0">
    <tr>
      <td width="10%"><b>Password: </b></td>
      <td><g:passwordField name="password" onkeyup="return passwordChanged();" onblur="checkEmptyFirstPassword();"/></td>
    </tr>

    <tr>
      <td width="10%">&nbsp;</td>
      <td width="10%"><div id = "strength"></div></td>
    </tr>
        <tr>
      <td width="10%"><b>Password again: </b></td>
      <td><g:passwordField name="password2" onkeyup="return identicalPassword();" onblur="checkEmptySecondPassword();"/></td>
    </tr>

    <tr>
      <td width="10%">&nbsp;</td>
      <td width="10%"><div id = "checkPass"></div></td>
    </tr>
    <tr>
      <td width="10%"><b>Email: </b></td>
      <td><g:textField name="email" onblur="checkEmptyEmail(),checkValidEmail();" onfocus="" onkeyup="" value="${session.user.email}"/></td>
    </tr>

    <tr>
      <td width="10%">&nbsp;</td>
      <td width="10%"><div id = "emailField"></div></td>
    </tr>
  </table>
  <g:submitToRemote value="Update" url="[controller:'resident', action:'changePassword']" onLoading="onLoading();" onFailure="onFailSubmit()" onSuccess="changePassSuccess(e)"/>
</g:form>
  </div>
  <div id="loading"><center><img id="spinner1" src="${resource(dir:'images', file:'spinner.gif')}" alt="Loading" /> Updating your account details..</center></div><div id="result"></div>
</body>
</html>
