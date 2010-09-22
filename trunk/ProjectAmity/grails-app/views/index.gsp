<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Welcome to Project Amity</title>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <script type="text/javascript" src="${resource(dir: 'js', file: 'loginscript.js')}" ></script>
    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}"/>
    <g:javascript library="application" />
    <modalbox:modalIncludes />

    <script type="text/javascript">
      
      function Init(response)
      {
        figures = eval( '(' + response.responseText + ')')

        $('carpools').innerHTML = '<h1>' + figures[0] + '</h1>'
        $('barters').innerHTML = '<h1>' + figures[1] + '</h1>'
        $('reports').innerHTML = '<h1>' + figures[2] + '</h1>'
      }

    </script>

  </head>

  <body class="thrColFixHdr" onload="${remoteFunction(controller: 'outer', action:'index', onSuccess:'Init(e)')}">

    <div class="wrapper">

      <div id="container">
        <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" id="logo"/>
        <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
        <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
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

        <div id="mainContent" style="min-height: 400px">

          <h1>Welcome to Project Amity</h1>

          <g:if test="${flash.loginOperationStatus}">
            <br/>${flash.loginOperationStatus}<br/>
          </g:if>

          <br/>

          <div id="loginForm" style="float: right; border: 2px solid #E6F0D2; padding: 5px; width: 270px">
            <g:form>
              <table border="0" cellspacing="0" width="100%" style="padding: 5px; background-color: #E6F0D2">
                <tr>
                  <td colspan="2"><p><b>Connect with your neighbours now!</b></p></td>
                </tr>
                <tr>
                  <td colspan="2"><p>&nbsp;</p></td>
                </tr>
                <tr>
                  <td width="30"><b>NRIC </b></td>
                  <td><g:textField name="nric" /></td>
                </tr>
                <tr>
                  <td colspan="2">&nbsp;</td>
                </tr>
                <tr>
                  <td width="30%"><b>Password </b></td>
                  <td><g:passwordField name="password" /></td>
                </tr>
                <tr>
                  <td colspan="2">&nbsp;</td>
                </tr>
                <tr style="text-align: center">
                  <td colspan="2"><g:submitToRemote value="Log In" url="[controller:'resident', action:'checkPassword']" onSuccess="checkLogin(e)"/></td>
                </tr>
                <tr style="text-align: center">
                  <td colspan="2">&nbsp;</td>
                </tr>
                <tr style="text-align: center">
                  <td colspan="2"><p><b><modalbox:createLink url="resident/resetPass" title="Reset Password" width="300" linkname="Forgot Password?" /></b></p></td>
                </tr>
              </table>
            </g:form>
          </div>

          <div id="leftPane" style="float: left; border: 0px solid black; width: 600px">
            <img src="${resource(dir:'images/amity',file:'tempbanner.jpg')}" title="Project Amity" alt="Project Amity" />
          </div>

          <div id="bottomStats" style="text-align:center; border: 0px solid black; margin-top: 240px">

            <table border="0" style="margin-left: auto; margin-right: auto; width: 100%">
              <tr style="text-align: center; vertical-align: middle; height: 40px">
                <td id="carpools"><h1>Loading...</h1></td>
                <td width="50px">&nbsp;</td>
                <td id="barters"><h1>Loading...</h1></td>
                <td width="50px">&nbsp;</td>
                <td id="reports"><h1>Loading...</h1></td>
              </tr>
              <tr style="text-align: center; vertical-align: middle; height: 30px; margin-top: 20px">
                <td style="border-top: 5px solid #E6F0D2"><p><b>Carpools</b></p></td>
                <td width="50px">&nbsp;</td>
                <td style="border-top: 5px solid #E6F0D2"><p><b>Barters</b></p></td>
                <td width="50px">&nbsp;</td>
                <td style="border-top: 5px solid #E6F0D2"><p><b>Reports</b></p></td>
              </tr>
            </table>

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