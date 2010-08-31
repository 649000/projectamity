package app

class Resident {


    String nric
    String name
    String address
    String postalCode
    String userid
    String password
    static hasMany = [ report: Report, indoorReport: IndoorReport, sentMessages : Message, receivedMessages : Message ]
    static hasOne = [ carpoolListing : CarpoolListing, cabpoolListing : CabpoolListing ]
    static mappedBy = [ sentMessages : 'sender', receivedMessages : 'receiver' ]

    static constraints = {
    }

}
