package app

class CarpoolListing
{

    static constraints =
    {
        departureMondayTime(nullable:true)
        departureMondayInterval(nullable:true)
        departureTuesdayTime(nullable:true)
        departureTuesdayInterval(nullable:true)
        departureWednesdayTime(nullable:true)
        departureWednesdayInterval(nullable:true)
        departureThursdayTime(nullable:true)
        departureThursdayInterval(nullable:true)
        departureFridayTime(nullable:true)
        departureFridayInterval(nullable:true)
        departureSaturdayTime(nullable:true)
        departureSaturdayInterval(nullable:true)
        departureSundayTime(nullable:true)
        departureSundayInterval(nullable:true)

        returnMondayTime(nullable:true)
        returnMondayInterval(nullable:true)
        returnTuesdayTime(nullable:true)
        returnTuesdayInterval(nullable:true)
        returnWednesdayTime(nullable:true)
        returnWednesdayInterval(nullable:true)
        returnThursdayTime(nullable:true)
        returnThursdayInterval(nullable:true)
        returnFridayTime(nullable:true)
        returnFridayInterval(nullable:true)
        returnSaturdayTime(nullable:true)
        returnSaturdayInterval(nullable:true)
        returnSundayTime(nullable:true)
        returnSundayInterval(nullable:true)

        oneOffDepartureTime(nullable:true)
        oneOffDepartureInterval(nullable:true)
        oneOffReturnTime(nullable:true)
        oneOffReturnInterval(nullable:true)

        dateDeactivated(nullable:true)
    }
    
    static belongsTo = [ resident: Resident ]
    static hasMany = [ carpoolRequests : CarpoolRequest ]

    String riderType // driver, passenger or cabpooler?

    String startAddress
    String startLatitude
    String startLongitude

    String endAddress
    String endLatitude
    String endLongitude

    String tripType // commute or oneOff?

    String departureMondayTime
    int departureMondayInterval
    String departureTuesdayTime
    int departureTuesdayInterval
    String departureWednesdayTime
    int departureWednesdayInterval
    String departureThursdayTime
    int departureThursdayInterval
    String departureFridayTime
    int departureFridayInterval
    String departureSaturdayTime
    int departureSaturdayInterval
    String departureSundayTime
    int departureSundayInterval

    String returnMondayTime
    int returnMondayInterval
    String returnTuesdayTime
    int returnTuesdayInterval
    String returnWednesdayTime
    int returnWednesdayInterval
    String returnThursdayTime
    int returnThursdayInterval
    String returnFridayTime
    int returnFridayInterval
    String returnSaturdayTime
    int returnSaturdayInterval
    String returnSundayTime
    int returnSundayInterval

    Date oneOffDepartureTime // consists of year, month, day, hour, minute
    int oneOffDepartureInterval
    Date oneOffReturnTime // consists of year, month, day, hour, minute
    int oneOffReturnInterval

    String notes // additional notes
    String status // active or inactive?

    Date dateDeactivated

}