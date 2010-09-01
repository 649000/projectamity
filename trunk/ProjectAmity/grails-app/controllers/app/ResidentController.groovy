package app

import java.util.*
import java.text.*


class ResidentController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")

    def index = { }

    def checkPassword = {

        def toReturn = "";
        def officer
        if( params.nric.charAt(0) == 'n' || params.nric.charAt(0) == 'N' )
        {
            officer = NEAOfficer.findByUserid(params.nric)

            if( officer == null )
            {
                toReturn = "Invalid NRIC / Password Combination"
            }
            else if(officer != null)
            {
                if( officer.password == params.password  )
                {
                    session.user = officer
                    toReturn = "Success NEA"
                }
                else
                {
                    toReturn = "Invalid NRIC / Password Combination"
                }
            }
        }
        else
        {
            def resident = Resident.findByNric(params.nric)

            if(resident != null)
            {
                if(resident.password == params.password)
                {
                    session.user = resident
                    println("Login Success")
                    toReturn="Success Resident"
                }
                else
                {
                    println("Wrong Password")
                    toReturn = "Invalid NRIC / Password Combination"
                }
            } else
            {
                println("Login Invalid")
                toReturn = "Invalid NRIC / Password Combination"
            }
        }

        render toReturn
    }

    def mLogin =
    {

        println("MOBILE LOGIN")


        def resident = Resident.findByNric(params.nric)
        def neaOfficer = NEAOfficer.findByUserid(params.nric)

        if(resident != null)
        {
            if(resident.password == params.password)
            {
                render "T|" + sdf.format( new Date() )+ "|" + resident.userid + "|Resident"
                println("T|" + sdf.format( new Date() )+ "|" + resident.userid + "|Resident")
            }
            else {
                render "F"
                println("F")
            }
        }
        else if (neaOfficer!=null)
        {
            if(neaOfficer.password == params.password)
            {
                render "T|" +  new Date()+ "|" + neaOfficer.userid + "|NEAOfficer"
                println("T|" +  new Date()+ "|" + neaOfficer.userid + "|NEAOfficer")
            }
            else {
                render "F"
                println("F")
            }
        }
        else {
            render "F"
            println("F")
        }
    }

    def mPostalCode = {
        def resident = Resident.findByUserid(params.userid)

        //return all the building level and stairwell.
        def level = "";
        def stairwell ="";
        def _building = Building.findAllByPostalCode(resident.postalCode)

        for(Building b: _building)
        {
            level+="|"+b.level
            stairwell+="|"+ b.stairwell
        }
        def toReturn = resident.postalCode+"~"+level+ "~" + stairwell

        render toReturn


    }
}
