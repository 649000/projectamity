package app

import grails.converters.JSON
import java.util.*
import java.text.*
import org.apache.commons.codec.binary.Base64

class NEAOfficerMobileController {
    def twitterService
    def GeoCoderService
    
    def index = { }
    def LoginAndroid = {
        def neaOff = NEAOfficer.findByUserid(params.userid)
        def toRender=""
        if (neaOff == null)
        {
            toRender=  "F"
        } else if(neaOff.password==params.password)
        {
            toRender = "T"
            neaOff.mLogin = true
        }
        render toRender
    }

    def LogoutAndroid =
    {
        try
        {  def neaOfficer = NEAOfficer.findByUserid(params.userid)
            neaOfficer.mLogin = false
            render "T"
        }
        catch (Exception e)
        {
            println(e)
            render "F"
        }
    }

    def setLocationAndroid =
    {
        try
        {
            def neaOfficer = NEAOfficer.findByUserid(params.userid)
            neaOfficer.latitude = Double.parseDouble(params.latitude.trim())
            neaOfficer.longitude = Double.parseDouble(params.longitude.trim())
            println("Officer's Current Coordinates: "+ params.latitude+", " + params.longitude)
        }
        catch(Exception e)
        {
            println(e)
        }
    }

    def getNearbyOfficer =
    {
        ArrayList officerList = new ArrayList()
        try
        {
            def neaOfficer = NEAOfficer.findByUserid(params.userid)
            def nearByOfficers = NEAOfficer.createCriteria().list
            {
                and {
                    eq("mLogin", "true")
                    ne("userid", params.userid)
                    isNotNull("longitude")
                    isNotNull("latitude")
                }
            }

            if(nearByOfficers != null)
            {
                for(NEAOfficer n: nearByOfficers)
                {
                    //Compare the distance
                    def dist = calculateDistance(neaOfficer.latitude, neaOfficer.longitude,n.latitude, n.longitude)

                    if(dist <=2.5)
                    {
                        officerList.add(n)
                    }
                }
            }

        }
        catch(Exception e)
        {
            println(e)
        }

        render officerList as JSON
    }

    def removeReportsAndroid =
    {
        String toRender =""
        try
        {
            if (params.category =="Indoor")
            {
                def iR = IndoorReport.find("from IndoorReport as r where r.id=?",[Long.parseLong(params.reportid.trim())])
                iR.neaOfficer = null;
                iR.resolvedImage = null;
                iR.resolvedDescription = null;
                iR.status = "Pending"
                println("Indoor Report removed successfully")
                toRender = "T"
            } else if (params.category =="Outdoor")
            {
                def report = Report.find("from Report as r where r.id=?",[Long.parseLong(params.reportid.trim())])
                report.neaOfficer = null;
                report.resolvedImage = null;
                report.resolvedDescription = null;
                report.status = "Pending"
                println("Outdoor Report removed successfully")
                toRender = "T"
            }
        }
        catch (Exception e)
        {
            println(e)
            toRender = "F"
        }

        render toRender
    }

    def acceptReportsAndroid =
    {
        def neaOfficer = NEAOfficer.findByUserid(params.userid)
        String toRender =""
        try
        {
            if (params.category =="Indoor")
            {
                def iR = IndoorReport.find("from IndoorReport as r where r.id=?",[Long.parseLong(params.reportid.trim())])

                iR.setNeaOfficer(neaOfficer)
                println("Indoor Report accepted successfully")
                toRender = "T"
            } else if (params.category =="Outdoor")
            {
                def report = Report.find("from Report as r where r.id=?",[Long.parseLong(params.reportid.trim())])
                report.setNeaOfficer(neaOfficer)
                println("Outdoor Report accepted successfully")
                toRender = "T"
            }
        }
        catch (Exception e)
        {
            println(e)
            toRender = "F"
        }

        render toRender
    }

    def getBuildingAndroid =
    {
        def iR = IndoorReport.find("from IndoorReport as i where i.id=?",[Long.parseLong(params.id.trim())])
        def   building = Building.findById(iR.building.id)
        render building.postalCode+"|"+ building.level+ "|"+ building.stairwell
    }

