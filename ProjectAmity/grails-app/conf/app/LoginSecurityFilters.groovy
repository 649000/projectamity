package app

class LoginSecurityFilters {

    def filters = {
        all(controller:'report|resident|building|NEAOfficer', action:'*') {
            before = {
                if(!session.user)
                {
                    redirect(url:"../index.gsp")
                } else if (session.user !=null && session.user.userid ==null)
                {
                     redirect(controller:'resident', action: 'definepro')
                }
                
            }
            after = {
                
            }
            afterView = {
                
            }
        }
    }
    
}
