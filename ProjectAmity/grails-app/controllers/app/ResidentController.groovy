package app

import java.util.*
import java.text.*


class ResidentController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")

    def index = {


        [ user: session.user]
        
    }

    def update =
    {
        
    }

    def definepro=
    {
        
    }

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
                    if(resident.userid == null)
                    {  toReturn="Success Resident|new"}
                    else if (resident.userid != null)
                    {
                        toReturn="Success Resident|existing"}
                    
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

    def initAccount ={
        try
        {
      
            def resident = Resident.findByNric(session.user.nric)
            println("Resident's Name: "+resident.name)
            println("Param's UserID: "+params.userid)
            println("Params Password 1, 2: " + params.password +", " + params.password2)
            resident.userid = params.userid

            if(params.password == params.password2)
            {
                println("Password Matched")
                resident.password = params.password
            }
            if( !resident.validate() ) {
                resident.errors.each {
                    println it
                }
            }
            render "T"
        }
        catch(Exception e)
        {
            println(e.toString())
    
        }
    }

    def changePassword = {

        try
        {

            def resident = Resident.findByNric(session.user.nric)
            println("Resident's Name: "+resident.name)
            println("Params Password 1, 2: " + params.password +", " + params.password2)
            if(params.password == params.password2)
            {
                println("Password Matched")
                resident.password = params.password
            }
            render "T"
        }
        catch(Exception e)
        {
            println(e.toString())

        }
        
    }

    def checkUser = {
        if( params.userid != null )
        {
            def resident = Resident.findByUserid(params.userid)
            if( resident != null )
            {
                render "F"
            }
            else
            {
                render "T"
            }
        } else{
            render "F"
        }
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
