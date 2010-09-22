package app

import grails.converters.*

class OuterController
{

    def index =
    {
        
        def numOfListings = CarpoolListing.createCriteria().count()
        {
            
        }

        def numOfBarters = Barter.createCriteria().count()
        {

        }

        def numOfIndoorReports = IndoorReport.createCriteria().count()
        {
            
        }

        def numOfOutdoorReports = Report.createCriteria().count()
        {

        }

        def numOfReports = numOfIndoorReports + numOfOutdoorReports

        def numbers = [ numOfListings, numOfBarters, numOfReports ]

        render numbers as JSON
        
    }
    
}
