var germ=null
var reponseObj = null
var buildingObj = null
var reportObj= null
var buildingInfoObj = null
var grabbedPlacemark = null
var buildingData=[];
var myPlacemark = null

function InitGermanium(response)
{
    $('test').innerHTML= "Please click on the placemark to view reports."
    responseObj = eval( '(' + response.responseText + ')')
    buildingObj = responseObj[0]
    reportObj = responseObj[1]
    buildingInfoObj = responseObj[2]
    Germanium.CreateInstance("myGerm", InitSuccessCallback, InitFailureCallback);
}

function InitSuccessCallback(obj)
{
    germ = obj
    var postalCode = buildingObj[0].postalCode
    //germ.Load("http://www.germanium3d.com/static/sample/" + postalCode + ".xlcl", null, LoadOk, LoadFail);
    germ.Load("http://www.germanium3d.com/static/sample/generic_building/generic_building.xlcl", null, LoadOk, LoadFail);

    for(var i=0;i<buildingObj.size();i++){
        // Create a placemark
        var myPlacemark = germ.CreatePlacemark("Level : " + buildingObj[i].level)
        // Set placemark position
        var point = germ.CreatePointGeometry()

        point.SetPosition(buildingObj[i].xcoordinate, buildingObj[i].ycoordinate, buildingObj[i].zcoordinate)
        myPlacemark.SetContent( "<font color =\"white\">|" +buildingObj[i].level +"|" + buildingObj[i].stairwell+ "|</font>")

        //      var whiteLabelStyle =  germ.CreateLabelStyle()
        //      whiteLabelStyle.SetTextColor("8A2BE2")
        //      var myStyleset = germ.CreateStyleSet()
        //      myStyleset.SetLabelStyle(whiteLabelStyle)
        //      myPlacemark.SetStyleSet(myStyleset)

        myPlacemark.SetGeometry(point)
        // Add placemark to the scene
        germ.AddPlacemark(myPlacemark)
    }
    germ.AddEventHandler(Germanium.Event.OnPlacemarkActivated, loadReports)


// create a placemark for drag & drop to get coords
//var pmark = germ.CreatePlacemark("Item to drag and drop");
//germ.AddPlacemark(pmark);
//germ.AddEventHandler(Germanium.Event.OnPlacemarkActivated, GetPlacemarkForDragAndDrop);
//germ.AddEventHandler(Germanium.Event.OnMouseUp, DropPlacemark);
}

function loadReports(event)
{
    $('test').innerHTML=""
    grabbedPlacemark = event.placemark
    var toSplitPlacemark = grabbedPlacemark.GetContent()
    var splittedPlacemark = toSplitPlacemark.split("|")
    //splittedPlacemark[1] = Level name
    //splittedPlacemark[2] = Stairwell location
    //splittedBuilding[0] = postal code
    //splittedBuilding[1] = level
    //splittedBuilding[2] = stairwell

    for(var j=0;j<buildingInfoObj.size();j++){
        var tobeSplittedbuilding = buildingInfoObj[j]
        var splittedBuilding = tobeSplittedbuilding.split("|")
        if(splittedBuilding[1]==splittedPlacemark[1] && splittedBuilding[2]==splittedPlacemark[2] )
        {
            for(var k=0;k<reportObj[j].size();k++){

             //   alert(reportObj[j][k].title)
              var splitteddate = reportObj[j][k].datePosted.split("T")
              reportObj[j][k].image
            $('test').innerHTML+= '<p>Title: </p><p>' + reportObj[j][k].title +'</p><p>Date: </p><p>' + splitteddate[0]+'</p><p>' + '<img width=\"240px\" src="/ProjectAmity/indoorreportimages/'+ reportObj[j][k].image+'"  </a>' +'</p><p>Description: </p><p>' + reportObj[j][k].description+'</p><p>Status:</p><p>' + reportObj[j][k].status +'</p></br><center>---</center>'
            }
        }
    }


}

function InitFailureCallback(message, code)
{
    alert("Germanium initialization failed: " + message + "; error code: " + code)
}


function LoadOk()
{
// Will be called if loading finishes successfully
//alert(a.type)
//alert('Loading successful!');
}

function LoadFail(errMsg, errCode)
{
    // Will be called if loading fails
    alert('Loading failed. Error code: ' + errCode + '; message: ' + errMsg)
}

//function GetPlacemarkForDragAndDrop(event)
//{
//   grabbedPlacemark = event.placemark;
//
//   // prevent scene navigation
//   germ.SetNavigationMode(Germanium.Navigation.Disabled);
//
//   // move placemark along with cursor
//   germ.AddEventHandler(Germanium.Event.OnMouseMove, DragPlacemark);
//}
//
//function DragPlacemark(event)
//{
//   // compute the 3D coordinates at which the mouse button is released
//   var coords = germ.GetEye().Compute3DIntersection(event.windowX, event.windowY);
//
//   // put grabbedPlacemark at the computed coordinates
//   grabbedPlacemark.GetGeometry().SetPosition(coords);
//}
//function DropPlacemark(event)
//{
//   if (grabbedPlacemark != null)
//   {
//      // stop moving placemark along with cursor
//      germ.RemoveEventHandler(Germanium.Event.OnMouseMove, DragPlacemark);
//
//      // compute the 3D coordinates at which the mouse button is released
//      var coords = germ.GetEye().Compute3DIntersection(event.windowX, event.windowY);
//
//      alert(coords)
//
//      // put grabbedPlacemark at the computed coordinates
//      grabbedPlacemark.GetGeometry().SetPosition(coords);
//
//      // reset grabbedPlacemark
//      grabbedPlacemark = null;
//
//      // resume scene navigation
//      germ.SetNavigationMode(Germanium.Navigation.Orbit);
//   }
//}
