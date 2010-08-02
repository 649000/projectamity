package app

class IndoorReport {

    static constraints = {
    }
    Date datePosted
    String image
    String title
    String description
    String status
    String moderationStatus
    String resolvedImage
    String resolvedDescription

    static belongsTo = [ resident : Resident, building:Building, neaOfficer : NEAOfficer ]

}
