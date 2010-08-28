package app

class Report {

    static constraints = {
                neaOfficer(nullable:true)
        resolvedDescription(nullable:true)
        resolvedImage(nullable:true)
    }

    Date datePosted
    String image
    String title
    String description
    String resolvedImage
    String resolvedDescription
    String category

    String status
    double latitude
    double longitude
    double altitude
    String moderationStatus
    static belongsTo = [ resident : Resident, neaOfficer : NEAOfficer ]
}
