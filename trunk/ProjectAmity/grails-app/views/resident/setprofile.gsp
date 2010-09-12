<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Define your Profile</title>
  <g:javascript library="scriptaculous" />
  <g:javascript library="prototype" />
  <script type="text/javascript" src="${resource(dir: 'js', file: 'defineprofilescript.js')}" ></script>

  <script type="text/javascript">
    function checkUsername()
{
  if($F('userid')=="" )
    {
      $('checkUserID').innerHTML = '<p><FONT COLOR="red">Username cannot be blank</FONT></p>'
      return
    }

    var url = '<g:createLink action="checkUser"/>'
    url += '?userid=' + $F('userid')

    new Ajax.Request(url,
    {
        method: 'post',
        onSuccess: function(response)
        {
            var content = response.responseText
            if(content == 'T')
            {
                $('checkUserID').innerHTML = '<p><FONT COLOR="green">Username is available.</FONT></p>'
            }
            else
            {
                $('checkUserID').innerHTML = '<p><FONT COLOR="red">Username is taken.</FONT></p>'
            }
        },
        onFailure: function(response)
        {
            
        }
    }
    );
}

  </script>


</head>
<body>
<g:form controller="resident">
  User ID: <g:textField name="userid" onblur="checkUsername();" onfocus=""/><div id = "checkUserID"></div><br/>
  Password: <g:passwordField name="password" onkeyup="return passwordChanged();" onblur="checkEmptyFirstPassword();"/><div id = "strength"></div><br/>
  Password again: <g:passwordField name="password2" onkeyup="return identicalPassword();" onblur="checkEmptySecondPassword();"/><div id = "checkPass"></div><br/>
  Image:<g:textField name="photo" onblur="checkEmptyPhoto();" onfocus=""/><div id = "photoField"></div><br/>
  <g:actionSubmit value="Update" action="initAccount" onclick="return checkBeforeSubmit()" />
</g:form>
</body>
</html>
