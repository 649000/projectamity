class BootStrap {

    def emailConfirmationService
    def init = { servletContext ->
        emailConfirmationService.onConfirmation = { email, uid ->
            log.info("User with id $uid has confirmed their email address $email")
            // now do something…
            println("User with id $uid has confirmed their email address $email")
            
            // Then return a map which will redirect the user to this destination
            return [controller:'resident', action:'emailconfirmation']
        }
        emailConfirmationService.onInvalid = { uid ->
            log.warn("User with id $uid failed to confirm email address after 30 days")
            return [controller:'resident', action:'emailconfirmationFail']
        }
        emailConfirmationService.onTimeout = { email, uid ->
            log.warn("User with id $uid failed to confirm email address after 30 days")
        }
    }
    def destroy = {
    }
}
