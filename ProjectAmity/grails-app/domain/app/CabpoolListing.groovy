package app

class CabpoolListing
{

    static constraints =
    {
        
    }

    String latitude
    String longitude
    Date timeStamp
    String destination

    static belongsTo = [ resident : Resident ]
    
}
