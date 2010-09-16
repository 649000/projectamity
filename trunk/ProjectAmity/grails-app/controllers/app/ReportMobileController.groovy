package app
import grails.converters.JSON
import org.apache.commons.codec.*
import org.apache.commons.codec.binary.Base64
import java.util.*
import java.text.*

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

        def saveOutdoor = {

        //After the mobile app initiated the url connection
        //Validation will be done on the mobile app
        try {
            def resident = Resident.findByUserid(params.userid)
            def report = new Report()
            Date d = new Date()
            report.datePosted = d
            report.image = params.imagename
            report.title = params.title
            report.description = params.description
            report.latitude = Double.parseDouble(params.latitude)
            report.longitude = Double.parseDouble(params.longitude)
            report.status = "Pending"
            report.moderationStatus = false
            report.category = "Outdoor"

            InputStream input = request.getInputStream()
            BufferedReader r = new BufferedReader(new InputStreamReader(input))
            StringBuffer buf = new StringBuffer()
            String line

            //Read the BufferedReader out and receives String data
            while ((line = r.readLine())!=null) {
		buf.append(line)
            }
            String imageString = buf.toString()
            Base64 b = new Base64()
            byte[] imageByteArray = b.decodeBase64(imageString)

            // FileOutputStream f = new FileOutputStream("/Users/nAzri/NetBeansProjects/ProjectAmity/web-app/outdoorreportimages/"+params.imagename)
            FileOutputStream f = new FileOutputStream("C:\\Documents and Settings\\Administrator\\My Documents\\NetBeansProjects\\ProjectAmity\\web-app\\outdoorreportimages\\"+params.imagename)
            f.write(imageByteArray);
            f.close();

            resident.addToReport(report)
            render "T"

        }
        catch(Exception e)
        {
            println("Error in saving outdoor report")
            e.printStackTrace()
            render "F"
        }
    }

    def saveIndoor = {
        println("Indoor reporting starts here")
        try {
            println(params.postalCode)
            def resident = Resident.findByUserid(params.userid)
            def _building = Building.findAllByPostalCode(params.postalCode)
            def theCorrectBuilding

            for(Building b: _building)
            {
                if(b.level==params.level && b.stairwell ==params.stairwell)
                {
                    println(b.level)
                    theCorrectBuilding = b
                }
            }
            def indoorReport = new IndoorReport()

            Date d = new Date()
            indoorReport.datePosted = d
            indoorReport.image = params.imagename
            indoorReport.title = params.title
            indoorReport.description = params.description
            // report.category = "Indoor"

            indoorReport.status = "Pending"
            report.category = "Indoor"
            indoorReport.moderationStatus = false

            InputStream input = request.getInputStream()
            BufferedReader r = new BufferedReader(new InputStreamReader(input))
            StringBuffer buf = new StringBuffer()
            String line

            //Read the BufferedReader out and receives String data
            while ((line = r.readLine())!=null) {
                buf.append(line)
            }
            String imageString = buf.toString()
            Base64 b = new Base64()
            byte[] imageByteArray = b.decodeBase64(imageString)


            FileOutputStream f = new FileOutputStream("/Users/nAzri/NetBeansProjects/ProjectAmity/web-app/indoorreportimages/"+params.imagename)
            //   FileOutputStream f = new FileOutputStream("C:\\Documents and Settings\\Administrator\\My Documents\\NetBeansProjects\\ProjectAmity\\web-app\\indoorreportimages\\"+params.imagename)
            f.write(imageByteArray);
            f.close();

            theCorrectBuilding.addToIndoorReport(indoorReport)
            resident.addToIndoorReport(indoorReport)
            render "T"
        }
        catch(Exception e)
        {
            println("Error in saving indoor report")
            e.printStackTrace()
            render "F"
        }
    }
}

