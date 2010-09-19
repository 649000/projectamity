package app

import grails.converters.JSON
import java.text.*;
import java.util.*;

class CarpoolListingController
{

    def messageCheckingService
    def libraryService
    def cinemaService
    def placesOfInterestService
    def counterService
    def supermarketService

    def currentUser

    def ajaxAddListing =
    {
        def resident = session.user

        def riderType = params.newCarpoolType

        def startAddress = params.newCarpoolFrom
        def startLatitude = params.newCarpoolFromLat
        def startLongitude = params.newCarpoolFromLng

        def endAddress = params.newCarpoolTo
        def endLatitude = params.newCarpoolToLat
        def endLongitude = params.newCarpoolToLng

        def status = 'active'

        if( params.newCarpoolTripTypeFinal.equalsIgnoreCase("commute") )
        {
            def tripType = "commute"

            def departureDays = parseCheckboxInputs(params.departureDays) // an array of Strings
            def returnDays = parseCheckboxInputs(params.returnDays) // an array of Strings

            if( departureDays.length == 0 && returnDays.length == 0 )
            {
                render 'You have yet to specify when you are travelling.'
            }

            def notes = removeHTML(params.newCarpoolNotes)

            CarpoolListing c = new CarpoolListing()
            c.resident = resident
            c.riderType = riderType
            c.startAddress = startAddress
            c.startLatitude = startLatitude
            c.startLongitude = startLongitude
            c.endAddress = endAddress
            c.endLatitude = endLatitude
            c.endLongitude = endLongitude
            c.status = status
            c.tripType = tripType
            c.notes = notes

            for(String s : departureDays)
            {
                if( s.equalsIgnoreCase("monday") )
                {
                    c.departureMondayTime = params.departureMondayTime
                    c.departureMondayInterval = parseIntervalInput( params.departureMondayInterval )
                }
                else if(  s.equalsIgnoreCase("tuesday") )
                {
                    c.departureTuesdayTime = params.departureTuesdayTime
                    c.departureTuesdayInterval = parseIntervalInput( params.departureTuesdayInterval )
                }
                else if(  s.equalsIgnoreCase("wednesday") )
                {
                    c.departureWednesdayTime = params.departureWednesdayTime
                    c.departureWednesdayInterval = parseIntervalInput( params.departureWednesdayInterval )
                }
                else if(  s.equalsIgnoreCase("thursday") )
                {
                    c.departureThursdayTime = params.departureThursdayTime
                    c.departureThursdayInterval = parseIntervalInput( params.departureThursdayInterval )
                }
                else if(  s.equalsIgnoreCase("friday") )
                {
                    c.departureFridayTime = params.departureFridayTime
                    c.departureFridayInterval = parseIntervalInput( params.departureFridayInterval )
                }
                else if( (  s.equalsIgnoreCase("saturday") ) )
                {
                    c.departureSaturdayTime = params.departureSaturdayTime
                    c.departureSaturdayInterval = parseIntervalInput( params.departureSaturdayInterval )
                }
                else if(  s.equalsIgnoreCase("sunday") )
                {
                    c.departureSundayTime = params.departureSundayTime
                    c.departureSundayInterval = parseIntervalInput( params.departureSundayInterval )
                }
            }

            for(String s : returnDays)
            {
                if( s.equalsIgnoreCase("monday") )
                {
                    c.returnMondayTime = params.returnMondayTime
                    c.returnMondayInterval = parseIntervalInput( params.returnMondayInterval )
                }
                else if(  s.equalsIgnoreCase("tuesday") )
                {
                    c.returnTuesdayTime = params.returnTuesdayTime
                    c.returnTuesdayInterval = parseIntervalInput( params.returnTuesdayInterval )
                }
                else if(  s.equalsIgnoreCase("wednesday") )
                {
                    c.returnWednesdayTime = params.returnWednesdayTime
                    c.returnWednesdayInterval = parseIntervalInput( params.returnWednesdayInterval )
                }
                else if(  s.equalsIgnoreCase("thursday") )
                {
                    c.returnThursdayTime = params.returnThursdayTime
                    c.returnThursdayInterval = parseIntervalInput( params.returnThursdayInterval )
                }
                else if(  s.equalsIgnoreCase("friday") )
                {
                    c.returnFridayTime = params.returnFridayTime
                    c.returnFridayInterval = parseIntervalInput( params.returnFridayInterval )
                }
                else if( (  s.equalsIgnoreCase("saturday") ) )
                {
                    c.returnSaturdayTime = params.returnSaturdayTime
                    c.returnSaturdayInterval = parseIntervalInput( params.returnSaturdayInterval )
                }
                else if(  s.equalsIgnoreCase("sunday") )
                {
                    c.returnSundayTime = params.returnSundayTime
                    c.returnSundayInterval = parseIntervalInput( params.returnSundayInterval )
                }
            }
            
            println( "Commute from " + startAddress + " to " + endAddress )

            if( c.save() )
            {
                render 'T'
            }
            else
            {
                println( c.errors )
                render 'F'
            }
        }
        else
        {
            def tripType = "oneOff"

            def oneOffOneWay = parseCheckboxInputs(params.oneOffOneWay)

            println( oneOffOneWay )

            CarpoolListing c = new CarpoolListing()
            c.resident = resident
            c.riderType = riderType
            c.startAddress = startAddress
            c.startLatitude = startLatitude
            c.startLongitude = startLongitude
            c.endAddress = endAddress
            c.endLatitude = endLatitude
            c.endLongitude = endLongitude
            c.status = status
            c.tripType = tripType

            if( oneOffOneWay == null )
            {
                // not a one-way trip
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
                Date convertedDate = dateFormat.parse( params.oneOffReturnDate_year + "/" + params.oneOffReturnDate_month + "/" + params.oneOffReturnDate_day + " " + params.oneOffReturnTime.substring(0,2) + ":" + params.oneOffReturnTime.substring(3,5) + " " + params.oneOffReturnTime.substring(6,8) );
                c.oneOffReturnTime = convertedDate
                c.oneOffReturnInterval = parseIntervalInput( params.oneOffReturnInterval )
            }

            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
            Date convertedDate2 = dateFormat2.parse( params.oneOffDepartureDate_year + "/" + params.oneOffDepartureDate_month + "/" + params.oneOffDepartureDate_day + " " + params.oneOffDepartureTime.substring(0,2) + ":" + params.oneOffDepartureTime.substring(3,5) + " " + params.oneOffDepartureTime.substring(6,8) );
            c.oneOffDepartureTime = convertedDate2
            c.oneOffDepartureInterval = parseIntervalInput( params.oneOffDepartureInterval )

            c.notes = removeHTML(params.newCarpoolNotesOneOff)

            println( "One-off trip from " + startAddress + " to " + endAddress )

            if( c.save() )
            {
                render 'T'
            }
            else
            {
                println( c.errors )
                render 'F'
            }
        }
    }

