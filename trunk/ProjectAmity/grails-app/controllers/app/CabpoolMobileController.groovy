package app

import java.text.*;
import java.util.*
import grails.converters.JSON

class CabpoolMobileController
{

    def updateLocation =
    {
        def user = params.user
        def lat = params.lat
        def lng = params.lng
        def dest = params.dest

        println("CabpoolMobile: " + user + " at " + lat + "," + lng + " heading to " + dest )

        if(user != null)
        {
            user = Resident.findByUserid(user)

            if( user.cabpoolListing == null )
            {
                def u = new CabpoolListing()
                u.latitude = lat
                u.longitude = lng
                if( dest == null )
                {
                    u.destination = "Not Defined"
                }
                else if( dest.equalsIgnoreCase("") )
                {
                    u.destination = "Not Defined"
                }
                else
                {
                    u.destination = dest
                }
                u.timeStamp = new Date()
                u.resident = user

                user.cabpoolListing =  u
            }
            else
            {
                def u = user.cabpoolListing
                u.latitude = lat
                u.longitude = lng
                if( dest == null )
                {
                    u.destination = "Not Defined"
                }
                else if( dest.equalsIgnoreCase("") )
                {
                    u.destination = "Not Defined"
                }
                else
                {
                    u.destination = dest
                }
                u.timeStamp = new Date()
            }

            render getNearbyCabpools( Double.valueOf(lat), Double.valueOf(lng), user ) as JSON
        }
    }

    def updateDestination =
    {
        def user = params.user
        double lat = Double.valueOf(params.lat)
        double lng = Double.valueOf(params.lng)
        def dest = params.dest

        println("CabpoolMobile: " + user + " heading to " + dest )

        if(user != null)
        {
            user = Resident.findByUserid(user)

            if( user.cabpoolListing == null )
            {
                def u = new CabpoolListing()
                if( dest == null )
                {
                    u.destination = "Not Defined"
                }
                else
                {
                    u.destination = dest
                }
                u.resident = user

                user.cabpoolListing =  u
            }
            else
            {
                def u = user.cabpoolListing
                if( dest == null )
                {
                    u.destination = "Not Defined"
                }
                else
                {
                    u.destination = dest
                }
            }

            render getNearbyCabpools(lat, lng, user) as JSON
        }
    }

    def ArrayList<CabpoolListing> getNearbyCabpools(double lat, double lng, Resident user)
    {
        def c = CabpoolListing.createCriteria().list(params)
        {
            and
            {
                ne("resident", user)
            }
        }

        // println( c.totalCount )
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
        Date timeStamp
        Date now = new Date()

        def filteredList = new ArrayList()
        for( CabpoolListing cl : c )
        {
            println( cl.timeStamp )
            timeStamp = new Date( cl.timeStamp.getTime() )
            def difference = ( now.getTime() - timeStamp.getTime() ) // milliseconds
            difference = (difference/60000)
            // now converted to minutes

            def dist = calculateDistance( Double.valueOf(lat), Double.valueOf(lng), Double.valueOf(cl.latitude), Double.valueOf(cl.longitude) )
            println( cl.resident.name + ", " + difference + " minutes ago and " + dist + "km away" )
            if( difference <= 30 && dist <= 2.5 )
            {
                filteredList.add( cl )
            }
        }

        def userids = new ArrayList()
        for(int i = 0 ; i < filteredList.size() ; i++ )
        {
            userids.add( filteredList.get(i).resident.userid )
        }

        def toReturn = [ filteredList, userids ]

        println( filteredList.size() + " MATCHES" )
        return toReturn
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