<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>


        <title>${listing.resident.name}'s Carpool Listing</title>

        <g:javascript library="scriptaculous" />
        <g:javascript library="prototype" />

        <script type="text/javascript" src="${resource(dir: 'js', file: 'carpoolScripts.js')}" ></script>

		<meta http-equiv="content-type" content="text/html; charset=utf-8" />

                <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
                <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />

        <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
srLJJlcXUmLMTM2KkMsePdU1A"
            type="text/javascript"></script>

        <script type="text/javascript" src="${resource(dir: 'js', file: 'carpoolScripts.js')}" ></script>

	</head>
<body class="thrColFixHdr"  onload="loadCarpoolMap(${listing.startLatitude}, ${listing.startLongitude}, ${listing.endLatitude}, ${listing.endLongitude})" onunload="GUnload()">

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
            <img src="${resource(dir:'images/amity',file:'bcarpool.png')}" border="0" id="pageTitle"/>
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
    <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
  </div>
  <div id="mainContent">

  <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->





<h1>${listing.resident.name}'s Listing</h1>

    <g:if test="${listing.status == 'Fulfilled'}">
      <br/><p style="background-color: #ffc0cb">This user's listing has already been fulfilled.</p>
    </g:if>

    <br/>
    <h2>User Information</h2>
    <br/>
    <table width="80%">
      <tr>
        <td width="20%"><b>Name: </b></td>
        <td>${listing.resident.name}</td>
        <td width="60%"></td>
      </tr>
      <tr>
        <td width="20%"><b>Listing Status: </b></td>
        <td>${listing.status}</td>
        <td width="60%"></td>
      </tr>
      <tr>
        <td width="20%"><b>User ID: </b></td>
        <td>${listing.resident.userid}</td>
        <td width="60%"><g:link controller="message" action="create" params="[receiverUserID: listing.resident.userid, subject: 'Your Carpool Listing']">Send Private Message</g:link></td>
      </tr>
      <tr>
        <td width="20%"><b>Name: </b></td>
        <td>${listing.type}</td>
        <td width="60%"></td>
      </tr>
      <tr>
        <td width="20%"><b>Frequency: </b></td>
        <td>${listing.frequency}</td>
        <td width="60%"></td>
      </tr>
    </table>

    <br/>
    <h2>Journey Information</h2>
    <br/>
    <table width="80%">
      <tr>
        <td width="20%"><b>Starting Location: </b></td>
        <td>${listing.startAddress}</td>
      </tr>
      <tr>
        <td width="20%"><b>Departure Time: </b></td>
        <td>${listing.departureTime}</td>
      </tr>
      <tr>
        <td width="20%"><b> </b></td>
        <td></td>
      </tr>
      <tr>
        <td width="20%"><b> </b></td>
        <td></td>
      </tr>
      <tr>
        <td width="20%"><b>Destination Location: </b></td>
        <td>${listing.endAddress}</td>
      </tr>
      <tr>
        <td width="20%"><b>Looking for: </b></td>
        <td>${listing.returnTime}</td>
      </tr>
    </table>
    <br/>

    <h2>Route Map</h2>
    <br/>
    <div id="map" style="width: 40%; height: 350px; border: 1px solid black"></div>
    <br/>
    <h2>Driving Directions</h2>
    <br/>
    <div id="directionsPane" style="width: 40%"></div>

    </div>
	<!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats --><br
class="clearfloat" />

<!-- end #container --></div>

			<div class="push"></div>

		</div>

		<div class="footer">
			<p>Copyright &copy; 2010 Team Smiley Face</p>
		</div>
	</body>
</html>