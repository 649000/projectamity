<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Deactivate Carpool Listing</title>

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
            alert('You cannot deactivate someone else\'s carpool listing!')
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
          alert('An error occured while we tried to deactivate your listing. Please try again soon!')
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
      <h1>Deactivating Your Listing</h1>
      <h3>Please wait...</h3>
    </div>

    <div id="sendingRequestSuccess" style="text-align: center">
      <h1>Success!</h1>
        <p><b>Your carpool listing has been deactivated.</b></p>
      <br/>
      <p><b><a href="#" onclick="Modalbox.hide(); return false">OK</a></b></p>
    </div>

    <script type="text/javascript">
      document.getElementById('sendingRequestContainer').hide()
      document.getElementById('sendingRequestSuccess').hide()
      Modalbox.resizeToContent()
    </script>

    <div id="composeRequestContainer">
      <h1 id="composeMessageHeader">Deactivate Carpool Listing</h1>
      <br/>
      <g:formRemote name="composeMessageForm" url="[action: 'ajaxDeactivateListing']"
                    onLoading="composeRequestFormSending()"
                    onSuccess="composeRequestFormSent(e)"
                    onFailure="composeRequestFormSent(e)">
        <table width="90%">
          <tr>
            <input type="hidden" name="listingId" value="${params.listingId}" />
          </tr>
          <tr>
            <td width="100%">Are you sure you wish to deactivate the carpool listing for the journey from ${params.startAddress} to ${params.endAddress}?<br/><br/>Deactivating a carpool listing is <b><font color="red">irreversible</font></b>.</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td style="text-align: center">
                <input type="submit" value="Yes, deactivate it now." />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" value="No, leave it as it is." onclick="Modalbox.hide(); return false" />
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