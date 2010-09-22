<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />
    <script type="text/javascript" src="${resource(dir: 'js', file: 'test.js')}"></script>
    <script type="text/javascript">
      changeName = function()
      {
        transfer=document.getElementById('photoName').value
      }
    </script>
  </head>
  <body>
    <h2>Share with people how wonderful your item looks like</h2>
  <g:uploadForm onsubmit="changeName()">
    Item photo:<br/><input id="photoName" input="imageFile" type="file" name="photoName" accept="image/jpeg" />
    <g:actionSubmit value="Submit" action="uploadPhoto" onclick="Modalbox.hide()" />
  </g:uploadForm>
  </body>
</html>