    def getReportsAndroid =
    {

        println "Get Reports Start"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")
        def neaOfficer = NEAOfficer.findByUserid(params.userid)
        //Retrieve only the reports that belongs to this user
        def outdoorResult = Report.findAllByNeaOfficer(neaOfficer)
        def indoorResult = IndoorReport.findAllByNeaOfficer(neaOfficer)
        ArrayList aL = new ArrayList()
        int counter=0
        if(outdoorResult != null)        {
            for(Report r:outdoorResult)            {
                if(r.moderationStatus.equalsIgnoreCase("true") && r.status.equalsIgnoreCase("Pending"))
                aL.add(r)
                //toReturn+= "|"+r.title + "-" + sdf.format(r.datePosted) + "-" +"Outdoor Report"
            }
            counter++
        }
        if(indoorResult != null)        {
            for(IndoorReport iR: indoorResult)            {
                if(iR.moderationStatus.equalsIgnoreCase("true") && iR.status.equalsIgnoreCase("Pending"))
                aL.add(iR)
                // toReturn+="|"+iR.title + "-" + sdf.format(iR.datePosted) + "-"+ "Indoor Report"
            }
            counter++
        }
        if (counter==0)        {
            toReturn = "F"
        }
        println "Get Reports Finish"
        render aL as JSON
    }

    def resolveOutdoorAndroid =
    {
        try
        {
            def resident
            def report = Report.find("from Report as r where r.id=?",[Long.parseLong(params.reportid.trim())])
            report.status = params.status
            report.resolvedDescription = params.newdescription
            report.resolvedImage = params.imageName
            def downloadedfile = request.getFile("image")
//
//            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
//            {
//                //Mac Directory
//                downloadedfile.transferTo(new File("web-app/outdoorreportimages/"+ params.imageName))
//            } else
//            {
//                //Windows Directory
//                downloadedfile.transferTo(new File("outdoorreportimages\\"+ params.imageName))
//            }
            downloadedfile.transferTo(new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\ProjectAmity\\outdoorreportimages\\"+ params.imageName))
            if(params.status == "Resolved")
            {
                //Notify the user that the problem has been solved.
                //newMessage.sender = session.user, no session exist.
                resident = Resident.findById(report.resident.id)
                println("Resident: "+ resident.userid)
                session.user = Resident.findByUserid("Project Amity")
                println("Resident2: "+ Resident.findByUserid("Project Amity").name)
                params.receiverUserID = resident.userid
                params.subject = "Your feedback has been heard."
                params.message = "Dear User, \n On " + report.datePosted + ", you have the sent a report regarding the environment. This is to notify you that an action has been taken and the matter has been resolved. \n Regards, \n Your friendly officers."
                redirect(controller:'message',action:'send', params:params)
                println("Message Sent")
            }
            render "T"
            println("Report successfully updated")
        }
        catch (Exception e)
        {
            e.printStackTrace()
            render "F"
        }

    }
    def resolveIndoorAndroid =
    {
        try
        {
            def resident
            def report = IndoorReport.find("from IndoorReport as r where r.id=?",[Long.parseLong(params.reportid.trim())])
            report.status = params.status
            report.resolvedDescription = params.newdescription
            report.resolvedImage = params.imageName
            def downloadedfile = request.getFile("image")
//            if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
//            {
//                //Mac Directory
//                downloadedfile.transferTo(new File("web-app/indoorreportimages/"+ params.imageName))
//            } else
//            {
//                //Window Directory
//                downloadedfile.transferTo(new File("indoorreportimages\\"+ params.imageName))
//            }

                        downloadedfile.transferTo(new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\ProjectAmity\\indoorreportimages\\"+ params.imageName))
            if(params.status == "Resolved")
            {
                //Notify the user that the problem has been solved.
                resident = Resident.findById(report.resident.id)
                println("Resident: "+ resident.userid)
                session.user = Resident.findByUserid("Project Amity")
                params.receiverUserID = resident.userid
                params.subject = "Your feedback has been heard."
                params.message = "Dear User, \n on " + report.datePosted + ", you have the sent a report regarding the environment. This is to notify you that an action has been taken and the matter has been resolved. \n Regards, \n Your friendly officers."
                redirect(controller:'message',action:'send', params:params)
                println("Message Sent")
            }
            render "T"
            println("Report successfully updated")
        } catch(Exception e)
        {
            e.printStackTrace()
            render "F"
        }
    }

    def calculateDistance(double startLat, double startLong, double endLat, double endLong)
    {
        def final EARTH_RADIUS = 6371
        def latDist = Math.toRadians(endLat - startLat)
        def longDist = Math.toRadians(endLong - startLong)
        def a = Math.sin( latDist/2 ) * Math.sin( latDist/2 ) +
        Math.cos( Math.toRadians(startLat) ) * Math.cos( Math.toRadians(endLat) ) *
        Math.sin( longDist/2 ) * Math.sin( longDist/2 )
        def c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) );
        def distance = EARTH_RADIUS * c
        return distance
        // this distance is in KM
    }

