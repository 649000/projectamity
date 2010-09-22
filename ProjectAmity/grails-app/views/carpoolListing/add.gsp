<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Add Carpool Listing</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <!-- <script type="text/javascript" src="${resource(dir: 'js', file: 'carpoolScripts.js')}" ></script> -->

    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />

    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
srLJJlcXUmLMTM2KkMsePdU1A" type="text/javascript"></script>

    <script type="text/javascript">

      // the variable that points to the GMap2
      var map
      // the variable that points to the GClientGeocoder object
      var geocoder

      // the variable that points to the GMarker for the starting location (add carpool listing)
      var newCarpoolStart
      // the variable that points to the GMarker for the ending location (add carpool listing)
      var newCarpoolEnd

      var commuteOrOneOff = 'commute'

      function validateAddCarpoolForm()
      {
        if( commuteOrOneOff == 'commute' )
        {
          var checkBoxes = [ $('departureMonday'), $('departureTuesday'), $('departureWednesday'), $('departureThursday'), $('departureFriday'), $('departureSaturday'), $('departureSunday'), $('returnMonday'), $('returnTuesday'), $('returnWednesday'), $('returnThursday'), $('returnFriday'), $('returnSaturday'), $('returnSunday') ]
          var countOfTickedBoxes = 0

          for( var i = 0 ; i < checkBoxes.length ; i++ )
          {
            if( checkBoxes[i].checked )
            {
              countOfTickedBoxes++
            }
          }

          if( countOfTickedBoxes <= 0 )
          {
            alert('You have yet to specify when you are travelling.')
            return false
          }
          else
          {
            return true
          }
        }
        else if( commuteOrOneOff == 'oneOff' )
        {
          if( oneOffOneWay.checked == false )
          {
            if( $('oneOffReturnDate_year').value < $('oneOffDepartureDate_year').value )
            {
              alert('Your time of return cannot be earlier than your departure time.')
              return false
            }
            else if( $('oneOffReturnDate_year').value == $('oneOffDepartureDate_year').value )
            {
              if( ($('oneOffReturnDate_month').value - $('oneOffDepartureDate_month').value) < 0 )
              {
                alert('Your time of return cannot be earlier than your departure time.')
                return false
              }
              else if( $('oneOffReturnDate_month').value == $('oneOffDepartureDate_month').value )
              {
                if( ($('oneOffReturnDate_day').value - $('oneOffDepartureDate_day').value) < 0 )
                {
                  alert('Your time of return cannot be earlier than your departure time.')
                  return false
                }
                else if( $('oneOffReturnDate_day').value == $('oneOffDepartureDate_day').value )
                {
                  var departureTime = $('oneOffDepartureTime').value
                  var returnTime = $('oneOffReturnTime').value

                  // compare the time
                  if( departureTime.substring(6,8) == 'PM' && returnTime.substring(6,8) == 'AM' )
                  {
                    alert('Your time of return cannot be earlier than your departure time.')
                    return false
                  }
                  else if( departureTime.substring(6,8) == returnTime.substring(6,8) )
                  {
                    if( returnTime.substring(0,2) < departureTime.substring(0,2) )
                    {
                      alert('Your time of return cannot be earlier than your departure time.')
                      return false
                    }
                    else if( returnTime.substring(0,2) == departureTime.substring(0,2) )
                    {
                      if( returnTime.substring(3,5) <= departureTime.substring(3,5) )
                      {
                        alert('Your time of return cannot be earlier than your departure time.')
                        return false
                      }
                    }
                    else
                    {
                      return true
                    }
                  }
                  else
                  {
                    return true
                  }
                }
                else
                {
                  return true
                }
              }
              else
              {
                return true
              }
            }
            else
            {
              return true
            }
          }
        }
      }

      function initialiseCarpoolPage()
      {
        $('addCarpoolStep1FromExample').hide()
        $('addCarpoolStep1ToExample').hide()
        $('addingCarpoolContainer').hide()
        $('addCarpoolLeftStep2Commute').hide()
        $('addCarpoolLeftStep2OneOff').hide()

        $('departureSaturdayTime').disable()
        $('departureSaturdayInterval').disable()
        $('departureSundayTime').disable()
        $('departureSundayInterval').disable()
        $('returnSaturdayTime').disable()
        $('returnSaturdayInterval').disable()
        $('returnSundayTime').disable()
        $('returnSundayInterval').disable()
      }

      function addCarpoolListenForTripTypeSelection()
      {
        if( $('newCarpoolCommute2').checked )
        {
          $('addCarpoolLeftStep2OneOff').hide()
          $('addCarpoolLeftStep2Commute').show()
          $('newCarpoolOneOff2').checked = true
          $('newCarpoolCommute2').checked = false
          $('newCarpoolOneOff').checked = false
          $('newCarpoolCommute').checked = true
          commuteOrOneOff = 'commute'
          $('newCarpoolTripTypeFinal').value = 'commute'
        }
        if( $('newCarpoolOneOff').checked )
        {
          $('addCarpoolLeftStep2OneOff').show()
          $('addCarpoolLeftStep2Commute').hide()
          $('newCarpoolOneOff2').checked = true
          $('newCarpoolCommute2').checked = false
          $('newCarpoolOneOff').checked = false
          $('newCarpoolCommute').checked = true
          commuteOrOneOff = 'oneOff'
          $('newCarpoolTripTypeFinal').value = 'oneOff'
        }
      }

      function addCarpoolOneOffTripIsOneWay()
      {
        if( $('oneOffOneWay').checked )
        {
          $('oneOffReturnDate_day').disable()
          $('oneOffReturnDate_month').disable()
          $('oneOffReturnDate_year').disable()
          $('oneOffReturnTime').disable()
          $('oneOffReturnInterval').disable()
        }
        else
        {
          $('oneOffReturnDate_day').enable()
          $('oneOffReturnDate_month').enable()
          $('oneOffReturnDate_year').enable()
          $('oneOffReturnTime').enable()
          $('oneOffReturnInterval').enable()
        }
      }

      function addCarpoolListenForDateSelection()
      {
        if( $('departureMonday').checked )
        {
          $('departureMondayTime').enable()
          $('departureMondayInterval').enable()
        }
        else
        {
          $('departureMondayTime').disable()
          $('departureMondayInterval').disable()
        }

        if( $('departureTuesday').checked )
        {
          $('departureTuesdayTime').enable()
          $('departureTuesdayInterval').enable()
        }
        else
        {
          $('departureTuesdayTime').disable()
          $('departureTuesdayInterval').disable()
        }

        if( $('departureWednesday').checked )
        {
          $('departureWednesdayTime').enable()
          $('departureWednesdayInterval').enable()
        }
        else
        {
          $('departureWednesdayTime').disable()
          $('departureWednesdayInterval').disable()
        }

        if( $('departureThursday').checked )
        {
          $('departureThursdayTime').enable()
          $('departureThursdayInterval').enable()
        }
        else
        {
          $('departureThursdayTime').disable()
          $('departureThursdayInterval').disable()
        }

        if( $('departureFriday').checked )
        {
          $('departureFridayTime').enable()
          $('departureFridayInterval').enable()
        }
        else
        {
          $('departureFridayTime').disable()
          $('departureFridayInterval').disable()
        }

        if( $('departureSaturday').checked )
        {
          $('departureSaturdayTime').enable()
          $('departureSaturdayInterval').enable()
        }
        else
        {
          $('departureSaturdayTime').disable()
          $('departureSaturdayInterval').disable()
        }

        if( $('departureSunday').checked )
        {
          $('departureSundayTime').enable()
          $('departureSundayInterval').enable()
        }
        else
        {
          $('departureSundayTime').disable()
          $('departureSundayInterval').disable()
        }

        if( $('returnMonday').checked )
        {
          $('returnMondayTime').enable()
          $('returnMondayInterval').enable()
        }
        else
        {
          $('returnMondayTime').disable()
          $('returnMondayInterval').disable()
        }

        if( $('returnTuesday').checked )
        {
          $('returnTuesdayTime').enable()
          $('returnTuesdayInterval').enable()
        }
        else
        {
          $('returnTuesdayTime').disable()
          $('returnTuesdayInterval').disable()
        }

        if( $('returnWednesday').checked )
        {
          $('returnWednesdayTime').enable()
          $('returnWednesdayInterval').enable()
        }
        else
        {
          $('returnWednesdayTime').disable()
          $('returnWednesdayInterval').disable()
        }

        if( $('returnThursday').checked )
        {
          $('returnThursdayTime').enable()
          $('returnThursdayInterval').enable()
        }
        else
        {
          $('returnThursdayTime').disable()
          $('returnThursdayInterval').disable()
        }

        if( $('returnFriday').checked )
        {
          $('returnFridayTime').enable()
          $('returnFridayInterval').enable()
        }
        else
        {
          $('returnFridayTime').disable()
          $('returnFridayInterval').disable()
        }

        if( $('returnSaturday').checked )
        {
          $('returnSaturdayTime').enable()
          $('returnSaturdayInterval').enable()
        }
        else
        {
          $('returnSaturdayTime').disable()
          $('returnSaturdayInterval').disable()
        }

        if( $('returnSunday').checked )
        {
          $('returnSundayTime').enable()
          $('returnSundayInterval').enable()
        }
        else
        {
          $('returnSundayTime').disable()
          $('returnSundayInterval').disable()
        }
      }

      function addCarpoolNextStep()
      {
        var errors = 'You have yet to specify the following compulsory items:\n\n'
        var moveOn = true

        if( $('newCarpoolType').value == '' )
        {
          errors += 'Whether you are a driver, passenger or cab pooler\n'
          moveOn = false
        }
        if( $('newCarpoolFromLat').value == '' || $('newCarpoolFromLng').value == '' )
        {
          errors += 'Your departure location\n'
          moveOn = false
        }
        if( $('newCarpoolToLat').value == '' || $('newCarpoolToLng').value == '' )
        {
          errors += 'Your destination\n'
          moveOn = false
        }
        
        if( moveOn )
        {
          if( commuteOrOneOff == 'oneOff' )
          {
            $('addCarpoolLeftStep1').hide()
            $('addCarpoolLeftStep2Commute').hide()
            $('addCarpoolLeftStep2OneOff').show()
          }
          else
          {
            $('addCarpoolLeftStep1').hide()
            $('addCarpoolLeftStep2Commute').show()
            $('addCarpoolLeftStep2OneOff').hide()
          }
        }
        else
        {
          alert(errors)
        }
      }

      function addCarpoolPreviousStep()
      {
        $('addCarpoolLeftStep2Commute').hide()
        $('addCarpoolLeftStep2OneOff').hide()
        $('addCarpoolLeftStep1').show()
      }

      function newCarpoolFromFocus()
      {
        $('addCarpoolStep1FromExample').show()
        $('addCarpoolStep1FromBlank').hide()
      }

      function newCarpoolFromBlur()
      {
        $('addCarpoolStep1FromExample').hide()
        $('addCarpoolStep1FromBlank').show()
      }

      function newCarpoolToFocus()
      {
        $('addCarpoolStep1ToExample').show()
        $('addCarpoolStep1ToBlank').hide()
      }

      function newCarpoolToBlur()
      {
        $('addCarpoolStep1ToExample').hide()
        $('addCarpoolStep1ToBlank').show()
      }

      // initialise the map when user wants to add a carpool listing
      function loadCarpoolMap()
      {
          if (GBrowserIsCompatible())
          {
              // GMap2 of the route map.
              map = new GMap2(document.getElementById("addCarpoolMap"))
              // map.setMapType(G_PHYSICAL_MAP);
              map.setCenter(new GLatLng(1.360117, 103.829041), 10)
              map.addControl(new GSmallZoomControl3D())

              geocoder = new GClientGeocoder()
              geocoder.setBaseCountryCode("SG")
          }
      }

      // a flag variable to note if user is defining the start or end location of
      // a new carpool listing
      var currentlyDefiningStartOrEnd

      // method that is called to start geocoding a user's start/end location input
      function addListingGeocodeJourneyPoint(address, type)
      {
        if( trim(address).length == 0 )
        {
          if(type == 'from')
          {
            newCarpoolStart = null
            $('newCarpoolFromLat').value = ''
            $('newCarpoolFromLng').value = ''
          }
          if(type == 'to')
          {
            newCarpoolEnd = null
            $('newCarpoolToLat').value = ''
            $('newCarpoolToLng').value = ''
          }
          resetMarkers()
          return
        }
        currentlyDefiningStartOrEnd = type
        geocoder.getLocations( address, addListingProcessGeocodeResult )
      }

      // method that is called to handle the geocoding response from Google after
      // user enters a start/end location when adding a carpool listing
      function addListingProcessGeocodeResult(response)
      {
        map.clearOverlays();
        if (!response || response.Status.code != 200)
        {
          // this chunk of code accounts for the scenario whereby the location
          // cannot be geocoded.
          alert('We were unable to find such a place.\nPlease refine the way this location is specified.')
          if(currentlyDefiningStartOrEnd == 'from')
          {
            newCarpoolStart = null
            addCarpoolPreviousStep()
            $('newCarpoolFrom').value = ''
            $('newCarpoolFromLat').value = ''
            $('newCarpoolFromLng').value = ''
            $('newCarpoolFrom').focus()
          }
          if(currentlyDefiningStartOrEnd == 'to')
          {
            newCarpoolEnd = null
            addCarpoolPreviousStep()
            $('newCarpoolTo').value = ''
            $('newCarpoolToLat').value = ''
            $('newCarpoolToLng').value = ''
            $('newCarpoolTo').focus()
          }
          resetMarkers()
          return
        }
        else
        {
          place = response.Placemark[0]

          var lat = place.Point.coordinates[1]
          var lng = place.Point.coordinates[0]

          if( lat < 1.145248 || lat > 1.496717 || lng < 103.570862 || lng > 104.063187 )
          {
            // this chunk of code accounts for the scenario whereby the location is
            // NOT in Singapore
            alert('We were unable to find such a place in Singapore.\nPlease refine the way this location is specified.')
            if(currentlyDefiningStartOrEnd == 'from')
            {
              newCarpoolStart = null
              addCarpoolPreviousStep()
              $('newCarpoolFrom').value = ''
              $('newCarpoolFromLat').value = ''
              $('newCarpoolFromLng').value = ''
              $('newCarpoolFrom').focus()
            }
            if(currentlyDefiningStartOrEnd == 'to')
            {
              newCarpoolEnd = null
              addCarpoolPreviousStep()
              $('newCarpoolTo').value = ''
              $('newCarpoolToLat').value = ''
              $('newCarpoolToLng').value = ''
              $('newCarpoolTo').focus()
            }
            resetMarkers()
            return
          }
          else
          {
            // Create custom marker icon
            var startIcon = new GIcon(G_DEFAULT_ICON);
            startIcon.image = "/ProjectAmity/images/amity/darkgreen_MarkerA.png";
            var endIcon = new GIcon(G_DEFAULT_ICON);
            endIcon.image = "/ProjectAmity/images/amity/darkgreen_MarkerB.png";

            // Set up our GMarkerOptions object
            var startMarkerOptions = { icon:startIcon };
            var endMarkerOptions = { icon:endIcon };

            var point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0])
            if(currentlyDefiningStartOrEnd == 'from')
            {
              newCarpoolStart = new GMarker(point, startMarkerOptions)
              map.addOverlay(newCarpoolStart)
              if( newCarpoolEnd )
              {
                map.addOverlay(newCarpoolEnd)
              }
              $('newCarpoolFrom').value = place.address
              $('newCarpoolFromLat').value = place.Point.coordinates[1]
              $('newCarpoolFromLng').value = place.Point.coordinates[0]
            }
            else if(currentlyDefiningStartOrEnd == 'to')
            {
              newCarpoolEnd = new GMarker(point, endMarkerOptions)
              if( newCarpoolStart )
              {
                map.addOverlay(newCarpoolStart)
              }
              map.addOverlay(newCarpoolEnd)
              $('newCarpoolTo').value = place.address
              $('newCarpoolToLat').value = place.Point.coordinates[1]
              $('newCarpoolToLng').value = place.Point.coordinates[0]
            }
            var directions = new GDirections(map)
            directions.load("from: " + newCarpoolStart.getLatLng() + " to: " + newCarpoolEnd.getLatLng() )
            GEvent.addListener(directions , "error" , resetMarkers);
            if( newCarpoolStart && newCarpoolEnd )
            {
              map.clearOverlays()
            }
          }
        }
      }

      // this function is called in case the markers in the add listing map need to be reset
      function resetMarkers()
      {
        map.clearOverlays()
        if( newCarpoolStart )
        {
          map.addOverlay(newCarpoolStart)
        }
        if( newCarpoolEnd )
        {
          map.addOverlay(newCarpoolEnd)
        }
        map.setCenter(new GLatLng(1.360117, 103.829041), 10)
      }

      function trim(str, chars)
      {
        return ltrim(rtrim(str, chars), chars);
      }

      function ltrim(str, chars)
      {
        chars = chars || "\\s";
        return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
      }

      function rtrim(str, chars)
      {
        chars = chars || "\\s";
        return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
      }

      function addCarpoolSaving()
      {
        $('addingCarpoolContainer').show()
        $('addCarpoolRight').hide()
        $('addCarpoolLeftStep2Commute').hide()
        $('addCarpoolLeftStep2OneOff').hide()

        window.scrollTo(0,0)
      }

      function addCarpoolSaved(response)
      {
        if(response!= null || response.responseText != null)
        {
          var content = response.responseText

          if( content == 'T' )
          {
            $('addingCarpoolContainer').hide()
            window.location="/ProjectAmity/carpoolListing/index"
            alert('Your new carpool listing has been saved!')
          }
          else if( content == 'F' )
          {
            initialiseCarpoolPage()
            alert('An error occured while we tried to save your listing. Please try again soon!')
          }
          else
          {
            initialiseCarpoolPage()
            alert(content)
          }
        }
        else
        {
          initialiseCarpoolPage()
          alert('An error occured while we tried to save your listing. Please try again soon!')
        }

      }

    </script>

  </head>
  
  <body class="thrColFixHdr" onload="initialiseCarpoolPage(); loadCarpoolMap()" onunload="GUnload()">

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
        
        <div id="navi">Welcome, <a href="#">${session.user.name}</a>.&nbsp;
          <g:if test="${params.messageModuleUnreadMessages > 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
          </g:if>
          <g:elseif test="${params.messageModuleUnreadMessages == 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
          </g:elseif>
          <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
        </div>
        
        <div id="mainContent" style="min-height: 800px">

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
          <resource:autoComplete skin="default" />
          <br/>

          <div id="addCarpool">
            <g:formRemote name="addCarpoolStep1" url="[action: 'ajaxAddListing']"
              onLoading="addCarpoolSaving()"
              onSuccess="addCarpoolSaved(e)"
              onFailure="addCarpoolSaved(e)">
            <fieldset id="addCarpoolLeftStep1" style="border: 0px solid black; width: 55%; float: left">
              <h1>Add Carpool Listing</h1>
              <br/>
                <table border="0" width="100%">
                  <tr>
                    <td colspan="3"><p><b>Step 1 - Tell us where you are going!</b></p></td>
                  </tr>
                  <tr>
                    <td colspan="3" width="30%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="30%"><p><b>You are a: </b></p></td>
                    <td>
                      <g:select name="newCarpoolType" from="${['Driver', 'Passenger', 'Cab Pooler']}" noSelection="['':'Select a Type']"/>
                      <g:hiddenField id="newCarpoolTripTypeFinal" name="newCarpoolTripTypeFinal" value="commute" />
                    </td>
                  </tr>
                  <tr>
                    <td colspan="3" width="30%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="30%"><p><b>You shall depart from: </b></p></td>
                    <td>
                      <g:textField name="newCarpoolFrom" size="40" onfocus="newCarpoolFromFocus()" onblur="newCarpoolFromBlur(); addListingGeocodeJourneyPoint(this.value, 'from')" />
                      <g:hiddenField id="newCarpoolFromLat" name="newCarpoolFromLat" value="" />
                      <g:hiddenField id="newCarpoolFromLng" name="newCarpoolFromLng" value="" />
                    </td>
                  </tr>
                  <tr id="addCarpoolStep1FromBlank">
                    <td colspan="3" width="30%">&nbsp;</td>
                  </tr>
                  <tr id="addCarpoolStep1FromExample">
                    <td><em>Example:</em></td>
                    <td><em>21 Tampines Avenue 1</em></td>
                  </tr>
                  <tr>
                    <td width="30%"><p><b>You are heading to: </b></p></td>
                    <td>
                      <g:textField name="newCarpoolTo" size="40" onfocus="newCarpoolToFocus()" onblur="newCarpoolToBlur(); addListingGeocodeJourneyPoint(this.value, 'to')" />
                      <g:hiddenField id="newCarpoolToLat" name="newCarpoolToLat" value="" />
                      <g:hiddenField id="newCarpoolToLng" name="newCarpoolToLng" value="" />
                    </td>
                  </tr>
                  <tr id="addCarpoolStep1ToBlank">
                    <td colspan="3" width="30%">&nbsp;</td>
                  </tr>
                  <tr id="addCarpoolStep1ToExample">
                    <td><em>Example:</em></td>
                    <td><em>Kallang, Singapore</em></td>
                  </tr>
                  <tr>
                    <td colspan="3"><p><b>You are only one step away!</b></p></td>
                  </tr>
                  <tr>
                    <td colspan="3" width="30%">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="3"><p><b><a href="#"  onClick="addCarpoolNextStep(); return false">Next Step</a></b></p></td>
                  </tr>
                </table>
            </fieldset>
            <fieldset id="addCarpoolLeftStep2Commute" style="border: 0px solid black; width: 55%; float: left">
              <h1>Add Carpool Listing</h1>
              <br/>
                <table border="0" width="100%">
                  <tr>
                    <td colspan="4"><p><b>Step 2 - Tell us about when your trip shall be!</b></p></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%"><p><b>This is a: </b></p></td>
                    <td colspan="3">
                      <label for="newCarpoolOneOff"><input type="radio" id="newCarpoolOneOff" name="newCarpoolTripType" value="one-off" onClick="addCarpoolListenForTripTypeSelection()" /> One-off trip</label>
                      &nbsp;<label for="newCarpoolCommute"><input type="radio" id="newCarpoolCommute" name="newCarpoolTripType" value="commute" checked onClick="addCarpoolListenForTripTypeSelection()" /> Commute</label>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td rowspan="7" width="20%"><p><b>Departure Schedule:</b></p></td>
                    <td width="30%"><label for="departureMonday"><g:checkBox id="departureMonday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="monday" /> Monday</label></td>
                    <td width="25%"><g:select name="departureMondayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureMondayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="departureTuesday"><g:checkBox id="departureTuesday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="tuesday" /> Tuesday</label></td>
                    <td width="25%"><g:select name="departureTuesdayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureTuesdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="departureWednesday"><g:checkBox id="departureWednesday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="wednesday" /> Wednesday</label></td>
                    <td width="25%"><g:select name="departureWednesdayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureWednesdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="departureThursday"><g:checkBox id="departureThursday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="thursday" /> Thursday</label></td>
                    <td width="25%"><g:select name="departureThursdayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureThursdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="departureFriday"><g:checkBox id="departureFriday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="friday" /> Friday</label></td>
                    <td width="25%"><g:select name="departureFridayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureFridayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="departureSaturday"><g:checkBox id="departureSaturday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="saturday" checked="false" /> Saturday</label></td>
                    <td width="25%"><g:select name="departureSaturdayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureSaturdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="departureSunday"><g:checkBox id="departureSunday" onClick="addCarpoolListenForDateSelection()" name="departureDays" value="sunday" checked="false" /> Sunday</label></td>
                    <td width="25%"><g:select name="departureSundayTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="departureSundayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td rowspan="7" width="20%"><p><b>Return Schedule:</b></p></td>
                    <td width="30%"><label for="returnMonday"><g:checkBox id="returnMonday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="monday" /> Monday</label></td>
                    <td width="25%"><g:select name="returnMondayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnMondayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="returnTuesday"><g:checkBox id="returnTuesday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="tuesday" /> Tuesday</label></td>
                    <td width="25%"><g:select name="returnTuesdayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnTuesdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="returnWednesday"><g:checkBox id="returnWednesday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="wednesday" /> Wednesday</label></td>
                    <td width="25%"><g:select name="returnWednesdayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnWednesdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="returnThursday"><g:checkBox id="returnThursday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="thursday" /> Thursday</label></td>
                    <td width="25%"><g:select name="returnThursdayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnThursdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="returnFriday"><g:checkBox id="returnFriday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="friday" /> Friday</label></td>
                    <td width="25%"><g:select name="returnFridayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnFridayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="returnSaturday"><g:checkBox id="returnSaturday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="saturday" checked="false" /> Saturday</label></td>
                    <td width="25%"><g:select name="returnSaturdayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnSaturdayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td width="30%"><label for="returnSunday"><g:checkBox id="returnSunday" onClick="addCarpoolListenForDateSelection()" name="returnDays" value="sunday" checked="false" /> Sunday</label></td>
                    <td width="25%"><g:select name="returnSundayTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="25%"><g:select name="returnSundayInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%"><p><b>Additional Notes:</b></p></td>
                    <td colspan="3">
                      <g:textArea name="newCarpoolNotes" value="" rows="5" cols="30" maxlength="200" />
                      <p><em>HTML tags are not allowed.</em></p>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="4"><p><b>You are only one step away!</b></p></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="2" style="text-align: center"><p><b><a href="#"  onClick="addCarpoolPreviousStep(); return false">Previous Step</a></b></p></td>
                    <td colspan="2" style="text-align: center"><input type="submit" id="btnAddCarpoolStep1" onClick="return validateAddCarpoolForm()" value="Save New Listing" /></td>
                  </tr>
                </table>
            </fieldset>
            <fieldset id="addCarpoolLeftStep2OneOff" style="border: 0px solid black; width: 55%; float: left">
              <h1>Add Carpool Listing</h1>
              <br/>
                <table border="0" width="100%">
                  <tr>
                    <td colspan="4"><p><b>Step 2 - Tell us about when your trip shall be!</b></p></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%"><p><b>This is a: </b></p></td>
                    <td colspan="3">
                      <label for="newCarpoolOneOff2"><input type="radio" id="newCarpoolOneOff2" name="newCarpoolTripType2" value="one-off2" checked onClick="addCarpoolListenForTripTypeSelection()" /> One-off trip</label>
                      &nbsp;<label for="newCarpoolCommute2"><input type="radio" id="newCarpoolCommute2" name="newCarpoolTripType2" value="commute2" onClick="addCarpoolListenForTripTypeSelection()" /> Commute</label>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%"><p><b>Departure Schedule:</b></p></td>
                    <td width="40%"><g:datePicker name="oneOffDepartureDate" value="${new Date() + 1}" precision="day" years="${[2010, 2011, 2012]}"/></td>
                    <td width="20%"><g:select name="oneOffDepartureTime" value="06:30 AM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="20%"><g:select name="oneOffDepartureInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%">&nbsp;</td>
                    <td colspan="3"><label for="oneOffOneWay"><g:checkBox id="oneOffOneWay" onClick="addCarpoolOneOffTripIsOneWay()" name="oneOffOneWay" value="true" checked="false" />&nbsp;This is a one-way trip.</label></td>
                  </tr>
                  <tr>
                    <td width="20%"><p><b>Return Schedule:</b></p></td>
                    <td width="40%"><g:datePicker name="oneOffReturnDate" value="${new Date() + 1}" precision="day" years="${[2010, 2011, 2012]}"/></td>
                    <td width="20%"><g:select name="oneOffReturnTime" value="05:00 PM" from="${['12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '01:00 AM', '01:15 AM', '01:30 AM', '01:45 AM', '02:00 AM', '02:15 AM', '02:30 AM', '02:45 AM', '03:00 AM', '03:15 AM', '03:30 AM', '03:45 AM', '04:00 AM', '04:15 AM', '04:30 AM', '04:45 AM', '05:00 AM', '05:15 AM', '05:30 AM', '05:45 AM', '06:00 AM', '06:15 AM', '06:30 AM', '06:45 AM', '07:00 AM', '07:15 AM', '07:30 AM', '07:45 AM', '08:00 AM', '08:15 AM', '08:30 AM', '08:45 AM', '09:00 AM', '09:15 AM', '09:30 AM', '09:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM', '01:00 PM', '01:15 PM', '01:30 PM', '01:45 PM', '02:00 PM', '02:15 PM', '02:30 PM', '02:45 PM', '03:00 PM', '03:15 PM', '03:30 PM', '03:45 PM', '04:00 PM', '04:15 PM', '04:30 PM', '04:45 PM', '05:00 PM', '05:15 PM', '05:30 PM', '05:45 PM', '06:00 PM', '06:15 PM', '06:30 PM', '06:45 PM', '07:00 PM', '07:15 PM', '07:30 PM', '07:45 PM', '08:00 PM', '08:15 PM', '08:30 PM', '08:45 PM', '09:00 PM', '09:15 PM', '09:30 PM', '09:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM']}" /></td>
                    <td width="20%"><g:select name="oneOffReturnInterval" from="${['± 15 mins', '± 30 mins', '± 45 mins', '± 1 hour']}" /></td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td width="20%"><p><b>Additional Notes:</b></p></td>
                    <td colspan="3">
                      <g:textArea name="newCarpoolNotesOneOff" value="" rows="5" cols="30" maxlength="200"/>
                      <p><em>HTML tags are not allowed.</em></p>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="4">&nbsp;</td>
                  </tr>
                  <tr>
                    <td colspan="2" style="text-align: center"><p><b><a href="#" onClick="addCarpoolPreviousStep(); return false">Previous Step</a></b></p></td>
                    <td colspan="2" style="text-align: center"><input type="submit" id="btnAddCarpoolStep1" onClick="return validateAddCarpoolForm()" value="Save New Listing" /></td>
                  </tr>
                </table>
              </g:formRemote>
            </fieldset>
            <div id="addCarpoolRight" style="border: 0px solid black; width: 40%; float: right; overflow: hidden">
              <div id="addCarpoolMap" style="float: right; width: 290px; height: 290px"></div>
            </div>
          </div>
          
          <div id="addingCarpoolContainer" style="text-align: center">
            <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Saving your carpool listing</h1>
            <h3>Please wait...</h3>
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