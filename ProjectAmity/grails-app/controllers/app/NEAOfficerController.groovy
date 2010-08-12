package app

import grails.converters.JSON
import java.util.*
import java.text.*
import org.apache.commons.codec.binary.Base64

class NEAOfficerController
{

    def twitterService

    def index =
    {
        
    }

    def investigate =
    {
        
    }

    def LoginAndroid = {

        println(params.userid)
        println(params.password)
        def neaOff = NEAOfficer.findByUserid(params.userid)
        render "T"
    }

    def getBuildingAndroid =
    {
        def iR = IndoorReport.find("from IndoorReport as i where i.id=?",[Long.parseLong(params.id.trim())])
        def   building = Building.findById(iR.building.id)
        render building.postalCode        
    }

    def getReportsAndroid =
    {
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
        render aL as JSON
    }

    def listReports =
    {
        def uncheckedIndoorReports
        def uncheckedOutdoorReports

        if( params.criteria != null )
        {
            uncheckedOutdoorReports = Report.createCriteria().list
            {
                and
                {
                    or
                    {
                        like("title", "%" + params.criteria + "%")
                        like("description", "%" + params.criteria + "%")
                    }
                    eq("moderationStatus", "false")
                }
            }
            uncheckedIndoorReports =  IndoorReport.executeQuery( "select b.postalCode, i.title, b.latitude,b.longitude, i.image, i.description, i.id from IndoorReport i, Building b where i.moderationStatus = 'false' AND ( title LIKE '%" + params.criteria + "%' OR description LIKE '%" + params.criteria + "%') AND i.building.id = b.id" )
        }
        else
        {
            uncheckedIndoorReports =  IndoorReport.executeQuery( "select b.postalCode, i.title, b.latitude,b.longitude, i.image, i.description, i.id from IndoorReport i, Building b where i.moderationStatus = 'false' AND i.building.id = b.id" )
            uncheckedOutdoorReports = Report.findAllByModerationStatus("false")
        }
        

        //println("Listing unapproved reports: " + uncheckedOutdoorReports.size + " outdoor and " + uncheckedIndoorReports.size + " indoor" )
        def list = [ uncheckedOutdoorReports, uncheckedIndoorReports ]
        render list as JSON
    }

    def listPendingReports =
    {
        def uncheckedIndoorReports
        def uncheckedOutdoorReports

        if( params.criteria != null )
        {
            uncheckedOutdoorReports = Report.createCriteria().list
            {
                and
                {
                    or
                    {
                        like("title", "%" + params.criteria + "%")
                        like("description", "%" + params.criteria + "%")
                    }
                    eq("moderationStatus", "true")
                    isNull("neaOfficer")
                }
            }
            uncheckedIndoorReports =  IndoorReport.executeQuery( "select b.postalCode, i.title, b.latitude,b.longitude, i.image, i.description, i.id from IndoorReport i, Building b where i.moderationStatus = 'true' AND ( title LIKE '%" + params.criteria + "%' OR description LIKE '%" + params.criteria + "%') AND i.neaOfficer IS NULL AND i.building.id = b.id" )
        }
        else
        {
            uncheckedIndoorReports =  IndoorReport.executeQuery( "select b.postalCode, i.title, b.latitude,b.longitude, i.image, i.description, i.id from IndoorReport i, Building b where i.moderationStatus = 'true' AND i.neaOfficer IS NULL AND i.building.id = b.id" )
            uncheckedOutdoorReports = Report.createCriteria().list
            {
                and
                {
                    eq("moderationStatus", "true")
                    isNull("neaOfficer")
                }
            }
        }

        def list = [ uncheckedOutdoorReports, uncheckedIndoorReports ]
        render list as JSON
    }

    def updateOutdoorModerationStatus =
    {
        def id = params.id
        def newStatus = params.status

        if(newStatus.equalsIgnoreCase("true"))
        {
            twitterService.updateStatus("A new outdoor report has been posted.")
        }

        // update moderation status of outdoor report
        def r = Report.findById(id)
        r.moderationStatus = newStatus
        println("Outdoor Report Moderation Success")
    }

    def updateIndoorModerationStatus =
    {
        def id = params.id
        def newStatus = params.status

        if(newStatus.equalsIgnoreCase("true"))
        {
            twitterService.updateStatus("A new indoor report has been posted.")
        }

        // update moderation status of outdoor report
        def i = IndoorReport.findById(id)
        i.moderationStatus = newStatus
        println("Indoor Report Moderation Success")
    }

    def investigateOutdoorReport =
    {
        def id = params.id
        def officerId = params.officerId.trim()

        // update moderation status of outdoor report
        def r = Report.findById(id)
        r.neaOfficer = NEAOfficer.findByUserid(officerId)
        println(NEAOfficer.findByUserid(officerId))
        println("Investigate outdoor report success")
    }

    def investigateIndoorReport =
    {
        def id = params.id
        def officerId = params.officerId.trim()

        // update moderation status of outdoor report
        def i = IndoorReport.findById(id)
        i.neaOfficer = NEAOfficer.findByUserid(officerId)
        println(NEAOfficer.findByUserid(officerId))
        println("Investigate indoor report success")
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
