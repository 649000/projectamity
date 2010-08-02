package app

class NEAOfficer {

    static constraints = {
    }
    static hasMany = [ reports : Report, indoorReports: IndoorReport  ]

    String userid
    String password
}
