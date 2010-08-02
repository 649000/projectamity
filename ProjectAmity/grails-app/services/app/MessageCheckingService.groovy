package app

class MessageCheckingService
{

    static transactional = true

    // Get the number of unread messages
    def int getUnreadMessages(Resident currentUser)
    {
        def unread = Message.createCriteria().list()
        {
            and
            {
                eq("isRead", false)
                eq("receiver", currentUser)
            }
        }
        return unread.size()
    }

}
