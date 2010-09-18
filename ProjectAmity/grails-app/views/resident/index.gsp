<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>User Profile</title>

      <g:javascript library="scriptaculous" />
      <g:javascript library="prototype" />
      <script type="text/javascript" src="${resource(dir: 'js', file: 'residentprofilescript.js')}" ></script>
      <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
      <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
          <g:javascript library="application" />
   <modalbox:modalIncludes />
  </head>
  <body class="thrColFixHdr" onLoad="${remoteFunction(action:'loadData',onSuccess:'Init(e)')}">

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
        <img src="${resource(dir:'images/amity',file:'.png')}" border="0" id="pageTitle"/>
        <div id="header">
          <h1>test</h1>
          <!-- end #header --></div>
        <div id="banner">&nbsp;</div>
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

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
        HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->


          <resource:portlet />
          <richui:portlet views="[1, 2, 3, 4,5,6]" action="changeView" >

            <table style="width: 1px; border: none;" >

              <tr>
                <td>
                  <richui:portletView id="1" slotStyle="width: 250px; height: 100px;" playerStyle="width: 250px; height: 100px;" >
                    <h1>Content 1</h1>

                  </richui:portletView>
                </td>
                <td>
                  <richui:portletView id="7" slotStyle="width: 250px; height: 100px;" playerStyle="width: 500px; height: 300px;">
                    <h2>User Info</h2>
                    Ice Cream. Nom Nom Nom. kthxbyebbq.
                    <table border="0">
     
                      <tr >
                        <td width="20%"><b>UserID:  <br/>Email:<br/>&nbsp; <g:if test="${session.user.emailConfirm == 'false'}"><br/>&nbsp;</g:if></b></td>
                        <td width="60%">${session.user.userid} <br/>${session.user.email} <g:if test="${session.user.emailConfirm == 'false'}">(Not Verified)<br/> <g:remoteLink action="resendEmailVerify">Resend Verification Email</g:remoteLink></g:if><g:if test="${session.user.emailConfirm != 'false'}">(Verified)</g:if><br/> <modalbox:createLink url="update" title="Update Account Details" width="350" linkname="Change Account Details" /></td>
                        <td><avatar:gravatar email="${session.user.email}" size="90"/></td>
                      </tr>
                      <tr>
                        
                        <td><b>NRIC:  <br/> Name: <br/>Address:  <br/>&nbsp;</b></td>
                        <td>${session.user.nric}<br/> ${session.user.name} <br/>${session.user.address} <br/>Singapore ${session.user.postalCode}</td>
                      </tr>
                    </table>

                  </richui:portletView>
                </td>
              </tr>
              <tr>
                <td>
                  <richui:portletView id="2" slotStyle="width: 250px; height: 100px;" playerStyle="width: 250px; height: 100px;">
                    <h1>Content 2</h1>
                  </richui:portletView>
                </td></tr>

              <tr>
                <td>
                  <richui:portletView id="3" slotStyle="width: 250px; height: 100px;" playerStyle="width: 250px; height: 100px;">
                    <h1>Content 3</h1>

                  </richui:portletView>
                </td>
              </tr>
              <tr>
                <td>
                  <richui:portletView id="4" slotStyle="width: 250px; height: 100px;" playerStyle="width: 250px; height: 100px;">
                    <h1>Content 4</h1>
                    You have made over 9000 carpool listings.
                  </richui:portletView>
                </td>
                <td>
                  <richui:portletView id="5" slotStyle="width: 250px; height: 100px;" playerStyle="width: 250px; height: 100px;">
                    <h1>Content 5</h1>
                    You have made over 9000 barter listings.
                  </richui:portletView>
                </td>
                <td>
                  <richui:portletView id="6" slotStyle="width: 500px; height: 100px;" playerStyle="width: 250px; height: 100px;">
                    <h1>Content 6</h1>
                    You have made over 9000 reports.
                  </richui:portletView>
                </td>
              </tr>
            </table>

          </richui:portlet>

        </div>
        <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats --><br class="clearfloat" />
        <!-- end #container --></div>

      <div class="push"></div>


      <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT
    HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
    </div>

    <div class="footer">
      <p>Copyright &copy; 2010 Team Smiley Face</p>
    </div>
  </body>
</html>