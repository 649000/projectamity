<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Welcome to Project Amity</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript" src="${resource(dir: 'js', file: 'loginscript.js')}" ></script>
    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>

  </head>

  <body class="thrColFixHdr">

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
<!--      <img src="${resource(dir:'images/amity',file:'bcarpool.png')}" border="0" id="pageTitle"/>-->

      <div id="header">
        <h1>test</h1>
        <!-- end #header -->
      </div>

      <div id="banner">
        &nbsp;
      </div>

      <div id="navi">
        <!--This if should only appear in the login page-->

      </div>

      <div id="mainContent">

        <h1>Welcome to Project Amity</h1>

        <g:if test="${flash.loginOperationStatus}">
          <br/>${flash.loginOperationStatus}<br/>
        </g:if>

        <br/>
        <g:form>
          <table width="60%">
            <tr>
              <td width="20%"><b>NRIC </b></td>
              <td><g:textField name="nric" /></td>
            </tr>
            <tr>
              <td width="20%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td width="20%"><b>Password </b></td>
              <td><g:passwordField name="password" /></td>
            </tr>
            <tr>
              <td width="20%">&nbsp;</td>
              <td></td>
            </tr>
            <tr>
              <td colspan="2"><g:submitToRemote value="Login" url="[controller:'resident', action:'checkPassword']" onSuccess="checkLogin(e)"/></td>
            </tr>
          </table>
        </g:form>

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