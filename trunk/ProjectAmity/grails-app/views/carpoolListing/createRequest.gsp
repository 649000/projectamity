<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Generate Carpool Request</title>

    <g:if test="${!session.user}">
        <%
          response.setStatus(301);
          response.setHeader( "Location", "/ProjectAmity" );
          response.setHeader( "Connection", "close" );
        %>
    </g:if>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript">

      composeMailValidateFields = function()
      {
        var message = $('requestMessage').value
        var proceed = true

        if( trim(message).length == 0 )
        {
          proceed = false
        }

        if( !proceed )
        {
          alert('Please write a message to go along with your carpool request.')
          return false
        }
        else
        {
          return true
        }
      }

      trim = function(str, chars)
      {
        return ltrim(rtrim(str, chars), chars);
      }

      ltrim = function(str, chars)
      {
              chars = chars || "\\s";
              return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
      }

      rtrim = function(str, chars)
      {
              chars = chars || "\\s";
              return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
      }

      composeRequestFormSent = function(response)
      {
        if(response!= null || response.responseText != null)
        {
          var content = response.responseText

          if( content == 'T' )
          {
            $('sendingRequestContainer').hide()
            $('composeRequestContainer').hide()
            $('sendingRequestSuccess').show()
            Modalbox.resizeToContent()
          }
          else if( content == 'F' )
          {
            $('sendingRequestContainer').hide()
            $('composeRequestContainer').show()
            alert('An error occured while we tried to send your message. Please try again soon!')
            Modalbox.resizeToContent()
          }
          else
          {
            $('sendingRequestContainer').hide()
            $('composeRequestContainer').show()
            alert(content)
            Modalbox.resizeToContent()
          }
        }
        else
        {
          $('sendingRequestContainer').hide()
          $('composeRequestContainer').show()
          alert('An error occured while we tried to send your carpool request. Please try again soon!')
          Modalbox.resizeToContent()
        }

        composeRequestFormSending = function()
        {
          $('composeRequestContainer').hide()
          $('sendingRequestContainer').show()
          Modalbox.resizeToContent()
        }

      }

    </script>

    <style type="text/css">
      body
      {
        font: 14px "Myriad Pro", "Trebuchet MS", "Helvetica Neue", Helvetica, Arial, Sans-Serif;
        line-height: 1.5;
        color: #333;
        background: #FFF;
        margin: 0; /* it's good practice to zero the margin and padding of the body element to account for differing browser defaults */
        padding: 0;
        color: #000000;
        height: 100%;
      }
    </style>

  </head>

  <body>

    <g:if test="${session.user}">

    <div id="sendingRequestContainer" style="text-align: center">
      <h1>Sending your request</h1>
      <h3>Please wait...</h3>
    </div>

    <div id="sendingRequestSuccess" style="text-align: center">
      <h1>Success!</h1>
      <p><b>Your carpool request has been successfully sent to ${params.recipientName} for him/her to accept or decline.</b></p>
      <br/>
      <p><b><a href="#" onclick="Modalbox.hide(); return false">OK</a></b></p>
    </div>

    <script type="text/javascript">
      document.getElementById('sendingRequestContainer').hide()
      document.getElementById('sendingRequestSuccess').hide()
      Modalbox.resizeToContent()
    </script>

    <div id="composeRequestContainer">
      <h1 id="composeMessageHeader">New Carpool Request</h1>
      <br/>
      <g:formRemote name="composeMessageForm" url="[action: 'ajaxSendRequest']"
                    onLoading="composeRequestFormSending()"
                    onSuccess="composeRequestFormSent(e)"
                    onFailure="composeRequestFormSent(e)">
        <table width="90%">
          <tr>
            <td><b>Message for ${params.recipientName}:</b></td>
            <input type="hidden" name="receiverUserID" value="${params.recipientUserid}" />
            <input type="hidden" name="subject" value="${params.subject}" />
            <input type="hidden" name="listingId" value="${params.listingId}" />
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td width="100%"><g:textArea id="requestMessage" name="message" value="${params.message}" rows="5" cols="50" maxlength="3000" /></td>
          </tr>
          <tr>
            <td><em>You may wish to add more details such as more specific requests and points for discussion. No HTML tags are allowed.</em></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td style="text-align: center">
                <input type="submit" value="Send Request" onclick="return composeMailValidateFields()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="Cancel" onclick="Modalbox.hide(); return false" />
            </td>
          </tr>
        </table>

      </g:formRemote>

    </div>

    </g:if>

    <g:else>
      <h1>Sorry, you have to log in to view content in this page.</h1>
    </g:else>

  </body>

</html>