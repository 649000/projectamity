<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>View Carpool Listing</title>

    <g:javascript library="application" />
    <modalbox:modalIncludes />

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <!-- <script type="text/javascript" src="${resource(dir: 'js', file: 'carpoolScripts.js')}" ></script> -->

    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />

    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAd5BRV15joT1H3f6yJabmLBQ1iQbuBnc0I-d59E6wLYQh5E96wBQPHyvvuDQI6z3-Mfm_roriueiGig" type="text/javascript"></script>

    <script type="text/javascript">

      function loadListingMap(startLat, startLong, endLat, endLong)
      {
          if (GBrowserIsCompatible())
          {
              // GMap2 of the route map.
              var map = new GMap2(document.getElementById("viewListingMap"))
              map.setCenter(new GLatLng(1.360117, 103.829041), 11)
              map.addControl(new GLargeMapControl3D())
              map.addControl(new GMapTypeControl())

              var start = new GLatLng(startLat,startLong)
              var end = new GLatLng(endLat, endLong)
              var directions = new GDirections(map)
              directions.load("from: " + start + " to: " + end )
          }
      }

      function checkIfCarpoolRequestExists()
      {
        Modalbox.show('<div><p>Please wait...</p></div>', {title: 'Processing Your Request', width: 500,
                                                           overlayClose: false, closeValue: '× Close',
                                                           params: $('createRequestHiddenForm').serialize() });

        var address = '<g:createLink action="checkIfCarpoolRequestExists"/>'
        address += '?listingID=' +${l.id}

        new Ajax.Request(address,
                            {
                              onLoading: function()
                                        {
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          Modalbox.hide( {afterHide: function()
                                                          {
                                                            if( response.responseText == 'F' )
                                                            {
                                                              response.responseText = null
                                                              Modalbox.show('/ProjectAmity/carpoolListing/createRequest.gsp', {  title: 'Send Carpool Request to ${l.resident.name}', width: 500, overlayClose: false, closeValue: '× Close'  });
                                                            }
                                                            else if( response.responseText == 'T' )
                                                            {
                                                              response.responseText = null
                                                              Modalbox.show('<div><p>You have already sent ${l.resident.name} a carpool request for this listing and it is still pending.</p><br/><p style=\"text-align: center\"><a href=\"#\" onclick=\'Modalbox.hide(); return false\'>OK</a></p></div>', {title: 'Sending Not Allowed', width: 500, overlayClose: false, closeValue: '× Close'});
                                                              Modalbox.resizeToContent()
                                                            }
                                                          }})
                                        },
                              onFailure: function(response)
                                        {
                                          // ModalBox.hide()
                                          Modalbox.show('<div><p>Owing to technical issues, we currently cannot allow you to send ${l.resident.name} a carpool request. Please try again soon!</p><br/><p style=\"text-align: center\"><a href=\"#\" onclick=\'Modalbox.hide(); return false\'>OK</a></p></div>', {title: 'Technical Error', width: 500, overlayClose: false, closeValue: '× Close'});
                                          Modalbox.resizeToContent()
                                        }
                            }
                        );
      }

      function triggerDeactivateForm(start, end, id)
      {
        $('rstartAddress').value = start
        $('rendAddress').value = end
        $('rlistingId').value = id

        Modalbox.show('/ProjectAmity/carpoolListing/deactivateListing.gsp', {  title: 'Deactivate Carpool Listing', width: 500, overlayClose: false, closeValue: '× Close', params: $('deactivateForm').serialize(), afterHide: function(){location.reload(true)} });
      }

    </script>

    <style type="text/css">

      

    </style>

  </head>

  <body class="thrColFixHdr" onload="loadListingMap(${l.startLatitude}, ${l.startLongitude}, ${l.endLatitude}, ${l.endLongitude})" onunload="GUnload()">

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

        <div id="mainContent" style="min-height: 1080px">

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
          <br/>
          <g:if test="${l.status == 'inactive'}">
            <div id="carpoolDeactivated" style="background-color: #FFEBE8; margin-bottom: 20px; text-align: center; vertical-align: middle; height: 50px">
              <table border="0" style="margin-left: auto; margin-right: auto; margin-top: 5px">
                <tr>
                  <td width="30px"><img style="border: 0px" src="/ProjectAmity/images/amity/carpooldeactivated.png" alt="Deactivated Listing" title="Deactivated Listing" width="30px" /></td>
                  <td><h3>&nbsp;This carpool listing is no longer active.</h3></td>
                </tr>
              </table>
            </div>
          </g:if>
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
            <h2>Actions</h2>
            <br/>
            <form method="get" id="createRequestHiddenForm">
              <input type="hidden" name="recipientName" value="${l.resident.name}" />
              <input type="hidden" name="recipientUserid" value="${l.resident.userid}" />
              <input type="hidden" name="listingId" value="${l.id}" />
              <g:if test="${l.tripType == 'commute'}">
                <input type="hidden" name="subject" value="Commute from ${l.startAddress} to ${l.endAddress}" />
              </g:if>
              <g:else>
                <input type="hidden" name="subject" value="One-off Trip from ${l.startAddress} to ${l.endAddress}" />
              </g:else>
              <g:if test="${l.riderType == 'Passenger'}">
                <input type="hidden" name="message" value="I saw the carpool listing you posted and I can be your driver." />
              </g:if>
              <g:elseif test="${l.riderType == 'Driver'}">
                <input type="hidden" name="message" value="I saw the carpool listing you posted and I can be your passenger." />
              </g:elseif>
              <g:else>
                <input type="hidden" name="message" value="I saw the carpool listing you posted and I can share a cab with you." />
              </g:else>
            </form>
            <form method="get" id="deactivateForm">
              <input type="hidden" id="rstartAddress" name="startAddress" value="" />
              <input type="hidden" id="rendAddress" name="endAddress" value="" />
              <input type="hidden" id="rlistingId" name="listingId" value="" />
            </form>
            <g:if test="${!session.user}">
              <g:if test="${l.status == 'active'}">
                <p><b><a href="#" onClick="alert('You need to log in in order to do this!'); return false">Send Carpool Request</a></b></p>
              </g:if>
              <g:else>
                <p><b>Not applicable for a deactivated carpool listing.</b></p>
              </g:else>
            </g:if>
            <g:elseif test="${session.user.userid != l.resident.userid}">
              <g:if test="${l.status == 'active'}">
                <g:if test="${params.requested == 'F'}">
                  <p><b><a href="#" title="Send Carpool Request to ${l.resident.name}" onclick=" checkIfCarpoolRequestExists(); return false "> Send Carpool Request</a></b></p>
                </g:if>
                <g:elseif test="${params.requested == 'A'}">
                  <p><b>You are already a confirmed carpool for this carpool listing.</b></p>
                </g:elseif>
                <g:else>
                  <p><b>You have already sent a request for this carpool listing and it is still pending.</b></p>
                </g:else>
              </g:if>
              <g:else>
                <p><b>Not applicable for a deactivated carpool listing.</b></p>
              </g:else>
            </g:elseif>
            <g:else>
              <g:if test="${l.status == 'active'}">
                <p><a href="#" onClick="alert('Edit'); return false"><img style="border: 0px" src="/ProjectAmity/images/amity/carpooledit.png" alt="Edit Listing" title="Edit Listing" width="12px" />&nbsp;<b>Edit Listing</b></a></p>
                <p><a href="/ProjectAmity/carpoolListing/match/${l.id}"><img style="border: 0px" src="/ProjectAmity/images/amity/carpoolmatch.png" alt="Find Matches" title="Find Matches" width="12px" />&nbsp;<b>Find Matches</b></a></p>
                <p><a href="#" onClick="triggerDeactivateForm('${l.startAddress}', '${l.endAddress}', '${l.id}'); return false"><img style="border: 0px" src="/ProjectAmity/images/amity/carpooldelete.png" alt="Delete Matches" title="Delete Matches" width="12px" />&nbsp;<b>Deactivate Listing</b></a></p>
              </g:if>
              <g:else>
                <p><b>Not applicable for a deactivated carpool listing.</b></p>
              </g:else>
            </g:else>
            <br/>
            <h2>${l.resident.name}</h2>
            <br/>
            <g:if test="${!session.user}">
              <p><b>User ID:</b><br/>${l.resident.userid}<br/><b><a href="#" onClick="alert('You need to log in in order to do this!'); return false">Send Private Message</a></b></p>
            </g:if>
            <g:elseif test="${session.user.userid != l.resident.userid}">
              <p><b>User ID:</b><br/>${l.resident.userid}<br/><a href="/ProjectAmity/carpoolListing/createMessage.gsp" title="Send Message to ${l.resident.name}" onclick="Modalbox.show(this.href, {  title: this.title, width: 500, params: Form.serialize('createRequestHiddenForm'), overlayClose: false, closeValue: '× Close'  }); return false; "><b>Send Private Message</b></a></p>
            </g:elseif>
            <g:else>
              <p><b>User ID:</b><br/>${l.resident.userid}<br/><b>(You)</b></p>
            </g:else>
            <br/>
            <p><b>Reputation:</b><br/>${params.positives} thumbs up<br/>${params.negatives} thumbs down</p>
            <br/>
            <h2>Confirmed Carpoolers</h2>
            <br/>
            <g:if test="${params.confirmedRiders}">
              <g:each var="r" in="${params.confirmedRiders}">
                <p>${r.resident.name} (<b>${r.resident.userid}</b>)</p>
              </g:each>
            </g:if>
            <g:else>
              <p>None</p>
            </g:else>
          </div>
          <div id="viewCarpoolDetails" style="border: 0px solid black; width: 70%; float: right">
            <div id="viewCarpoolShare" style="border: 0px solid black; width: 250px; margin-top: 5px; float: right">
              <!-- AddThis Button BEGIN -->
              <a class="addthis_button" href="http://www.addthis.com/bookmark.php?v=250&amp;username=projectamity"><img src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125" height="16" alt="Bookmark and Share" style="border:0"/></a>
              <script type="text/javascript">var addthis_config = {"data_track_clickback":true};</script>
              <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=projectamity"></script>
              <!-- AddThis Button END -->
            </div>
            <h2>About this Carpool</h2>
            <br/>
            <table border="0" cellspacing="0" width="100%">
              <tr>
                <td style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Route Map</h3></td>
              </tr>
            </table>
            <div id="viewListingMap" style="width: 100%; height: 250px; border: 0px solid black"></div>
            <br/>
            <table border="0" cellspacing="0" width="100%">
              <tr>
                <td colspan="7" style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Departure</h3></td>
              </tr>
              <g:if test="${l.tripType == 'commute'}">
                <tr style="text-align: center; vertical-align: middle; font-weight: bold">
                  <td>Mon</td>
                  <td>Tue</td>
                  <td>Wed</td>
                  <td>Thu</td>
                  <td>Fri</td>
                  <td>Sat</td>
                  <td>Sun</td>
                </tr>
                <tr style="text-align: center; vertical-align: middle">
                  <td>${l.departureMondayTime}</td>
                  <td>${l.departureTuesdayTime}</td>
                  <td>${l.departureWednesdayTime}</td>
                  <td>${l.departureThursdayTime}</td>
                  <td>${l.departureFridayTime}</td>
                  <td>${l.departureSaturdayTime}</td>
                  <td>${l.departureSundayTime}</td>
                </tr>
                <tr style="text-align: center; vertical-align: middle">
                  <g:if test="${l.departureMondayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureMondayInterval}">
                    <td>± ${l.departureMondayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.departureTuesdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureTuesdayInterval}">
                    <td>± ${l.departureTuesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.departureWednesdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureWednesdayInterval}">
                    <td>± ${l.departureWednesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.departureThursdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureThursdayInterval}">
                    <td>± ${l.departureThursdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.departureFridayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureFridayInterval}">
                    <td>± ${l.departureFridayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.departureSaturdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureSaturdayInterval}">
                    <td>± ${l.departureSaturdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.departureSundayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.departureSundayInterval}">
                    <td>± ${l.departureSundayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                </tr>
              </g:if>
              <g:else>
                <tr>
                  <td colspan="7" style="text-align: center; vertical-align: middle"><p>${params.departureTiming}</p></td>
                </tr>
              </g:else>
              <tr>
                <td colspan="7" style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Return</h3></td>
              </tr>
              <g:if test="${l.tripType == 'commute'}">
                <tr style="text-align: center; vertical-align: middle; font-weight: bold">
                  <td>Mon</td>
                  <td>Tue</td>
                  <td>Wed</td>
                  <td>Thu</td>
                  <td>Fri</td>
                  <td>Sat</td>
                  <td>Sun</td>
                </tr>
                <tr style="text-align: center; vertical-align: middle">
                  <td>${l.returnMondayTime}</td>
                  <td>${l.returnTuesdayTime}</td>
                  <td>${l.returnWednesdayTime}</td>
                  <td>${l.returnThursdayTime}</td>
                  <td>${l.returnFridayTime}</td>
                  <td>${l.returnSaturdayTime}</td>
                  <td>${l.returnSundayTime}</td>
                </tr>
                <tr style="text-align: center; vertical-align: middle">
                  <g:if test="${l.returnMondayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnMondayInterval}">
                    <td>± ${l.returnMondayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.returnTuesdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnTuesdayInterval}">
                    <td>± ${l.returnTuesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.returnWednesdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnWednesdayInterval}">
                    <td>± ${l.returnWednesdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.returnThursdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnThursdayInterval}">
                    <td>± ${l.returnThursdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.returnFridayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnFridayInterval}">
                    <td>± ${l.returnFridayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.returnSaturdayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnSaturdayInterval}">
                    <td>± ${l.returnSaturdayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                  <g:if test="${l.returnSundayInterval == '60'}">
                    <td>± 1 hour</td>
                  </g:if>
                  <g:elseif test="${l.returnSundayInterval}">
                    <td>± ${l.returnSundayInterval} minutes</td>
                  </g:elseif>
                  <g:else>
                    <td></td>
                  </g:else>
                </tr>
              </g:if>
              <g:else>
                <tr>
                  <td colspan="7" style="text-align: center; vertical-align: middle"><p>${params.returnTiming}</p></td>
                </tr>
              </g:else>
            </table>
            <br/>
            <table border="0" cellspacing="0" width="100%">
              <tr>
                <td colspan="5" style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Places Near the Destination</h3></td>
              </tr>
              <tr style="text-align: center; vertical-align: middle">
                <td width="25%"><em>1.29km away</em></td>
                <td width="25%"><em>1.29km away</em></td>
                <td width="25%"><em>1.29km away</em></td>
                <td width="25%"><em>1.29km away</em></td>
              </tr>
              <tr style="text-align: center; vertical-align: middle">
                <td>
                  <img src="http://maps.google.com/maps/api/staticmap?size=125x125&markers=color:green|label:1|1.356374,103.944561&zoom=14&sensor=false" />
                </td>
                <td>
                  <img src="http://maps.google.com/maps/api/staticmap?size=125x125&markers=color:green|label:2|1.3528955,103.9448539&zoom=14&sensor=false" />
                </td>
                <td>
                  <img src="http://maps.google.com/maps/api/staticmap?size=125x125&markers=color:green|label:3|1.3523183,103.9434035&zoom=14&sensor=false" />
                </td>
                <td>
                  <img src="http://maps.google.com/maps/api/staticmap?size=125x125&markers=color:green|label:4|1.37802,103.955007&zoom=14&sensor=false" />
                </td>
              </tr>
              <tr style="text-align: center; vertical-align: middle">
                <td width="25%"><b>Tampines Regional Library</b><br/>(Library)</td>
                <td width="25%"><b>Tampines</b><br/>(Cinema)</td>
                <td width="25%"><b>Century Cineplex</b><br/>(Cinema)</td>
                <td width="25%"><b>Escape Theme Park</b><br/>(Place of Interest)</td>
              </tr>
            </table>
            <br/>
            <table border="0" cellspacing="0" width="100%" style="overflow: hidden; table-layout: fixed">
              <tr>
                <td style="text-align: center; vertical-align: middle; background-color: #E6F0D2"><h3>Additional Notes from ${l.resident.name}</h3></td>
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