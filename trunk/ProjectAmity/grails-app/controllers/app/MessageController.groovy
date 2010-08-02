package app

import java.text.SimpleDateFormat
import java.util.Calendar

class MessageController
{

    def messageCheckingService
    def currentUser

    // Load the user's inbox
    def index =
    {
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
            newMessage.subject = subject
            newMessage.message = message
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
