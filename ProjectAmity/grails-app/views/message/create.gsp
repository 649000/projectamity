<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Compose Message</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript" src="${resource(dir: 'js', file: 'messageScripts.js')}" ></script>
    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>

  </head>

  <body onload="initialiseCreateMessagePage()" class="thrColFixHdr">

    <div class="wrapper">

    <div id="container">
      <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" id="logo"/>
      <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
      <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
      <img src="${resource(dir:'images/amity',file:'home.png')}" id="home"/>
      <a href="${createLink(controller: 'report', action:'index')}" >
      <img src="${resource(dir:'images/amity',file:'report.png')}" border="0" id="report"/></a>
      <a href="${createLink(controller: 'carpoolListing', action:'index')}" >
      <img src="${resource(dir:'images/amity',file:'carpool.png')}" border="0" id="carpool"/></a>
      <img src="${resource(dir:'images/amity',file:'bmessage.png')}" border="0" id="pageTitle"/>

      <div id="header">
        <h1>test</h1>
        <!-- end #header -->
      </div>

      <div id="banner">
        &nbsp;
      </div>

      <div id="navi">Welcome, <a href="#">${session.user.name}</a>.&nbsp;
        <g:if test="${params.messageModuleUnreadMessages > 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
        </g:if>
        <g:elseif test="${params.messageModuleUnreadMessages == 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
        </g:elseif>
        <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
      </div>

      <div id="mainContent">

        <h3><a href="${createLink(controller: 'message', action:'index')}">Inbox</a> | <a href="${createLink(controller: 'message', action:'sent')}">Sent Messages</a></h3>

        <h1>Compose a Message</h1>

        <div id="sendMessageStatus"><br/>${params.errors}<br/></div>

        <g:form>
          <table width="80%">
            <tr>
              <td width="10%"><b>To: </b></td>
              <td width="40%"><g:textField name="receiverUserID" value="${params.receiverUserID}" size="25" onfocus="toggleControl('createMessageToHelpText')" onblur="toggleControl('createMessageToHelpText')" /></td>
              <td><span id="createMessageToHelpText">Enter the recipient's User ID.</span></td>
            </tr>
            <tr>
              <td width="10%"><b> </b></td>
              <td width="40%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td width="10%"><b>Subject: </b></td>
              <td width="40%"><g:textField name="subject" value="${params.subject}" size="40" maxlength="100" /></td>
              <td></td>
            </tr>
            <tr>
              <td width="10%"><b> </b></td>
              <td width="40%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td colspan="3"><b>Your Message: </b></td>
            </tr>
            <tr>
              <td width="10%"><b> </b></td>
              <td width="40%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td width="10%"><b> </b></td>
              <td width="40%"><g:textArea name="message" value="${params.message}" rows="5" cols="30" maxlength="3000" /></td>
              <td></td>
            </tr>
            <tr>
              <td width="10%"><b> </b></td>
              <td width="40%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td colspan="3"><g:actionSubmit value="Send" action="send" /></td>
            </tr>
          </table>
        </g:form>

      </div>
      <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats -->
      <br class="clearfloat" />
      <!-- end #container -->
    </div>

    <div class="push"></div>

    <!--end wrapper-->
    </div>

    <div class="footer">
      <p>Copyright &copy; 2010 Team Smiley Face</p>
    </div>

  </body>

</html>