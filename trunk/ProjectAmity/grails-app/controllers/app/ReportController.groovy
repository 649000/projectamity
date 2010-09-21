package app
import grails.converters.JSON
import org.apache.commons.codec.*
import org.apache.commons.codec.binary.Base64
import java.util.*
import java.text.*

class ReportController {

    def messageCheckingService
    def TwitterService
    def GeoCoderService
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")
    def index = {
        if(session.user!=null)
        {
            if(session.user.userid == null)
            {
                //redirect(controller:'resident', action: 'definepro')
            } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
            {
                //redirect(controller:'NEAOfficer', action: 'index')
            }else if (session.user.emailConfirm == "false")
            {
                //redirect(controller:'resident', action: 'index')
            }
            else
            {
                params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
                        def outdoorReport = Report.createCriteria()
        def now = new Date()

        //Retrieve all outdoor reports
        def list = outdoorReport.list {
            and {
                //183 days is about 6 months
                between('datePosted',now-183,now)
                eq("moderationStatus", "true")

            }
        }
                [params : params, list:list]
            }

        }else
        {
            //redirect(url:"../index.gsp")
        }
    }


    def loadData = {

        //Need to retrieve only reports that are posted 6 months ago till now.
        def buildingList = new ArrayList()
        def outdoorReport = Report.createCriteria()
        def indoorReport = IndoorReport.createCriteria()
        def now = new Date()

        //Retrieve all outdoor reports
        def outdoorResults = outdoorReport.list {
            and {
                //183 days is about 6 months
                between('datePosted',now-183,now)
                eq("moderationStatus", "true")

            }
        }
        //        def indoorResults = indoorReport.list {
        //                count("id")
        //            building {
        //               // count("id")
        //                groupProperty("postalCode")
        //
        //            }
        //        }

        //Number inclides old report and unmoderated reports
        def confusingList =  IndoorReport.executeQuery( "select b.postalCode, count(i.id), b.latitude,b.longitude from IndoorReport i , Building b where i.building.id = b.id group by b.postalCode" )

        //       for(def i=0;i<confusingList.size();i++)
        //       {
        //           println("Postal Code : " + confusingList[i][0] + "Amount of Reports: " + confusingList[i][1])
        //       }
        

        def list =[outdoorResults, confusingList]
        render list as JSON
    }

    def verify =
    {
        //        session.latitude=params.latitude
        //        session.longitude=params.longitude
        //        session.range=params.range
        //        session.userid=params.user.userid
        //        session.latitude="1.4036496"
        //        session.longitude="103.7883013"
        //        session.range="10"
        //        session.userid="Tan Di Di"
        params.userid="Tan Di Di"
        params.latitude="1.4036496"
        params.longitude="103.7883013"
        params.destination="Tampines"
        params.maprang="2"
        def cabpool=new CabpoolListing(params)
        println(cabpool.errors)
        cabpool.save()
    }

    def individual=
    {
        try {
            def report = Report.find("from Report as r where r.id=?",[Long.parseLong(params.id.trim())])
            def resident = Resident.find("from Resident as res where res.id=?",(report.resident.id))
            if (report.moderationStatus == "true")
            {
                return [report: report, date: sdf.format( report.datePosted ), res: resident, loc: GeoCoderService.getAddress(report.latitude, report.longitude)]
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
    def loadData2 =
    {
        //        session.range=Double.parseDouble(session.range+"")/5
        //        println(session.range)
        //        params.latitude=session.latitude
        //        params.longitude=session.longitude
        //        params.range=session.range
        //        params.userid=session.userid

        def toReturn=CabpoolListing.findAll()
        render toReturn as JSON
    }

    def sendData =
    {
        render(params.data)
    }

}
