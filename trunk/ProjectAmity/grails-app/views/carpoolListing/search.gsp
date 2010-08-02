<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Carpool Listing Search</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript" src="${resource(dir: 'js', file: 'carpoolScripts.js')}" ></script>
    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>

  </head>

  <body class="thrColFixHdr" onload="initialiseSearchPage()">

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

      <div id="mainContent" style="height: 700px">

        <h3><a href="${createLink(controller: 'carpoolListing', action:'index')}">Define Your Listing</a> | <a href="${createLink(controller: 'carpoolListing', action:'search')}">Search for Listings</a></h3>

        <div id="map" style="width: 10px; height: 10px"></div>

        <div id="searchPane" style="width: 30%; float: left">

          <resource:autoComplete skin="default" />
          <h1>Carpool Listing Search</h1>

          <br/>
          <p>Enter only those attributes you wish to search by.</p>

          <g:form>
            <br/>
            <b>Destination Location:</b>
            <br/>
            <br/>
            <richui:autoComplete name="endAddress" action="${createLinkTo('dir': 'carpoolListing/searchAJAX')}" forceSelection="true"  value="${params.endAddress}" />
            <br/>
            <b>Departure Time:</b>
            <br/>
            <br/>
            <p>Between <g:select name="departureTimeFrom" from="${['0000', '0100', '0200', '0300', '0400', '0500', '0600', '0700', '0800', '0900', '1000', '1100', '1200', '1300', '1400', '1500', '1600', '1700', '1800', '1900', '2000', '2100', '2200', '2300']}" value="${params.departureTimeFrom}"
            noSelection="['':'HHMM']"/> and <g:select name="departureTimeTo" from="${['0000', '0100', '0200', '0300', '0400', '0500', '0600', '0700', '0800', '0900', '1000', '1100', '1200', '1300', '1400', '1500', '1600', '1700', '1800', '1900', '2000', '2100', '2200', '2300']}" value="${params.departureTimeTo}"
            noSelection="['':'HHMM']"/></p>
            <br/>
            <b>Return Time:</b>
            <br/><br/>
            <p>Between <g:select name="returnTimeFrom" from="${['0000', '0100', '0200', '0300', '0400', '0500', '0600', '0700', '0800', '0900', '1000', '1100', '1200', '1300', '1400', '1500', '1600', '1700', '1800', '1900', '2000', '2100', '2200', '2300']}" value="${params.returnTimeFrom}"
            noSelection="['':'HHMM']"/> and <g:select name="returnTimeTo" from="${['0000', '0100', '0200', '0300', '0400', '0500', '0600', '0700', '0800', '0900', '1000', '1100', '1200', '1300', '1400', '1500', '1600', '1700', '1800', '1900', '2000', '2100', '2200', '2300']}" value="${params.returnTimeTo}"
            noSelection="['':'HHMM']"/></p>
            <br/>
            <b>Frequency: </b>
            <br/>
            <br/>
            <g:select name="frequency" from="${['Weekdays', 'Weekends']}" value="${params.frequency}"
            noSelection="['':'Select a Frequency']"/>
            <br/>
            <br/>
            <b>Looking for: </b>
            <br/>
            <br/>
            <g:select name="type" from="${['Driver', 'Passenger', 'Cab Pool']}" value="${params.type}"
            noSelection="['':'Who Are You Seeking?']"/>
            <br/>
            <br/>
<!--            <g:submitToRemote url="[controller: 'CarpoolListing', action:'listPlaces']"
                              value="Search"
                              onSuccess="parseResults(e)" />-->
            <g:actionSubmit value="Search" action="search" />
          </g:form>

        </div>

        <div id="searchResults" style="width: 60%; float: right">

          <g:if test="${listings}">
            <g:each in="${listings}" var="l">
              <table>
                <tr>
                  <td colspan="2">
                    <h2><g:link controller="carpoolListing" action="view" params="[id: l?.resident.id]">${l?.resident.name}</g:link></h2>
                  </td>
                </tr>
                <tr>
                  <td width="70%">
                  <p>Starting Location: ${l?.startAddress}<br/>
                  Leaving Starting Location at: ${l?.departureTime}<br/>
                  Destination Location: ${l?.endAddress}<br/>
                  Returning to Starting Location at: ${l?.returnTime}</p>
                  <p>Frequency: ${l.frequency}<br/>
                  Looking for: ${l.type}</p>
                  </td>
                  <td width="30%">
                    <img src="http://maps.google.com/maps/api/staticmap?size=200x200&markers=color:green|label:A|${l.startLatitude},${l.startLongitude}&markers=color:red|label:B|${l.endLatitude},${l.endLongitude}&path=${l.startLatitude},${l.startLongitude}|${l.endLatitude},${l.endLongitude}&sensor=false" alt="Journey Map" title="Journey Map" />
                  </td>
                </tr>
              </table>
            </g:each>

            <br/>
            <g:paginate total="${params.totalResults}" controller="carpoolListing" action="search" params="[ surroundingDestinations : params.surroundingDestinations , endAddress: params.endAddress, departureTimeFrom : params.departureTimeFrom, departureTimeTo : params.departureTimeTo, returnTimeFrom : params.returnTimeFrom, returnTimeTo : params.returnTimeTo, frequency : params.frequency, type : params.type ]" />
            <hr/>
            <p>${params.totalResults} listing(s) found.</p>
          </g:if>

          <g:if test="${params.totalResults == 0}">
            <p>No results found!</p>
          </g:if>

        </div>

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