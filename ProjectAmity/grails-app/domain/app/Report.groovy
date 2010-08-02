package app

class Report {

    static constraints = {
    }

    Date datePosted
    String image
    String title
    String description
    String resolvedImage
    String resolvedDescription

    String status
    double latitude
    double longitude
    double altitude
    String moderationStatus
    static belongsTo = [ resident : Resident, neaOfficer : NEAOfficer ]
}
