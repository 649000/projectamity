package app

class Barter {

    static constraints = {
        itemName (nullable:false)
        itemPhoto (nullable:false)
        itemCondition (nullable:true)
        itemValue (nullable:true)
        itemCategory (nullable:false)
        itemCategory2 (nullable:false)
        itemDescription (nullable:true)
        itemStartAction (nullable:false)
        itemEndDate (nullable:false)
        resident (nullable:false)
    }

    String itemName
    String itemPhoto
    String itemCondition
    double itemValue
    String itemCategory
    String itemCategory2
    String itemDescription
    String itemStartAction
    Date itemEndDate
    String itemStatus

    static belongsTo = [ resident: Resident ]
}
