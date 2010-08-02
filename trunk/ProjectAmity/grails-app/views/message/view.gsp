<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>View Message from ${message.sender.name}</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript" src="${resource(dir: 'js', file: 'messageScripts.js')}" ></script>
    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>

  </head>

  <body class="thrColFixHdr">

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

        <h1>Subject: ${message.subject}</h1>

        <br/>

        <table width="80%">
          <tr>
            <td width="10%"><b>From:</b></td>
            <td width="30%">${message.sender.name}</td>
            <td>(${message.sender.userid})</td>
          </tr>
          <tr>
            <td width="10%"><b>To:</b></td>
            <td width="30%">${message.receiver.name}</td>
            <td>(${message.receiver.userid})</td>
          </tr>
          <tr>
            <td width="10%"><b>Subject:</b></td>
            <td width="30%">${message.subject}</td>
            <td></td>
          </tr>
          <tr>
            <td width="10%"></td>
            <td width="30%">&nbsp;</td>
            <td></td>
          </tr>
          <tr>
            <td colspan="3"><b>On ${message.timeStamp}, ${message.sender.name} wrote:</b></td>
          </tr>
          <tr>
            <td width="10%"></td>
            <td width="30%">&nbsp;</td>
            <td></td>
          </tr>
          <tr>
            <td colspan="3">${message.message}</td>
          </tr>
        </table>

        <br/>
        <g:if test="${session.user.userid == message.receiver.userid}">
          <p><g:link action="create" params="[receiverUserID: message?.sender.userid, subject: 'Re: ' + message.subject , message: '\n\n\nOn ' + message.timeStamp + ', ' + message.sender.name + ' wrote: \n\n' + message.message]">Reply</g:link></p>
        </g:if>
        <g:elseif test="${session.user.userid == message.sender.userid}">
            <p><g:link action="create" params="[receiverUserID: message?.receiver.userid, subject: 'Re: ' + message.subject , message: '\n\n\nOn ' + message.timeStamp + ', ' + message.sender.name + ' wrote: \n\n' + message.message]">Reply</g:link></p>
        </g:elseif>

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