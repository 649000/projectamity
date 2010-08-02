package app

class CinemaService {

    static transactional = true

    def serviceMethod() {

    }

    def ArrayList getCinemas()
    {
        def retrievedContent;

        def destinationList = new ArrayList<Destination>()

        try {
            URL _url = new URL("https://api.projectnimbus.org/coodataservice.svc/CinemaSet");
            URLConnection _urlConn = _url.openConnection();

            _urlConn.setRequestProperty("accept", "*/*");
            _urlConn.addRequestProperty("AccountKey", "cxa2010");
            _urlConn.addRequestProperty("UniqueUserID", "98000002145269875154125015201452");

            retrievedContent = _urlConn.content.text

            def xml = new XmlSlurper().parseText(retrievedContent);

            xml.entry.each {
                def destinationz = new Destination()
                destinationz.name = it.content.properties.Name.text()
                destinationz.longitude = it.content.properties.Longitude.text()
                destinationz.latitude = it.content.properties.Latitude.text()
                destinationList.add(destinationz)
            }

        } catch (Exception e) {
            println(e);
        }


       // println(retrievedContent);
        return destinationList

    }

}