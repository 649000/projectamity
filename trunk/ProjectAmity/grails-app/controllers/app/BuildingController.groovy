package app

import grails.converters.JSON

class BuildingController {

    def messageCheckingService

    def index = {

        //User will be redirected to this index closure

        //This controller will retrieve the building's info based on the postal code
        println("Params Received (Postal Code): " + params.postalCode)
        session.postalCode = params.postalCode

        if(session.user != null)
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            [params : params]
        }
    }


    def loadBuilding =
    {
        //Retrieve building info based on postal code to load building
        def _building = Building.findAllByPostalCode(session.postalCode)
        //Store all the indoor reports
        def reportList = new ArrayList()
        def buildingInfoList = new ArrayList()

        for(Building b: _building)
        {
            def r = IndoorReport.findAllByBuilding(b)
            if(r.size() !=0)
            {
                String buildingInfo = b.postalCode + "|" + b.level + "|"+ b.stairwell
                buildingInfoList.add(buildingInfo)
                // println(buildingInfo)
                reportList.add(r)
                // println(r)
            }
        }

        def toReturn =[_building, reportList, buildingInfoList]
        render toReturn as JSON


    }

}
