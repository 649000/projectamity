package app

import grails.converters.JSON
import java.util.*
import java.text.*
import org.apache.commons.codec.binary.Base64

class NEAOfficerController
{

    def twitterService
    def GeoCoderService

    def index =
    {

    }

    def investigate =
    {

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
    
}
