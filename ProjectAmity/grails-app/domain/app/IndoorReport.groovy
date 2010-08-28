package app

class IndoorReport {

    static constraints = {
       
        resolvedDescription(nullable:true)
        resolvedImage(nullable:true)
        neaOfficer(nullable:true)
    }
    Date datePosted
    String image
    String title
    String description
    String status
    String moderationStatus
    String resolvedImage
    String resolvedDescription
    String category

    static belongsTo = [ resident : Resident, building:Building, neaOfficer : NEAOfficer ]

}
