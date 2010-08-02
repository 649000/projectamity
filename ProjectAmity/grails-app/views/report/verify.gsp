<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps</title>
      <script src="http://maps.google.com/maps?file=api&amp;v=3&amp;key=ABQIAAAAl3XLeSqUNe8Ev9bdkkHWFBTlogEOPz-D7BlWWD22Bqn0kvQxhBQR-
          srLJJlcXUmLMTM2KkMsePdU1A"
  type="text/javascript"></script>
      <g:javascript library="scriptaculous" />
  <g:javascript library="prototype" />
  <script type="text/javascript">
    function sendResultsBackToServer(aroundarea)
    {
      ${remoteFunction(action:'sendData', params:'\'data=\'+aroundarea')}
    }
    </script>
  </head>
  <body onLoad="${remoteFunction(action:'loadData2',onSuccess:'init(e)')}">

    <div id="map" style="width: 800px; height: 500px"></div>
    <div id="message">&nbsp;</div>

    <script type="text/javascript" src="${resource(dir: 'js', file: 'test.js')}" ></script>
  </body>

</html>