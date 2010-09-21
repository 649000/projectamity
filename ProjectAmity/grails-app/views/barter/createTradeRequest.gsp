<!--
  To change this template, choose Tools | Templates
  and open the template in the editor.
-->

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sample title</title>
    <script type="text/javascript">
      var results = new Array()
       asdf = function(response) {
        results = eval( '(' + response.responseText + ')' )
        var html=""
        html+="Select items from your inventory to trade: "
        html+="<span id=\"spanItemTrade\">"
        html+="<select name=\"itemTrade\" id=\"itemTrade\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].id+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="</span>"
        html+="<input type=\"button\" value=\"Add to proposing item(s).\" onclick=\"addToPanel()\" />"
        html+="<div id=\"proposingPanel\" style=\"width:580px; border:dashed black 1px;height:100px;overflow-y: hidden;\"><table width=\"0\" id=\"scroller\"><tr><td id=\"scrollercontent\">&nbsp;</td></tr></table></div>"
        $('itemPropose').innerHTML=html
      }

      var ihtml=new Array()
      var width=100
      var leftpx=2;
      var toRemove=new Array()

      var requestItemsArray=new Array()

      addToPanel = function() {
        var widthpx=width+'px'
        leftpx+=2;
        
        ihtml.push("<div id=\""+document.getElementById('itemTrade')[document.getElementById('itemTrade').selectedIndex].innerHTML+$('itemTrade').value+"\" style=\"width: 80px; height: 80px;float: left;left: "+leftpx+"px;position: relative;border:solid black 1px;\"><img src=\"http://localhost:8080/ProjectAmity/images/database/oreo.jpg\" height=\"80\" width=\"80\"/><a href=\"#\" onclick=\"removeproposingitem(\'"+$('itemTrade').value+"\',\'"+leftpx+"\')\"><img src=\"http://localhost:8080/ProjectAmity/images/amity/delete_2.png\" height=\"20\" width=\"20\" style=\"z-index: 2;position:absolute;right: 0px;top:0px;\"/></a></div>")
        
        $('scroller').setStyle({width: widthpx})
        var html=""
        for(var i=0; i<ihtml.length; i++) {
            html+=ihtml[i]
          }
        $('scrollercontent').innerHTML=html
        width+=80

requestItemsArray.push($('itemTrade').value)
alert(requestItemsArray.toString())
      }

      removeproposingitem = function (divname, leftlength) {
        for(var i=0; i<ihtml.length; i++) {
            //if(ihtml[i]=="<div id=\""+$('itemTrade').value+"\" style=\"width: 80px; height: 80px;float: left;left: "+leftlength+"px;position: relative;border:solid black 1px;\"><img src=\"http://localhost:8080/ProjectAmity/images/database/oreo.jpg\" height=\"80\" width=\"80\"/><a href=\"#\" onclick=\"removeproposingitem(\'"+$('itemTrade').value+"\',\'"+leftlength+"\')\"><img src=\"http://localhost:8080/ProjectAmity/images/amity/delete_2.png\" height=\"20\" width=\"20\" style=\"z-index: 2;position:absolute;right: 0px;top:0px;\"/></a></div>")
              if(ihtml[i].match("<div id=\""+divname))
              ihtml[i]=""
            
            //alert(ihtml)
          }
          leftpx-=2;
        $(divname).remove()
        return false;
      }

      sendRequest = function () {
        var a=$('loggedInUser').value
      var b=$('resident').value
      var c=requestItemsArray.toString()
      var d='Trade with items'
${remoteFunction(action:'createRequest', onSuccess:'asdfgasd(e)', params:'\'partyone=\'+a+\'&partytwo=\'+b+\'&involvedItems=\'+c+\'&barterAction=\'+d+\'\'')}
      }
    </script>
  </head>
  <body>
  <g:hiddenField name="itemName" value="${params.loggedInUser}" id="loggedInUser" />
  <g:hiddenField name="resident" value="${params.resident}" id="resident" />
  <h3>Request <b>${params.resident}</b> to trade <b>${params.itemName}</b></h3>

  <script type="text/javascript">
${remoteFunction(action:'listyouritems', onSuccess:'asdf(e)', params:'\'resident=\'+resident')}
  </script>
  <fieldset>
    <legend>Summary of item you want : </legend>
    <table width="580">
      <tbody>
        <tr>
          <td width="150" rowspan="4"><img src="http://localhost:8080/ProjectAmity/barter/../images/database/${params.itemPhoto}" width="140"/></td>
          <td colspan="3"><h2>${params.itemName}</h2></td>
        </tr>  <tr>    <td width="120">Estimated Value : </td>
          <td width="304" colspan="2">${params.itemValue}</td>
        </tr>
        <tr>
          <td>Current Condition :
          </td>
          <td colspan="2">${params.itemCondition}</td>
        </tr>
        <tr>
          <td valign="top">Description: </td>
          <td colspan="2">${params.itemDescription}</td>
        </tr>
      </tbody>
    </table>
  </fieldset>

  <fieldset>
    <legend>Item(s) you are proposing : </legend>
    <div id="itemPropose"></div>

  </fieldset>

  <div style="width: 600px; text-align: right; padding-top: 10px;">
    <g:actionSubmit value="Send request" onclick="sendRequest()"/>
  </div>

</body>
</html>
