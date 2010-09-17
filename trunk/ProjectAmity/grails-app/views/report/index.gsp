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
      <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
      <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />
  </head>
  <body class="thrColFixHdr"  onLoad="${remoteFunction(action:'loadData',onSuccess:'Init(e)')}" onunload="GUnload()">

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
          
          <h2>
            <span id="reporthideinfo"><a href="#" onclick="reporthidemap(); return false">Hide map</a></span> |
            <span id="reportshowinfo"><a href="#" onclick="reportshowmap(); return false">Show map</a></span>
          </h2>
          <br/>
          <div id="map" style="width: 100%; height: 600px;"></div>
          <div id="reportinfo" style="width: 100%; height: 600px;">
            <resource:accordion skin="myaccordion" />
            <richui:accordion>
              <g:each in="${list}" var="report">
                <richui:accordionItem id="1" caption="${report.title}">
                  <table width="900" border="0" height="100">
                    <tr>
                      <td width="200">&nbsp;<img src="../outdoorreportimages/${report.image}" width="200"/></td>

                      <g:if test="${report.status == 'Resolved'}">
                        <td>&nbsp;<p>Status:<br/> <b>${report.status}</b></p><p>Description: <br/>${report.description}</p></td>
                        <td>&nbsp;</td>
                      </g:if>
                      <g:if test="${report.status == 'Pending'}">
                        <td>&nbsp;<p>Status:<br/> ${report.status}</p><p>Description: <br/>${report.description}</p></td>
                        <td>&nbsp;</td>
                      </g:if> 
                      <g:if test="${report.status == 'Resolved'}">
                       
                        <td width="200">&nbsp;<img src="../outdoorreportimages/${report.resolvedImage}" width="200"/></td>
                        <td>&nbsp;<p>Official Statement</p><p>Description: <br/>${report.resolvedDescription}</p></td>
                      </g:if>

                    </tr>
                  </table>
                </richui:accordionItem>
              </g:each>
            </richui:accordion>

          </div>


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