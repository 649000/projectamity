<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Indoor Reports for Postal Code: ${params.postalCode}</title>
    <script src="http://api.germanium3d.com/?v=1.4&key=cxa2010"></script>
    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />
    <script type="text/javascript" src="${resource(dir: 'js', file: 'germaniumscript.js')}" ></script>

    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
  </head>

  <body class="thrColFixHdr"   onLoad="${remoteFunction(action:'loadBuilding',onSuccess:'InitGermanium(e)')}">

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
        <img src="${resource(dir:'images/amity',file:'breport.png')}" border="0" id="pageTitle"/>
        <div id="header">
          <h1>test</h1>
          <!-- end #header --></div>
        <div id="banner">&nbsp;</div>
        <div id="navi">Welcome, <a href="../resident">${session.user.name}</a>.&nbsp;
          <g:if test="${params.messageModuleUnreadMessages > 1}">
            You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
          </g:if>
          <g:elseif test="${params.messageModuleUnreadMessages == 1}">
            You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
          </g:elseif>
          <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
        </div>
        <div id="mainContent">

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->



          <div style="height:600px; width: 960px;">
            <div id="myGerm" style="width: 69.8%; height: 600px;float:left;"></div>
          <div id="test" style="overflow: auto; width: 27.5%; float:right;height: 578px; border:solid 1px #d7d7d7; background-color:#f7f7f7; padding: 10px;"</div>
          </div>


        </div>
        <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats --><br class="clearfloat" />
        <!-- end #container --></div>

      <div class="push"></div>


      <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
    </div>

    <div class="footer">
      <p>Copyright &copy; 2010 Team Smiley Face</p>
    </div>
  </body>
</html>