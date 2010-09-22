<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
    <script type="text/javascript">
      function asdf()
      {
alert('fucked here')
      $('imagie').src=$('text').value
      }

function asdfg()
      { alert('fuck you man, image not valid.')
      }
    </script>
  </head>
  <body>
    <h1>Sample line</h1>
  <g:textField id="text" name="text"/>
  <img id="imagie" src="" onerror="asdfg(); return false;" />
  <g:actionSubmit onclick="asdf();return false;" value="fuck you"/>
  </body>
</html>
