package app

class LoginSecurityFilters {

    def filters = {
        all(controller:'report', action:'index|loadData|verify|sendData|loadData2') {
            before = {
                if(!session.user)
                {
                    redirect(url:"../index.gsp")
                } else if (session.user !=null && session.user.userid ==null)
                {
                    redirect(controller:'resident', action: 'definepro')
                } else if (Resident.findByUserid(session.user.userid) ==null)
                {
                    //redirect(url:"../index.gsp")
                    redirect(controller:'NEAOfficer', action: 'index')
                }
            }
            after = {
                
            }
            afterView = {
                
            }
        }
        all(controller:'building', action:'index|loadBuilding') {
            before = {
                if(!session.user)
                {
                    redirect(url:"../index.gsp")
                } else if (session.user !=null && session.user.userid ==null)
                {
                    redirect(controller:'resident', action: 'definepro')
                } else if (Resident.findByUserid(session.user.userid) ==null)
                {
                    //redirect(url:"../index.gsp")
                    redirect(controller:'NEAOfficer', action: 'index')
                }
            }
            after = {

            }
            afterView = {

            }
        }

        neaCheck(controller:'NEAOfficer', action:'*') {
            before = {
                if(!session.user || session.user.userid ==null)
                {
                    redirect(url:"../index.gsp")
                } else if (session.user !=null && session.user.userid !=null)
                {
                    def neaOff = NEAOfficer.findByUserid(session.user.userid)
                    println(neaOff)
                    if(neaOff == null)
                    {redirect(url:"../index.gsp")}
                }

            }
        }

        residentCheck(controller:'resident', action:'index|update|changePassword') {
            
            before = {
                if(session.user ==null)
                {
                    redirect(url:"../index.gsp")
                } else if (session.user.userid ==null)
                {
                    redirect(controller:'resident', action: 'definepro')
                } else if (Resident.findByUserid(session.user.userid) ==null)
                {
                    redirect(url:"../index.gsp")
                }

            }
        }

        residentInitAccount(controller:'resident', action:'initAccount|definepro') {
            before = {
                if(session.user ==null)
                {
                    redirect(url:"../index.gsp")
                } else if (session.user.userid !=null)
                {
                    redirect(controller:'resident', action: 'index')
                }

            }
        }
    }
    
}
