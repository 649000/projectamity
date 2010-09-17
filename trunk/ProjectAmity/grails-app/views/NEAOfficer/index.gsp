<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Location-Based Report Administration</title>

    <script src="http://api.germanium3d.com/?v=1.4&key=0c1db0e05cd88587a664a659962b25c0"></script>
    <script src="http://maps.google.com/maps?file=api&amp;v=3&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
          srLJJlcXUmLMTM2KkMsePdU1A"
  type="text/javascript"></script>
    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript">

      //google.load("maps", "2");
      var map = null
      var markers
      var reportObj = null
      var outdoorReport = null
      var indoorReport = null
      var toGeoCode = null

      function initUnmod(response)
      {
          reportObj = eval( '(' + response.responseText + ')')
          outdoorReport = reportObj[0]
          indoorReport = reportObj[1]
          //$('moderateUpdate').fade();
        //  new Effect.Opacity('moderateUpdate', { from: 1.0, to: 0.7, duration: 0.1 })
         $('moderateUpdate').innerHTML = '<br/><p>Moderation status has been updated!</p>'
         $('moderateUpdate').hide();


          var centerPoint = new GLatLng(1.354625, 103.816681);
          var zoom = 11;


          if (GBrowserIsCompatible()==true)
          {
              map = new GMap2(document.getElementById("map"))
              map.enableContinuousZoom();
              map.enableScrollWheelZoom();
              map.setMapType(G_HYBRID_MAP);
              map.setCenter(centerPoint, zoom);
              map.addControl(new GLargeMapControl());
              map.addControl(new GMapTypeControl());

              loadMarkers()
          }

      }

      function loadMarkers()
      {

          $('outdoorList').innerHTML = ''
          $('indoorList').innerHTML = ''

          if(outdoorReport.size() > 0)
          {
              //Traverse outdoorReport and print out all the markers
              var outdoorHTML = '<h2>Outdoor Reports</h2><br/>' +
                                '<table>' +
                                '<tr><td align="center"><b>Title</b></td><td align="center"><b>Image</b></td><td align="center"><b>Description</b></td><td align="center"><b>Location</b></td><td align="center"><b>Approve</b></td><td align="center"><b>Reject</b></td></tr>' +
                                '<tr><td align="center">&nbsp;</td><td align="center"></td><td align="center"></td></tr>'

              for (var i=0;i < outdoorReport.size() ; i++)
              {
                  var coordinates = new GLatLng(outdoorReport[i].latitude, outdoorReport[i].longitude);
                  var marker = new GMarker(coordinates);
                  marker.bindInfoWindowHtml('<p><b>' + outdoorReport[i].title + '</b></p><br/>' +
                      '<p><img src="/ProjectAmity/outdoorreportimages/'+ outdoorReport[i].image+'" width="200"/></p><br/>' +
                      '<p>' + outdoorReport[i].description + '</p><br/>' +
                      '<table><tr><td width=\"100\"><a href=\"#\" onClick=\"updateOutdoorReport(\'' + outdoorReport[i].id + '\', \'true\'); return false\">Approve</a></td><td width=\"100\"><a href=\"#\" onClick=\"updateOutdoorReport(\'' + outdoorReport[i].id + '\', \'rejected\'); return false\">Reject</a></td></tr></table>');

                  outdoorHTML += '<tr><td width="150px">' + outdoorReport[i].title + '</td><td align="center" width="150px"><img src="/ProjectAmity/outdoorreportimages/' + outdoorReport[i].image+ '" width="100"/></td><td width="250px"> ' + outdoorReport[i].description + ' </td><td align="center" width="150px"><img src="http://maps.google.com/maps/api/staticmap?markers=' + outdoorReport[i].latitude + ',' + outdoorReport[i].longitude + '&zoom=12&size=130x130&sensor=false" /></td><td align="center" width="100px"><a href=\"#\" onClick=\"updateOutdoorReport(\'' + outdoorReport[i].id + '\', \'true\'); return false\">Approve</a></td><td align="center" width="100px"><a href=\"#\" onClick=\"updateOutdoorReport(\'' + outdoorReport[i].id + '\', \'rejected\'); return false\">Reject</a></td></tr>'

                  map.addOverlay(marker);
              }

              outdoorHTML += '</table>'
              $('outdoorList').innerHTML = outdoorHTML
          }


          if(indoorReport.size() > 0)
          {
              var geocode = new GClientGeocoder();
              geocode.setBaseCountryCode("SG");

              var indoorHTML = '<h2>Indoor Reports</h2><br/>' +
                      '<table>' +
                      '<tr><td align="center"><b>Title</b></td><td align="center"><b>Image</b></td><td align="center"><b>Description</b></td><td align="center"><b>Location</b></td><td align="center"><b>Approve</b></td><td align="center"><b>Reject</b></td></tr>' +
                      '<tr><td align="center">&nbsp;</td><td align="center"></td><td align="center"></td></tr>'

              for(var k=0 ; k < indoorReport.size(); k++)
              {
                  var _coordinates = new GLatLng(indoorReport[k][2], indoorReport[k][3])
                  var _marker = new GMarker(_coordinates)

                  _marker.bindInfoWindowHtml("<p><b>" + indoorReport[k][1] + "</b></p><br/>" +
                      "<p><b>Location:</b> S(" + indoorReport[k][0] + ")</p><br/>" +
                      '<p><img src="/ProjectAmity/indoorreportimages/'+ indoorReport[k][4] + '" width="200"/></p><br/>' +
                      '<p>' + indoorReport[k][5] + '</p><br/>' +
                      "<table><tr><td width=\"100\"><a href=\"#\" onClick=\"updateIndoorReport(\'" + indoorReport[k][6] + "\', \'true\'); return false\">Approve</a></td><td width=\"100\"><a href=\"#\" onClick=\"updateIndoorReport(\'" + indoorReport[k][6] + "\', \'rejected\'); return false\">Reject</a></td></tr></table>")

                  indoorHTML += '<tr><td width="150px">' + indoorReport[k][1] + '</td><td align="center" width="150px"><img src="/ProjectAmity/indoorreportimages/' + indoorReport[k][4] + '" width="100"/></td><td width="250px"> ' + indoorReport[k][5] + ' </td><td align="center" width="150px"><img src="http://maps.google.com/maps/api/staticmap?markers=' + indoorReport[k][2] + ',' + indoorReport[k][3] + '&zoom=12&size=130x130&sensor=false" /></td><td align="center" width="100px"><a href=\"#\" onClick=\"updateIndoorReport(\'' + indoorReport[k][6] + '\', \'true\'); return false\">Approve</a></td><td align="center" width="100px"><a href=\"#\" onClick=\"updateIndoorReport(\'' + indoorReport[k][6] + '\', \'rejected\'); return false\">Reject</a></td></tr>'

                  map.addOverlay(_marker)
              }

              indoorHTML += '</table>'
              $('indoorList').innerHTML = indoorHTML
          }

          $('map').hide()
          $('outdoorList').show()
          $('indoorList').show()

      }

      function updateOutdoorReport(id, status)
      {
          ${remoteFunction(action:'updateOutdoorModerationStatus', after: 'updateModerate()', params: ' \'id=\' + id + \'&status=\' + status  '  )}
      }

      function updateIndoorReport(id, status)
      {
          ${remoteFunction(action:'updateIndoorModerationStatus', after: 'updateModerate()', params: ' \'id=\' + id + \'&status=\' + status  '  )}
      }

      function updateModerate()
      {
       // $('moderateUpdate').innerHTML = '<br/><p>Moderation status has been updated!</p>'
       $('moderateUpdate').appear({ duration: 3.0 });
       Effect.Pulsate('moderateUpdate');
      setTimeout ( "hideModerate()", 4000 );
      }
      function hideModerate()
      {
       // setTimeout ( "setToBlack()", 2000 );
      // $('moderateUpdate').hide();
    $('moderateUpdate').hide();
      }

      function toggleControl(element)
      {
          $(element).toggle();
      }

    </script>



    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>

  </head>

  <body class="thrColFixHdr" onLoad="${remoteFunction(action:'listReports',onSuccess:'initUnmod(e)')}">

    <div class="wrapper">

    <div id="container">
      <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" id="logo"/>
      <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
      <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
      <img src="${resource(dir:'images/amity',file:'home.png')}" id="home" style="margin:70px 0px 0px 870px;"/>
      <img src="${resource(dir:'images/amity',file:'breport2.png')}" border="0" id="pageTitle"/>

      <div id="header">
        <h1>test</h1>
        <!-- end #header -->
      </div>

      <div id="banner">
        &nbsp;
      </div>

      <div id="navi">Welcome, <a href="#">${session.user.userid}</a>.&nbsp;
        <g:if test="${params.messageModuleUnreadMessages > 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
        </g:if>
        <g:elseif test="${params.messageModuleUnreadMessages == 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
        </g:elseif>
        <span id="navi2"><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
      </div>

      <div id="mainContent">

        <h1>Report Administration - Moderation</h1>


        <br/>
        <h3><a href="${createLink(controller: 'NEAOfficer', action:'index')}">Moderate Reports</a> | <a href="${createLink(controller: 'NEAOfficer', action:'investigate')}">Investigate Reports</a></h3>

        <div id="moderation">

          <br/>
          <g:form>
            <p><g:textField id="criteria" name="criteria" />&nbsp;<g:submitToRemote
                                                    url="[controller: 'NEAOfficer', action:'listReports']"
                                                    value="Search"
                                                    onSuccess="initUnmod(e)" /></p>

          </g:form>

          <p><br/><g:checkBox name="moderateShowMap" value="${false}" onClick="toggleControl('map'); toggleControl('outdoorList'); toggleControl('indoorList')" /> Show in Map</p>

          <div id="moderateUpdate"></div>

          <br/>

          <div id="map" style="width: 100%; height: 500px;"></div>
          <div id="outdoorList"></div>
          <br/>
          <div id="indoorList"></div>

        </div>

        <div id="unmoderated">
          
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