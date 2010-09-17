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
       if (session.user.userid != null)
        {
            //redirect(controller: 'resident', action: 'index')
            render(view:"index")
        }
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
                    if(resident.userid == "")
                    {  toReturn="Success Resident|new"
                        println("New Resident")}
                    else if (resident.userid != "")
                    {
                        toReturn="Success Resident|existing"
                        println("Existing Resident")}
                    
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
            def errors=""
            def resident = Resident.findByNric(session.user.nric)
            if(params.userid !="")
            {
                resident.userid = params.userid
            } else if (params.userid == "")
            {
                errors+="Username cannot be blank.\n"
            }else             if(params.userid.charAt(0).toUpperCase()=="N" && params.userid.charAt(1).toUpperCase()==('E') && params.userid.charAt(2).toUpperCase()==('A'))
            {
                errors+="Invalid username."
            }
            if(params.email !="")
            {
                resident.email = params.email
            }else
            {
                errors+="Email cannot be blank.\n"
            }

            if(params.password == params.password2 && params.password != "" && params.password2!="")
            {
                println("Password Matched")
                resident.password = params.password
            } else if (params.password =="" || params.password2 =="")
            {
                errors+="Password cannot be blank.\n"
            } else if (params.password != params.password2)
            {
                errors+="Password does not match.\n"
            }

            if(errors=="")
            {
                redirect(controller:"report",action:"index")
                session.user = resident
            }
            else
            {
                redirect(controller:"report",action:"index")
                flash.errors = errors
            }
        }
        catch(Exception e)
        {
            println(e.toString())
    
        }
    }

    def changePassword = {

        try
        {
            def errors=""
            def resident = Resident.findByNric(session.user.nric)
            if(params.password == params.password2 && params.password != "" && params.password2!="")
            {
                println("Password Matched")
                resident.password = params.password
            } else if (params.password =="" || params.password2 =="")
            {
                errors+="Password cannot be blank.\n"
            } else if (params.password != params.password2)
            {
                errors+="Password does not match.\n"
            }

            if(params.email != "")
            {
                resident.email = params.email
            } else
            {
                errors+= "Email cannot be blank"
            }

            if(errors=="")
            {
                redirect(controller:"report",action:"index")
            }
            else
            {
                redirect(controller:"report",action:"index")
                flash.errors = errors
            }
              
        }
        catch(Exception e)
        {
            println(e.toString())

        }
        
    }

    def checkUser = {
        if( params.userid != null )
        {
            if(params.userid.charAt(0).toUpperCase()=="N" && params.userid.charAt(1).toUpperCase()==('E') && params.userid.charAt(2).toUpperCase()==('A'))
            {
                render "I"
            }
            else
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
