package app

class Building {

    static constraints = {
    }

    String postalCode
    double latitude
    double longitude
    double xcoordinate
    double ycoordinate
    double zcoordinate
    String level
    String stairwell
     static hasMany = [ indoorReport : IndoorReport]
}
