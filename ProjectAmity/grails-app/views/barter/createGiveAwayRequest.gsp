<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
    <script type="text/javascript">
sendRequest = function () {
      var a=$('loggedInUserId').value
  var b=$('resident').value
  var c=$('itemId').value
  var d='Trade with items'
${remoteFunction(action:'createRequest', onSuccess:'changeDivMessage()', params:'\'partyone=\'+a+\'&partytwo=\'+b+\'&involvedItems=\'+c+\'&barterAction=\'+d+\'\'')}
}

changeDivMessage = function()
{
  var html=""
  html+="<i>Request successfully sent!</i>"
  html+="<br/>This box will close automatically in 2 seconds or click <a href=\"#\" onclick\"Modalbox.hide()\">"
  html+="here</a> to close."
  $('sendPanel').innerHTML=html

  var t=setTimeout("Modalbox.hide()",2000);
}
      </script>
  </head>
  <body>
<g:hiddenField name="itemName" value="${params.loggedInUserId}" id="loggedInUserId" />
  <g:hiddenField name="resident" value="${params.residentId}" id="resident" />
  <g:hiddenField name="itemId" value="${params.itemId}" id="itemId" />
<div id="sendPanel">
    <h2>Are you sure you want to request for this free item?</h2>
<g:actionSubmit value="Yes" onclick="sendRequest()"/><g:actionSubmit value="No" onclick="Modalbox.hide()"/>
</div>
  </body>
</html>