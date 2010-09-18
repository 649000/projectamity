package app

import java.util.*
import java.text.*
import grails.converters.JSON

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

    def loadInbox =
    {
        def user = params.user
        def timeStamp = params.timeStamp
        def newMessages

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
                    // ge("timeStamp", timeStamp )
                    eq("isRead", false)
                    order("timeStamp", "desc")
                }
            }
            println( newMessages.size() )
            println( newMessages )
        }

        if( newMessages.size() > 0 )
        {
            render newMessages as JSON
        }
        else
        {
            render 'N'
        }
    }

    def viewMsg =
    {
        def messageID = params.messageID
        def message

        message = Message.findById(messageID)
        println( message )

        if( message != null )
        {
            def list = [ message, message.sender.name, message.receiver.name, message.sender.userid, message.receiver.userid ]
            render list as JSON
        }
        else
        {
            render 'N'
        }
    }

    def sendFromAndroid =
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
            if( params.newMessage != null )
            {
                if( params.newMessage.equalsIgnoreCase("false") )
                {
                    newMessage.subject = "RE: " + removeHTML(subject)
                }
                else
                {
                    newMessage.subject =  removeHTML(subject)
                }
            }
            else
            {
                newMessage.subject =  removeHTML(subject)
            }
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

    def markAsRead =
    {
        def messageID = params.messageID
        println( "SERVER IS HERE" )

        def m = Message.findById(messageID)

        m.isRead = true

        render 'T'
    }

}
