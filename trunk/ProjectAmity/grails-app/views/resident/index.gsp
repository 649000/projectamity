<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>User Panel</title>
  <g:javascript library="scriptaculous" />
  <g:javascript library="prototype" />
  <script type="text/javascript" src="${resource(dir: 'js', file: 'residentprofilescript.js')}" ></script>
</head>
<body>
<g:form controller="resident">
  NRIC: ${session.user.nric}
  Name:${session.user.name}
  Address:${session.user.address} Singapore ${session.user.postalCode} <br/>

  Password: <g:passwordField name="password" onkeyup="return passwordChanged();"/><div id = "strength"></div><br/>
  Password again: <g:passwordField name="password2" onkeyup="return identicalPassword();"/><div id = "checkPass"></div><br/>
  <g:actionSubmit value="Update" action="changePassword" onclick="return checkBeforeSubmit()" />
</g:form>

</body>
</html>
