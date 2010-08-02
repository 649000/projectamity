package app

class CarpoolListing {

    static constraints = {
    }
    static belongsTo = [resident: Resident]

    String startAddress
    String startLatitude
    String startLongitude

    String endAddress
    String endLatitude
    String endLongitude

    String departureTime
    String returnTime

    String frequency // which days of the week?
    String type // type of person the Resident is --> LOOKING FOR <-- driver or passenger or cab pooling
    String status // pending or accepted?

}
