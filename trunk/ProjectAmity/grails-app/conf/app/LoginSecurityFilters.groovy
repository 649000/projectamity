package app

class LoginSecurityFilters {

    def filters = {
        all(controller:'report', action:'index|loadData|verify|sendData|loadData2') {
            before = {
                if(session.user==null)
                {
                    redirect(url:"http://projectamity.info/ProjectAmity/index.gsp")
                } else if (session.user.userid ==null)
                {
                    redirect(controller:'resident', action: 'definepro')
                } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
                {
                    redirect(controller:'NEAOfficer', action: 'index')
                }
                else if (session.user.emailConfirm == "false")
                {
                    redirect(controller:'resident', action: 'index')
                }
            }
            after = {
                
            }
            afterView = {
                
            }
        }
        all(controller:'building', action:'index|loadBuilding') {
            before = {
                if(session.user==null)
                {
                    redirect(url:"http://projectamity.info/ProjectAmity/index.gsp")
                } else if (session.user.userid ==null)
                {
                    redirect(controller:'resident', action: 'definepro')
                } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
                {
                    redirect(controller:'NEAOfficer', action: 'index')
                } else if (session.user.emailConfirm == "false")
                {
                    redirect(controller:'resident', action: 'index')
                }
            }
            after = {

            }
            afterView = {

            }
        }

        neaCheck(controller:'NEAOfficer', action:'*') {
            before = {
                if(session.user == null)
                {
                    redirect(url:"http://projectamity.info/ProjectAmity/index.gsp")
                } else if (session.user.userid.charAt(0).toUpperCase()!="N" && session.user.userid.charAt(1).toUpperCase()!="E" &&session.user.userid.charAt(2).toUpperCase()!="A")
                {                   
                    redirect(url:"./index.gsp")
                }

            }
        }

        residentCheck(controller:'resident', action:'index|update|changePassword') {
            
            before = {
                if(session.user ==null)
                {
                    redirect(url:"http://projectamity.info/ProjectAmity/index.gsp")
                } else if (session.user.userid ==null)
                {
                    redirect(controller:'resident', action: 'definepro')
                } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
                {
                    redirect(controller:'NEAOfficer', action: 'index')
                }

            }
        }

        residentInitAccount(controller:'resident', action:'initAccount|definepro') {
            before = {
                if(session.user ==null)
                {
                    redirect(url:"http://projectamity.info/ProjectAmity/index.gsp")
                } else if (session.user.userid !=null)
                {
                    if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
                    redirect(controller:'NEAOfficer', action: 'index')
                    else
                    redirect(controller:'resident', action: 'index')
                } 

            }
        }
    }
    
}