    def getRecommendedReportsAndroid =
    {
        ArrayList reportList = new ArrayList()
        def radius = 0.0
        if (params.radius.equalsIgnoreCase("1")) {
            radius = 1.0
        } else if (params.radius.equalsIgnoreCase( "3")){
            radius = 3.0
        } else if (params.radius.equalsIgnoreCase( "5") ){
            radius = 5.0
        } else if (params.radius.equalsIgnoreCase("7") ){
            radius = 7.0
        } else if (params.radius.equalsIgnoreCase( "9") ){
            radius = 9.0
        } else if (params.radius.equalsIgnoreCase("all")) {
            radius = Double.MAX_VALUE
        }
        println("Radius Chosen: " + radius + " Coordinates: " + params.latitude + ", " + params.longitude)
        //Retrieve pending reports
        def pendingOutdoor = Report.createCriteria(), pendingIndoor = IndoorReport.createCriteria()
        def resultsOutdoor = pendingOutdoor.list()
        {
            and {
                eq("moderationStatus", "true")
                isNull("neaOfficer")
            }
        }
        def resultsIndoor = pendingIndoor.list()
        {
            and {
                eq("moderationStatus", "true")
                isNull("neaOfficer")
            }
        }

        for(Report r: resultsOutdoor)
        {
            def dist = calculateDistance(Double.parseDouble(params.latitude.trim()), Double.parseDouble(params.longitude.trim()),r.latitude, r.longitude)
            if ( dist<=radius)
            {
                reportList.add(r)
                println("Outdoor Report distance: "+ dist)
            }
        }

        for(IndoorReport iR: resultsIndoor)
        {
            def building = Building.findById(iR.building.id)
            def buildingCoordinates = GeoCoderService.getCoordinates("Singapore "+ building.postalCode)
            def dist = calculateDistance(Double.parseDouble(params.latitude.trim()), Double.parseDouble(params.longitude.trim()),Double.parseDouble(buildingCoordinates[0].trim()), Double.parseDouble(buildingCoordinates[1].trim()))
            if ( dist<=radius)

            { println("Indoor Report distance: "+ dist)
                reportList.add(iR)}
        }
        if(reportList[0] != null)
        println reportList[0].title

        render reportList as JSON
    }
    def getRecommendedReportsBasedOnReportsLocationAndroid =
    {//incomplete
        def neaOff = NEAOfficer.findByUserid(params.userid)
        ArrayList reportList = new ArrayList()
        def outdoorResult = Report.findAllByNeaOfficer(neaOfficer)
        def indoorResult = IndoorReport.findAllByNeaOfficer(neaOfficer)
        if(outdoorResult != null)        {
            for(Report r:outdoorResult)            {
                if(r.moderationStatus.equalsIgnoreCase("true") && r.status.equalsIgnoreCase("Pending"))
                reportList.add(r)
            }
        }
        if(indoorResult != null)        {
            for(IndoorReport iR: indoorResult)            {
                if(iR.moderationStatus.equalsIgnoreCase("true") && iR.status.equalsIgnoreCase("Pending"))
                reportList.add(iR)
            }
        }
        //Retrieve pending reports
        def pendingOutdoor = Report.createCriteria(), pendingIndoor = IndoorReport.createCriteria()
        def resultsOutdoor = pendingOutdoor.list()
        {
            and {
                eq("moderationStatus", "false")
                isNull("neaOfficer")
            }
        }
        def resultsIndoor = pendingIndoor.list()
        {
            and {
                eq("moderationStatus", "false")
                isNull("neaOfficer")
            }
        }


    }
    def mGetReports =
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")
        //Need to load all the reports based from this user id and then send it back.
        def neaOfficer = NEAOfficer.findByUserid(params.userid)

        def outdoorReport = Report.findAllByNeaOfficer(neaOfficer)
        def indoorReport = IndoorReport.findAllByNeaOfficer(neaOfficer)

        def toReturn="F"
        int counter=0
        if(outdoorReport != null)
        {

            for(Report r:outdoorReport)
            {

                toReturn+= "|"+r.title + "-" + sdf.format(r.datePosted) + "-" +"Outdoor Report"
            }
            counter++
        }
        if(indoorReport != null)
        {

            for(IndoorReport iR: indoorReport)
            {
                toReturn+="|"+iR.title + "-" + sdf.format(iR.datePosted) + "-"+ "Indoor Report"
            }
            counter++
        }

        if (counter==0)
        {
            toReturn = "F"
        }

