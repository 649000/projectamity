<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Location Based Reporting</title>
      <script src="http://api.germanium3d.com/?v=1.4&key=0c1db0e05cd88587a664a659962b25c0"></script>
      <g:javascript library="scriptaculous" />
      <g:javascript library="prototype" />
      <script type="text/javascript" src="${resource(dir: 'js', file: 'reportscript.js')}" ></script>
      <script src="http://maps.google.com/maps?file=api&amp;v=3&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
              srLJJlcXUmLMTM2KkMsePdU1A"
      type="text/javascript"></script>
      <script type="text/javascript" src="${resource(dir: 'js', file: 'defineprofilescript.js')}" ></script>
      <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
      <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
      <script type="text/javascript">
  function checkUsername()
{
if($F('userid')=="" )
  {
    $('checkUserID').innerHTML = '<p><FONT COLOR="red">Username cannot be blank</FONT></p>'
    return
  }

  var url = '<g:createLink action="checkUser"/>'
  url += '?userid=' + $F('userid')

  new Ajax.Request(url,
  {
      method: 'post',
      onSuccess: function(response)
      {
          var content = response.responseText
          if(content == 'T')
          {
              $('checkUserID').innerHTML = '<FONT COLOR="green">Username is available.</FONT>'
          }
          else if (content =='F')
          {
              $('checkUserID').innerHTML = '<FONT COLOR="red">Username is taken.</FONT>'
          } else if (content =="I")
          {
            $('checkUserID').innerHTML = '<FONT COLOR="red">Invalid username.</FONT>'
          }
      },
      onFailure: function(response)
      {

      }
  }
  );
}

      </script>
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
        <img src="${resource(dir:'images/amity',file:'breport.png')}" border="0" id="pageTitle"/>
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
          As this is your first time logging in, please define the following:
          <g:form controller="resident">
            User ID: <br/><g:textField name="userid" onblur="checkUsername();" onfocus=""/><div id = "checkUserID"></div>
            Password: <br/><g:passwordField name="password" onkeyup="return passwordChanged();" onblur="checkEmptyFirstPassword();"/><div id = "strength"></div>
            Password again: <br/><g:passwordField name="password2" onkeyup="return identicalPassword();" onblur="checkEmptySecondPassword();"/><div id = "checkPass"></div>
            Email:<br/><g:textField name="email" onblur="checkEmptyEmail(),checkValidEmail();" onfocus=""/><div id = "emailField"></div><br/>
            <g:actionSubmit value="Update" action="initAccount" onclick="return checkBeforeSubmit()" />
          </g:form>


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