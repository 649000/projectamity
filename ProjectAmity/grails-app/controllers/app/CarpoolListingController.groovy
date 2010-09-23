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
        if(session.user!=null)
        {
            if(session.user.userid == null)
            {
                //User has not logged in
                //redirect(controller:'resident', action: 'definepro')
            } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
            {
                //User has logged in but an NEAOfficer
                //redirect(controller:'NEAOfficer', action: 'index')
            }else if (session.user.emailConfirm == "false")
            {
                //User has logged in but has not verify email
                //redirect(controller:'resident', action: 'index')
            }
            else
            {
                //Means user is loggedin, has verified email & is not a neaofficer
            }

        }else
        {
            //redirect(url:"../index.gsp")
        }

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

    def getDestinations =
    {
        def lat = params.lat
        def lng = params.lng

        def places = Destination.createCriteria().list()
        {
        }

        // startlat startlng endlat endlng
        ArrayList<Destination> candidates = new ArrayList()
        ArrayList<Double> distance = new ArrayList()

        for(Destination d : places)
        {
            Double dist = calculateDistance( Double.valueOf(lat), Double.valueOf(lng), Double.valueOf(d.latitude), Double.valueOf(d.longitude) )
            if( candidates.size() == 0 )
            {
                candidates.add(d)
                distance.add(dist)
            }
            else
            {
                if( dist < distance.get(0) )
                {
                    candidates.add( 0, d )
                    distance.add( 0, dist )
                }
                else
                {
                    candidates.add( d )
                    distance.add( dist )
                }
            }
        }

        ArrayList<Destination> top4 = new ArrayList()
        ArrayList<Double> top4Dist = new ArrayList()
        for( int i = 0  ; i < 4 ; i++ )
        {
            top4.add( candidates.get(i) )
        }
        for( int j = 0  ; j < 4 ; j++ )
        {
            top4Dist.add( truncate(distance.get(j)) )
        }

        def results = [ top4, top4Dist ]

        render results as JSON

    }

    def double truncate(double x)
    {
        if ( x > 0 )
        {
            return Math.floor(x * 100)/100;
        }
        else
        {
            return Math.ceil(x * 100)/100;
        }
    }


    def ajaxMatch =
    {
        def listingId = params.listingId

        // The listing for which matches are to be found.
        def listing = CarpoolListing.findById( listingId )

        // The list of possible matches. Not filtered yet.
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

        // holds the list of candidates who qualify to be matches
        ArrayList<CarpoolListing> candidates = new ArrayList()
        // holds a score that allows us to rank the matches
        ArrayList<Integer> matchCount = new ArrayList()

        for( CarpoolListing l : listings )
        {
            println( l.id + " " + calculateDistance(Double.valueOf(listing.startLatitude), Double.valueOf(listing.startLongitude), Double.valueOf(l.startLatitude), Double.valueOf(l.startLongitude)) + ", " + calculateDistance(Double.valueOf(listing.endLatitude), Double.valueOf(listing.endLongitude), Double.valueOf(l.endLatitude), Double.valueOf(l.endLongitude)) )
            // first round of elimination - only carpool listings whose start AND end locations are 3km from the
            // start and end location of the carpool listing for which matches are to be found qualify
            if( (calculateDistance(Double.valueOf(listing.startLatitude), Double.valueOf(listing.startLongitude), Double.valueOf(l.startLatitude), Double.valueOf(l.startLongitude)) <= 3) && (calculateDistance(Double.valueOf(listing.endLatitude), Double.valueOf(listing.endLongitude), Double.valueOf(l.endLatitude), Double.valueOf(l.endLongitude)) <= 3) )
            {
                if( listing.tripType.equalsIgnoreCase("commute") )
                {
                    // checkTimings will compare all the departure and return timings to
                    // see count how many actually match
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
                    long diff = Math.abs(milis2 - milis1)

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
                        long diff2 = Math.abs(milis4 - milis3)

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

        // only return the top 5 matches
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

        def listingIndex, candidateIndex

        // Monday departure time
        if( p.departureMondayTime != null && q.departureMondayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureMondayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureMondayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureMondayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureMondayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureMondayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureMondayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureMondayInterval) ) >= times.length) && q.departureTuesdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureTuesdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureMondayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }
            
            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureMondayInterval) ) < 0) && q.departureSundayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureSundayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureMondayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Tuedsday departure time
        if( p.departureTuesdayTime != null && q.departureTuesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureTuesdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureTuesdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureTuesdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureTuesdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureTuesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureTuesdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureTuesdayInterval) ) >= times.length) && q.departureWednesdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureWednesdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureTuesdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureTuesdayInterval) ) < 0) && q.departureMondayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureMondayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureTuesdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Wednesday departure time
        if( p.departureWednesdayTime != null && q.departureWednesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureWednesdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureWednesdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureWednesdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureWednesdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureWednesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureWednesdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureWednesdayInterval) ) >= times.length) && q.departureThursdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureThursdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureWednesdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureWednesdayInterval) ) < 0) && q.departureTuesdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureTuesdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureWednesdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Thursday departure time
        if( p.departureThursdayTime != null && q.departureThursdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureThursdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureThursdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureThursdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureThursdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureThursdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureThursdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureThursdayInterval) ) >= times.length) && q.departureFridayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureFridayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureThursdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureThursdayInterval) ) < 0) && q.departureWednesdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureWednesdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureThursdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Friday departure time
        if( p.departureFridayTime != null && q.departureFridayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureFridayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureFridayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureFridayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureFridayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureFridayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureFridayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureFridayInterval) ) >= times.length) && q.departureSaturdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureSaturdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureFridayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureFridayInterval) ) < 0) && q.departureThursdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureThursdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureFridayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Saturday departure time
        if( p.departureSaturdayTime != null && q.departureSaturdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureSaturdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureSaturdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureSaturdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureSaturdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureSaturdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureSaturdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureSaturdayInterval) ) >= times.length) && q.departureSundayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureSundayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureSaturdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureSaturdayInterval) ) < 0) && q.departureFridayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureFridayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureSaturdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Sunday departure time
        if( p.departureSundayTime != null && q.departureSundayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureSundayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.departureSundayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.departureSundayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.departureSundayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.departureSundayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.departureSundayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.departureSundayInterval) ) >= times.length) && q.departureMondayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.departureMondayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.departureSundayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.departureSundayInterval) ) < 0) && q.departureSaturdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.departureSaturdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.departureSundayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Monday return time
        if( p.returnMondayTime != null && q.returnMondayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnMondayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnMondayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnMondayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnMondayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnMondayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnMondayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnMondayInterval) ) >= times.length) && q.returnTuesdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnTuesdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnMondayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnMondayInterval) ) < 0) && q.returnSundayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnSundayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnMondayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Tuedsday return time
        if( p.returnTuesdayTime != null && q.returnTuesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnTuesdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnTuesdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnTuesdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnTuesdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnTuesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnTuesdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnTuesdayInterval) ) >= times.length) && q.returnWednesdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnWednesdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnTuesdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnTuesdayInterval) ) < 0) && q.returnMondayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnMondayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnTuesdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Wednesday return time
        if( p.returnWednesdayTime != null && q.returnWednesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnWednesdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnWednesdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnWednesdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnWednesdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnWednesdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnWednesdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnWednesdayInterval) ) >= times.length) && q.returnThursdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnThursdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnWednesdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnWednesdayInterval) ) < 0) && q.returnTuesdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnTuesdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnWednesdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Thursday return time
        if( p.returnThursdayTime != null && q.returnThursdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnThursdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnThursdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnThursdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnThursdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnThursdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnThursdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnThursdayInterval) ) >= times.length) && q.returnFridayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnFridayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnThursdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnThursdayInterval) ) < 0) && q.returnWednesdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnWednesdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnThursdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Friday return time
        if( p.returnFridayTime != null && q.returnFridayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnFridayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnFridayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnFridayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnFridayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnFridayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnFridayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnFridayInterval) ) >= times.length) && q.returnSaturdayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnSaturdayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnFridayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnFridayInterval) ) < 0) && q.returnThursdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnThursdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnFridayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Saturday return time
        if( p.returnSaturdayTime != null && q.returnSaturdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnSaturdayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnSaturdayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnSaturdayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnSaturdayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnSaturdayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnSaturdayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnSaturdayInterval) ) >= times.length) && q.returnSundayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnSundayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnSaturdayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnSaturdayInterval) ) < 0) && q.returnFridayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnFridayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnSaturdayInterval)) ) )
                {
                    matchingCount++
                }
            }
        }

        // Sunday return time
        if( p.returnSundayTime != null && q.returnSundayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnSundayTime )
            candidateIndex = ArrayUtils.indexOf( times, q.returnSundayTime )

            if( Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , p.returnSundayInterval) || Math.abs(listingIndex - candidateIndex) <= ArrayUtils.indexOf(intervals , q.returnSundayInterval ) )
            {
                matchingCount++
            }
        }
        if( p.returnSundayTime != null )
        {
            listingIndex = ArrayUtils.indexOf( times, p.returnSundayTime )
            if( (( listingIndex + ArrayUtils.indexOf(intervals, p.returnSundayInterval) ) >= times.length) && q.returnMondayTime != null )
            {
                if( ArrayUtils.indexOf(times, q.returnMondayTime) <= ( (listingIndex+ArrayUtils.indexOf(intervals, p.returnSundayInterval)) - times.length ) )
                {
                    matchingCount++
                }
            }

            if( (( listingIndex - ArrayUtils.indexOf(intervals, p.returnSundayInterval) ) < 0) && q.returnSaturdayTime != null )
            {
                if( (times.length - ArrayUtils.indexOf(times, q.returnSaturdayTime)) >= Math.abs( (listingIndex-ArrayUtils.indexOf(intervals, p.returnSundayInterval)) ) )
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