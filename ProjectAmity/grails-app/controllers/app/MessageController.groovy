package app

import java.text.SimpleDateFormat
import java.util.Calendar
import grails.converters.JSON

class MessageController
{

    def messageCheckingService
    def currentUser

    // Load the user's inbox
    def index =
    {
        if(session.user!=null)
        {
            if(session.user.userid == null)
            {
            //User has not logged in
            //redirect(controller:'resident', action: 'definepro')
            } else if (session.user.userid.charAt(0).toUpperCase()=="N" && session.user.userid.charAt(1).toUpperCase()=="E" &&session.user.userid.charAt(2).toUpperCase()=="A")
            {
//User has logged in but an NEAOfficer
                //redirect(controller:'NEAOfficer', action: 'index')
            }else if (session.user.emailConfirm == "false")
            {
//User has logged in but has not verify email
                //redirect(controller:'resident', action: 'index')
            }
            else
            {
                //Means user is loggedin, has verified email & is not a neaofficer
            }

        }else
        {
            //redirect(url:"../index.gsp")
        }

        params.max = 5
        def inboxMessages = Message.createCriteria().list(params)
        {
            and
            {
                eq("receiver", session.user)
            }
            order("timeStamp", "desc")
        }

        println(inboxMessages.totalCount)
        params.totalResults = inboxMessages.totalCount
        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
        [inboxMessages : inboxMessages, params : params]
    }

    def inbox =
    {
        def inboxMessages = Message.createCriteria().list(params)
        {
            and
            {
                eq("receiver", session.user)
            }
            order("timeStamp", "desc")
        }

        println(inboxMessages.totalCount)
        params.totalResults = inboxMessages.totalCount
        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
        [inboxMessages : inboxMessages, params : params]
    }

    def ajaxLoadInbox =
    {
        def inboxMessages = Message.createCriteria().list(params)
        {
            and
            {
                eq("receiver", session.user)
            }
            order("timeStamp", "desc")
        }

        println("AJAX Load Inbox: " + inboxMessages.totalCount)

        def senderNames = new String[ inboxMessages.totalCount ]
        def senderUserids = new String[ inboxMessages.totalCount ]

        for( int i = 0 ; i < inboxMessages.totalCount ; i++ )
        {
            senderNames[i] = inboxMessages[i].sender.name
            senderUserids[i] = inboxMessages[i].sender.userid
        }

        def toReturn = [ inboxMessages, senderNames, senderUserids ]

        render toReturn as JSON
    }

    def ajaxMarkAsRead =
    {
        def messageID = params.messageID
        def userUniqueId = params.user
        println( "SERVER IS HERE, MESSAGE IS " + messageID + ", USER IS " + userUniqueId )

        def m = Message.findById(messageID)

        if( !m.isRead )
        {
            if(   String.valueOf(m.receiver.id).equalsIgnoreCase( String.valueOf(userUniqueId) )   )
            {
                m.isRead = true
                println("Message of ID " + m.id + " and subject \"" + m.subject + "\" marked as read.")
            }
        }
        
    }

    def ajaxSend =
    {
        println("STUB FOR AJAX SEND")

        def errors = ''
        def recipient
        def subject
        def message

        // Check if recipient exists
        recipient = params.receiverUserID
        if( Resident.findByUserid(recipient) == null )
        {
            errors += '\nA recipient by the User ID of ' + recipient + ' does not exist!\nAre you sure you have typed the recipient\'s User ID correctly?'
        }
        else
        {
            recipient = Resident.findByUserid(recipient)
        }

        // Check if the subject is specified.
        if( !params.subject.trim().equals("") )
        {
            subject = params.subject
        }
        else
        {
            errors += '\nYou did not specify the subject of the message.'
        }

        // Check if the message is specified.
        if( !params.message.trim().equals("") )
        {
            message = params.message
        }
        else
        {
            errors += '\nYou cannot send an empty message.'
        }

        if( errors.length() > 0 )
        {
            errors = 'Some Errors Occured!\n' + errors
            render errors
        }
        else
        {
            def newMessage = new Message()
            newMessage.sender = session.user
            newMessage.receiver = recipient
            newMessage.subject = removeHTML(subject)
            newMessage.message = removeHTML(message)
            newMessage.timeStamp = new Date()
            newMessage.isRead = false

            if( newMessage.save() )
            {
                render 'T'
            }
            else
            {
                println( newMessage.errors )
                render 'F'
            }
        }

    }

