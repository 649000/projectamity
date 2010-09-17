package app
import grails.converters.JSON

class BarterController {

    def index = {
    }

    def create = {
        println(params)
        params.itemStartDate=new Date()
        params.itemEndDate=new Date() + Integer.parseInt(params.itemTime)

        params.resident="2"

        if(params.itemCategory=="Vehicles") {
            params.itemCategory2 = "Automotive"
        } else if(params.itemCategory=="Automotive Parts") {
            params.itemCategory2 = "Automotive"
        } else if(params.itemCategory=="Automotive Accessories") {
            params.itemCategory2 = "Automotive"
        }

        else if(params.itemCategory=="Baby Clothes & Shoes") {
            params.itemCategory2 = "Baby Care"
        } else if(params.itemCategory=="Baby Food") {
            params.itemCategory2 = "Baby Care"
        } else if(params.itemCategory=="Other Baby Care Products") {
            params.itemCategory2 = "Baby Care"
        }

        else if(params.itemCategory=="Children's Books") {
            params.itemCategory2 = "Books & Stationery"
        } else if(params.itemCategory=="Comics") {
            params.itemCategory2 = "Books & Stationery"
        } else if(params.itemCategory=="Computer, IT , Internet") {
            params.itemCategory2 = "Books & Stationery"
        } else if(params.itemCategory=="Fiction Books") {
            params.itemCategory2 = "Books & Stationery"
        } else if(params.itemCategory=="Stationery") {
            params.itemCategory2 = "Books & Stationery"
        }

        else if(params.itemCategory=="Men's Clothes") {
            params.itemCategory2 = "Clothing, Shoes & Accessories"
        } else if(params.itemCategory=="Women's Clothes") {
            params.itemCategory2 = "Clothing, Shoes & Accessories"
        } else if(params.itemCategory=="Maternity Clothes") {
            params.itemCategory2 = "Clothing, Shoes & Accessories"
        } else if(params.itemCategory=="Clothing Accessories") {
            params.itemCategory2 = "Clothing, Shoes & Accessories"
        } else if(params.itemCategory=="Shoes") {
            params.itemCategory2 = "Clothing, Shoes & Accessories"
        }

        else if(params.itemCategory=="Men's Clothes") {
            params.itemCategory2 = "Furniture"
        } else if(params.itemCategory=="Women's Clothes") {
            params.itemCategory2 = "Gardening & Plants"
        } else if(params.itemCategory=="Maternity Clothes") {
            params.itemCategory2 = "Other Home & Gardening Items"
        }

        else if(params.itemCategory=="Music") {
            params.itemCategory2 = "Music & Multimedia"
        } else if(params.itemCategory=="Video") {
            params.itemCategory2 = "Music & Multimedia"
        } else if(params.itemCategory=="Musical Instruments") {
            params.itemCategory2 = "Music & Multimedia"
        } else if(params.itemCategory=="Video Games") {
            params.itemCategory2 = "Music & Multimedia"
        }

        else if(params.itemCategory=="Bath & Body") {
            params.itemCategory2 = "Health & Beauty"
        } else if(params.itemCategory=="Beauty Tools") {
            params.itemCategory2 = "Health & Beauty"
        } else if(params.itemCategory=="Other Health & Beauty Items") {
            params.itemCategory2 = "Health & Beauty"
        }

         else if(params.itemCategory=="Sporting Goods") {
            params.itemCategory2 = "Sports"
        }

         else if(params.itemCategory=="All") {
            params.itemCategory2 = "Miscellaneous"
        }

        else if(params.itemCategory=="Television & Monitors") {
            params.itemCategory2 = "Electronics"
        } else if(params.itemCategory=="Mobile Devices") {
            params.itemCategory2 = "Electronics"
        } else if(params.itemCategory=="Household Appliances") {
            params.itemCategory2 = "Electronics"
        } else if(params.itemCategory=="Computer Peripherals") {
            params.itemCategory2 = "Electronics"
        } else if(params.itemCategory=="Other electronic items") {
            params.itemCategory2 = "Electronics"
        }

        else if(params.itemCategory=="Plushies") {
            params.itemCategory2 = "Collectables"
        } else if(params.itemCategory=="Coins") {
            params.itemCategory2 = "Collectables"
        } else if(params.itemCategory=="Antiques") {
            params.itemCategory2 = "Collectables"
        } else if(params.itemCategory=="Toys") {
            params.itemCategory2 = "Collectables"
        } else if(params.itemCategory=="Stamps") {
            params.itemCategory2 = "Collectables"
        } else if(params.itemCategory=="Art") {
            params.itemCategory2 = "Collectables"
        } else if(params.itemCategory=="Other Collectable items") {
            params.itemCategory2 = "Collectables"
        }

        def toBeSaved = new Barter(params)
        toBeSaved.save()
        println("=== ERROR ===" + toBeSaved.getErrors() )
    }

    def searchcategory = {
        def catList=Category.findAllByKeyword(params.itemCategorySearch)
        render catList as JSON
    }

    def list = {
        //println(params.items.replaceAll("\\+", " "))
        def residentNo="1"
        def barterList=Barter.findAllByItemCategory2AndResidentNot(params.items.replaceAll("\\+", " "), residentNo)
        println(barterList.size())
        render barterList as JSON
    }

    def listyouritems = {
        def barterList=Barter.findAllByResident(params.resident)
        render barterList as JSON
    }

    def createRequest = {
        params.requestStatus="0"
        def toBeSaved = new BarterRequest(params)
        toBeSaved.save()
        println("=== ERROR ===" + toBeSaved.getErrors() )
    }

    def listRequest = {
        println("this one"+params)
        def toBeRetrieved = BarterRequest.createCriteria().list
        {
            eq ("partytwo", params.resident)
            or {
                eq ("barterAction", "Trade with items")
                eq ("barterAction", "Give away")
            }
        }
        println (toBeRetrieved)
        render toBeRetrieved as JSON
    }

}
