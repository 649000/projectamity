package app

import grails.converters.JSON

class CarpoolListingController
{

    def messageCheckingService
    def libraryService
    def cinemaService
    def placesOfInterestService
    def counterService
    def supermarketService

    def currentUser

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