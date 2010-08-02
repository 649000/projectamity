    var centerPoint = new GLatLng(1.354625, 103.816681);
    var zoom = 11;
    var polygon;

    function init(response)
    {

reportObj = eval( '(' + response.responseText + ')')


if (GBrowserIsCompatible()) {


      map = new GMap2(document.getElementById("map"))
        map.enableContinuousZoom();
        map.enableScrollWheelZoom();
        map.setMapType(G_HYBRID_MAP);
        map.setCenter(centerPoint, zoom);
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());

 
      GPolygon.prototype.Contains = function(point) {
        var j=0;
        var oddNodes = false;
        var x = point.lng();
        var y = point.lat();
        for (var i=0; i < this.getVertexCount(); i++) {
          j++;
          if (j == this.getVertexCount()) {j = 0;}
          if (((this.getVertex(i).lat() < y) && (this.getVertex(j).lat() >= y))
          || ((this.getVertex(j).lat() < y) && (this.getVertex(i).lat() >= y))) {
            if ( this.getVertex(i).lng() + (y - this.getVertex(i).lat())
            /  (this.getVertex(j).lat()-this.getVertex(i).lat())
            *  (this.getVertex(j).lng() - this.getVertex(i).lng())<x ) {
              oddNodes = !oddNodes
            }
          }
        }
        return oddNodes;
      }

      GPolyline.prototype.Contains = GPolygon.prototype.Contains;

var aroundarea=new Array()
for(var i=0; i<reportObj.length;i++)
    {
        var coordinates = new GLatLng(reportObj[i].latitude, reportObj[i].longitude)
    drawCircle(coordinates, reportObj[i].maprang)
        if (polygon.Contains(coordinates)) {
          aroundarea.push(reportObj[i].userid+'-'+report[i].destination)
        }
    }

//alert(aroundarea)

sendResultsBackToServer(aroundarea)

    }

function drawCircle(center, radius)
{

    var circlePoints=new Array()
    var polyOptions = {geodesic:true}
with (Math) {

		var rLat = (radius/3963.189) * (180/PI);
		var rLng = rLat/cos(center.lat() *(PI/180));

		for (var a = 0 ; a < 361 ; a++ ) {
			var aRad = a*(PI/180);
			var x = center.lng() + (rLng *cos(aRad));
			var y = center.lat() + (rLat *sin(aRad));

                        var point = new GLatLng(parseFloat(y),parseFloat(x),true, polyOptions);

			circlePoints.push(point);
		}
	}


    var strokeColor='#FF0000'
    var strokeWeight=4
    var strokeOpacity=1
    var fillColor='#0000FF'
    var fillOpacity=0.2

   polygon=new GPolygon(circlePoints, strokeColor, strokeWeight, strokeOpacity, fillColor, fillOpacity)

map.addOverlay(polygon)

}

}