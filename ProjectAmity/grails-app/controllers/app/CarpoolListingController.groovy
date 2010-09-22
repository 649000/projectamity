package app

import grails.converters.JSON
import org.apache.commons.lang.ArrayUtils;
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

    def add =
    {
        currentUser = session.user

        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)

        [ params : params ]
    }

    def view =
    {
        if( session.user )
        {
            currentUser = session.user
        }

        def l = CarpoolListing.findById( params.id )

        if( session.user )
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)
        }

        def confirmedRiders = CarpoolRequest.createCriteria().list()
        {
            eq("carpoolListing", l)
            eq("status", "Accepted")
        }
        params.confirmedRiders = confirmedRiders

        if( session.user )
        {
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
                request = CarpoolRequest.createCriteria().get()
                {
                    and
                    {
                        eq("carpoolListing", l)
                        eq( "resident", Resident.findById(session.user.id) )
                        eq("status", "Accepted")
                    }
                }
                if( request == null )
                {
                    params.requested = "F"
                }
                else
                {
                    params.requested = "A"
                }
            }
            else
            {
                params.requested = "T"
            }
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

        def positives = CarpoolRating.createCriteria().count()
        {
            and
            {
                eq("rated", l.resident )
                eq("rating", "Good")
            }
        }
        params.positives = positives

        def negatives = CarpoolRating.createCriteria().count()
        {
            and
            {
                eq("rated", l.resident )
                eq("rating", "Bad")
            }
        }
        params.negatives = negatives

        [ l : l , params : params ]
    }

    def match =
    {
        if( session.user )
        {
            currentUser = session.user
        }

        def l = CarpoolListing.findById( params.id )

        if( session.user )
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(currentUser)
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

    def index =
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

    def ajaxLoadRatings =
    {

        currentUser = session.user

        def ratings =  CarpoolRating.executeQuery(
        "select l.tripType, l.startAddress, l.endAddress, l.riderType, r.rated.name, r.id, l.id, r.rated.userid from CarpoolRating r, CarpoolRequest c, CarpoolListing l where r.request.id = c.id and c.carpoolListing.id = l.id and r.rating = 'Nil' and r.rater.id = '" + session.user.id + "' order by l.id asc" )

        render ratings as JSON

    }

    def ajaxLoadInactiveListings =
    {
        currentUser = session.user

        def listings = CarpoolListing.createCriteria().list(params)
        {
            and
            {
                eq("resident", session.user)
                eq("status", "inactive")
            }
            order("dateDeactivated", "desc")
            maxResults(5)
        }

        render listings as JSON

    }

    def ajaxLoadRequests =
    {
        currentUser = session.user

        def requests =  CarpoolRequest.executeQuery( "select l.tripType, l.startAddress, l.endAddress, l.riderType, c.resident.name, c.message.message, c.id, l.id, c.resident.userid from CarpoolRequest c, CarpoolListing l where c.carpoolListing.id = l.id and c.status = 'Pending' and l.status = 'active' and l.resident.id = '" + session.user.id + "' order by l.id asc" )

        println( requests )

        render requests as JSON

    }

    def checkRating =
    {
        def resident = params.resident

        if( Resident.findByUserid(resident) )
        {
            resident = Resident.findByUserid(resident)
        }
        else
        {
            params.statement = "We could not check this person\'s reputation because no such person exists."
        }

        def positives = CarpoolRating.createCriteria().count()
        {
            and
            {
                eq("rated", resident )
                eq("rating", "Good")
            }
        }

        def negatives = CarpoolRating.createCriteria().count()
        {
            and
            {
                eq("rated", resident )
                eq("rating", "Bad")
            }
        }

        params.statement = "This person has received <b>" + positives + "</b> positive rating(s) and <b>" + negatives + "</b> negative rating(s)."
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

    def ajaxDeactivateListing =
    {

        def listing

        listing = params.listingId
        if( CarpoolListing.findById(listing) == null )
        {
            errors += '\nA carpool listing by the ID of ' + listing + ' does not exist!'
        }
        else
        {
            listing = CarpoolListing.findById(listing)
        }

        if( listing.resident.id != session.user.id )
        {
            // Not allowed to deactivate someone else's listings
            render 'F'
        }
        else
        {
            listing.dateDeactivated = new Date()
            listing.status = "inactive"
            render 'T'
        }

    }

    def ajaxRate =
    {

        def rating

        rating = params.ratingId
        if( CarpoolRating.findById(rating) == null )
        {
            errors += '\nA carpool rating by the ID of ' + rating + ' does not exist!'
        }
        else
        {
            rating = CarpoolRating.findById(rating)
        }

        if( rating.rater.id != session.user.id )
        {
            // Not allowed to deactivate someone else's listings
            render 'F'
        }
        else
        {
            rating.rating = params.rating
            render 'T'
        }

    }

    def ajaxSendResponse =
    {

        def errors = ''
        def recipient
        def request
        def subject
        def message
        def mode

        mode = params.mode

        request = params.requestId
        if( CarpoolRequest.findById(request) == null )
        {
            errors += '\nA carpool request by the ID of ' + request + ' does not exist!'
        }
        else
        {
            request = CarpoolRequest.findById(request)
        }

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

            if( newMessage.save() )
            {
                // Update the status of the carpool request
                if( mode.equalsIgnoreCase("Accept") )
                {
                    request.status = "Accepted"

                    // Create new CarpoolRating objects
                    CarpoolRating r1 = new CarpoolRating()
                    r1.rater = session.user
                    r1.rated = recipient
                    r1.rating = "Nil"
                    r1.request = request
                    r1.save()

                    CarpoolRating r2 = new CarpoolRating()
                    r2.rater = recipient
                    r2.rated = session.user
                    r2.rating = "Nil"
                    r2.request = request
                    r2.save()
                }
                else
                {
                    request.status = "Declined"
                }

                render 'T'
            }
            else
            {
                println( newMessage.errors )
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

    def ajaxMatch =
    {
        def listingId = params.listingId

        def listing = CarpoolListing.findById( listingId )

        def listings = CarpoolListing.createCriteria().list(params)
        {
            and
            {
                // listing must not belong to current user
                ne("resident", session.user)

                if( listing.tripType.equalsIgnoreCase("commute") )
                {
                    eq("tripType", "commute")
                }
                else
                {
                    eq("tripType", "oneOff")
                }

                if( listing.riderType.equalsIgnoreCase("Passenger") )
                {
                    eq("riderType", "Driver")
                }
                else if( listing.riderType.equalsIgnoreCase("Driver") )
                {
                    eq("riderType", "Passenger")
                }
                else
                {
                    eq("riderType", "Cab Pooler")
                }
                eq("status", "active")
            }
        }

        ArrayList<CarpoolListing> candidates = new ArrayList()
        ArrayList<Integer> matchCount = new ArrayList()

        for( CarpoolListing l : listings )
        {
            println( l.id + " " + calculateDistance(Double.valueOf(listing.startLatitude), Double.valueOf(listing.startLongitude), Double.valueOf(l.startLatitude), Double.valueOf(l.startLongitude)) + ", " + calculateDistance(Double.valueOf(listing.endLatitude), Double.valueOf(listing.endLongitude), Double.valueOf(l.endLatitude), Double.valueOf(l.endLongitude)) )
            if( (calculateDistance(Double.valueOf(listing.startLatitude), Double.valueOf(listing.startLongitude), Double.valueOf(l.startLatitude), Double.valueOf(l.startLongitude)) <= 3) && (calculateDistance(Double.valueOf(listing.endLatitude), Double.valueOf(listing.endLongitude), Double.valueOf(l.endLatitude), Double.valueOf(l.endLongitude)) <= 3) )
            {
                if( listing.tripType.equalsIgnoreCase("commute") )
                {
                    def relevanceCount = checkTimings(listing, l)
                    if( relevanceCount > 0 )
                    {
                        if( candidates.size() == 0 )
                        {
                            candidates.add(l)
                            matchCount.add(relevanceCount)
                        }
                        else
                        {
                            if( matchCount.get(0) < relevanceCount )
                            {
                                candidates.add( 0, l )
                                matchCount.add( 0, l )
                            }
                            else
                            {
                                candidates.add( l )
                                matchCount.add( l )
                            }
                        }
                    }
                }
                else
                {
                    int relevance = 0

                    // check if the timings of the one-off trip match
                    Calendar cal1 = Calendar.getInstance()
                    Calendar cal2 = Calendar.getInstance()

                    cal1.setTime( listing.oneOffDepartureTime )
                    cal2.setTime( l.oneOffDepartureTime )

                    // Get the represented date in milliseconds
                    long milis1 = cal1.getTimeInMillis()
                    long milis2 = cal2.getTimeInMillis()

                    // Calculate difference in milliseconds
                    long diff = milis2 - milis1

                    // Calculate difference in minutes
                    long diffMinutes = diff / (60 * 1000)

                    if( diffMinutes <= listing.oneOffDepartureInterval )
                    {
                        relevance++
                    }

                    if( listing.oneOffReturnTime != null )
                    {
                        // check if the timings of the one-off trip match
                        Calendar cal3 = Calendar.getInstance()
                        Calendar cal4 = Calendar.getInstance()

                        cal3.setTime( listing.oneOffReturnTime )
                        cal4.setTime( l.oneOffReturnTime )

                        // Get the represented date in milliseconds
                        long milis3 = cal3.getTimeInMillis()
                        long milis4 = cal4.getTimeInMillis()

                        // Calculate difference in milliseconds
                        long diff2 = milis4 - milis3

                        // Calculate difference in minutes
                        long diffMinutes2 = diff2 / (60 * 1000)

                        if( diffMinutes2 <= listing.oneOffReturnInterval )
                        {
                            relevance++
                        }
                    }

                    if( relevance > 0 )
                    {
                        if( candidates.size() == 0 )
                        {
                            candidates.add(l)
                            matchCount.add(relevance)
                        }
                        else
                        {
                            if( matchCount.get(0) < relevance )
                            {
                                candidates.add( 0, l )
                                matchCount.add( 0, l )
                            }
                            else
                            {
                                candidates.add( l )
                                matchCount.add( l )
                            }
                        }
                    }
                }
            }
        }

        if( candidates.size() <= 5 )
        {
            render candidates as JSON
        }
        else
        {
            ArrayList<CarpoolListing> top5 = new ArrayList(5)
            for( int i = 0 ; i < 5 ; i++ )
            {
                top5.add( candidates.get(i) )
            }
            render top5 as JSON
        }

        for(CarpoolListing l : candidates)
        {
            println( l.id + ", " + l.startAddress + " to " + l.endAddress )
        }

        render candidates as JSON

    }

    def int checkTimings(CarpoolListing p, CarpoolListing q)
    {
        def String[] times = [ "12:00 AM", "12:15 AM", "12:30 AM", "12:45 AM", "01:00 AM", "01:15 AM", "01:30 AM", "01:45 AM", "02:00 AM", "02:15 AM", "02:30 AM", "02:45 AM", "03:00 AM", "03:15 AM", "03:30 AM", "03:45 AM", "04:00 AM", "04:15 AM", "04:30 AM", "04:45 AM", "05:00 AM", "05:15 AM", "05:30 AM", "05:45 AM", "06:00 AM", "06:15 AM", "06:30 AM", "06:45 AM", "07:00 AM", "07:15 AM", "07:30 AM", "07:45 AM", "08:00 AM", "08:15 AM", "08:30 AM", "08:45 AM", "09:00 AM", "09:15 AM", "09:30 AM", "09:45 AM", "10:00 AM", "10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM", "11:15 AM", "11:30 AM", "11:45 AM", "12:00 PM", "12:15 PM", "12:30 PM", "12:45 PM", "01:00 PM", "01:15 PM", "01:30 PM", "01:45 PM", "02:00 PM", "02:15 PM", "02:30 PM", "02:45 PM", "03:00 PM", "03:15 PM", "03:30 PM", "03:45 PM", "04:00 PM", "04:15 PM", "04:30 PM", "04:45 PM", "05:00 PM", "05:15 PM", "05:30 PM", "05:45 PM", "06:00 PM", "06:15 PM", "06:30 PM", "06:45 PM", "07:00 PM", "07:15 PM", "07:30 PM", "07:45 PM", "08:00 PM", "08:15 PM", "08:30 PM", "08:45 PM", "09:00 PM", "09:15 PM", "09:30 PM", "09:45 PM", "10:00 PM", "10:15 PM", "10:30 PM", "10:45 PM", "11:00 PM", "11:15 PM", "11:30 PM", "11:45 PM" ]
        def int[] intervals = [ 0, 15, 30, 45, 60 ]
        def int matchingCount = 0

        def pin, qin

        if( p.departureMondayTime != null && q.departureMondayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureMondayTime )
            qin = ArrayUtils.indexOf( times, q.departureMondayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureMondayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureMondayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureMondayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureMondayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureMondayTime) ) >= times.length) && q.departureTuesdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureTuesdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureMondayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.departureTuesdayTime != null && q.departureTuesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureTuesdayTime )
            qin = ArrayUtils.indexOf( times, q.departureTuesdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureTuesdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureTuesdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureTuesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureTuesdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureTuesdayTime) ) >= times.length) && q.departureWednesdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureWednesdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureTuesdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.departureWednesdayTime != null && q.departureWednesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureWednesdayTime )
            qin = ArrayUtils.indexOf( times, q.departureWednesdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureWednesdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureWednesdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureWednesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureWednesdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureWednesdayTime) ) >= times.length) && q.departureThursdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureThursdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureWednesdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.departureThursdayTime != null && q.departureThursdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureThursdayTime )
            qin = ArrayUtils.indexOf( times, q.departureThursdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureThursdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureThursdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureThursdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureThursdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureThursdayTime) ) >= times.length) && q.departureFridayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureFridayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureThursdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.departureFridayTime != null && q.departureFridayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureFridayTime )
            qin = ArrayUtils.indexOf( times, q.departureFridayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureFridayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureFridayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureFridayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureFridayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureFridayTime) ) >= times.length) && q.departureSaturdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureSaturdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureFridayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.departureSaturdayTime != null && q.departureSaturdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureSaturdayTime )
            qin = ArrayUtils.indexOf( times, q.departureSaturdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureSaturdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureSaturdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureSaturdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureSaturdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureSaturdayTime) ) >= times.length) && q.departureSundayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureSundayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureSaturdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.departureSundayTime != null && q.departureSundayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureSundayTime )
            qin = ArrayUtils.indexOf( times, q.departureSundayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.departureSundayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.departureSundayInterval) )
            {
                matchingCount++
            }
        }
        if( p.departureSundayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.departureSundayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.departureSundayTime) ) >= times.length) && q.departureMondayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.departureMondayTime) <= ( (pin+ArrayUtils.indexOf( times, p.departureSundayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }

        if( p.returnMondayTime != null && q.returnMondayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnMondayTime )
            qin = ArrayUtils.indexOf( times, q.returnMondayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnMondayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnMondayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnMondayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnMondayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnMondayTime) ) >= times.length) && q.returnTuesdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnTuesdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnMondayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.returnTuesdayTime != null && q.returnTuesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnTuesdayTime )
            qin = ArrayUtils.indexOf( times, q.returnTuesdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnTuesdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnTuesdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnTuesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnTuesdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnTuesdayTime) ) >= times.length) && q.returnWednesdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnWednesdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnTuesdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.returnWednesdayTime != null && q.returnWednesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnWednesdayTime )
            qin = ArrayUtils.indexOf( times, q.returnWednesdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnWednesdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnWednesdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnWednesdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnWednesdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnWednesdayTime) ) >= times.length) && q.returnThursdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnThursdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnWednesdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.returnThursdayTime != null && q.returnThursdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnThursdayTime )
            qin = ArrayUtils.indexOf( times, q.returnThursdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnThursdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnThursdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnThursdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnThursdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnThursdayTime) ) >= times.length) && q.returnFridayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnFridayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnThursdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.returnFridayTime != null && q.returnFridayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnFridayTime )
            qin = ArrayUtils.indexOf( times, q.returnFridayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnFridayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnFridayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnFridayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnFridayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnFridayTime) ) >= times.length) && q.returnSaturdayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnSaturdayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnFridayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.returnSaturdayTime != null && q.returnSaturdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnSaturdayTime )
            qin = ArrayUtils.indexOf( times, q.returnSaturdayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnSaturdayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnSaturdayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnSaturdayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnSaturdayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnSaturdayTime) ) >= times.length) && q.returnSundayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnSundayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnSaturdayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }
        if( p.returnSundayTime != null && q.returnSundayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnSundayTime )
            qin = ArrayUtils.indexOf( times, q.returnSundayTime )

            if( Math.abs(pin -qin) <= ArrayUtils.indexOf( intervals,p.returnSundayInterval) || Math.abs(pin-qin) <= ArrayUtils.indexOf( intervals,p.returnSundayInterval) )
            {
                matchingCount++
            }
        }
        if( p.returnSundayTime != null )
        {
            pin = ArrayUtils.indexOf( times, p.returnSundayTime )
            if( (( pin + ArrayUtils.indexOf( times, p.returnSundayTime) ) >= times.length) && q.returnMondayTime != null )
            {
                if( ArrayUtils.indexOf( times, q.returnMondayTime) <= ( (pin+ArrayUtils.indexOf( times, p.returnSundayTime)) - times.length ) )
                {
                    matchingCount++
                }
            }
        }

        return matchingCount
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

}