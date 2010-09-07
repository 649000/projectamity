//google.load("maps", "2");
var map=null;
var markers;
var reportObj = null;
var outdoorReport = null;
var indoorReport = null;
var toGeoCode=null;



function Init(response) {

    reportObj = eval( '(' + response.responseText + ')')
    outdoorReport = reportObj[0];
    indoorReport = reportObj[1];

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
    $('reportinfo').hide()
}

function changeDesc(desc)
{
    document.getElementById('thisdesc').innerHTML=desc
}

function changeimage(imagename)
{
    document.getElementById('thisimg').src=imagename
}

var allimageids=new Array()

function loadMarkers()
{
    //Traverse outdoorReport and print out all the markers

    for (var i=0;i<outdoorReport.size();i++)
    {
        var coordinates = new GLatLng(outdoorReport[i].latitude, outdoorReport[i].longitude);
        var marker = new GMarker(coordinates);

        //marker.bindInfoWindowHtml('<p><b>' + outdoorReport[i].title + '</b></p><p>' + '<img src="/ProjectAmity/outdoorreportimages/'+ outdoorReport[i].image+'" width="400"  height="300"</a>' + '</p><p>' + outdoorReport[i].description + '</p><p> Status: ' + outdoorReport[i].status +'</p>');
//marker.bindInfoWindowHtml(outdoorReport[i].title + '<img src="/ProjectAmity/outdoorreportimages/'+ outdoorReport[i].image+'" width="400"  height="300"</a>' + '</p><p>' + outdoorReport[i].description + '</p><p> Status: ' + outdoorReport[i].status +'</p>');

var html='';

html+='<div class=\"reportBubbleMain\">'
html+='<div class=\"reportBubbleHeader\"><b>'
html+=outdoorReport[i].title
html+='</b></div>'
html+='<div class=\"reportBubblePictureBefore\">'
html+='<img height=\"300\" id=\"thisimg\" width=\"400\" src="../outdoorreportimages/'
html+=outdoorReport[i].image
html+='\"/>'
html+='</div>'
if (outdoorReport[i].status ==("Resolved"))
    {
html+='<a href=\"#\" onclick=\'changeimage('
html+='\"'
html+='../outdoorreportimages/'
html+=outdoorReport[i].image
html+='\"'
html+='); changeDesc('
html+='\"'
html+=outdoorReport[i].description
html+='\"'
html+='); return false\'> '
html+='Before'
html+='</a>'
html+=' | '
html+='<a href=\"#\" onclick=\'changeimage('
html+='\"'
html+='../outdoorreportimages/'
html+=outdoorReport[i].resolvedImage
html+='\"'
html+='); changeDesc('
html+='\"'
html+=outdoorReport[i].resolvedDescription
html+='\"'
html+='); return false\'> '
html+='After'
html+='</a>'
html+=' | '
    } 
html+=' Status: '
html+=outdoorReport[i].status
html+='</div>'
html+='<div class="reportBubbleDesc" id=\"thisdesc\">'
html+=outdoorReport[i].description
html+='</div>'
html+='</div>'

marker.bindInfoWindowHtml(html)

        map.addOverlay(marker);
    }

    // println("Postal Code : " + confusingList[i][0] + "Amount of Reports: " + confusingList[i][1]

    var geocode = new GClientGeocoder();
    geocode.setBaseCountryCode("SG");

    for (var k=0; k<indoorReport.size(); k++)
    {

       //  alert(""+indoorReport[k][0]+" "+indoorReport[k][1]+" "+indoorReport[k][2]+" "+indoorReport[k][3])


          var _coordinates = new GLatLng(indoorReport[k][2], indoorReport[k][3])
          var _marker = new GMarker(_coordinates)

        

          _marker.bindInfoWindowHtml("<p><b>Postal Code: " + indoorReport[k][0] + "</b></p><p> Indoor Reports " +indoorReport[k][1] + " case(s) has been reported. </p><p> <a href=\"http://localhost:8080/ProjectAmity/building/index?postalCode="+ indoorReport[k][0] +"\">View</a> </p>")
           map.addOverlay(_marker)
//        toGeoCode = "Singapore " + indoorReport[k][0];
//        var casesReport = indoorReport[k][1] + " cases has been reported.";
//        alert(casesReport)
//
//        geocode.getLocations(toGeoCode,function loadMarkers2(response)
//        {
//
//            if ((response == null) || (response.Status.code != G_GEO_SUCCESS))
//            {
//                alert("\"" + toGeoCode + "\" not found");
//            }
//            else
//            {
//
//                var place = response.Placemark[0];
//                var point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
//                var marker = new GMarker(point, {
//                    title: place.address
//                } );
//                map.addOverlay(marker);
//                marker.bindInfoWindowHtml(casesReport);
//
//            }
//        })

    }
}

//function loadMarkers2(response)
//{
//    if ((response == null) || (response.Status.code != G_GEO_SUCCESS))
//    {
//        alert("\"" + toGeoCode + "\" not found");
//    }
//    else
//    {
//        //alert("boo")
//        var place = response.Placemark[0];
//        var point = new GLatLng(place.Point.coordinates[1], place.Point.coordinates[0]);
//        // _map.setCenter(point);
//        var marker = new GMarker(point, {
//            title: place.address
//        } );
//        map.addOverlay(marker);
//        marker.bindInfoWindowHtml(casesReport);
//    }
//}

function reportshowmap()
{
    $('map').show()
    $('reportinfo').hide()
}

function reporthidemap()
{
    $('map').hide()
    $('reportinfo').show()
}