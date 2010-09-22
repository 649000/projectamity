<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

  <head>

    <title>Check a Carpooler's Rating</title>

    <g:if test="${!session.user}">
        <%
          response.setStatus(301);
          response.setHeader( "Location", "/ProjectAmity" );
          response.setHeader( "Connection", "close" );
        %>
    </g:if>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />

    <style type="text/css">
      body
      {
        font: 14px "Myriad Pro", "Trebuchet MS", "Helvetica Neue", Helvetica, Arial, Sans-Serif;
        line-height: 1.5;
        color: #333;
        background: #FFF;
        margin: 0; /* it's good practice to zero the margin and padding of the body element to account for differing browser defaults */
        padding: 0;
        color: #000000;
        height: 100%;
      }
    </style>

  </head>

  <body>

    <g:if test="${session.user}">

      <h3>${params.statement}</h3>

    </g:if>

    <g:else>
      <h1>Sorry, you have to log in to view content in this page.</h1>
    </g:else>

  </body>

</html>