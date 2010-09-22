<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Location Based Reporting</title>
      <script src="http://api.germanium3d.com/?v=1.4&key=0c1db0e05cd88587a664a659962b25c0"></script>
      <g:javascript library="scriptaculous" />
      <g:javascript library="prototype" />
      <script type="text/javascript" src="${resource(dir: 'js', file: 'reportscript.js')}" ></script>
      <script src="http://maps.google.com/maps?file=api&amp;v=3&amp;key=ABQIAAAAd5BRV15joT1H3f6yJabmLBQ1iQbuBnc0I-d59E6wLYQh5E96wBQPHyvvuDQI6z3-Mfm_roriueiGig"
      type="text/javascript"></script>
      <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
      <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
       <resource:lightBox />
  </head>
  <body class="thrColFixHdr"  onLoad="${remoteFunction(action:'loadData',onSuccess:'Init(e)')}" onunload="GUnload()">

    <div class="wrapper">

      <div id="container">
        <a href="${createLink(controller: 'resident', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" border="0" id="logo"/></a>
        <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
        <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
        <a href="${createLink(controller: 'resident', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'home.png')}" border="0" id="home"/></a>
        <a href="${createLink(controller: 'report', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'report.png')}" border="0" id="report"/></a>
        <a href="${createLink(controller: 'carpoolListing', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'carpool.png')}" border="0" id="carpool"/></a>
        <a href="${createLink(controller: 'barter', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'barter.png')}" border="0" id="barter"/></a>
        <a href="${createLink(controller: 'report', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'breport.png')}" border="0" id="pageTitle"/></a>
        <div id="header">
          <h1>test</h1>
          <!-- end #header --></div>
        <div id="banner">&nbsp;</div>
        <div id="navi">Welcome, <a href="#">${session.user.name}</a>.&nbsp;
          <g:if test="${params.messageModuleUnreadMessages > 1}">
            You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
          </g:if>
          <g:elseif test="${params.messageModuleUnreadMessages == 1}">
            You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
          </g:elseif>
          <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="${createLink(controller: 'resident', action:'residentLogout')}" ><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
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

          <h2>
            <span id="reporthideinfo"><a href="#" onclick="reporthidemap(); return false">Hide map</a></span> |
            <span id="reportshowinfo"><a href="#" onclick="reportshowmap(); return false">Show map</a></span>
          </h2>
          <br/>
          <div id="map" style="width: 100%; height: 600px;"></div>
          <div id="reportinfo" style="width: 100%; height: 600px;">

            <h2>Outdoor Reports</h2><br/>
            <table>
              <tr><td align="center"><b>Title</b></td><td align="center"><b>Image</b></td><td align="center"><b>Description</b></td><td align="center"><b>Location</b></td><td align="center"><b>Official Feedback</b></td><td align="center"><b>Resolved Image</b></td></tr>
              <tr><td align="center">&nbsp;</td><td align="center"></td><td align="center"></td></tr>
              <g:each in="${list}" var="report">

                <tr><td width="150px"> ${report.title}<br/>Status: ${report.status}<br/><a href="individual?id=${report.id}"> Permalink</a></td><td align="center" width="150px"><richui:lightBox href="/ProjectAmity/outdoorreportimages/${report.image}"><img src="/ProjectAmity/outdoorreportimages/${report.image}" width="100"/></richui:lightBox></td><td width="200px"> ${report.description}</td><td align="center" width="150px"><img src="http://maps.google.com/maps/api/staticmap?markers=${report.latitude},${report.longitude}&zoom=12&size=130x130&sensor=false" /><td width="200px"> ${report.resolvedDescription}</td><td align="center" width="150px"><g:if test="${report.resolvedImage != null}"><richui:lightBox href="/ProjectAmity/outdoorreportimages/${report.resolvedImage}"><img src="/ProjectAmity/outdoorreportimages/${report.resolvedImage}" width="100"/></richui:lightBox></g:if></td></td></tr>
              </g:each>
            </table>
            <h2>Indoor Reports</h2><br/>
            <table>
              <tr><td align="center"><b>Title</b></td><td align="center"><b>Image</b></td><td align="center"><b>Description</b></td><td align="center"><b>Location</b></td><td align="center"><b>Official Feedback</b></td><td align="center"><b>Resolved Image</b></td></tr>
              <tr><td align="center">&nbsp;</td><td align="center"></td><td align="center"></td></tr>
              <g:each in="${indoorList}" var="report">

                <tr><td width="150px"> ${report.title}<br/>Status: ${report.status}<br/><a href="../building/individual?id=${report.id}"> Permalink</a></td><td align="center" width="150px"><richui:lightBox href="/ProjectAmity/indoorreportimages/${report.image}"><img src="/ProjectAmity/indoorreportimages/${report.image}" width="100"/></richui:lightBox></td><td width="200px"> ${report.description}</td><td align="center" width="150px"><img src="http://maps.google.com/maps/api/staticmap?markers=Singapore ${report.building.postalCode}&zoom=12&size=130x130&sensor=false" /><td width="200px"> ${report.resolvedDescription}</td><td align="center" width="150px"><g:if test="${report.resolvedImage != null}"><richui:lightBox href="/ProjectAmity/indoorreportimages/${report.image}"><img src="/ProjectAmity/indoorreportimages/${report.resolvedImage}" width="100"/></richui:lightBox></g:if></td></td></tr>
              </g:each>
            </table>
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