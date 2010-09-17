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
        itemEndAction (nullable:false)
        itemStartDate (nullable:false)
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
    String itemEndAction
    Date itemStartDate
    Date itemEndDate
    String resident
}
