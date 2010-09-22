<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
  <g:javascript library="scriptaculous" />
  <g:javascript library="prototype" />
  <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
26srLJJlcXUmLMTM2KkMsePdU1A" type="text/javascript"></script> 
  <script type="text/javascript">
function loadCarpoolMap()
{
    if (GBrowserIsCompatible())
    {
        // GMap2 of the route map.
        var map = new GMap2(document.getElementById("map"))
        map.setCenter(new GLatLng(1.360117, 103.829041), 11)
        map.addControl(new GLargeMapControl3D())
        map.addControl(new GMapTypeControl())
        map.enableContinuousZoom();
        map.enableScrollWheelZoom();
        map.setMapType(G_HYBRID_MAP);
        map.setCenter(centerPoint, zoom);
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
    }
}
  </script>
</head>
<body onload="loadCarpoolMap();">
  <h1>Sample line</h1>
  <div id="map" style="width: 600px; height: 400px;"></div>
</body>
</html>
