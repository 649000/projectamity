package app

class Resident
{


    String nric
    String name
    String address
    String addressLatitude
    String addressLongitude
    String postalCode
    String userid
    String password
    static hasMany = [ report: Report, indoorReport: IndoorReport, sentMessages : Message, receivedMessages : Message, carpoolListings : CarpoolListing ]
    static hasOne = [ cabpoolListing : CabpoolListing ]
    static mappedBy = [ sentMessages : 'sender', receivedMessages : 'receiver' ]

    static constraints = {

        userid(nullable:true)
    }

}
