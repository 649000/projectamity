<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Message Inbox</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript" src="${resource(dir: 'js', file: 'messageScripts.js')}" ></script>
    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>

    <script type="text/javascript">

      var topMostInboxMsgIndex = 0
      var topMostSentMsgIndex = 0
      var inboxMessages
      var inboxSenderNames
      var inboxSenderUserids
      var sentMessages
      var sentReceiverNames
      var sentReceiverUserids

      function hideIrrelevantViews()
      {
        $('viewMessageContainer').hide()
        $('composeMessageContainer').hide()
        $('createMessageToSpinner').hide()
        $('createMessageToHelpText').hide()
        $('sendingMessageContainer').hide()
        $('sentMessagesContainer').hide()
      }

      function olderInbox()
      {
        topMostInboxMsgIndex += 5
        displayInboxMessages()
      }

      function newerInbox()
      {
        topMostInboxMsgIndex -= 5
        displayInboxMessages()
      }

      function initialiseMessagePage()
      {
          // new Ajax.PeriodicalUpdater(container, url[, options])
          new Ajax.PeriodicalUpdater( '' , '<g:createLink action="ajaxLoadInbox"/>',
          {
            method: 'get',
            frequency: 5,
            decay: 1,
            onSuccess:
            function(response)
            {
              parseInboxMessages(response)
            }
          } );

      }

      function parseInboxMessages(response)
      {
        var incoming = eval( '(' + response.responseText + ')' )
        inboxMessages = incoming[0]
        inboxSenderNames = incoming[1]
        inboxSenderUserids = incoming[2]

        displayInboxMessages()
      }

      function  displayInboxMessages()
      {
        $('inboxFrom1').innerHTML = '<p>' + inboxSenderNames[topMostInboxMsgIndex] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + inboxSenderUserids[topMostInboxMsgIndex] + '</a>)</p>'
        if( inboxMessages[topMostInboxMsgIndex].isRead == true )
        {
          $('inboxSubject1').innerHTML = '<p><a href=\"#\"  onClick=\"viewInboxMessage(0); return false\">' + inboxMessages[topMostInboxMsgIndex].subject + '</a></p>'
        }
        else
        {
          $('inboxSubject1').innerHTML = '<p><b><a href=\"#\"  onClick=\"viewInboxMessage(0); return false\">' + inboxMessages[topMostInboxMsgIndex].subject + '</a></b></p>'
        }
        $('inboxTimeStamp1').innerHTML = '<p>' + inboxMessages[topMostInboxMsgIndex].timeStamp + '</p>'

        if( inboxSenderNames[topMostInboxMsgIndex + 1] != null )
        {
          $('inboxFrom2').innerHTML = '<p>' + inboxSenderNames[topMostInboxMsgIndex + 1] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + inboxSenderUserids[topMostInboxMsgIndex + 1] + '</a>)</p>'
          if( inboxMessages[topMostInboxMsgIndex + 1].isRead == true )
          {
            $('inboxSubject2').innerHTML = '<p><a href=\"#\"  onClick=\"viewInboxMessage(1); return false\">' + inboxMessages[topMostInboxMsgIndex + 1].subject + '</a></p>'
          }
          else
          {
            $('inboxSubject2').innerHTML = '<p><b><a href=\"#\"  onClick=\"viewInboxMessage(1); return false\">' + inboxMessages[topMostInboxMsgIndex + 1].subject + '</a></b></p>'
          }
          $('inboxTimeStamp2').innerHTML = '<p>' + inboxMessages[topMostInboxMsgIndex + 1].timeStamp + '</p>'
          $('inboxSlot2').show()
        }
        else
        {
          $('inboxSlot2').hide()
        }

        if( inboxSenderNames[topMostInboxMsgIndex + 2] != null )
        {
          $('inboxFrom3').innerHTML = '<p>' + inboxSenderNames[topMostInboxMsgIndex + 2] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + inboxSenderUserids[topMostInboxMsgIndex + 2] + '</a>)</p>'
          if( inboxMessages[topMostInboxMsgIndex + 2].isRead == true )
          {
            $('inboxSubject3').innerHTML = '<p><a href=\"#\"  onClick=\"viewInboxMessage(2); return false\">' + inboxMessages[topMostInboxMsgIndex + 2].subject + '</a></p>'
          }
          else
          {
            $('inboxSubject3').innerHTML = '<p><b><a href=\"#\"  onClick=\"viewInboxMessage(2); return false\">' + inboxMessages[topMostInboxMsgIndex + 2].subject + '</a></b></p>'
          }
          $('inboxTimeStamp3').innerHTML = '<p>' + inboxMessages[topMostInboxMsgIndex + 2].timeStamp + '</p>'
          $('inboxSlot3').show()
        }
        else
        {
          $('inboxSlot3').hide()
        }

        if( inboxSenderNames[topMostInboxMsgIndex + 3] != null )
        {
          $('inboxFrom4').innerHTML = '<p>' + inboxSenderNames[topMostInboxMsgIndex + 3] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + inboxSenderUserids[topMostInboxMsgIndex + 3] + '</a>)</p>'
          if( inboxMessages[topMostInboxMsgIndex + 3].isRead == true )
          {
            $('inboxSubject4').innerHTML = '<p><a href=\"#\"  onClick=\"viewInboxMessage(3); return false\">' + inboxMessages[topMostInboxMsgIndex + 3].subject + '</a></p>'
          }
          else
          {
            $('inboxSubject4').innerHTML = '<p><b><a href=\"#\"  onClick=\"viewInboxMessage(3); return false\">' + inboxMessages[topMostInboxMsgIndex + 3].subject + '</a></b></p>'
          }
          $('inboxTimeStamp4').innerHTML = '<p>' + inboxMessages[topMostInboxMsgIndex + 3].timeStamp + '</p>'
          $('inboxSlot4').show()
        }
        else
        {
          $('inboxSlot4').hide()
        }

        if( inboxSenderNames[topMostInboxMsgIndex + 4] != null )
        {
          $('inboxFrom5').innerHTML = '<p>' + inboxSenderNames[topMostInboxMsgIndex + 4] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + inboxSenderUserids[topMostInboxMsgIndex + 4] + '</a>)</p>'
          if( inboxMessages[topMostInboxMsgIndex + 4].isRead == true )
          {
            $('inboxSubject5').innerHTML = '<p><a href=\"#\"  onClick=\"viewInboxMessage(4); return false\">' + inboxMessages[topMostInboxMsgIndex + 4].subject + '</a></p>'
          }
          else
          {
            $('inboxSubject5').innerHTML = '<p><b><a href=\"#\"  onClick=\"viewInboxMessage(4); return false\">' + inboxMessages[topMostInboxMsgIndex + 4].subject + '</a></b></p>'
          }
          $('inboxTimeStamp5').innerHTML = '<p>' + inboxMessages[topMostInboxMsgIndex + 4].timeStamp + '</p>'
          $('inboxSlot5').show()
        }
        else
        {
          $('inboxSlot5').hide()
        }

        if( inboxMessages.length < 5 )
        {
          $('inboxPagination').innerHTML = '<p><b>1</b> - <b>' + inboxMessages.length + '</b> of <b>' + inboxMessages.length + '</b>  messages.</p>'
        }
        else
        {
          var currentTopMost = 1
          if( topMostInboxMsgIndex > 0 )
          {
            currentTopMost = (topMostInboxMsgIndex + 1)
          }

          if( currentTopMost == 1 )
          {
            $('inboxPagination').innerHTML = '<p><b>' + currentTopMost + '</b> - <b>' + (currentTopMost + 4) + '</b> of <b>' + inboxMessages.length + '</b>  messages. <a href=\"#\"  onClick=\"olderInbox(); return false\">Older ></a></p>'
          }
          else if( (inboxMessages.length - currentTopMost) <= 4 )
          {
            $('inboxPagination').innerHTML = '<p><a href=\"#\"  onClick=\"newerInbox(); return false\">< Newer</a> <b>' + currentTopMost + '</b> - <b>' + (currentTopMost + 4) + '</b> of <b>' + inboxMessages.length + '</b>  messages.</p>'
          }
          else
          {
            $('inboxPagination').innerHTML = '<p><a href=\"#\"  onClick=\"newerInbox(); return false\">< Newer</a> <b>' + currentTopMost + '</b> - <b>' + (currentTopMost + 4) + '</b> of <b>' + inboxMessages.length + '</b>  messages. <a href=\"#\"  onClick=\"olderInbox(); return false\">Older ></a></p>'
          }
        }
      }

      function viewInboxMessage(index)
      {
        var indexToShow = topMostInboxMsgIndex + index

        var address = '<g:createLink action="ajaxMarkAsRead"/>'
        address += '?messageID=' + inboxMessages[indexToShow].id
        address += '&user=' + ${session.user.id}

        new Ajax.Request(address,
                            {
                              method: 'post',
                            }
                        );

        $('viewMessageSubject').innerHTML = inboxMessages[indexToShow].subject
        $('viewMessageFromName').innerHTML = '<p>' + inboxSenderNames[indexToShow] + '</p>'
        $('viewMessageFromUserid').innerHTML = '<p>(' + inboxSenderUserids[indexToShow] + ')</p>'
        $('viewMessageToName').innerHTML = '<p>${session.user.name} (You)</p>'
        $('viewMessageToUserid').innerHTML = '<p>(${session.user.userid})</p>'
        $('viewMessageSubjectSmall').innerHTML = inboxMessages[indexToShow].subject
        $('viewMessageTimeStamp').innerHTML = '<b>On ' + inboxMessages[indexToShow].timeStamp + ', ' + inboxSenderNames[indexToShow] + ' wrote:</b>'
        $('viewMessageMessage').innerHTML = inboxMessages[indexToShow].message
        $('viewMessageReplyCell').innerHTML = '<p><a href=\"#\"  onClick=\"composeMessage(true,\'' + inboxSenderUserids[indexToShow] + '\',\'RE: ' + inboxMessages[indexToShow].subject + '\'); return false\"><img src=\"${resource(dir:'images/amity',file:'messagingreplymail.png')}\" width=\"12px\"/> Reply</a></p>'

        document.title = 'View Message from ' + inboxSenderNames[indexToShow]

        $('inboxContainer').hide()
        $('composeMessageContainer').hide()
        $('viewMessageContainer').show()
      }

      function composeMessage(isReply, receiver, subject)
      {
        if( !isReply )
        {
          document.title = 'Compose Message'
          $('composeMessageHeader').innerHTML = 'Compose a Message'

          $('receiverUserID').hide()
          $('receiverUserIDOverlay').show()

          $('receiverUserID').value = ''
          $('composeMailSubject').value = ''
          $('composeMailMessage').value = ''
        }
        else if( isReply == true )
        {
          document.title = 'Compose Reply'
          $('composeMessageHeader').innerHTML = 'Compose a Reply'

          $('receiverUserID').show()
          $('receiverUserIDOverlay').hide()

          $('receiverUserID').value = receiver
          $('composeMailSubject').value = subject
        }

        $('inboxContainer').hide()
        $('viewMessageContainer').hide()
        $('sentMessagesContainer').hide()
        $('createMessageToSpinner').hide()
        $('createMessageToHelpText').hide()
        $('composeMessageBlocked').hide()
        $('composeMailSendBtn').enable()
        $('composeMessageContainer').show()

        if( isReply )
        {
          checkIfRecipientExists()
        }
      }

      function checkIfRecipientExists()
      {

        var userid =  $('receiverUserID').value

        var address = '<g:createLink action="ajaxCheckUser"/>'
        address += '?userid=' + userid

        new Ajax.Request(address,
                            {
                              onLoading: function()
                                        {
                                          $('composeMailSendBtn').disable()
                                          $('createMessageToSpinner').show()
                                          $('createMessageToHelpText').hide()
                                        },
                              method: 'post',
                              onSuccess: function(response)
                                        {
                                          $('createMessageToSpinner').hide()
                                          var content = response.responseText
                                          if(content == 'T')
                                          {
                                            $('createMessageToHelpText').innerHTML = '<p><font color="green">This user exists.</font></p>'
                                            $('composeMessageBlocked').hide()
                                            $('composeMailSendBtn').enable()
                                          }
                                          else
                                          {
                                            $('createMessageToHelpText').innerHTML = '<p><font color="red">This user does not exist!</font></p>'
                                            $('composeMessageBlocked').show()
                                          }
                                          $('createMessageToHelpText').show()
                                        },
                              onFailure: function(response)
                                        {
                                          $('createMessageToSpinner').hide()
                                          $('createMessageToHelpText').innerHTML = '<p><font color="red">This user does not exist!</font></p>'
                                          $('composeMessageBlocked').show()
                                          $('createMessageToHelpText').show()
                                        }
                            }
                        );
      }

      function composeMailValidateFields()
      {
        var receiver = $('receiverUserID').value
        var subject = $('composeMailSubject').value
        var message = $('composeMailMessage').value
        var proceed = true

        var prompt = 'You have left out the following in your message:\n'

        if( trim(receiver).length == 0 )
        {
          prompt += '\nA valid recipient User ID'
          proceed = false
        }
        if( trim(subject).length == 0 )
        {
          prompt += '\nSubject'
          proceed = false
        }
        if( trim(message).length == 0 )
        {
          prompt += '\nMessage'
          proceed = false
        }

        if( !proceed )
        {
          alert(prompt)
          return false
        }
        else
        {
          return true
        }
      }

      function trim(str, chars)
      {
        return ltrim(rtrim(str, chars), chars);
      }

      function ltrim(str, chars)
      {
              chars = chars || "\\s";
              return str.replace(new RegExp("^[" + chars + "]+", "g"), "");
      }

      function rtrim(str, chars)
      {
              chars = chars || "\\s";
              return str.replace(new RegExp("[" + chars + "]+$", "g"), "");
      }

      function backToInbox()
      {
        topMostInboxMsgIndex = 0
        displayInboxMessages()

        document.title = 'Message Inbox'

        $('inboxContainer').show()
        $('viewMessageContainer').hide()
        $('sentMessagesContainer').hide()
        $('composeMessageContainer').hide()
      }

      function composeMessageFormSending()
      {
        $('sendingMessageContainer').show();
        $('composeMessageContainer').hide();
      }

      function composeMessageFormSent(response)
      {
        if(response!= null || response.responseText != null)
        {
          var content = response.responseText

          if( content == 'T' )
          {
            $('sendingMessageContainer').hide()
            backToInbox()
            alert('Your message has been successfully sent!')
          }
          else if( content == 'F' )
          {
            $('sendingMessageContainer').hide()
            $('composeMessageContainer').show()
            alert('An error occured while we tried to send your message. Please try again soon!')
          }
          else
          {
            $('sendingMessageContainer').hide()
            $('composeMessageContainer').show()
            alert(content)
          }
        }
        else
        {
          $('sendingMessageContainer').hide()
          $('composeMessageContainer').show()
          alert('An error occured while we tried to send your message. Please try again soon!')
        }

      }

      function getSentMessages()
      {
        var address = '<g:createLink action="ajaxLoadSent"/>'

        new Ajax.Request(address,
                            {
                              method: 'get',
                              onSuccess: function(response)
                                        {
                                          var incoming = eval( '(' + response.responseText + ')' )
                                          sentMessages = incoming[0]
                                          sentReceiverNames = incoming[1]
                                          sentReceiverUserids = incoming[2]

                                          displaySentMessages()
                                        }
                            }
                        );
      }

      function  displaySentMessages()
      {
        $('sentTo1').innerHTML = '<p>' + sentReceiverNames[topMostSentMsgIndex] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + sentReceiverUserids[topMostSentMsgIndex] + '</a>)</p>'
        $('sentSubject1').innerHTML = '<p><a href=\"#\"  onClick=\"viewSentMessage(0); return false\">' + sentMessages[topMostSentMsgIndex].subject + '</a></p>'
        $('sentTimeStamp1').innerHTML = '<p>' + sentMessages[topMostSentMsgIndex].timeStamp + '</p>'

        if( sentReceiverNames[topMostSentMsgIndex + 1] != null )
        {
          $('sentTo2').innerHTML = '<p>' + sentReceiverNames[topMostSentMsgIndex + 1] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + sentReceiverUserids[topMostSentMsgIndex + 1] + '</a>)</p>'
          $('sentSubject2').innerHTML = '<p><a href=\"#\"  onClick=\"viewSentMessage(1); return false\">' + sentMessages[topMostSentMsgIndex + 1].subject + '</a></p>'
          $('sentTimeStamp2').innerHTML = '<p>' + sentMessages[topMostSentMsgIndex + 1].timeStamp + '</p>'
          $('sentSlot2').show()
        }
        else
        {
          $('sentSlot2').hide()
        }

        if( sentReceiverNames[topMostSentMsgIndex + 2] != null )
        {
          $('sentTo3').innerHTML = '<p>' + sentReceiverNames[topMostSentMsgIndex + 2] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + sentReceiverUserids[topMostSentMsgIndex + 2] + '</a>)</p>'
          $('sentSubject3').innerHTML = '<p><a href=\"#\"  onClick=\"viewSentMessage(2); return false\">' + sentMessages[topMostSentMsgIndex + 2].subject + '</a></p>'
          $('sentTimeStamp3').innerHTML = '<p>' + sentMessages[topMostSentMsgIndex + 2].timeStamp + '</p>'
          $('sentSlot3').show()
        }
        else
        {
          $('sentSlot3').hide()
        }

        if( sentReceiverNames[topMostSentMsgIndex + 3] != null )
        {
          $('sentTo4').innerHTML = '<p>' + sentReceiverNames[topMostSentMsgIndex + 3] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + sentReceiverUserids[topMostSentMsgIndex + 3] + '</a>)</p>'
          $('sentSubject4').innerHTML = '<p><a href=\"#\"  onClick=\"viewSentMessage(3); return false\">' + sentMessages[topMostSentMsgIndex + 3].subject + '</a></p>'
          $('sentTimeStamp4').innerHTML = '<p>' + sentMessages[topMostSentMsgIndex + 3].timeStamp + '</p>'
          $('sentSlot4').show()
        }
        else
        {
          $('sentSlot4').hide()
        }

        if( sentReceiverNames[topMostSentMsgIndex + 4] != null )
        {
          $('sentTo5').innerHTML = '<p>' + sentReceiverNames[topMostSentMsgIndex + 4] + ' (<a href=\"#\"  onClick=\"alert(\'Send this fella a message.\'); return false\">' + sentReceiverUserids[topMostSentMsgIndex + 4] + '</a>)</p>'
          $('sentSubject5').innerHTML = '<p><a href=\"#\"  onClick=\"viewSentMessage(4); return false\">' + sentMessages[topMostSentMsgIndex + 4].subject + '</a></p>'
          $('sentTimeStamp5').innerHTML = '<p>' + sentMessages[topMostSentMsgIndex + 4].timeStamp + '</p>'
          $('sentSlot5').show()
        }
        else
        {
          $('sentSlot5').hide()
        }

        if( sentMessages.length < 5 )
        {
          $('inboxPagination').innerHTML = '<p><b>1</b> - <b>' + sentMessages.length + '</b> of <b>' + sentMessages.length + '</b>  messages.</p>'
        }
        else
        {
          var currentTopMost = 1
          if( topMostSentMsgIndex > 0 )
          {
            var currentTopMost = (topMostSentMsgIndex + 1)
          }

          if( currentTopMost == 1 )
          {
            $('sentMessagesPagination').innerHTML = '<p><b>' + currentTopMost + '</b> - <b>' + (currentTopMost + 4) + '</b> of <b>' + sentMessages.length + '</b>  messages. <a href=\"#\"  onClick=\"olderSent(); return false\">Older ></a></p>'
          }
          else if( (sentMessages.length - currentTopMost) <= 4 )
          {
            $('sentMessagesPagination').innerHTML = '<p><a href=\"#\"  onClick=\"newerSent(); return false\">< Newer</a> <b>' + currentTopMost + '</b> - <b>' + (currentTopMost + 4) + '</b> of <b>' + sentMessages.length + '</b>  messages.</p>'
          }
          else
          {
            $('sentMessagesPagination').innerHTML = '<p><a href=\"#\"  onClick=\"newerSent(); return false\">< Newer</a> <b>' + currentTopMost + '</b> - <b>' + (currentTopMost + 4) + '</b> of <b>' + sentMessages.length + '</b>  messages. <a href=\"#\"  onClick=\"olderSent(); return false\">Older ></a></p>'
          }
        }
      }

      function olderSent()
      {
        topMostSentMsgIndex += 5
        displaySentMessages()
      }

      function newerSent()
      {
        topMostSentMsgIndex -= 5
        displaySentMessages()
      }

      function viewSentItems()
      {
        getSentMessages()

        topMostSentMsgIndex = 0

        document.title = 'Sent Messages'

        $('inboxContainer').hide()
        $('viewMessageContainer').hide()
        $('composeMessageContainer').hide()
        $('sentMessagesContainer').show()
      }

      function viewSentMessage(index)
      {
        var indexToShow = topMostSentMsgIndex + index

        $('viewMessageSubject').innerHTML = sentMessages[indexToShow].subject
        $('viewMessageFromName').innerHTML = '<p>${session.user.name} (You)</p>'
        $('viewMessageFromUserid').innerHTML = '<p>(${session.user.userid})</p>'
        $('viewMessageToName').innerHTML = '<p>' + sentReceiverNames[indexToShow] + '</p>'
        $('viewMessageToUserid').innerHTML = '<p>(' + sentReceiverUserids[indexToShow] + ')</p>'
        $('viewMessageSubjectSmall').innerHTML = sentMessages[indexToShow].subject
        $('viewMessageTimeStamp').innerHTML = '<b>On ' + sentMessages[indexToShow].timeStamp + ', you (${session.user.name}) wrote:</b>'
        $('viewMessageMessage').innerHTML = sentMessages[indexToShow].message
        $('viewMessageReplyCell').innerHTML = '<p><a href=\"#\"  onClick=\"composeMessage(true,\'' + sentReceiverUserids[indexToShow] + '\',\'RE: ' + inboxMessages[indexToShow].subject + '\'); return false\"><img src=\"${resource(dir:'images/amity',file:'messagingreplymail.png')}\" width=\"12px\"/> Reply</a></p>'

        document.title = 'View Message to ' + sentReceiverNames[indexToShow]

        $('sentMessagesContainer').hide()
        $('composeMessageContainer').hide()
        $('viewMessageContainer').show()
      }

    </script>

  </head>

  <body onload="hideIrrelevantViews();initialiseMessagePage()" class="thrColFixHdr">

    <div class="wrapper">

    <div id="container">
      <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" id="logo"/>
      <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
      <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
      <img src="${resource(dir:'images/amity',file:'home.png')}" id="home"/>
      <a href="${createLink(controller: 'report', action:'index')}" >
      <img src="${resource(dir:'images/amity',file:'report.png')}" border="0" id="report"/></a>
      <a href="${createLink(controller: 'carpoolListing', action:'index')}" >
      <img src="${resource(dir:'images/amity',file:'carpool.png')}" border="0" id="carpool"/></a>
      <img src="${resource(dir:'images/amity',file:'bmessage.png')}" border="0" id="pageTitle"/>

      <div id="header">
        <h1>test</h1>
        <!-- end #header -->
      </div>

      <div id="banner">
        &nbsp;
      </div>

      <div id="navi">Welcome, <a href="#">${session.user.name}</a>.&nbsp;
        <g:if test="${params.messageModuleUnreadMessages > 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">${params.messageModuleUnreadMessages} unread messages</a>.
        </g:if>
        <g:elseif test="${params.messageModuleUnreadMessages == 1}">
          You have <a href="${createLink(controller: 'message', action:'index')}">1 unread message</a>.
        </g:elseif>
        <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="asdf"><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
      </div>

      <div id="mainContent">

        <br/>

        <div id="inboxContainer">
          <h1>Message Inbox</h1>
          <br/>
          <div id="inboxMessages">
            <table width="80%" cellspacing="0">
              <tr><td style="background: url(${resource(dir:'images/amity',file:'messagingtopgrad.jpg')}) repeat-x; vertical-align:bottom" colspan="3"><a href="#"  onClick="composeMessage(); return false"><img src="${resource(dir:'images/amity',file:'messagingcomposemail.png')}" style="vertical-align:bottom; width: 15px; height: 15px"/><b> Compose New Message</b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"  onClick="viewSentItems(); return false"><img src="${resource(dir:'images/amity',file:'messagingsentmail.png')}" style="vertical-align:bottom; width: 15px; height: 15px"/><b> View Sent Messages</b></a></td></tr>
              <tr bgcolor="#E6F0D2"><td width="30%"><b>From</b></td><td width="50%"><b>Subject</b></td><td><b>Time Sent</b></td></tr>
              <tr id="inboxSlot1"><td id="inboxFrom1">Loading...</td><td id="inboxSubject1">Loading...</td><td id="inboxTimeStamp1">Loading...</td></tr>
              <tr id="inboxSlot2" bgcolor="#E6F0D2"><td id="inboxFrom2">Loading...</td><td id="inboxSubject2">Loading...</td><td id="inboxTimeStamp2">Loading...</td></tr>
              <tr id="inboxSlot3"><td id="inboxFrom3">Loading...</td><td id="inboxSubject3">Loading...</td><td id="inboxTimeStamp3">Loading...</td></tr>
              <tr id="inboxSlot4" bgcolor="#E6F0D2"><td id="inboxFrom4">Loading...</td><td id="inboxSubject4">Loading...</td><td id="inboxTimeStamp4">Loading...</td></tr>
              <tr id="inboxSlot5"><td id="inboxFrom5">Loading...</td><td id="inboxSubject5">Loading...</td><td id="inboxTimeStamp5">Loading...</td></tr>
            </table>
          </div>
          <br/>
          <div id="inboxPagination"></div>
        </div>

        <div id="viewMessageContainer">
          
          <h1 id="viewMessageSubject">Loading...</h1>
          
          <br/>

          <table width="80%">
            <tr>
              <td width="10%"><b>From:</b></td>
              <td id="viewMessageFromName" width="30%">Loading...</td>
              <td id="viewMessageFromUserid">Loading...</td>
            </tr>
            <tr>
              <td width="10%"><b>To:</b></td>
              <td id="viewMessageToName" width="30%">Loading...</td>
              <td id="viewMessageToUserid">Loading...</td>
            </tr>
            <tr>
              <td width="10%"><b>Subject:</b></td>
              <td id="viewMessageSubjectSmall" width="30%">Loading...</td>
              <td></td>
            </tr>
            <tr>
              <td width="10%"></td>
              <td width="30%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td id="viewMessageTimeStamp" colspan="3"><b>Loading...</b></td>
            </tr>
            <tr>
              <td width="10%"></td>
              <td width="30%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td id="viewMessageMessage" colspan="3">Loading...</td>
            </tr>
          </table>

          <br/>
          <table width="80%">
            <tr>
              <td width="30%">
                <p><a href="#"  onClick="backToInbox(); return false"><img src="${resource(dir:'images/amity',file:'messagingbacktoinbox.png')}" width="12px"/> Inbox</a></p>
              </td>
              <td width="30%">
                <p><a href="#"  onClick="viewSentItems(); return false"><img src="${resource(dir:'images/amity',file:'messagingsentmail.png')}" width="12px"/> Sent Messages</a></p>
              </td>
              <td id="viewMessageReplyCell">
                <p><a href="#"  onClick="composeMessage(true); return false"><img src="${resource(dir:'images/amity',file:'messagingreplymail.png')}" width="12px"/> Reply</a></p>
              </td>
            </tr>
          </table>
          
        </div>

        <div id="sendingMessageContainer" style="text-align: center">
          <h1><img src="${resource(dir:'images',file:'spinner.gif')}" width="20px"/> Sending your message</h1>
          <h3>Please wait...</h3>
        </div>

        <div id="composeMessageContainer">

          <p><a href="#"  onClick="backToInbox(); return false"><img src="${resource(dir:'images/amity',file:'messagingbacktoinbox.png')}" width="12px"/> Inbox</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="#"  onClick="viewSentItems(); return false"><img src="${resource(dir:'images/amity',file:'messagingsentmail.png')}" width="12px"/> Sent Messages</a></p>
          <br/>
          <h1 id="composeMessageHeader">Compose a Message</h1>
          <br/>
          <g:formRemote name="composeMessageForm" url="[action: 'ajaxSend']"
                        onLoading="composeMessageFormSending()"
                        onSuccess="composeMessageFormSent(e)"
                        onFailure="composeMessageFormSent(e)">
            <table width="80%">
              <tr>
                <td width="10%"><b>To: </b></td>
                <td width="40%"><g:textField id="receiverUserIDOverlay" name="receiverUserIDOverlay" size="25" value="Enter the recipient's User ID." onfocus="toggleControl(this); toggleControl('receiverUserID')" onblur="" /><g:textField id="receiverUserID" name="receiverUserID" size="25" onfocus="" onblur="checkIfRecipientExists()" /></td>
                <td><span id="createMessageToSpinner"><img src="${resource(dir:'images',file:'spinner.gif')}" width="12px"/></span><span id="createMessageToHelpText"></span></td>
              </tr>
              <tr>
                <td width="10%"><b> </b></td>
                <td width="40%">&nbsp;</td>
                <td></td>
              </tr>
              <tr>
                <td width="10%"><b>Subject: </b></td>
                <td width="40%"><g:textField id="composeMailSubject" name="subject" size="40" maxlength="100" /></td>
                <td></td>
              </tr>
              <tr>
                <td width="10%"><b> </b></td>
                <td width="40%">&nbsp;</td>
                <td></td>
              </tr>
              <tr>
                <td colspan="3"><b>Your Message: </b></td>
              </tr>
              <tr>
                <td width="10%"><b> </b></td>
                <td width="40%">&nbsp;</td>
                <td></td>
              </tr>
              <tr>
                <td width="10%"><b> </b></td>
                <td width="40%"><g:textArea id="composeMailMessage" name="message" value="" rows="5" cols="30" maxlength="3000" /></td>
                <td></td>
              </tr>
              <tr>
                <td width="10%"><b> </b></td>
                <td width="40%">&nbsp;</td>
                <td></td>
              </tr>
              <tr>
                <td colspan="3">
                    <input id="composeMailSendBtn" type="submit" value="Send Mail" onclick="return composeMailValidateFields()" />
                </td>
              </tr>
              <tr>
                <td colspan="3"><p>&nbsp;</p></td>
              </tr>
              <tr id="composeMessageBlocked">
                <td colspan="3"><p><b><font color="red">You can send this message only if you specify a recipient User ID that exists.</font></b></p></td>
              </tr>
            </table>

          </g:formRemote>

        </div>

        <div id="sentMessagesContainer">
          <h1>Sent Messages</h1>
          <br/>
          <div id="sentMessages">
            <table width="80%" cellspacing="0">
              <tr><td style="background: url(${resource(dir:'images/amity',file:'messagingtopgrad.jpg')}) repeat-x; vertical-align:bottom" colspan="3"><a href="#"  onClick="composeMessage(); return false"><img src="${resource(dir:'images/amity',file:'messagingcomposemail.png')}" style="vertical-align:bottom; width: 15px; height: 15px"/><b> Compose New Message</b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><a href="#"  onClick="backToInbox(); return false"><img src="${resource(dir:'images/amity',file:'messagingbacktoinbox.png')}" width="12px"/> Back to Inbox</a></b></td></tr>
              <tr bgcolor="#E6F0D2"><td width="30%"><b>To</b></td><td width="50%"><b>Subject</b></td><td><b>Time Sent</b></td></tr>
              <tr id="sentSlot1"><td id="sentTo1">Loading...</td><td id="sentSubject1">Loading...</td><td id="sentTimeStamp1">Loading...</td></tr>
              <tr id="sentSlot2" bgcolor="#E6F0D2"><td id="sentTo2">Loading...</td><td id="sentSubject2">Loading...</td><td id="sentTimeStamp2">Loading...</td></tr>
              <tr id="sentSlot3"><td id="sentTo3">Loading...</td><td id="sentSubject3">Loading...</td><td id="sentTimeStamp3">Loading...</td></tr>
              <tr id="sentSlot4" bgcolor="#E6F0D2"><td id="sentTo4">Loading...</td><td id="sentSubject4">Loading...</td><td id="sentTimeStamp4">Loading...</td></tr>
              <tr id="sentSlot5"><td id="sentTo5">Loading...</td><td id="sentSubject5">Loading...</td><td id="sentTimeStamp5">Loading...</td></tr>
            </table>
          </div>
          <br/>
          <div id="sentMessagesPagination"></div>
        </div>

      </div>
      <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats -->
      <br class="clearfloat" />
      <!-- end #container -->
    </div>

    <div class="push"></div>

    <!--end wrapper-->
    </div>

    <div class="footer">
      <p>Copyright &copy; 2010 Team Smiley Face</p>
    </div>

  </body>

</html>