    def String[] parseCheckboxInputs(String param)
    {
        def toReturn = new String[1]
        toReturn[0] = param
        return toReturn
    }

    def String[] parseCheckboxInputs(String[] param)
    {
        return param
    }

    def int parseIntervalInput(String param)
    {
        if( param.length() != 9 )
        {
            return 60
        }
        else
        {
            return Integer.valueOf( param.substring(2,4) )
        }
    }

    def String removeHTML(String htmlString)
    {
        // Remove HTML tag from java String
        String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");

        // Remove Carriage return from java String
        noHTMLString = noHTMLString.replaceAll("\r", "<br/>");

        // Remove New line from java string and replace html break
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
        noHTMLString = noHTMLString.replaceAll("\"", "&quot;");
        return noHTMLString;
    }

    def index =
    {
        currentUser = session.user

        loadData()

        if(currentUser.carpoolListing.departureTime.length() == 4)
        {
            params.departureTimeHour = currentUser.carpoolListing.departureTime.substring(0,2)
            params.departureTimeMinute = currentUser.carpoolListing.departureTime.substring(2,4)
        }

        if(currentUser.carpoolListing.returnTime.length() == 4)
        {
            params.returnTimeHour = currentUser.carpoolListing.returnTime.substring(0,2)
            params.returnTimeMinute = currentUser.carpoolListing.returnTime.substring(2,4)
        }

        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)

