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
      var ihtml=[]
       asdf = function(response) {
        var results = eval( '(' + response.responseText + ')' )
        var html=""
        if(results.length==0)
          {
            html+="<i>You have no items to barter.<i>"
            $('sendRequest').hide()
          }
          else
            {
              html+="<span id=\"spanItemTrade\">"
              html+="Select items from your inventory to trade: "
        html+="<select name=\"itemTrade\" id=\"itemTrade\" onchange=\"shiftItem()\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].id+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="<select name=\"itemTrade\" id=\"itemTradeName\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].itemName+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="<select name=\"itemTrade\" id=\"itemTradePhoto\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].itemPhoto+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="<select name=\"itemTrade\" id=\"itemTradeCondition\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].itemCondition+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="<select name=\"itemTrade\" id=\"itemTradeDescription\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].itemDescription+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="<select name=\"itemTrade\" id=\"itemTradeValue\" >"
        for(var i=0; i<results.length;i++) {
          html+=results[i].itemName
          html+="<option value=\""+results[i].itemValue+"\" >"+results[i].itemName+"</option>"
        }
        html+="</select>"
        html+="<input type=\"button\" value=\"Use this item to for trade proposal.\" onclick=\"addToPanel()\" />"
        html+="</span>"
        html+="<div id=\"proposingPanel\"></div>"
        $('sendRequest').show()
            }

        $('itemPropose').innerHTML=html
        $('itemTradeName').hide()
        $('itemTradePhoto').hide()
        $('itemTradeDescription').hide()
        $('itemTradeCondition').hide()
        $('itemTradeValue').hide()
      }

      var itemsInvovled=[]
      var proposingitem=""

shiftItem = function() {
  $('itemTradeName').selectedIndex=$('itemTrade').selectedIndex
  $('itemTradePhoto').selectedIndex=$('itemTrade').selectedIndex
    $('itemTradeDescription').selectedIndex=$('itemTrade').selectedIndex
  $('itemTradeCondition').selectedIndex=$('itemTrade').selectedIndex
    $('itemTradeValue').selectedIndex=$('itemTrade').selectedIndex

//  alert($('itemTradeName').selectedIndex)
}

addToPanel = function() {
  proposingitem=$('itemTrade').value
  var html=""
  html+="<table width=\"580\">"
html+="      <tbody>"
html+="        <tr>"
html+="          <td width=\"150\" rowspan=\"4\"><img src=\"http://localhost:8080/ProjectAmity/barter/../images/database/"+$('itemTradePhoto').value+"\" width=\"140\"/></td>"
html+="          <td colspan=\"3\"><h2>"+$('itemTradeName').value+"</h2></td>"
html+="        </tr>  <tr>    <td width=\"120\">Estimated Value : </td>"
html+="          <td width=\"304\" colspan=\"2\">"+$('itemTradeValue').value+"</td>"
html+="        </tr>"
html+="        <tr>"
html+="          <td>Current Condition :"
html+="          </td>"
html+="          <td colspan=\"2\">"+$('itemTradeCondition').value+"</td>"
html+="        </tr>"
html+="        <tr>"
html+="          <td valign=\"top\">Description: </td>"
html+="          <td colspan=\"2\">"+$('itemTradeDescription').value+"</td>"
html+="        </tr>"
html+="      </tbody>"
html+="    </table>"
html+="  </fieldset>"
$('proposingPanel').innerHTML=html
}

sendRequest = function () {
  if(proposingitem!="")
    {
      var a=$('loggedInUserId').value
  var b=$('resident').value
  itemsInvovled.push(proposingitem)
  itemsInvovled.push($('itemId').value)
  var c=itemsInvovled.toString()
  var d='Trade with items'
${remoteFunction(action:'createRequest', onSuccess:'changeDivMessage()', params:'\'partyone=\'+a+\'&partytwo=\'+b+\'&involvedItems=\'+c+\'&barterAction=\'+d+\'\'')}
    } else
      {
        alert('You have not selected anything to trade!')
      }
  
}

changeDivMessage = function()
{
  var html=""
  html+="<i>Request successfully sent!</i>"
  html+="<br/>This box will close automatically in 2 seconds or click <a href=\"#\" onclick\"Modalbox.hide()\">"
  html+="here</a> to close."
  $('sendPanel').innerHTML=html

  var t=setTimeout("Modalbox.hide()",2000);
}

    </script>
  </head>
  <body>
    <div id="sendPanel">
  <g:hiddenField name="itemName" value="${params.loggedInUserId}" id="loggedInUserId" />
  <g:hiddenField name="resident" value="${params.residentId}" id="resident" />
  <g:hiddenField name="itemId" value="${params.itemId}" id="itemId" />
  <h3>Request <b>${params.residentName}</b> to trade <b>${params.itemName}</b></h3>

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

  <div id="sendRequest" style="width: 600px; text-align: right; padding-top: 10px;">
    <g:actionSubmit value="Send request" onclick="sendRequest()"/>
  </div>
</div>
</body>
</html>
