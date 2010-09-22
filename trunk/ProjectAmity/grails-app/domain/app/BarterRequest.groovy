package app

class BarterRequest {

    static constraints = {
        partyonename(nullable:true)
        partyonename(nullable:true)
    }

    String barterAction
    String partyone
    String partytwo
    String partyonename
    String partytwoname
    String requestStatus
    String involvedItems
}
