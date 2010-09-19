package app

class CarpoolRequest
{

    static constraints =
    {
    }

    String status
    Message message
    
    static belongsTo = [ carpoolListing : CarpoolListing, resident : Resident ]

}
