package app

class NEAOfficer {

    static constraints = {
        latitude(nullable:true)
        longitude(nullable:true)
        mLogin(nullable:true)
    }
    static hasMany = [ reports : Report, indoorReports: IndoorReport  ]

    String userid
    String password
    String name
    String mLogin
    String phoneNumber
    double latitude
    double longitude
}
