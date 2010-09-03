package app
import grails.converters.JSON

class ReportMobileController {

    def index = {}

    def outdoorReportAndroid =
    {
        try{
            def resident = Resident.findByUserid(params.userid)
            def report = new Report()
            Date d = new Date()
            report.datePosted = d
            report.image = params.imageName
            report.title = params.title
            report.description = params.description
            report.latitude = Double.parseDouble(params.latitude)
            report.longitude = Double.parseDouble(params.longitude)
            report.status = "Pending"
            report.moderationStatus = false
            report.category = "Outdoor"

            def downloadedfile = request.getFile("image")

            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
            {
                //Mac Directory
                downloadedfile.transferTo(new File("web-app/outdoorreportimages/"+ params.imageName))
            } else
            {
                //Windows Directory
                  downloadedfile.transferTo(new File("outdoorreportimages\\"+ params.imageName))
            }
            resident.addToReport(report)
            render "T"
        } catch (Exception e)
        {
            println("Error in saving outdoor report")
            e.printStackTrace()
            render "F"
        }
    }

    def indoorReportAndroid = {
        try{
            def resident = Resident.findByUserid(params.userid)

            def report = new IndoorReport()
            Date d = new Date()
            report.datePosted = d
            report.image = params.imageName
            report.title = params.title
            report.description = params.description
            report.status = "Pending"
            report.moderationStatus = false
            report.category = "Indoor"
            def downloadedfile = request.getFile("image")

            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
            {
                //Mac Directory
                downloadedfile.transferTo(new File("web-app/indoorreportimages/"+ params.imageName))
            } else
            {
                //Windows Directory
                downloadedfile.transferTo(new File("indoorreportimages\\"+ params.imageName))
            }
            def building = Building.createCriteria()
            def _building = building.get {
                projections {
                    eq("level",params.level )
                    eq("postalCode",resident.postalCode )
                    eq("stairwell",params.location)
                }
            }
            _building.addToIndoorReport(report)
            resident.addToIndoorReport(report)
            render "T"
        } catch (Exception e)
        {
            println("Error in saving indoor report")
            e.printStackTrace()
            render "F"
        }
        
    }

    def getLevel = {
        //Retrieve all the building available for the given userid.
        def resident = Resident.findByUserid(params.userid)
        // def building = Building.findAllByPostalCode(resident.postalCode)
        //  def building = IndoorReport.find("from Building as i where i.userid=?",[Long.parseLong(params.userid.trim())])
        //render building as JSON

        def building = Building.createCriteria()
        def _building = building.list {
            projections {
                distinct("level")
                eq("postalCode",resident.postalCode )
            }
        }
        render _building as JSON
    }
    def getLocation = {
        def resident = Resident.findByUserid(params.userid)
        def building = Building.createCriteria()
        def _building = building.list {
            projections {
                eq("level",params.level )
                eq("postalCode",resident.postalCode )
            }
        }
        render _building as JSON
    }
}