        render toReturn
    }
    def eachReport = {
        def theCorrectReport
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd")
        println(params.title + " Date:"+ params.date + " Category:"+ params.category)
        def toReturn="F";
        def building
        if(params.category == "Outdoor Report")
        {
            def outdoorReport = Report.findAllByTitle(params.title)

            if(outdoorReport != null)
            {
                for(Report r: outdoorReport)
                {
                    println("Compare Dates: "+sdf.format(r.datePosted).toString() + " = "+ params.date)
                    if( sdf.format(r.datePosted).toString() == params.date)
                    {

                        theCorrectReport = r
                    }
                }

                toReturn = "Outdoor|" + theCorrectReport.title + "|" + sdf2.format(theCorrectReport.datePosted) + "|" + theCorrectReport.description+ "|" + theCorrectReport.latitude+ "|" + theCorrectReport.longitude

                render toReturn
            }
        } else if (params.category == "Indoor Report")
        {
            def indoorReport = IndoorReport.findAllByTitle(params.title)
            if(indoorReport != null)
            {
                for(IndoorReport iR: indoorReport)
                {

                    if( sdf.format(iR.datePosted) == params.date)
                    {
                        theCorrectReport = iR
                        print("Value of iR: " + iR.title)
                        //    def building = Building.findByIndoorReport(iR)
                        building = Building.findById(iR.building.id)
                    }


                }
                toReturn = "Indoor|" +  theCorrectReport.title + "|" + sdf2.format(theCorrectReport.datePosted) + "|" + theCorrectReport.description+ "|" + building.postalCode+ "|" + building.level+ "|" + building.stairwell
            }

            render toReturn
        }
    }
    private String[] split(String original, String separator) {
        Vector nodes = new Vector();
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create split string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
                System.out.println(result[loop]);
            }

        }
        return result;
    }
    def resolveOutdoor =
    {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")
            def buildInfo = split(params.info, "|")
            println("Params Received: "+ params.info + " "+ params.status + " " + params.imagename)
            println("Build Info: "+ buildInfo)
            def reports = Report.findAllByTitle(buildInfo[0])
            def resident

            println(reports)

            for(Report iR: reports)
            {
                if(sdf.format(iR.datePosted).toString() == buildInfo[1])
                {
                    println("New Status:" + params.status)
                    iR.status = params.status
                    iR.resolvedDescription = params.description
                    iR.resolvedImage = params.imagename
                    if(params.status == "Resolved")
                    {
                        //Notify the user that the problem has been solved.
                        resident = Resident.findById(iR.resident.id)
                        println("Resident: "+ resident.userid)

                        params.sender  = Resident.findByName("Project Amity")
                        params.receiverUserID = resident.userid
                        params.subject = "Your feedback has been heard."
                        params.message = "Dear User, \n on " + iR.datePosted + ", you have the sent a report regarding the environment. This is to notify you that an action has been taken and the matter has been resolved. \n Regards, \n Your friendly officer."
                        redirect(controller:'message',action:'send', params:params)
                    }
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
                    FileOutputStream f = new FileOutputStream("C:\\Documents and Settings\\Administrator\\My Documents\\NetBeansProjects\\ProjectAmity\\web-app\\outdoorreportimagesResolved\\"+params.imagename)
                    f.write(imageByteArray);
                    f.close();
                }
            }

            render "T"
        }
        catch (Exception e)
        {
            render "F"
            e.printStackTrace()
        }
    }

    def resolveIndoor = {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd")
            def buildInfo = split(params.info, "|")

            def reports = IndoorReport.findAllByTitle(buildInfo[0])

            for(IndoorReport iR: reports)
            {
                if(sdf.format(iR.datePosted).toString() == buildInfo[1])
                {
                    iR.status = params.status
                    iR.resolvedDescription = params.descriptions
                    iR.resolvedImage = params.imagename
                    if(params.status == "Resolved")
                    {
                        //Notify the user that the problem has been solved.
                        resident = Resident.findById(iR.resident.id)
                        println("Resident: "+ resident.userid)

                        params.sender  = Resident.findByName("Project Amity")
                        params.receiverUserID = resident.userid
                        params.subject = "Your feedback has been heard."
                        params.message = "Dear User, \n on " + iR.datePosted + ", you have the sent a report regarding the environment. This is to notify you that an action has been taken and the matter has been resolved. \n Regards, \n Your friendly officer."
                        redirect(controller:'message',action:'send', params:params)
                    }

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
                    FileOutputStream f = new FileOutputStream("C:\\Documents and Settings\\Administrator\\My Documents\\NetBeansProjects\\ProjectAmity\\web-app\\indoorreportimagesResolved\\"+params.imagename)
                    f.write(imageByteArray);
                    f.close();
                }
            }

            render "T"
        }
        catch (Exception e)
        {
            render "F"
            e.printStackTrace()
        }

    }
}
