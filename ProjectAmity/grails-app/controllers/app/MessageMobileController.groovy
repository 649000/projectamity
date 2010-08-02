package app

import java.util.*
import java.text.*

class MessageMobileController
{

    def send =
    {
        def sender = params.sender
        def receiver = params.receiver
        def subject = params.subject
        def message = params.message
        println( sender )

        def errors = ''

        // Check if recipient exists
        if( receiver == null || Resident.findByUserid(receiver) == null )
        {
            errors += 'Your recipient\'s User ID|'
        }
        else
        {
            receiver = Resident.findByUserid(receiver)
        }

        // Check if the subject is specified.
        if( subject == null || subject.trim().equals("") )
        {
            errors += 'Subject|'
        }

        // Check if the message is specified.
        if( message == null || message.trim().equals("") )
        {
            errors += 'Message|'
        }

        if( errors.length() > 0 )
        {
            errors = '@|' + errors.substring(0, (errors.length() - 1) )
            // If there are missing fields, throw back the error message.
            render errors
        }
        else if(sender != null)
        {
            def newMessage = new Message()
            newMessage.sender = Resident.findByUserid(sender)
            newMessage.receiver = receiver
            newMessage.subject = subject
            newMessage.message = message
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

    def check =
    {
        def user = params.user
        def timeStamp = params.timeStamp
        println(timeStamp)
        def newMessages
        def message = ''

        // Check if recipient exists
        if( user != null )
        {
            user = Resident.findByUserid(user)
            newMessages = Message.createCriteria().list
            {
                and
                {
                    eq("receiver", user)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                    timeStamp = sdf.parse(timeStamp);
                    SimpleDateFormat checker = new SimpleDateFormat("dd MMMM yyyy");
                    println( checker.format(timeStamp) )
                    ge("timeStamp", timeStamp )
                    eq("isRead", false)
                    order("timeStamp", "asc")
                }
            }
            println( newMessages.size() )
            println( newMessages )
        }

        if( newMessages.size() > 0 )
        {
            def newMsg = newMessages[0]

            message += 'Y|'
            message += newMsg.sender.name + '|'
            message += newMsg.sender.userid + '|'
            message += newMsg.receiver.name + '|'
            message += newMsg.receiver.userid + '|'
            message += newMsg.subject + '|'
            message += newMsg.message + '|'
            message += newMsg.timeStamp

            newMessages[0].isRead = true

            render message
        }
        else
        {
            render 'N'
        }
    }

}
