package app

class ReportMobileController {

    def index = { }

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
            report.altitude = Double.parseDouble(params.altitude)
            report.status = "Pending"
            report.moderationStatus = false
            report.category = "Outdoor"

            def downloadedfile = request.getFile("image")
            downloadedfile.transferTo(new File("c:/jars/"+params.imageName))
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
            def report = new Report()
            Date d = new Date()
            report.datePosted = d
            report.image = params.imageName
            report.title = params.title
            report.description = params.description
            report.latitude = Double.parseDouble(params.latitude)
            report.longitude = Double.parseDouble(params.longitude)
            report.altitude = Double.parseDouble(params.altitude)
            report.status = "Pending"
            report.moderationStatus = false
            report.category = "Outdoor"

            def downloadedfile = request.getFile("image")
            downloadedfile.transferTo(new File("c:/jars/"+params.imageName))
            resident.addToReport(report)
            render "T"
        } catch (Exception e)
        {
            println("Error in saving outdoor report")
            e.printStackTrace()
            render "F"
        }
        
    }
}

