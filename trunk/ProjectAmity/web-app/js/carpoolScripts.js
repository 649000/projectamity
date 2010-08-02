function initialiseCarpoolPage()
{
    $('destinationAutoComplete').hide()
    $('saving').hide()
    $('map').hide()
}

function initialiseSearchPage()
{
    $('map').hide()
}

function toggleControl(element)
{
    $(element).toggle();
}

// Maps API JS
function loadCarpoolMap(startLat, startLong, endLat, endLong)
{
    if (GBrowserIsCompatible())
    {
        // GMap2 of the route map.
        var map = new GMap2(document.getElementById("map"))
        map.setCenter(new GLatLng(1.360117, 103.829041), 11)
        map.addControl(new GLargeMapControl3D())
        map.addControl(new GMapTypeControl())

        var start = new GLatLng(startLat,startLong)
        var end = new GLatLng(endLat, endLong)
        var directionsPanel = document.getElementById("directionsPane")
        var directions = new GDirections(map, directionsPanel)
        directions.load("from: " + start + " to: " + end )
    }
}

// AJAX Save
function prepareSaveListing()
{
    $('defineListingStatus').hide()
}

function updateListingSaveStatus(response)
{
    $('defineListingStatus').show()
    $('defineListingStatus').innerHTML = '<br/>' + response.responseText
}

function updateListingSaveStatus(response)
{
    $('defineListingStatus').show()
    $('defineListingStatus').innerHTML = '<br/>' + response.responseText
}