        [ listing : currentUser.carpoolListing, params : params ]
    }

    def new_add =
    {
        currentUser = session.user

        loadData()

        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)

        [ params : params ]
    }

    def new_view =
    {
        if( session.user )
        {
            currentUser = session.user
        }

        loadData()

        def l = CarpoolListing.findById( params.id )

        if( session.user )
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)
        }

        def request = CarpoolRequest.createCriteria().get()
        {
            and
            {
                eq("carpoolListing", l)
                eq( "resident", Resident.findById(session.user.id) )
                eq("status", "Pending")
            }
        }

        if( request == null )
        {
            params.requested = "F"
        }
        else
        {
            params.requested = "T"
        }

        if( l.tripType.equalsIgnoreCase("oneOff") )
        {
            SimpleDateFormat formatter = new SimpleDateFormat ("d MMMM yyyy 'at' hh:mma");
            def departureTiming = formatter.format( l.oneOffDepartureTime )
            if( l.oneOffDepartureInterval == 60 )
            {
                departureTiming += ", ± 1 hour"
            }
            else
            {
                departureTiming += ", ± " + l.oneOffDepartureInterval + " mins"
            }
            params.departureTiming = departureTiming

            if( l.oneOffReturnTime != null )
            {
                def returnTiming = formatter.format( l.oneOffReturnTime )
                if( l.oneOffReturnInterval == 60 )
                {
                    returnTiming += ", ± 1 hour"
                }
                else
                {
                    returnTiming += ", ± " + l.oneOffReturnInterval + " mins"
                }
                params.returnTiming = returnTiming
            }
            else
            {
                params.returnTiming = "This is a one-way trip."
            }
        }

        [ l : l , params : params ]
    }

    def new_index =
    {
        currentUser = session.user

        loadData()

        def listings = CarpoolListing.createCriteria().list(params)
        {
            and
            {
                eq("resident", session.user)
            }
            // order("timeStamp", "desc")
        }

        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)

        [ listings : listings , params : params ]
    }

    def ajaxLoadActiveListings =
    {
        currentUser = session.user

        def listings = CarpoolListing.createCriteria().list(params)
        {
            and
            {
                eq("resident", session.user)
                eq("status", "active")
            }
            // order("timeStamp", "desc")
        }

        render listings as JSON

    }

    def checkIfCarpoolRequestExists =
    {
        def id = params.listingID

        def listing = CarpoolListing.findById(id)

        if( session.user == null )
        {
            render 'ERROR'
        }

        def request = CarpoolRequest.createCriteria().get()
        {
            and
            {
                eq("carpoolListing", listing)
                eq( "resident", Resident.findById(session.user.id) )
                eq("status", "Pending")
            }
        }

        if( request == null )
        {
            render 'F'
        }
        else
        {
            render 'T'
        }
    }

    def ajaxSendRequest =
    {

        def errors = ''
        def recipient
        def subject
        def message

        // Check if recipient exists
        recipient = params.receiverUserID
        if( Resident.findByUserid(recipient) == null )
        {
            errors += '\nA recipient by the User ID of ' + recipient + ' does not exist!'
        }
        else
        {
            recipient = Resident.findByUserid(recipient)
        }

        // Check if the subject is specified.
        if( !params.subject.trim().equals("") )
        {
            subject = params.subject
        }
        else
        {
            errors += '\nYou did not specify the subject of the message.'
        }

        // Check if the message is specified.
        if( !params.message.trim().equals("") )
        {
            message = params.message
        }
        else
        {
            errors += '\nYou cannot send an empty message.'
        }

        if( errors.length() > 0 )
        {
            errors = 'Some Errors Occured!\n' + errors
            render errors
        }
        else
        {
            def newMessage = new Message()
            newMessage.sender = session.user
            newMessage.receiver = recipient
            newMessage.subject = subject
            newMessage.message = removeHTML(message)
            newMessage.timeStamp = new Date()
            newMessage.isRead = false

            def newRequest = new CarpoolRequest()
            newRequest.resident = session.user
            newRequest.carpoolListing = CarpoolListing.findById(params.listingId)
            newRequest.status = 'Pending'
            newRequest.message = newMessage

            if( newMessage.save() && newRequest.save() )
            {
                render 'T'
            }
            else
            {
                println( newMessage.errors )
                println( newRequest.errors )
                render 'F'
            }
        }

    }

    def ajaxSendMessage =
    {

        def errors = ''
        def recipient
        def subject
        def message

        // Check if recipient exists
        recipient = params.receiverUserID
        if( Resident.findByUserid(recipient) == null )
        {
            errors += '\nA recipient by the User ID of ' + recipient + ' does not exist!'
        }
        else
        {
            recipient = Resident.findByUserid(recipient)
        }

        // Check if the subject is specified.
        if( !params.subject.trim().equals("") )
        {
            subject = params.subject
        }
        else
        {
            errors += '\nYou did not specify the subject of the message.'
        }

        // Check if the message is specified.
        if( !params.message.trim().equals("") )
        {
            message = params.message
        }
        else
        {
            errors += '\nYou cannot send an empty message.'
        }

        if( errors.length() > 0 )
        {
            errors = 'Some Errors Occured!\n' + errors
            render errors
        }
        else
        {
            def newMessage = new Message()
            newMessage.sender = session.user
            newMessage.receiver = recipient
            newMessage.subject = removeHTML(subject)
            newMessage.message = removeHTML(message)
            newMessage.timeStamp = new Date()
            newMessage.isRead = false

            if( newMessage.save() )
            {
                render 'T'
            }
            else
            {
                println( newMessage.errors )
                render 'F'
            }
        }

    }

    def loadData()
    {
        println("Start of loadData Action")
        //println("CounterService: " + CounterService._Counter())

        if( counterService._Counter() == 1 )
        {
            println("Start CounterService: " + counterService.CounterValue())
            Destination.executeUpdate("delete Destination d where d.name != :_name", [_name:"NULL"])
            //Loading for the first time.

            for(Destination d : cinemaService.getCinemas())
            {
                d.save()
            }

            for(Destination d : libraryService.getLibraries())
            {
                d.save()
            }

            for(Destination d : placesOfInterestService.getPlaces())
            {
                d.save()
            }

            for(Destination d : supermarketService.getSupermarkets())
            {
                d.save()
            }
        }
        println("End CounterService: " + counterService.CounterValue())


    }

    def searchAJAX =
    {
        def destinations = Destination.findAllByNameLike("%${params.query}%")
        println("Keyword: " + params.query + " You are here. " + destinations.size() )
        //Create XML response
        render(contentType: "text/xml")
        {
	    results()
            {
	        destinations.each
                { destination ->
		    result()
                    {
		        name(destination.name)
                        //Optional id which will be available in onItemSelect
                        id(destination.id)
		    }
		}
            }
        }
    }

    def save =
    {
        def errors = ''

        def status
        def departureHour
        def departureMinute
        def returnHour
        def returnMinute
        def frequency
        def type

        // Retrieve listing status
        if( !params.status.trim().equals("") )
        {
            status = params.status
        }
        else
        {
            errors += '<li>You did not specify your listing\'s status.</li>'
        }

        // Parse the departure time
        if( !params.departureTimeHour.trim().equals("") )
        {
            departureHour = params.departureTimeHour
        }
        else
        {
            errors += '<li>You did not specify your departure hour.</li>'
        }
        if( !params.departureTimeMinute.trim().equals("") )
        {
            departureMinute = params.departureTimeMinute
        }
        else
        {
            errors += '<li>You did not specify your departure minute.</li>'
        }

        // Parse the return time
        if( !params.returnTimeHour.trim().equals("") )
        {
            returnHour = params.returnTimeHour
        }
        else
        {
            errors += '<li>You did not specify your return hour.</li>'
        }
        if( !params.returnTimeMinute.trim().equals("") )
        {
            returnMinute = params.returnTimeMinute
        }
        else
        {
            errors += '<li>You did not specify your return minute.</li>'
        }

        // Retrieve frequence and type
        if( !params.frequency.trim().equals("") )
        {
            frequency = params.frequency
        }
        else
        {
            errors += '<li>You did not specify your frequency.</li>'
        }
        if( !params.type.trim().equals("") )
        {
            type = params.type
        }
        else
        {
            errors += '<li>You did not specify the type of person you are looking for.</li>'
        }

        if( errors.length() > 0 )
        {
            // If there are missing fields, throw back the error message.
            render '<p>Some Errors Occured!</p><ul>' + errors + '</ul>'
        }
        else
        {
            // Otherwise, update relevant fields in the CarpoolListing

            currentUser = session.user
            def currentListing = CarpoolListing.findByResident(currentUser)

            def departureTime = departureHour + departureMinute
            def returnTime = returnHour + returnMinute

            currentListing.status = status
            currentListing.departureTime = departureTime
            currentListing.returnTime = returnTime
            currentListing.frequency = frequency
            currentListing.type = type

            if( !params.endAddress.trim().equals("") )
            {
                currentListing.endAddress = params.endAddress

                def d = Destination.findByName(params.endAddress)
                currentListing.endLatitude = d.latitude
                currentListing.endLongitude = d.longitude
            }

            def d = Destination.findByName(params.endAddress)

            def compatiblePeople = CarpoolListing.createCriteria().list(params)
            {
                and
                {
                    eq("endLatitude", d.latitude)
                    eq("endLongitude", d.longitude)

                    // Don't retrieve the current user's listing
                    ne("resident", session.user)
                    // Retrieve only pending listings
                    eq("status", "Pending")
                }
            }

            if( compatiblePeople.size() > 0 )
            {
                for(CarpoolListing c : compatiblePeople)
                {
                    def m = new Message()
                    m.subject = "We think we might have found somewhere to carpool with you!"
                    m.receiver = c.resident
                    m.sender = Resident.findByName("Project Amity")
                    m.message = currentListing.resident.name + " (" + currentListing.resident.userid + ") is going to the same place (" + currentListing.endAddress + ") as you."
                    m.timeStamp = new Date()
                    m.isRead = false
                    m.save()
                    println( m.errors )
                }
            }
            
            render 'Listing Successfully Saved!'
        }
    }

    def view =
    {
        def listingToView = CarpoolListing.findByResident( Resident.findById(params.id) )
        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
        [ listing : listingToView, params : params ]
    }

    def search =
    {
        // prevent nullpointerexception.
        // when search.gsp is first loaded, no form fields exist and all the
        // param values are null
        if( params.endAddress != null )
        {
            params.max = 2

            if( params.neighboursOnly != null )
            {
                def neighboursOnly = params.neighboursOnly
            }

            def listings = CarpoolListing.createCriteria().list(params)
            {
                and
                {
                    if( !params.endAddress.trim().equals("") )
                    {
                        eq("endAddress", params.endAddress)
                    }

                    if( !params.departureTimeFrom.trim().equals("") && !params.departureTimeTo.trim().equals("") )
                    {
                        between("departureTime", params.departureTimeFrom, params.departureTimeTo)
                    }

                    if( !params.returnTimeFrom.trim().equals("") && !params.returnTimeTo.trim().equals("") )
                    {
                        between("returnTime", params.returnTimeFrom, params.returnTimeTo)
                    }

                    if( !params.frequency.trim().equals("") )
                    {
                        eq("frequency", params.frequency)
                    }

                    if( !params.type.trim().equals("") )
                    {
                        if( params.type.equals("Driver") )
                        {
                            eq("type", "Passenger")
                        }
                        else if( params.type.equals("Passenger") )
                        {
                            eq("type", "Driver")
                        }
                        else if( params.type.equals("Cab Pool") )
                        {
                            eq("type", "Cab Pool")
                        }
                    }

                    // Don't retrieve the current user's listing
                    ne("resident", session.user)
                    // Retrieve only pending listings
                    eq("status", "Pending")
                }
            }
            println(listings.totalCount)
            params.totalResults = listings.totalCount
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            [listings : listings, params : params]
        }
        else
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            [params : params]
        }
    }

}