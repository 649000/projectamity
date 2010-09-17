package app

import grails.converters.JSON
import java.util.*
import java.text.*

class BuildingController {

    def messageCheckingService
    def GeoCoderService
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
            def resident = Resident.find("from Resident as res where res.id=?",(report.resident.id))
            def building = Building.find("from Building as b where b.id=?",(report.building.id))
            def loc = GeoCoderService.getAddress(building.latitude, building.longitude)
            if (report.moderationStatus == "true")
            {
                return [report: report, date: sdf.format( report.datePosted ), res: resident, loc: loc]
            }
            else
            {
                return [report: null, date: null, res: null, loc: null]
            }
        }  catch (Exception e)
        {
            e.printStackTrace()
        }
    }

}
