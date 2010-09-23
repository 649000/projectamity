package app

class PlacesOfInterestService {

    static transactional = true

    def serviceMethod() {

    }

    def ArrayList getPlaces()
    {
        def retrievedContent;

        def destinationList = new ArrayList<Destination>()

        try {
            URL _url = new URL("https://api.projectnimbus.org/stbodataservice.svc/PlaceSet");
            URLConnection _urlConn = _url.openConnection();

            _urlConn.setRequestProperty("accept", "*/*");

            _urlConn.addRequestProperty("AccountKey", "cxa2010");
            _urlConn.addRequestProperty("UniqueUserID", "98765432145269875154125015201452");

            retrievedContent = _urlConn.content.text

            def xml = new XmlSlurper().parseText(retrievedContent);

            xml.entry.each {
                def destinationz = new Destination()
                destinationz.name = it.content.properties.Name.text()
                destinationz.longitude = it.content.properties.Longitude.text()
                destinationz.latitude = it.content.properties.Latitude.text()
                destinationz.type = "Place of Interest"
                destinationList.add(destinationz)
            }

        } catch (Exception e) {
            println(e);
        }


       // println(retrievedContent);
        return destinationList

    }

}