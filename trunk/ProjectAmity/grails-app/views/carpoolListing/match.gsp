<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Find Matching Carpool Listings</title>

    <g:javascript library="application" />
    <modalbox:modalIncludes />

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />

    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
srLJJlcXUmLMTM2KkMsePdU1A" type="text/javascript"></script>

    <script type="text/javascript">

      var map
      var matches

      function findMatches()
      {

        var address = '<g:createLink action="ajaxMatch"/>'
        address += '?listingId=' +${l.id}

        new Ajax.Request(address,
                            {
                              onLoading: function()
                                        {
                                          $('matchesMain').innerHTML =
                                          '<div style=\"text-align: center\">' +
                                          '  <h1><img src=\"\/ProjectAmity\/images\/spinner.gif\" width=\"20px\"/> Loading matching listings</h1>' +
                                          '  <h3>Please wait...</h3>' +
                                          '</div>'
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          matches = eval( '(' + response.responseText + ')' )
                                          parseMatches()
                                        },
                              onFailure: function(response)
                                        {
                                        }
                            }
                        );

      }

      function parseMatches()
      {
        var html = ''

        if(matches.length == 0)
        {
          $('matches').hide()
          $('cannotFindMatches').show()
        }

        for( var i = 0 ; i < matches.length ; i++ )
        {

          var startIcon = new GIcon(G_DEFAULT_ICON);
          startIcon.image = "/ProjectAmity/images/amity/carpool" + (i+1) + "A.png";
          startMarkerOptions = { icon:startIcon };
          
          var startLatLng = new GLatLng(matches[i].startLatitude, matches[i].startLongitude)
          var startMarker = new GMarker(startLatLng, startMarkerOptions)
          map.addOverlay(startMarker)

          var endIcon = new GIcon(G_DEFAULT_ICON);
          endIcon.image = "/ProjectAmity/images/amity/carpool" + (i+1) + "B.png";
          endMarkerOptions = { icon:endIcon };

          var endLatLng = new GLatLng(matches[i].endLatitude, matches[i].endLongitude)
          var endMarker = new GMarker(endLatLng, endMarkerOptions)
          map.addOverlay(endMarker)

          if( matches[i].tripType == 'commute' )
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"100%\">'
            html += '<tr>'
            html += ' <td colspan=\"7\"><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + matches[i].id + '\"><h3>' + (i+1) + '. Commute from ' + matches[i].startAddress + ' to<br/>' + matches[i].endAddress + ' as a ' +  matches[i].riderType + '</h3></a></td>'
            html += '</tr>'
            html += '<tr>'
            html += '  <td colspan=\"7\" style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Departure</b></td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle; font-weight: bold\">'
            html += '  <td>Mon</td>'
            html += '  <td>Tue</td>'
            html += '  <td>Wed</td>'
            html += '  <td>Thu</td>'
            html += '  <td>Fri</td>'
            html += '  <td>Sat</td>'
            html += '  <td>Sun</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingTiming(matches[i].departureMondayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].departureTuesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].departureWednesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].departureThursdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].departureFridayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].departureSaturdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].departureSundayTime) + '</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingInterval(matches[i].departureMondayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].departureTuesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].departureWednesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].departureThursdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].departureFridayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].departureSaturdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].departureSundayInterval) + '</td>'
            html += '</tr>'
            html += '<tr>'
            html += '  <td colspan=\"7\" style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Return</b></td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle; font-weight: bold\">'
            html += '  <td>Mon</td>'
            html += '  <td>Tue</td>'
            html += '  <td>Wed</td>'
            html += '  <td>Thu</td>'
            html += '  <td>Fri</td>'
            html += '  <td>Sat</td>'
            html += '  <td>Sun</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingTiming(matches[i].returnMondayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].returnTuesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].returnWednesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].returnThursdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].returnFridayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].returnSaturdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(matches[i].returnSundayTime) + '</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingInterval(matches[i].returnMondayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].returnTuesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].returnWednesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].returnThursdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].returnFridayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].returnSaturdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(matches[i].returnSundayInterval) + '</td>'
            html += '</tr>'
            html += '</table>'
            html += '<br/><br/>'
          }
          else
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"100%\">'
            html += '  <tr>'
            html += '    <td colspan=\"7\"><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + matches[i].id + '\"><h3>' + (i+1) + '. One-off trip from ' + matches[i].startAddress + ' to<br/>' + matches[i].endAddress + ' as a ' +  matches[i].riderType + '</h3></a></td>'
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Departure</b></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\">'
            html += '    <td>' + parseListingDateFormat( matches[i].oneOffDepartureTime ) + ', ' + parseListingInterval(matches[i].oneOffDepartureInterval) + '</td>'
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Return</b></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\">'
            if( matches[i].oneOffReturnTime )
            {
              html += '    <td>' + parseListingDateFormat( matches[i].oneOffReturnTime ) + ', ' + parseListingInterval(matches[i].oneOffReturnInterval) + '</td>'
            }
            else
            {
              html += '    <td>This is a one-way trip.</td>'
            }
            html += '  </tr>'
            html += '</table>'
            html += '<br/><br/>'
          }
        }

        // map.zoomOut()
        $('matchesMain').innerHTML = html
      }

      function parseListingTiming(timing)
      {
        if( timing )
        {
          return timing
        }
        else
        {
          return '-'
        }
      }

      function parseListingInterval(interval)
      {
        if( interval )
        {
          if( interval == '60' )
          {
            return '± 1 hour'
          }
          else if( interval == '0' )
          {
            return '-'
          }
          else
          {
            return '± ' + interval + ' mins'
          }
        }
        else
        {
          return '-'
        }
      }

      function parseListingDateFormat(date)
      {
        var months = [ 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ]

        var hours = date.getHours()
        var minutes = date.getMinutes()

        var suffix = 'AM';
        if (hours >= 12)
        {
          suffix = 'PM';
          hours = hours - 12;
        }
        if (hours == 0)
        {
          hours = 12;
        }
        if(hours < 10)
        {
          hours = '0' + hours
        }

        if (minutes < 10)
        {
          minutes = '0' + minutes
        }

        return date.getDate() + ' ' + months[date.getMonth()] + ' ' + date.getFullYear() + ' at ' + hours + ':' + minutes + suffix
      }

      function loadListingMap(startLat, startLong, endLat, endLong)
      {
          $('cannotFindMatches').hide()
          
          if (GBrowserIsCompatible())
          {
              // GMap2 of the route map.
              map = new GMap2(document.getElementById("viewListingMap"))
              map.setCenter(new GLatLng(1.360117, 103.829041), 11)
              map.addControl(new GLargeMapControl3D())
              map.addControl(new GMapTypeControl())

              var start = new GLatLng(startLat,startLong)
              var end = new GLatLng(endLat, endLong)
              var directions = new GDirections(map)
              directions.load("from: " + start + " to: " + end )
          }
      }

    </script>

    <style type="text/css">



    </style>

  </head>

  <body class="thrColFixHdr" onload="loadListingMap(${l.startLatitude}, ${l.startLongitude}, ${l.endLatitude}, ${l.endLongitude}); findMatches()" onunload="GUnload()">

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

        <div id="banner">&nbsp;</div>

        <div id="navi">
          <g:if test="${session.user}">
            Welcome, <a href="#">${session.user.name}</a>.&nbsp;
            <g:if test="${params.messageModuleUnreadMessages > 1}">
            You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
            </g:if>
            <g:elseif test="${params.messageModuleUnreadMessages == 1}">
            You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
            </g:elseif>
            <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
          </g:if>
        </div>

        <div id="mainContent" style="min-height: 1900px">

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
          <br/>
          <div id="viewCarpoolHeader" style="border: 0px solid black; margin-bottom: 20px">
            <g:if test="${l.tripType == 'commute'}">
              <h1>Commute from ${l.startAddress} to<br/>
                  ${l.endAddress} as a ${l.riderType}</h1>
            </g:if>
            <g:else>
              <h1>One-off Trip from ${l.startAddress} to<br/>
                  ${l.endAddress} as a ${l.riderType}</h1>
            </g:else>
          </div>
          <div id="viewCarpoolLeftPane" style="border: 0px solid black; width: 25%; float: left">
            <h2>About Your Listing</h2>
            <br/>
            <g:if test="${l.tripType == 'commute'}">
              <table border="0" cellspacing="0" width="100%">
                <tr>
                  <td colspan="2" style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Departure</h3></td>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">M</td>
                  <g:if test="${l.departureMondayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureMondayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureMondayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureMondayTime}, ± ${l.departureMondayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">T</td>
                  <g:if test="${l.departureTuesdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureTuesdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureTuesdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureTuesdayTime}, ± ${l.departureTuesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">W</td>
                  <g:if test="${l.departureWednesdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureWednesdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureWednesdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureWednesdayTime}, ± ${l.departureWednesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">T</td>
                  <g:if test="${l.departureThursdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureThursdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureThursdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureThursdayTime}, ± ${l.departureThursdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">F</td>
                  <g:if test="${l.departureFridayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureFridayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureFridayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureFridayTime}, ± ${l.departureFridayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">S</td>
                  <g:if test="${l.departureSaturdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureSaturdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureSaturdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureSaturdayTime}, ± ${l.departureSaturdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">S</td>
                  <g:if test="${l.departureSundayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureSundayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureSundayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.departureSundayTime}, ± ${l.departureSundayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
              </table>
              <br/><br/>
              <table border="0" cellspacing="0" width="100%">
                <tr>
                  <td colspan="2" style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Return</h3></td>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">M</td>
                  <g:if test="${l.returnMondayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnMondayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnMondayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnMondayTime}, ± ${l.returnMondayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">T</td>
                  <g:if test="${l.returnTuesdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnTuesdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnTuesdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnTuesdayTime}, ± ${l.returnTuesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">W</td>
                  <g:if test="${l.returnWednesdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnWednesdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnWednesdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnWednesdayTime}, ± ${l.returnWednesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">T</td>
                  <g:if test="${l.returnThursdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnThursdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnThursdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnThursdayTime}, ± ${l.returnThursdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">F</td>
                  <g:if test="${l.returnFridayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnFridayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnFridayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnFridayTime}, ± ${l.returnFridayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">S</td>
                  <g:if test="${l.returnSaturdayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnSaturdayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnSaturdayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnSaturdayTime}, ± ${l.returnSaturdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
                <tr>
                  <td width="15%" style="text-align: center; vertical-align: middle; border-bottom: 1px solid #E6F0D2; font-weight: bold">S</td>
                  <g:if test="${l.returnSundayInterval == '60'}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnSundayTime}, ± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnSundayInterval}">
                    <td width="85%" style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2">${l.returnSundayTime}, ± ${l.returnSundayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td style="text-align: left; vertical-align: middle; border-bottom: 1px solid #E6F0D2"></td>
                  </g:else>
                </tr>
              </table>
            </g:if>
            <g:else>
              <table border="0" cellspacing="0" width="100%">
                <tr>
                  <td style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Departure</h3></td>
                </tr>
                <tr>
                  <td><p>${params.departureTiming}</p></td>
                </tr>
                <tr>
                  <td style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Return</h3></td>
                </tr>
                <tr>
                  <td><p>${params.returnTiming}</p></td>
                </tr>
              </table>
            </g:else>
            <br/>
            <table border="0" cellspacing="0" width="100%" style="overflow: hidden; table-layout: fixed">
              <tr>
                <td style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Additional Notes</h3></td>
              </tr>
              <tr>
                <g:if test="${l.notes}">
                  <td><p>${l.notes}</p></td>
                </g:if>
                <g:else>
                  <td>None.</td>
                </g:else>
              </tr>
            </table>
          </div>
          <div id="viewCarpoolDetails" style="border: 0px solid black; width: 70%; float: right">
            <h2>Matching Listings</h2>
            <br/>
            <table border="0" cellspacing="0" width="100%">
              <tr>
                <td style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Route Map</h3></td>
              </tr>
            </table>
            <div id="viewListingMap" style="width: 100%; height: 250px; border: 0px solid black"></div>
            <br/>
            <div id="matches" style="border: 0px solid black; min-height: 150px">
              <br/>
              <div id="matchesMain" style="border: 0px solid black">
                <div style="text-align: center">
                  <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Loading matching listings</h1>
                  <h3>Please wait...</h3>
                </div>
              </div>
            </div>
            <div id="cannotFindMatches" style="text-align: center">
              <h2>There were no carpool listings that matched yours.</h2>
              <br/><br/>
              <p>We recommend that you&nbsp;
              <!-- AddThis Button BEGIN -->
              <a class="addthis_button" addthis:url="http://www.projectamity.info/ProjectAmity/carpoolListing/view/${l.id}" href="http://www.addthis.com/bookmark.php?v=250&amp;username=projectamity"><img src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125" height="16" alt="Bookmark and Share" style="border:0"/></a>
              <script type="text/javascript">var addthis_config = {"data_track_clickback":true};</script>
              <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=projectamity"></script>
              <!-- AddThis Button END -->
              &nbsp;your carpool listing so that more people can see it!</p>
            </div>
          </div>

          <br/>

        </div>

        <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats -->
        <br class="clearfloat" />

      <!-- end #container -->
      </div>

      <div class="push"></div>

    </div>

    <div class="footer">
    <p>Copyright &copy; 2010 Team Smiley Face</p>
    </div>
  </body>

</html>