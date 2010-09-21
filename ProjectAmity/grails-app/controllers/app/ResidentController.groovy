package app

import java.util.*
import java.text.*
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;

class ResidentController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
    def emailConfirmationService
     
    def index = {
        if(session.user!=null)
        {
            if(session.user.userid == null)
            {
                //redirect(controller:'resident', action: 'definepro')
            } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
            {
                //redirect(controller:'NEAOfficer', action: 'index')
            }else if (session.user.emailConfirm == "false")
            {
                //redirect(controller:'resident', action: 'index')
            }
            else
            {
                session.user = Resident.findByNric(session.user.nric)
                //def report = Report.find("from Report as r where r.id=?",session.user.id)
                // def indoorreport = IndoorReport.find("from IndoorReport as r where r.id=?",session.user.id)
                def counter=0
                def report = Report.findAllByResident(session.user)
                def indoorreport = IndoorReport.findAllByResident(session.user)

                for(Report r: report)
                {
                    if (r.moderationStatus=="true")
                    counter++
                }

                for(IndoorReport iR: indoorreport)
                {
                    if (iR.moderationStatus=="true")
                    counter++
                }

                //if(indoorreport.)
                [reportCount: counter]
            }

        }else
        {
            //redirect(url:"../index.gsp")
        }

    }

    def update =
    {
        
    }
    def resetPass =
    {
        
    }

    def changeView = {
        //Obtain id of view which has been moved (could be "1")
        //    	String drag = params.drag
        //        println drag
        //
        //        //Obtain id of view where view has been moved to (could be "slot_1_2")
        //        //where 1 stands for page 1 and 2 is view with id 2
        //        String drop = params.drop
        //        println drop
        //Persist new view positions

    }
    def emailconfirmation =
    {
        Resident.findByNric(session.user.nric).emailConfirm = "true"
        session.user.emailConfirm = "true"
    }

    def emailconfirmationFail =
    {
        if(Resident.findByNric(session.user.nric).emailConfirm != "true")
        {
            Resident.findByNric(session.user.nric).emailConfirm = "false"
        }
    }

    def resendEmailVerify =

    {       try
        { emailConfirmationService.sendConfirmation(session.user.email,
      "Please confirm your email address.", [from:"server@yourdomain.com"])
            println("Sent Verification Email")
        }
        catch(Exception e)
        {
            e.printStackTrace()
        }
    }

    def boolean isValidEmailAddress(String emailAddress)
    {
        // a null string is invalid
        if ( emailAddress == "" )
        return false;
        // a string without a "@" is an invalid email address
        if ( emailAddress.indexOf("@") < 0 )
        return false;
        // a string without a "."  is an invalid email address
        if ( emailAddress.indexOf(".") < 0 )
        return false;
        try
        {
            InternetAddress internetAddress = new InternetAddress(emailAddress);
            return true;
        }
        catch (AddressException ae)
        {
            // log exception
            return false;
        }
    }
    def String getRandomString(int length) {
        def charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }
    def resetPassword =
    {   def errors=""
        if(isValidEmailAddress(params.email)==false)
        {
            errors+="Invalid email.|"
        }

        if(params.nric != "")
        {
            def resident = Resident.findByNric(params.nric)
            if(resident !=null)
            {
                if(resident.email == params.email)
                {
                    def newPass = getRandomString(12)
                    println newPass
                    resident.password = newPass
                    
                    sendMail {
                        to resident.email
                        subject "Your password at Project Amity has been reset."
                        body "Hi " + resident.name + ", \n\n Your new password is "+  newPass + "\n\nWith Much Love,\n Project Amity"
                    }
                    
                } else
                errors+="The account's email does not match the NRIC."
            }
            else
            errors+="Invalid NRIC & Email combination."
        }else
        errors+="NRIC cannot be blank.|"


        if(errors != "")
        {
            render "1|" +errors
        } else
        {
            render "2|A new password has been send to the user's email address."
        }
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
                    if(resident.userid == null)
                    {  toReturn="Success Resident|new"
                        println("New Resident")}
                    else if (resident.userid != null)
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
            def dupResident = Resident.findByEmail(params.email)
            def dupResidentName = Resident.findByUserid(params.userid)

            if (params.userid == "")
            {
                errors+="Username cannot be blank.\n"
            }else             if(params.userid.charAt(0).toUpperCase()=="N" && params.userid.charAt(1).toUpperCase()==('E') && params.userid.charAt(2).toUpperCase()==('A'))
            {
                errors+="Invalid username."
            }
            if(dupResidentName != null)
            {
                errors+="Username is taken\n"
            }
            if(params.email =="")
            {
                errors+="Email cannot be blank.\n"
            }
            
            if(dupResident != null)
            {
                errors+="Email already exist in system.\n"
            }

            if(isValidEmailAddress(params.email)==false)
            {
                errors+="Invalid email.\n"
            }
        
            if(params.password != params.password2)
            {
                errors+="Password does not match.\n"
            }

            if (params.password =="" || params.password2 =="")
            {
                errors+="Password cannot be blank.\n"
            }


            if(errors=="")
            {
                resident.password = params.password
                resident.userid = params.userid
                resident.email = params.email
                redirect(controller:"report",action:"index")
                resident.emailConfirm ="false"
                emailConfirmationService.sendConfirmation(params.email,
      "Please confirm your email address.", [from:"server@yourdomain.com"])
                session.user = resident
            }
            else
            {
                redirect(controller:"report",action:"definepro")
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
            def dupResident = Resident.findByEmail(params.email)
            if(isValidEmailAddress(params.email)==false)
            {
                errors+="Invalid email.|"
            }
            if (params.password =="" || params.password2 =="")
            {
                errors+="Password cannot be blank.|"
            } else if (params.password != params.password2)
            {
                errors+="Password does not match.|"
            }

            if(dupResident != null)
            {
                //Email exist

                if(dupResident.email ==resident.email)
                {
  
                }
                else
                {
                    errors+="Email already exist in system.|"
                }
            }
            
            
            if(errors!="")
            { println errors
                render errors
            }else
            {
                if(resident.email == params.email)
                {
                    resident.password = params.password
                }else
                {

                    emailConfirmationService.sendConfirmation(params.email,
      "Please confirm your email address.", [from:"server@yourdomain.com"])
                    resident.password = params.password
                    resident.email = params.email
                    resident.emailConfirm ="false"
                }
                
                session.user =resident
                render "T"
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

    def checkNRIC =
    {
        if(params.nric != null)
        {
            def resident  = Resident.findByNric(params.nric)
            if( resident != null )
            {
                render "T"
            }
            else
            {
                render "F"
            }
        }
        
        else
        render "F"
    }
    def checkEmailUpdate =
    {
        if(params.email != null)
        {
           
            def resident  = Resident.findByEmail(params.email)
            if( resident == null )
            {   //Email doesn't exist
                println "T1"
                render "T"
            }
            else if(params.email == session.user.email)
            {   //Email does exist but belongs to user
                println "T2"
                render "T"
            }  else
            {
                render "F"
            }
        }

        else
        render "F"
    }
    def checkEmailDefine =
    {
        if(params.email != null)
        {

            def resident  = Resident.findByEmail(params.email)
            if( resident == null )
            {   //Email doesn't exist
                println "T1"
                render "T"
            }
            else if(params.email == session.user.email)
            {   //Email does exist but belongs to user
                println "T2"
                render "T"
            }  else
            {   println "F"
                render "F"
            }
        }

        else
        render "F"
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
