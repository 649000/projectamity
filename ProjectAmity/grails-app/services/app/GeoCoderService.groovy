package app

class GeoCoderService {

    static transactional = true

    def serviceMethod() {

    }

    def String[] getCoordinates (String address)
    {
        String[] toReturn  = new String[2]
        //String[0] = latitude
        //String[1] = longitude
        try{
            URL _url = new URL("http://maps.google.com/maps/api/geocode/xml?address=" + address.encodeAsURL() + "&sensor=false");
            URLConnection _urlConn = _url.openConnection();
            def retrievedContent = _urlConn.content.text
           

            def xml = new XmlSlurper().parseText(retrievedContent);
            toReturn[0]= xml.result.geometry.location.lat.text()
            toReturn[1]= xml.result.geometry.location.lng.text()

        }
        catch (Exception e)
        {
            println(e)
        }
        return toReturn
    }

    def String getAddress (double latitude, double longitude)
    {
                String toReturn  = ""
        //String[0] = latitude
        //String[1] = longitude
        try{
            URL _url = new URL("http://maps.google.com/maps/api/geocode/xml?latlng=" + latitude.encodeAsURL()+","+ longitude.encodeAsURL() + "&sensor=false");
            URLConnection _urlConn = _url.openConnection();
            def retrievedContent = _urlConn.content.text

            def xml = new XmlSlurper().parseText(retrievedContent);
            toReturn= xml.result[0].formatted_address.text()
        }
        catch (Exception e)
        {
            println(e)
        }
        return toReturn
    }
}
