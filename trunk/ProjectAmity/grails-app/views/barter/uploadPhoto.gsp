<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
  </head>
  <body>
    <h2>Share with people how wonderful your item looks like</h2>
  <g:uploadForm>
    Item photo:<br/><input type="file" name="photoName" accept="image/jpeg" />
    <g:actionSubmit value="Submit" action="uploadPhoto" />
  </g:uploadForm>
  </body>
</html>
