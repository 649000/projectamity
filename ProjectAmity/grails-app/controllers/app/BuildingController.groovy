package app

import grails.converters.JSON
import java.util.*
import java.text.*

class BuildingController {

    def messageCheckingService
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")

    def index = {

        //User will be redirected to this index closure

        //This controller will retrieve the building's info based on the postal code
        println("Params Received (Postal Code): " + params.postalCode)
        session.postalCode = params.postalCode

        if(session.user != null&& Resident.findByUserid(session.user.userid) !=null)
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
    def individual=
    {
        try {
            def report = IndoorReport.find("from IndoorReport as r where r.id=?",[Long.parseLong(params.id.trim())])
            return [report: report, date: sdf.format( report.datePosted )]
        }  catch (Exception e)
        {
            e.printStackTrace()
        }
    }

}