    def ajaxCheckUser =
    {
        if( params.userid != null )
        {
            if( Resident.findByUserid( params.userid ) != null )
            {
                render 'T'
            }
            else
            {
                render 'F'
            }
        }
    }

    def ajaxLoadSent =
    {
        def sentMessages = Message.createCriteria().list(params)
        {
            and
            {
                eq("sender", session.user)
            }
            order("timeStamp", "desc")
        }

        println("AJAX Load Sent : " + sentMessages.totalCount)

        def receiverNames = new String[ sentMessages.totalCount ]
        def receiverUserids = new String[ sentMessages.totalCount ]

        for( int i = 0 ; i < sentMessages.totalCount ; i++ )
        {
            receiverNames[i] = sentMessages[i].receiver.name
            receiverUserids[i] = sentMessages[i].receiver.userid
        }

        def toReturn = [ sentMessages, receiverNames, receiverUserids ]

        render toReturn as JSON
    }

    def String removeHTML(String htmlString)
    {
        // Remove HTML tag from java String
        String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");

        // Remove Carriage return from java String
        noHTMLString = noHTMLString.replaceAll("\r", "<br/>");

        // Remove New line from java string and replace html break
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
        noHTMLString = noHTMLString.replaceAll("\"", "&quot;");
        return noHTMLString;
    }

    // load the list of sent messages
    def sent =
    {

        params.max = 5
        def sentMessages = Message.createCriteria().list(params)
        {
            and
            {
                eq("sender", session.user)
            }
            order("timeStamp", "desc")
        }

        println(sentMessages.totalCount)
        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
        params.totalResults = sentMessages.totalCount
        [sentMessages : sentMessages, params : params]
    }

    // view a message
    def view =
    {
        session.user = Resident.get(7)

        def messageToView = Message.findById(params.id)

        if(messageToView == null)
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            flash.messageOperationStatus = '<br/><p>The message you are trying to view does not exist!</p>'
            redirect(action: 'index')
        }
        else if( session.user != messageToView.sender && session.user != messageToView.receiver )
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            flash.messageOperationStatus = '<br/><p>You are not authorised to view this message!</p>'
            redirect(action: 'index')
        }
        else if( session.user != messageToView.sender )
        {
            messageToView.isRead = true
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            [message : messageToView, params : params]
        }
        else
        {
            params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
            [message : messageToView, params : params]
        }
    }

    // closure to call before create.gsp is loaded.
    def create =
    {
        params.messageModuleUnreadMessages = messageCheckingService.getUnreadMessages(session.user)
        [params : params]
    }

    // action that is triggered once user chooses to send a message
    def send =
    {
        def errors = ''
        def recipient
        def subject
        def message

        // Check if recipient exists
        recipient = params.receiverUserID
        if( Resident.findByUserid(recipient) == null )
        {
            errors += '<li>A recipient by the name of ' + recipient + ' does not exist!<br/>Are you sure you have typed the recipient\'s User ID correctly?'
        }
        else
        {
            recipient = Resident.findByUserid(recipient)
        }

        // Check if the subject is specified.
        if( !params.subject.trim().equals("") )
        {
            subject = params.subject
        }
        else
        {
            errors += '<li>You did not specify the subject of the message.</li>'
        }

        // Check if the message is specified.
        if( !params.message.trim().equals("") )
        {
            message = params.message
        }
        else
        {
            errors += '<li>You cannot send an empty message.</li>'
        }

        if( errors.length() > 0 )
        {
            errors = '<p>Some Errors Occured!</p><ul>' + errors
            errors += '</ul>'
            params.errors = errors
            // If there are missing fields, throw back the error message.
            render(view: 'create', params : params )
        }
        else
        {
            def newMessage = new Message()
            newMessage.sender = session.user
            newMessage.receiver = recipient
            newMessage.subject = removeHTML(subject)
            newMessage.message = removeHTML(message)
            newMessage.timeStamp = new Date()
            newMessage.isRead = false

            if( newMessage.save() )
            {
                flash.messageOperationStatus = '<br/><p>Your message has been sent!</p>'
                redirect( action: 'index' )
            }
            else
            {
                println( newMessage.errors )
                params.errors = '<p>An error occured when sending your message. Would you like to try again?</p>'
                render(view: 'create', params : params )
            }
        }
    }

}
