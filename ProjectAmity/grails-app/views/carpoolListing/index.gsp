<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Carpool</title>

    <g:if test="${!session.user}">
        <%
          response.setStatus(301);
          response.setHeader( "Location", "/ProjectAmity" );
          response.setHeader( "Connection", "close" );
        %>
    </g:if>

    <g:javascript library="application" />
    <modalbox:modalIncludes />

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />



    <script type="text/javascript">

      var activeListingsStatus = 'shown'
      var activeListings

      var inactiveListingsStatus = 'shown'
      var inactiveListings

      var requestsStatus = 'shown'
      var requests

      var ratingsStatus = 'shown'
      var ratings

      function initialiseCarpoolPage()
      {
        $('activeListingsShow').hide()
        $('activeListingsHide').hide()

        $('inactiveListingsShow').hide()
        $('inactiveListingsHide').hide()

        $('requestsShow').hide()
        $('requestsHide').hide()

        $('ratingsShow').hide()
        $('ratingsHide').hide()
      }

      function updateActiveListings()
      {

        var address = '<g:createLink action="ajaxLoadActiveListings"/>'

        new Ajax.Request(address,
                            {
                              onLoading: function()
                                        {
                                          $('activeListingsMain').innerHTML =
                                          '<div style=\"text-align: center\">' +
                                          '  <h1><img src=\"\/ProjectAmity\/images\/spinner.gif\" width=\"20px\"/> Loading your active carpool listings</h1>' +
                                          '  <h3>Please wait...</h3>' +
                                          '</div>'
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          activeListings = eval( '(' + response.responseText + ')' )
                                          parseActiveListings()
                                        },
                              onFailure: function(response)
                                        {
                                        }
                            }
                        );
                        
      }

      function updateInactiveListings()
      {
        var address2 = '<g:createLink action="ajaxLoadInactiveListings"/>'

        new Ajax.Request(address2,
                            {
                              onLoading: function()
                                        {
                                          $('inactiveListingsMain').innerHTML =
                                          '<div style=\"text-align: center\">' +
                                          '  <h1><img src=\"\/ProjectAmity\/images\/spinner.gif\" width=\"20px\"/> Loading your deactivated carpool listings</h1>' +
                                          '  <h3>Please wait...</h3>' +
                                          '</div>'
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          inactiveListings = eval( '(' + response.responseText + ')' )
                                          parseInactiveListings()
                                        },
                              onFailure: function(response)
                                        {
                                        }
                            }
                        );
      }

      function updateRequests()
      {
        var address3 = '<g:createLink action="ajaxLoadRequests"/>'

        new Ajax.Request(address3,
                            {
                              onLoading: function()
                                        {
                                          $('requestsMain').innerHTML = 
                                          '<div style=\"text-align: center\">' +
                                          '  <h1><img src=\"\/ProjectAmity\/images\/spinner.gif\" width=\"20px\"/> Loading requests that are pending your approval</h1>' +
                                          '  <h3>Please wait...</h3>' +
                                          '</div>'
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          requests = eval( '(' + response.responseText + ')' )
                                          parseRequests()
                                        },
                              onFailure: function(response)
                                        {
                                        }
                            }
                        );
      }

      function updateRatings()
      {
        var address4 = '<g:createLink action="ajaxLoadRatings"/>'

        new Ajax.Request(address4,
                            {
                              onLoading: function()
                                        {
                                          $('ratingsMain').innerHTML =
                                          '<div style=\"text-align: center\">' +
                                          '  <h1><img src=\"\/ProjectAmity\/images\/spinner.gif\" width=\"20px\"/> Loading carpoolers who are pending your evaluation</h1>' +
                                          '  <h3>Please wait...</h3>' +
                                          '</div>'
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          ratings = eval( '(' + response.responseText + ')' )
                                          parseRatings()
                                        },
                              onFailure: function(response)
                                        {
                                        }
                            }
                        );
      }

      function parseActiveListings()
      {
        var html = ''

        if( activeListings.length == 0 )
        {
          $('activeListings').hide()
        }

        if( activeListings.length > 0 )
        {
          $('activeListings').show()
        }

        for( var i = 0 ; i < activeListings.length ; i++ )
        {
          if( activeListings[i].tripType == 'commute' )
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '<tr>'
            html += ' <td colspan=\"7\"><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + activeListings[i].id + '\"><h3>Commute from ' + activeListings[i].startAddress + ' to<br/>' + activeListings[i].endAddress + ' as a ' +  activeListings[i].riderType + '</h3></a></td>'
            html += ' <td rowspan=\"10\" width=\"25%\" style=\"text-align: center; vertical-align: middle\"><img src=\"http://maps.google.com/maps/api/staticmap?size=150x150&markers=color:green|label:A|' + activeListings[i].startLatitude + ',' + activeListings[i].startLongitude + '&markers=color:red|label:B|' + activeListings[i].endLatitude + ',' + activeListings[i].endLongitude + '&sensor=false\" alt=\"Journey Map\" title=\"Journey Map\" /></td>'
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
            html += '  <td>' + parseListingTiming(activeListings[i].departureMondayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].departureTuesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].departureWednesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].departureThursdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].departureFridayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].departureSaturdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].departureSundayTime) + '</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingInterval(activeListings[i].departureMondayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].departureTuesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].departureWednesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].departureThursdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].departureFridayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].departureSaturdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].departureSundayInterval) + '</td>'
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
            html += '  <td>' + parseListingTiming(activeListings[i].returnMondayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].returnTuesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].returnWednesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].returnThursdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].returnFridayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].returnSaturdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(activeListings[i].returnSundayTime) + '</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingInterval(activeListings[i].returnMondayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].returnTuesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].returnWednesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].returnThursdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].returnFridayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].returnSaturdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(activeListings[i].returnSundayInterval) + '</td>'
            html += '</tr>'
            html += '<tr>'
            html += '  <td colspan=\"7\" style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\">'
            html += '    <a href=\"\/ProjectAmity\/carpoolListing\/match\/' + activeListings[i].id + '\"><img style=\"border: 0px\" src=\"\/ProjectAmity\/images\/amity\/carpoolmatch.png\" alt=\"Find Matches\" title=\"Find Matches\" width=\"12px\" />&nbsp;<b>Find Matches</b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
            html += '    <a href=\"#\" onClick=\"triggerDeactivateForm(\'' + activeListings[i].startAddress + '\', \'' + activeListings[i].endAddress + '\', \'' + activeListings[i].id + '\'); return false\"><img style=\"border: 0px\" src=\"\/ProjectAmity\/images\/amity\/carpooldelete.png\" alt=\"Delete Matches\" title=\"Delete Matches\" width=\"12px\" />&nbsp;<b>Deactivate Listing</b></a>'
            html += '  </td>'
            html += '</tr>'
            html += '</table>'
            html += '<br/><br/>'
          }
          else
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '  <tr>'
            html += '    <td colspan=\"7\"><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + activeListings[i].id + '\"><h3>One-off trip from ' + activeListings[i].startAddress + ' to<br/>' + activeListings[i].endAddress + ' as a ' +  activeListings[i].riderType + '</h3></a></td>'
            html += '    <td rowspan=\"6\" width=\"25%\" style=\"text-align: center; vertical-align: middle\"><img src=\"http://maps.google.com/maps/api/staticmap?size=150x150&markers=color:green|label:A|' + activeListings[i].startLatitude + ',' + activeListings[i].startLongitude + '&markers=color:red|label:B|' + activeListings[i].endLatitude + ',' + activeListings[i].endLongitude + '&sensor=false\" alt=\"Journey Map\" title=\"Journey Map\" /></td>'
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Departure</b></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\">'
            html += '    <td>' + parseListingDateFormat( activeListings[i].oneOffDepartureTime ) + ', ' + parseListingInterval(activeListings[i].oneOffDepartureInterval) + '</td>'
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Return</b></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\">'
            if( activeListings[i].oneOffReturnTime )
            {
              html += '    <td>' + parseListingDateFormat( activeListings[i].oneOffReturnTime ) + ', ' + parseListingInterval(activeListings[i].oneOffReturnInterval) + '</td>'
            }
            else
            {
              html += '    <td>This is a one-way trip.</td>'
            }
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\">'
            html += '    <a href=\"\/ProjectAmity\/carpoolListing\/match\/' + activeListings[i].id + '\"><img style=\"border: 0px\" src=\"\/ProjectAmity\/images\/amity\/carpoolmatch.png\" alt=\"Find Matches\" title=\"Find Matches\" width=\"12px\" />&nbsp;<b>Find Matches</b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
            html += '    <a href=\"#\" onClick=\"triggerDeactivateForm(\'' + activeListings[i].startAddress + '\', \'' + activeListings[i].endAddress + '\', \'' + activeListings[i].id + '\'); return false\"><img style=\"border: 0px\" src=\"\/ProjectAmity\/images\/amity\/carpooldelete.png\" alt=\"Delete Matches\" title=\"Delete Matches\" width=\"12px\" />&nbsp;<b>Deactivate Listing</b></a>'
            html += '    </td>'
            html += '  </tr>'
            html += '</table>'
            html += '<br/><br/>'
          }
        }

        $('activeListingsMain').innerHTML = html
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

      function parseInactiveListings()
      {
        var html = ''

        if( inactiveListings.length == 0 )
        {
          $('inactiveListings').hide()
        }

        if( inactiveListings.length > 0 )
        {
          $('inactiveListings').show()
        }

        for( var i = 0 ; i < inactiveListings.length ; i++ )
        {
          if( inactiveListings[i].tripType == 'commute' )
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '<tr>'
            html += ' <td colspan=\"7\"><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + inactiveListings[i].id + '\"><h3>Commute from ' + inactiveListings[i].startAddress + ' to<br/>' + inactiveListings[i].endAddress + ' as a ' +  inactiveListings[i].riderType + '</h3></a></td>'
            html += ' <td rowspan=\"10\" width=\"25%\" style=\"text-align: center; vertical-align: middle\"><img src=\"http://maps.google.com/maps/api/staticmap?size=150x150&markers=color:green|label:A|' + inactiveListings[i].startLatitude + ',' + inactiveListings[i].startLongitude + '&markers=color:red|label:B|' + inactiveListings[i].endLatitude + ',' + inactiveListings[i].endLongitude + '&sensor=false\" alt=\"Journey Map\" title=\"Journey Map\" /></td>'
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
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureMondayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureTuesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureWednesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureThursdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureFridayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureSaturdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].departureSundayTime) + '</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureMondayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureTuesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureWednesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureThursdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureFridayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureSaturdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].departureSundayInterval) + '</td>'
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
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnMondayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnTuesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnWednesdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnThursdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnFridayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnSaturdayTime) + '</td>'
            html += '  <td>' + parseListingTiming(inactiveListings[i].returnSundayTime) + '</td>'
            html += '</tr>'
            html += '<tr style=\"text-align: center; vertical-align: middle\">'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnMondayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnTuesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnWednesdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnThursdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnFridayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnSaturdayInterval) + '</td>'
            html += '  <td>' + parseListingInterval(inactiveListings[i].returnSundayInterval) + '</td>'
            html += '</tr>'
            html += '</table>'
            html += '<br/><br/>'
          }
          else
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '  <tr>'
            html += '    <td colspan=\"7\"><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + inactiveListings[i].id + '\"><h3>One-off trip from ' + inactiveListings[i].startAddress + ' to<br/>' + inactiveListings[i].endAddress + ' as a ' +  inactiveListings[i].riderType + '</h3></a></td>'
            html += '    <td rowspan=\"6\" width=\"25%\" style=\"text-align: center; vertical-align: middle\"><img src=\"http://maps.google.com/maps/api/staticmap?size=150x150&markers=color:green|label:A|' + inactiveListings[i].startLatitude + ',' + inactiveListings[i].startLongitude + '&markers=color:red|label:B|' + inactiveListings[i].endLatitude + ',' + inactiveListings[i].endLongitude + '&sensor=false\" alt=\"Journey Map\" title=\"Journey Map\" /></td>'
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Departure</b></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\">'
            html += '    <td>' + parseListingDateFormat( inactiveListings[i].oneOffDepartureTime ) + ', ' + parseListingInterval(inactiveListings[i].oneOffDepartureInterval) + '</td>'
            html += '  </tr>'
            html += '  <tr>'
            html += '    <td style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\"><b>Return</b></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\">'
            if( inactiveListings[i].oneOffReturnTime )
            {
              html += '    <td>' + parseListingDateFormat( inactiveListings[i].oneOffReturnTime ) + ', ' + parseListingInterval(inactiveListings[i].oneOffReturnInterval) + '</td>'
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

        $('inactiveListingsMain').innerHTML = html
      }

      function respondToRequest(mode, subject, userid, name, requestid, message)
      {
        $('qrecipientName').value = name
        $('qrecipientUserid').value = userid
        $('qrequestId').value = requestid
        $('qmode').value = mode
        $('qsubject').value = subject
        $('qmessage').value = message

        Modalbox.show('/ProjectAmity/carpoolListing/respondToRequest.gsp', {  title: 'Respond to Carpool Request', width: 500, overlayClose: false, closeValue: '× Close', params: $('respondRequestForm').serialize(), afterHide: function(){updateRequests()} });
      }

      function fillHiddenForm(subject, userid, name)
      {
        $('qrecipientName').value = name
        $('qrecipientUserid').value = userid
        $('qsubject').value = subject
      }

      function triggerDeactivateForm(start, end, id)
      {
        $('rstartAddress').value = start
        $('rendAddress').value = end
        $('rlistingId').value = id

        Modalbox.show('/ProjectAmity/carpoolListing/deactivateListing.gsp', {  title: 'Deactivate Carpool Listing', width: 500, overlayClose: false, closeValue: '× Close', params: $('deactivateForm').serialize(), afterHide: function(){updateActiveListings(); updateInactiveListings(); updateRequests()} });
      }

      function defineRating(rating, ratingId, name, id)
      {
        $('vratingId').value = ratingId
        $('vrating').value = rating
        $('vratedId').value = id
        $('vratedName').value = name

        Modalbox.show('/ProjectAmity/carpoolListing/rate.gsp', {  title: 'Rate a Carpooler', width: 500, overlayClose: false, closeValue: '× Close', params: $('ratingForm').serialize(), afterHide: function(){updateRatings();} });
      }

      function activeListingsHover()
      {
        if( activeListingsStatus == 'shown' )
        {
          $('activeListingsHide').show()
        }
        else
        {
          $('activeListingsShow').show()
        }
      }

      function activeListingsHoverOut()
      {
        $('activeListingsHide').hide()
        $('activeListingsShow').hide()
      }

      function activeListingsShowHide()
      {
        if( activeListingsStatus == 'shown' )
        {
          $('activeListingsMain').hide()
          $('activeListingsShow').show()
          $('activeListingsHide').hide()
          activeListingsStatus = 'hidden'
        }
        else
        {
          $('activeListingsMain').show()
          $('activeListingsShow').hide()
          $('activeListingsHide').show()
          activeListingsStatus = 'shown'
        }
      }

      function inactiveListingsHover()
      {
        if( inactiveListingsStatus == 'shown' )
        {
          $('inactiveListingsHide').show()
        }
        else
        {
          $('inactiveListingsShow').show()
        }
      }

      function inactiveListingsHoverOut()
      {
        $('inactiveListingsHide').hide()
        $('inactiveListingsShow').hide()
      }

      function inactiveListingsShowHide()
      {
        if( inactiveListingsStatus == 'shown' )
        {
          $('inactiveListingsMain').hide()
          $('inactiveListingsShow').show()
          $('inactiveListingsHide').hide()
          inactiveListingsStatus = 'hidden'
        }
        else
        {
          $('inactiveListingsMain').show()
          $('inactiveListingsShow').hide()
          $('inactiveListingsHide').show()
          inactiveListingsStatus = 'shown'
        }
      }

      function requestsHover()
      {
        if( requestsStatus == 'shown' )
        {
          $('requestsHide').show()
        }
        else
        {
          $('requestsShow').show()
        }
      }

      function requestsHoverOut()
      {
        $('requestsHide').hide()
        $('requestsShow').hide()
      }

      function requestsShowHide()
      {
        if( requestsStatus == 'shown' )
        {
          $('requestsMain').hide()
          $('requestsShow').show()
          $('requestsHide').hide()
          requestsStatus = 'hidden'
        }
        else
        {
          $('requestsMain').show()
          $('requestsShow').hide()
          $('requestsHide').show()
          requestsStatus = 'shown'
        }
      }

      function parseRequests()
      {

        if( requests.length == 0 )
        {
          $('requests').hide()
        }

        if( requests.length > 0 )
        {
          $('requests').show()
        }

        var html = ''

        for( var i = 0 ; i < requests.length ; i++ )
        {
          if( i == 0 )
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '  <tr style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\">'
            if( requests[i][0] == 'commute' )
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + requests[i][7] + '\"><h3>Commute from ' + requests[i][1] + ' to<br/>' + requests[i][2] + ' as a ' + requests[i][3] + '</h3></a></td>'
            }
            else
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + requests[i][7] + '\"><h3>One-off trip from ' + requests[i][1] + ' to<br/>' + requests[i][2] + ' as a ' + requests[i][3] + '</h3></a></td>'
            }
            html += '  </tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
            html += '  <tr style=\"text-align: left; vertical-align: middle\">'
            html += '    <td style=\"border-bottom: 2px solid #E6F0D2\"><p><b><a href=\"\/ProjectAmity\/carpoolListing\/checkRating\/?resident=' + requests[i][8] + '\" onClick=\"Modalbox.show(this.href, {  title: this.title, width: 500, overlayClose: false, closeValue: \'× Close\'  }); return false;\" title=\"Check Rating\">' + requests[i][4] + '</a> (' + requests[i][8] + ') has sent you a carpool request for this listing and has this to say:</b><br/>' + requests[i][5] + '</p></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\"><td style=\"border-bottom: 2px solid #E6F0D2\"><p><a href=\"\" onclick=\"respondToRequest(\'Accept\', \'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\', \'' + requests[i][6] + '\', \'We shall carpool for the journey from ' + requests[i][1] + ' to ' + requests[i][2] + ' then!\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolaccept.png\" alt=\"Accept Request\" title=\"Accept Request\" width=\"12px\" />&nbsp;<b>Accept</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\" onclick=\"respondToRequest(\'Decline\', \'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\', \'' + requests[i][6] + '\', \'I am sorry, but we cannot carpool for the journey from ' + requests[i][1] + ' to ' + requests[i][2] + '.\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolreject.png\" alt=\"Decline Request\" title=\"Decline Request\" width=\"12px\" />&nbsp;Decline</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/ProjectAmity/carpoolListing/createMessage.gsp\" title=\"Send Message to ' + requests[i][4] + '\" onclick="fillHiddenForm(\'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\'); Modalbox.show(this.href, {  title: this.title, width: 500, params: Form.serialize(\'respondRequestForm\'), overlayClose: false, closeValue: \'× Close\'  }); return false; "><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolsendpm.png\" alt=\"Send Private Message\" title=\"Send Private Message\" width=\"12px\" />&nbsp;Send Private Message</b></a></p></td></tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
          }
          else if( requests[i][7] != requests[i - 1][7] )
          {
            html += '</table>'
            html += '<br/>'
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '  <tr style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\">'
            if( requests[i][0] == 'commute' )
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + requests[i][7] + '\"><h3>Commute from ' + requests[i][1] + ' to<br/>' + requests[i][2] + ' as a ' + requests[i][3] + '</h3></a></td>'
            }
            else
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + requests[i][7] + '\"><h3>One-off trip from ' + requests[i][1] + ' to<br/>' + requests[i][2] + ' as a ' + requests[i][3] + '</h3></a></td>'
            }
            html += '  </tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
            html += '  <tr style=\"text-align: left; vertical-align: middle\">'
            html += '    <td style=\"border-bottom: 2px solid #E6F0D2\"><p><b><a href=\"\/ProjectAmity\/carpoolListing\/checkRating\/?resident=' + requests[i][8] + '\" onClick=\"Modalbox.show(this.href, {  title: this.title, width: 500, overlayClose: false, closeValue: \'× Close\'  }); return false;\" title=\"Check Rating\">' + requests[i][4] + '</a> (' + requests[i][8] + ') has sent you a carpool request for this listing and has this to say:</b><br/>' + requests[i][5] + '</p></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\"><td style=\"border-bottom: 2px solid #E6F0D2\"><p><a href=\"\" onclick=\"respondToRequest(\'Accept\', \'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\', \'' + requests[i][6] + '\', \'We shall carpool for the journey from ' + requests[i][1] + ' to ' + requests[i][2] + ' then!\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolaccept.png\" alt=\"Accept Request\" title=\"Accept Request\" width=\"12px\" />&nbsp;<b>Accept</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\" onclick=\"respondToRequest(\'Decline\', \'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\', \'' + requests[i][6] + '\', \'I am sorry, but we cannot carpool for the journey from ' + requests[i][1] + ' to ' + requests[i][2] + '.\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolreject.png\" alt=\"Decline Request\" title=\"Decline Request\" width=\"12px\" />&nbsp;Decline</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/ProjectAmity/carpoolListing/createMessage.gsp\" title=\"Send Message to ' + requests[i][4] + '\" onclick="fillHiddenForm(\'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\'); Modalbox.show(this.href, {  title: this.title, width: 500, params: Form.serialize(\'respondRequestForm\'), overlayClose: false, closeValue: \'× Close\'  }); return false; "><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolsendpm.png\" alt=\"Send Private Message\" title=\"Send Private Message\" width=\"12px\" />&nbsp;Send Private Message</b></a></p></td></tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
          }
          else if( requests[i][7] == requests[i - 1][7] )
          {
            html += '  <tr style=\"text-align: left; vertical-align: middle\">'
            html += '    <td style=\"border-bottom: 2px solid #E6F0D2\"><p><b><a href=\"\/ProjectAmity\/carpoolListing\/checkRating\/?resident=' + requests[i][8] + '\" onClick=\"Modalbox.show(this.href, {  title: this.title, width: 500, overlayClose: false, closeValue: \'× Close\'  }); return false;\" title=\"Check Rating\">' + requests[i][4] + '</a> (' + requests[i][8] + ') has sent you a carpool request for this listing and has this to say:</b><br/>' + requests[i][5] + '</p></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\"><td style=\"border-bottom: 2px solid #E6F0D2\"><p><a href=\"\" onclick=\"respondToRequest(\'Accept\', \'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\', \'' + requests[i][6] + '\', \'We shall carpool for the journey from ' + requests[i][1] + ' to ' + requests[i][2] + ' then!\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolaccept.png\" alt=\"Accept Request\" title=\"Accept Request\" width=\"12px\" />&nbsp;<b>Accept</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\" onclick=\"respondToRequest(\'Decline\', \'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\', \'' + requests[i][6] + '\', \'I am sorry, but we cannot carpool for the journey from ' + requests[i][1] + ' to ' + requests[i][2] + '.\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolreject.png\" alt=\"Decline Request\" title=\"Decline Request\" width=\"12px\" />&nbsp;Decline</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/ProjectAmity/carpoolListing/createMessage.gsp\" title=\"Send Message to ' + requests[i][4] + '\" onclick="fillHiddenForm(\'Journey from ' + requests[i][1] + ' to ' + requests[i][2] + '\', \'' + requests[i][8] + '\', \'' + requests[i][4] + '\'); Modalbox.show(this.href, {  title: this.title, width: 500, params: Form.serialize(\'respondRequestForm\'), overlayClose: false, closeValue: \'× Close\'  }); return false; "><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolsendpm.png\" alt=\"Send Private Message\" title=\"Send Private Message\" width=\"12px\" />&nbsp;Send Private Message</b></a></p></td></tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
          }

          if( i == (requests.length - 1) )
          {
            html += '</table>'
            html += '<br/>'
          }
        }

        $('requestsMain').innerHTML = html
      }

      function ratingsHover()
      {
        if( ratingsStatus == 'shown' )
        {
          $('ratingsHide').show()
        }
        else
        {
          $('ratingsShow').show()
        }
      }

      function ratingsHoverOut()
      {
        $('ratingsHide').hide()
        $('ratingsShow').hide()
      }

      function ratingsShowHide()
      {
        if( ratingsStatus == 'shown' )
        {
          $('ratingsMain').hide()
          $('ratingsShow').show()
          $('ratingsHide').hide()
          ratingsStatus = 'hidden'
        }
        else
        {
          $('ratingsMain').show()
          $('ratingsShow').hide()
          $('ratingsHide').show()
          ratingsStatus = 'shown'
        }
      }

      function parseRatings()
      {

        if( ratings.length == 0 )
        {
          $('ratings').hide()
        }

        if( ratings.length > 0 )
        {
          $('ratings').show()
        }

        var html = ''

        for( var i = 0 ; i < ratings.length ; i++ )
        {
          if( i == 0 )
          {
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '  <tr style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\">'
            if( ratings[i][0] == 'commute' )
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + ratings[i][6] + '\"><h3>Commute from ' + ratings[i][1] + ' to<br/>' + ratings[i][2] + ' as a ' + ratings[i][3] + '</h3></a></td>'
            }
            else
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + ratings[i][6] + '\"><h3>One-off trip from ' + ratings[i][1] + ' to<br/>' + ratings[i][2] + ' as a ' + ratings[i][3] + '</h3></a></td>'
            }
            html += '  </tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
            html += '  <tr style=\"text-align: left; vertical-align: middle\">'
            html += '    <td style=\"border-bottom: 2px solid #E6F0D2\"><p><b>' + ratings[i][4] + ' (' + ratings[i][7] + ') carpooled with you for this particular carpool listing. How was the carpool with him/her?</p></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\"><td style=\"border-bottom: 2px solid #E6F0D2\"><p><a href=\"\" onclick=\"defineRating(\'Good\', \'' + ratings[i][5] + '\', \'' + ratings[i][4] + '\' ,\'' + ratings[i][7] + '\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolgood.png\" alt=\"Good\" title=\"Good\" width=\"12px\" />&nbsp;<b>Good</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\" onclick=\"defineRating(\'Bad\', \'' + ratings[i][5] + '\', \'' + ratings[i][4] + '\' ,\'' + ratings[i][7] + '\');; return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolbad.png\" alt=\"Bad\" title=\"Bad\" width=\"12px\" />&nbsp;Bad</b></a></p></td></tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
          }
          else if( ratings[i][6] != ratings[i - 1][6] )
          {
            html += '</table>'
            html += '<br/>'
            html += '<table border=\"0\" cellspacing=\"0\" width=\"80%\">'
            html += '  <tr style=\"text-align: center; vertical-align: middle; background-color: #E6F0D2\">'
            if( ratings[i][0] == 'commute' )
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + ratings[i][6] + '\"><h3>Commute from ' + ratings[i][1] + ' to<br/>' + ratings[i][2] + ' as a ' + ratings[i][3] + '</h3></a></td>'
            }
            else
            {
              html += '    <td><a href=\"\/ProjectAmity\/carpoolListing\/view\/' + ratings[i][6] + '\"><h3>One-off trip from ' + ratings[i][1] + ' to<br/>' + ratings[i][2] + ' as a ' + ratings[i][3] + '</h3></a></td>'
            }
            html += '  </tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
            html += '  <tr style=\"text-align: left; vertical-align: middle\">'
            html += '    <td style=\"border-bottom: 2px solid #E6F0D2\"><p><b>' + ratings[i][4] + ' (' + ratings[i][7] + ') carpooled with you for this particular carpool listing. How was the carpool with him/her?</p></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\"><td style=\"border-bottom: 2px solid #E6F0D2\"><p><a href=\"\" onclick=\"defineRating(\'Good\', \'' + ratings[i][5] + '\', \'' + ratings[i][4] + '\' ,\'' + ratings[i][7] + '\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolgood.png\" alt=\"Good\" title=\"Good\" width=\"12px\" />&nbsp;<b>Good</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\" onclick=\"defineRating(\'Bad\', \'' + ratings[i][5] + '\', \'' + ratings[i][4] + '\' ,\'' + ratings[i][7] + '\');; return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolbad.png\" alt=\"Bad\" title=\"Bad\" width=\"12px\" />&nbsp;Bad</b></a></p></td></tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
          }
          else if( ratings[i][6] == ratings[i - 1][6] )
          {
            html += '  <tr style=\"text-align: left; vertical-align: middle\">'
            html += '    <td style=\"border-bottom: 2px solid #E6F0D2\"><p><b>' + ratings[i][4] + ' (' + ratings[i][7] + ') carpooled with you for this particular carpool listing. How was the carpool with him/her?</p></td>'
            html += '  </tr>'
            html += '  <tr style=\"text-align: center; vertical-align: middle\"><td style=\"border-bottom: 2px solid #E6F0D2\"><p><a href=\"\" onclick=\"defineRating(\'Good\', \'' + ratings[i][5] + '\', \'' + ratings[i][4] + '\' ,\'' + ratings[i][7] + '\'); return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolgood.png\" alt=\"Good\" title=\"Good\" width=\"12px\" />&nbsp;<b>Good</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\" onclick=\"defineRating(\'Bad\', \'' + ratings[i][5] + '\', \'' + ratings[i][4] + '\' ,\'' + ratings[i][7] + '\');; return false\"><img style=\"border: 0px\" src=\"/ProjectAmity/images/amity/carpoolbad.png\" alt=\"Bad\" title=\"Bad\" width=\"12px\" />&nbsp;Bad</b></a></p></td></tr>'
            html += '  <tr><td>&nbsp;</td></tr>'
          }

          if( i == (ratings.length - 1) )
          {
            html += '</table>'
            html += '<br/>'
          }
        }

        $('ratingsMain').innerHTML = html
      }

    </script>

    <style type="text/css">
      .showHideButton
      {
        float: left;
        border: 0px solid black;
        margin-left: 10px;
        margin-top: 10px;
        vertical-align: middle
      }

      /* :hover is a pseudo selector to use to set the mouseover attributes */
      .showHideButton:hover
      {
        background-color: #7FA828;
        color: #FFFFFF
      }
    </style>

  </head>

  <body class="thrColFixHdr" onload="initialiseCarpoolPage(); updateActiveListings(); updateInactiveListings(); updateRequests(); updateRatings()">

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
        <a href="${createLink(controller: 'carpool', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'bcarpool.png')}" border="0" id="pageTitle"/></a>

        <div id="header">
          <h1>test</h1>
          <!-- end #header -->
        </div>

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

        <div id="mainContent" style="min-height: 600px">

            <form method="get" id="respondRequestForm">
              <input type="hidden" id="qrecipientName" name="recipientName" value="" />
              <input type="hidden" id="qrecipientUserid" name="recipientUserid" value="" />
              <input type="hidden" id="qrequestId" name="requestId" value="" />
              <input type="hidden" id="qsubject" name="subject" value="" />
              <input type="hidden" id="qmessage" name="message" value="" />
              <input type="hidden" id="qmode" name="mode" value="" />
            </form>

            <form method="get" id="deactivateForm">
              <input type="hidden" id="rstartAddress" name="startAddress" value="" />
              <input type="hidden" id="rendAddress" name="endAddress" value="" />
              <input type="hidden" id="rlistingId" name="listingId" value="" />
            </form>

            <form method="get" id="ratingForm">
              <input type="hidden" id="vratingId" name="ratingId" value="" />
              <input type="hidden" id="vrating" name="rating" value="" />
              <input type="hidden" id="vratedName" name="ratedName" value="" />
              <input type="hidden" id="vratedId" name="ratedId" value="" />
            </form>

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
          <br/>
          <div id="addCarpoolListingLink" style="float: right; padding-top: 30px">
            <a href="/ProjectAmity/carpoolListing/add"><img style="border: 0px" src="${resource(dir:'images/amity',file:'addcarpoollisting.jpg')}" alt="Add Carpool Listing" title="Add Carpool Listing"/></a>
          </div>
          <br/>
          <div id="requests" style="border: 0px solid black; min-height: 150px" onMouseOver="requestsHover()" onMouseOut="requestsHoverOut()" >
            <div id="requestsHeader" style="border: 0px solid black; float: left">
              <h1>Requests Pending Your Approval</h1>
            </div>
            <div id="requestsHide" onClick="requestsShowHide()" class="showHideButton">
              <p><b>Hide Content</b></p>
            </div>
            <div id="requestsShow" onClick="requestsShowHide()" class="showHideButton">
              <p><b>Show Content</b></p>
            </div>
            <br/>
            <div id="requestsMain" style="border: 0px solid black; padding-top: 50px">
              <div style="text-align: center">
                <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Loading requests that are pending your approval</h1>
                <h3>Please wait...</h3>
              </div>
            </div>
          </div>

          <br/>
          <div id="ratings" style="border: 0px solid black; min-height: 150px" onMouseOver="ratingsHover()" onMouseOut="ratingsHoverOut()" >
            <div id="ratingsHeader" style="border: 0px solid black; float: left">
              <h1>Carpoolers Pending Your Evaluation</h1>
            </div>
            <div id="ratingsHide" onClick="ratingsShowHide()" class="showHideButton">
              <p><b>Hide Content</b></p>
            </div>
            <div id="ratingsShow" onClick="ratingsShowHide()" class="showHideButton">
              <p><b>Show Content</b></p>
            </div>
            <br/>
            <div id="ratingsMain" style="border: 0px solid black; padding-top: 50px">
              <div style="text-align: center">
                <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Loading carpoolers who are pending your evaluation</h1>
                <h3>Please wait...</h3>
              </div>
            </div>
          </div>

          <br/>
          <div id="activeListings" style="border: 0px solid black; min-height: 150px" onMouseOver="activeListingsHover()" onMouseOut="activeListingsHoverOut()" >
            <div id="activeListingsHeader" style="border: 0px solid black; float: left">
              <h1>Your Active Carpool Listings</h1>
            </div>
            <div id="activeListingsHide" onClick="activeListingsShowHide()" class="showHideButton">
              <p><b>Hide Content</b></p>
            </div>
            <div id="activeListingsShow" onClick="activeListingsShowHide()" class="showHideButton">
              <p><b>Show Content</b></p>
            </div>
            <br/>
            <div id="activeListingsMain" style="border: 0px solid black; padding-top: 50px">
              <div style="text-align: center">
                <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Loading your active carpool listings</h1>
                <h3>Please wait...</h3>
              </div>
            </div>
          </div>

          <br/>
          <div id="inactiveListings" style="border: 0px solid black; min-height: 150px" onMouseOver="inactiveListingsHover()" onMouseOut="inactiveListingsHoverOut()" >
            <div id="inactiveListingsHeader" style="border: 0px solid black; float: left">
              <h1>Last 5 Deactivated Carpool Listings</h1>
            </div>
            <div id="inactiveListingsHide" onClick="inactiveListingsShowHide()" class="showHideButton">
              <p><b>Hide Content</b></p>
            </div>
            <div id="inactiveListingsShow" onClick="inactiveListingsShowHide()" class="showHideButton">
              <p><b>Show Content</b></p>
            </div>
            <br/>
            <div id="inactiveListingsMain" style="border: 0px solid black; padding-top: 50px">
              <div style="text-align: center">
                <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Loading your deactivated carpool listings</h1>
                <h3>Please wait...</h3>
              </div>
            </div>
          </div>

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