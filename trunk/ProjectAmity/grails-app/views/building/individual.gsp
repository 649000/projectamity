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
      <resource:lightBox />
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
        <div id="navi">&nbsp;
          <span id="navi2"></span>
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


          <h1>${report.title}</h1>
          <!-- AddThis Button BEGIN -->
<a class="addthis_button" href="http://www.addthis.com/bookmark.php?v=250&amp;username=xa-4c9098cd65402250"><img src="http://s7.addthis.com/static/btn/v2/lg-share-en.gif" width="125" height="16" alt="Bookmark and Share" style="border:0"/></a>
<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=xa-4c9098cd65402250"></script>
<!-- AddThis Button END -->

                  <table width="900" border="0" height="100">
                    <tr>
                      

                      <g:if test="${report.status == 'Resolved'}">
                        <td>&nbsp;<p>Status:<br/> <b>${report.status}</b></p><br/><p>Date: <br/>${date}</p><br/><p>Location: <br/>${loc}</p><br/><p>Description: <br/>${report.description}</p></td>
                        <td>&nbsp;</td>
                      </g:if>
                      <g:if test="${report.status == 'Pending'}">
                        <td>&nbsp;<p>Status:<br/> ${report.status}</p><br/><p>Date: <br/>${date}</p><br/><p>Location: <br/>${loc}</p><br/><p>Description: <br/>${report.description}</p></td>
                        <td>&nbsp;</td>
                      </g:if>
                      <td width="200">&nbsp;<richui:lightBox href="../indoorreportimages/${report.image}"><img src="../indoorreportimages/${report.image}" width="300"/></richui:lightBox></td>
                    </tr>

                    <g:if test="${report.status == 'Resolved'}">
                    <tr>
                        
                        <td>&nbsp;<p>Updated actions</p><br/><p>Description: <br/>${report.resolvedDescription}</p></td>
                        <td>&nbsp;</td>
                        <td width="200">&nbsp;<richui:lightBox href="../indoorreportimages/${report.resolvedImage}"><img src="../indoorreportimages/${report.resolvedImage}" width="300"/></richui:lightBox></td>
                    </tr>
                    </g:if>
                  </table>

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