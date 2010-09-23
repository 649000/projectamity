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
  var c=$('message').value
  var d=$('barteraction').value
  var e=$('partyonename').value
  var f=$('partytwoname').value
  var g=$('involvedItems').value
  var h=$('requestid').value
${remoteFunction(action:'acceptRequest', onSuccess:'changeDivMessage()', params:'\'partyone=\'+a+\'&partytwo=\'+b+\'&message=\'+c+\'&partyonename=\'+e+\'&partytwoname=\'+f+\'&involveditems=\'+g+\'&requestid=\'+h+\'&barterAction=\'+d+\'\'')}
}

changeDivMessage = function()
{
  var html=""
  html+="<i>Message successfully sent!</i>"
  html+="<br/>This box will close automatically in 2 seconds or click <a href=\"#\" onclick\"Modalbox.hide()\">"
  html+="here</a> to close."
  $('sendPanel').innerHTML=html

  var t=setTimeout("Modalbox.hide()",2000);
}
    </script>
  </head>
  <body>
    <div id="sendPanel">
      <g:hiddenField name="itemName" value="${params.partyone}" id="loggedInUserId" />
  <g:hiddenField name="resident" value="${params.partytwo}" id="resident" />
  <g:hiddenField name="involvedItems" value="${params.involveditems}" id="involvedItems" />
  <g:hiddenField name="itemId" value="${params.barteraction}" id="barteraction" />
  <g:hiddenField name="message" value="${params.message}" id="message" />
  <g:hiddenField name="partyonename" value="${params.partyonename}" id="partyonename" />
  <g:hiddenField name="partytwoname" value="${params.partytwoname}" id="partytwoname" />
  <g:hiddenField name="requestid" value="${params.requestid}" id="requestid" />
      <h2>Are you sure that you want to ACCEPT this request?</h2>
      <u><b><i>Changed can't be undone.</i></b></u>
      <br/>
      Enter a message to confirm:
      <g:textArea rows="6" cols="72" name="message">${params.message}</g:textArea>
      <br/><g:actionSubmit value="Confirm accept request" onclick="sendRequest()"/>
      <g:actionSubmit value="Cancel" onclick="Modalbox.hide()"/>
    </div>
  </body>
</html>
