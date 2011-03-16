<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>


    <title>Barter Home</title>

    <g:if test="${!session.user}">
<%
response.setStatus(301);
response.setHeader( "Location", "/ProjectAmity" );
response.setHeader( "Connection", "close" );
%>
    </g:if>

    <g:javascript library="scriptaculous" />
    <g:javascript library="prototype" />
    <script type="text/javascript" src="${resource(dir: 'js', file: 'test.js')}"></script>
    <g:javascript library="application" />
    <modalbox:modalIncludes />

    <script type="text/javascript">
  var resident=''
    function loadHide()
{
    resident=$('resident').value
    $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').show()
    $('itemdisplay').hide()
${remoteFunction(action:'listRequest', onSuccess:'listRequests(e)', params:'\'resident=\'+resident')}
    $('tab1dark').hide()
    $('tab2dark').hide()
    $('tab3dark').hide()
    $('tab4dark').hide()
    $('itemSearch').hide()
    $('searchOptions').hide()
    $('itemStartAction').hide()
}
function viewyouritems(response)
{
var results = eval( '(' + response.responseText + ')' )
var html=""
if(results.length==0)
  {
    html+="<i>You do not have any item posting currently.</i><b><a href=\"#\" onclick=\"createitemshow()\"> Click here to add a posting.</a></b>"
  }
                    
                    for(var i=0; i<results.length; i++)
                      {
                        html+="<div style=\"float: left;margin:0 10px 10px 0\">"
                        html+="<div style=\"background-color: #f0f0f0;border: 1px solid #d5d5d5;vertical-align: middle;width:120px;height:90px;padding: 5px 5px 5px 5px;margin:0 0 5px 0\">"
                        html+="<a href=\"#\" onclick=\"viewitem(\'"+results[i].id+"\'); return false;\">"
                          html+="<img src=\"/../../ProjectAmity/images/database/"+results[i].itemPhoto+"\" height=\"90\" width=\"120\" border=\"0\"/><br/>"
                          html+="</a>"
                          html+="</div>"
                          html+="<b><a href=\"#\" onclick=\"viewitem(\'"+results[i].id+"\'); return false;\">"+results[i].itemName+"</a></b>"
                          html+="<br/>"+ results[i].itemStartAction

                            var sDate = new Date();
  var eDate = new Date(results[i].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  html+="<br/>"+ daysApart + " days left"
                          html+="<br/>$"+ results[i].itemValue
                          html+="</div>"
                      }
                      $('yourlistitem').innerHTML=html

                      $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').show()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').hide()
    $('itemdisplay').hide()
    if(results.length>0)
      {
        var heightpx=results.length/6
        heightpx=Math.ceil(heightpx)*190
        heightpx=heightpx+'px'
    $('yourlistitem').setStyle({
  height: heightpx
});
      }


}

function viewallitems(response)
{
var results = eval( '(' + response.responseText + ')' )
var html=""

if(results[0].length==0)
  {
    html+="<i>No products in this category.</i>"
  }
else
  {
    for(var i=0; i<results[0].length; i++)
                      {
                        html+="<div style=\"float: left;margin:0 10px 10px 0\">"
                        html+="<div style=\"background-color: #f0f0f0;border: 1px solid #d5d5d5;vertical-align: middle;width:120px;height:90px;padding: 5px 5px 5px 5px;margin:0 0 5px 0\">"
                        html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0][i].id+"\'); return false;\">"
                          html+="<img src=\"/../../ProjectAmity/images/database/"+results[0][i].itemPhoto+"\" height=\"90\" width=\"120\" border=\"0\"/><br/>"
                          html+="</a>"
                          html+="</div>"
                          
                          if(results[0][i].itemName.length>=17)
                            {
                              html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0][i].id+"\'); return false;\">"
                              html+=results[0][i].itemName.substr(0,17) + "...</a><br/>"
                            } else {
                              html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0][i].id+"\'); return false;\">"
                              html+=results[0][i].itemName + "</a><br/>"
                            }

                          html+= results[0][i].itemStartAction

                            var sDate = new Date();
  var eDate = new Date(results[0][i].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  html+="<br/>"+ daysApart + " days left"

                          html+="<br/>$"+ results[0][i].itemValue
                          if(results[1][i].name.length>=17)
                            {
                              html+="<br/><a href=\"#\">"+ results[1][i].name.substr(0,17) + "...</a>"
                            } else {
                              html+="<br/><a href=\"#\">"+ results[1][i].name + "</a>"
                            }
                          
                          //html+="<br/><a href=\"#\" onclick=\"createRequest(\'"+results[0][i].id+"\',\'"+results[1][i].id+"\',\'"+results[0][i].itemStartAction+"\'); return false;\">Request</a>"
                          html+="</div>"
                      }
  }
                    
                    


    $('listitem').innerHTML=html

    $('yourlistitem').hide()
    $('createbarteritem').hide()
    $('listitem').show()
    $('itemdisplay').hide()
    $('requestpanel').hide()
    $('itemSearch').hide()
    $('searchOptions').hide()

    if(results.length>0)
      {
        var heightpx=results.length/6
        heightpx=Math.ceil(heightpx)*220
        heightpx=heightpx+'px'
    $('listitem').setStyle({
  height: heightpx
});
      }
}

function createRequest(itemid, traderid, startAction)
{
  //username here
  var a=resident
  var b=traderid
  var c=itemid
  var d=startAction
${remoteFunction(action:'createRequest', onSuccess:'asdfgasd(e)', params:'\'partyone=\'+a+\'&partytwo=\'+b+\'&involvedItems=\'+c+\'&barterAction=\'+d+\'\'')}
}

function listRequests(response)
{
  var results = eval( '(' + response.responseText + ')' )
  var html=""
  if(results.length==0)
    {
      html+="<i>You have no requests currently.</i>"
    }
    else
      {

        html+="<table width=\"900\">"
html+="  <tr>"
html+="    <th height=\"37\" colspan=\"2\" scope=\"row\" html+=align=\"left\"><h2>View all requests</h2></th>"
html+="  </tr>"
html+="  <tr>"
html+="    <th width=\"232\" height=\"335\" scope=\"row\" valign=\"top\" >"
        for(var i=0; i<results.length; i++)
                      {
                        



html+="<div style=\"background-color: #f0f0f0; width:330px;border: 1px solid #d5d5d5;margin: 0 0 5px 0;\">"
                      html+=results[i].partyonename
                      html+="<form method=\"get\" id=\"acceptRejectHiddenForm\">"
html+="<input type=\"hidden\" name=\"partyone\" value=\""+results[i].partyone+"\"/>"
html+="<input type=\"hidden\" name=\"partyonename\" value=\""+results[i].partyonename+"\"/>"
html+="<input type=\"hidden\" name=\"partytwo\" value=\""+results[i].partytwo+"\"/>"
html+="<input type=\"hidden\" name=\"partytwoname\" value=\""+results[i].partytwoname+"\"/>"
html+="<input type=\"hidden\" name=\"barteraction\" value=\""+results[i].barterAction+"\"/>"
html+="<input type=\"hidden\" name=\"involveditems\" value=\""+results[i].involvedItems+"\"/>"
html+="<input type=\"hidden\" name=\"requestid\" value=\""+results[i].id+"\"/>"
var message="Hi "
messageReject="Hi "
message+=results[i].partyonename+",\u000D\u000D"
message+="I have accepted your "
messageReject+=results[i].partyonename+",\u000D\u000D"
messageReject+="I sincerely apologize but I am not interested to deal with you this time. I hope that we will be able to there are other opportunities  in the future."
messageReject+="\u000D\u000DYour sincerely\u000D"+results[i].partytwoname

                      html+=" has requested to "
                      if(results[i].barterAction=="Trade with items")
                        {
                          html+=' trade items with you.'
                          message+="trade request for your item. Let's confirm a meeting time and meet somewhere which is convinient to both of us."
                          message+="\u000D\u000DCheers\u000D"+results[i].partytwoname
                        }
                        else if(results[i].barterAction=="Give away")
                        {
                          html+=' receive your item.'
                          message+="give you my item. Let's confirm a meeting time and meet somewhere which is convinient to both of us."
                          message+="\u000D\u000DCheers\u000D"+results[i].partytwoname
                        }
                        else if(results[i].barterAction=="Selling")
                        {
                          message+="sell you my item. Let's confirm a meeting time and meet somewhere which is convinient to both of us."
                          message+="\u000D\u000DCheers\u000D"+results[i].partytwoname
                          html+=' buy your item.'
                        }
                        else if(results[i].barterAction=="Create wishlist")
                        {
                          message+="buy your item. Let's confirm a meeting time and meet somewhere which is convinient to both of us."
                          message+="\u000D\u000DCheers\u000D"+results[i].partytwoname
                          html+=' sell you an item.'
                        }

                        html+="<input type=\"hidden\" name=\"message\" value=\""+message+"\"/>"
                        html+="<input type=\"hidden\" name=\"messageReject\" value=\""+messageReject+"\"/>"
html+="</form>"

                        
                        html+="<a onclick=\"Modalbox.show(this.href, {title: this.title, width: 630, height: 280, overlayClose: false, params: Form.serialize(\'acceptRejectHiddenForm\')}); return false;\"  title=\"Confirm accept request\" href=\"/../ProjectAmity/barter/confirmAcceptRequestResponse.gsp\">Accept</a> |"
                        html+="<a onclick=\"Modalbox.show(this.href, {title: this.title, width: 630, height: 280, overlayClose: false, params: Form.serialize(\'acceptRejectHiddenForm\')}); return false;\"  title=\"Confirm reject request\" href=\"/../ProjectAmity/barter/confirmRejectRequestResponse.gsp\">Reject</a> |"
                        html+="<a href=\"#\" onclick=\"requestDetails(\'"+results[i].barterAction+"\',\'"+results[i].partytwoname+"\',\'"+results[i].partyonename+"\',\'"+results[i].involvedItems+"\');return false;\"> View Details</a> "
                        html+="</div>"
                      }

                      html+="</th>"
html+="    <td width=\"656\" valign=\"top\"><div id=\"requestDetails\" style=\"background-color: #f0f0f0; width:550px; height:320px;border: 1px solid #d5d5d5; padding: 5px 5px 5px 5px;\"><i>Click view details to view more details on the request.</i></div></td>"
html+="  </tr>"
html+="</table>"
      }
                    
                    
                      $('requestpanel').innerHTML=html
}

//function viewyouritem(asdf6
//{
//alert(asdf)
//}

function requestDetails(action,name2, name1,items)
{
  var html=""

  html+="<table width=\"550\">"
html+="  <tr>"
html+="    <th scope=\"row\">"

html+=name1+ " has requested to "

if(action=="Trade with items")
  {
    html+=" trade <b><u>his/her</u></b>"
  }else if(action=="Selling")
  {
    html+="buy <b><u>your</u></b>"
  } else if(action=="Give away")
  {
    html+="claim <b><u>your</u></b> free"
  } else if(action=="Create wishlist")
  {
    html+="sell you <b><u>his/her</u></b>"
  }

html+="</th>"
html+="  </tr>"
html+="  <tr>"
html+="    <th scope=\"row\"><div id=\"peopleRequestPanel\"></div></th>"
html+="  </tr>"
if(action=="Trade with items")
  {
html+="  <tr>"
html+="    <th scope=\"row\">with <b><u>YOUR</u></b></th>"
html+="  </tr>"
html+="  <tr>"
html+="    <th scope=\"row\"><div id=\"yourRequestPanel\"></div></th>"
html+="  </tr>"
  }
html+="</table>"

  $('requestDetails').innerHTML=html
${remoteFunction(action:'retrieveItems', onSuccess:'displayRequestItems(e)', params:'\'items=\'+items')}
}

function displayRequestItems(response)
{
  var results = eval( '(' + response.responseText + ')' )
  //alert(results.length)
  var html=""
  if(results.length>1)
    {
      for(var i=0; i<results.length-1;i++)
    {
      html+="<div style=\"float: left;margin:0 10px 10px 0\">"
                        html+="<div style=\"background-color: #f0f0f0;border: 1px solid #d5d5d5;vertical-align: middle;width:120px;height:90px;padding: 5px 5px 5px 5px;margin:0 0 5px 0\">"
                        html+="<a href=\"#\" onclick=\"viewitem(\'"+results[i].id+"\'); return false;\">"
                          html+="<img src=\"/../../ProjectAmity/images/database/"+results[i].itemPhoto+"\" height=\"90\" width=\"120\" border=\"0\"/><br/>"
                          html+="</a>"
                          html+="</div>"
                          html+="<b><a href=\"#\" onclick=\"viewitem(\'"+results[i].id+"\'); return false;\">"+results[i].itemName+"</a></b>"
                          html+="<br/>$"+ results[i].itemValue
                          html+="</div>"
    }

    $('peopleRequestPanel').innerHTML=html

    var ihtml=""
      ihtml+="<div style=\"float: left;margin:0 10px 10px 0\">"
                        ihtml+="<div style=\"background-color: #f0f0f0;border: 1px solid #d5d5d5;vertical-align: middle;width:120px;height:90px;padding: 5px 5px 5px 5px;margin:0 0 5px 0\">"
                        ihtml+="<a href=\"#\" onclick=\"viewitem(\'"+results[results.length-1].id+"\'); return false;\">"
                          ihtml+="<img src=\"/../../ProjectAmity/images/database/"+results[results.length-1].itemPhoto+"\" height=\"90\" width=\"120\" border=\"0\"/><br/>"
                          ihtml+="</a>"
                          ihtml+="</div>"
                          ihtml+="<b><a href=\"#\" onclick=\"viewitem(\'"+results[results.length-1].id+"\'); return false;\">"+results[results.length-1].itemName+"</a></b>"
                          ihtml+="<br/>$"+ results[results.length-1].itemValue
                          ihtml+="</div>"
    $('yourRequestPanel').innerHTML=ihtml
    }
    else
      {
        html+="<div style=\"float: left;margin:0 10px 10px 0\">"
                        html+="<div style=\"background-color: #f0f0f0;border: 1px solid #d5d5d5;vertical-align: middle;width:120px;height:90px;padding: 5px 5px 5px 5px;margin:0 0 5px 0\">"
                        html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0].id+"\'); return false;\">"
                          html+="<img src=\"/../../ProjectAmity/images/database/"+results[0].itemPhoto+"\" height=\"90\" width=\"120\" border=\"0\"/><br/>"
                          html+="</a>"
                          html+="</div>"
                          html+="<b><a href=\"#\" onclick=\"viewitem(\'"+results[0].id+"\'); return false;\">"+results[0].itemName+"</a></b>"
                          html+="<br/>$"+ results[0].itemValue
                          html+="</div>"
                          $('peopleRequestPanel').innerHTML=html
      }
  

$('requestDetails').setStyle({
  height: '400px'
});

}

function searchResults(response)
{
    var results = eval( '(' + response.responseText + ')' )
    var html=""

    for(var i=0; i<results.length;i++)
    {
        if(results.length>0)    {
            html+="<a href=\"#\" onclick=\"searchResultText(\'"+results[i].catlvlone+"\');return false;\">"+results[i].catlvlone+"</a>"
        }
    }

    if(results.length==0)
      {
        html+="<i>No results found.</i>"
      }

    $('searchResults').innerHTML=html
}

function searchResultText(catlvlone)
{
    $('itemCategory').value=catlvlone
    $('categorySearchSpan').hide()
    $('categorySpan').show()
    $('categoryShow').hide()
}

function showSearchCategory()
{
    $('categorySearchSpan').show()
    $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
}


function showAllCategory()
{
    $('categorySearchSpan').hide()
    $('categorySpan').hide()
    $('categoryShow').show()
    return false;
}



function checkKeycode(e) {
    var keycode;
    if (window.event) {
        keycode = window.event.keyCode
    } else if (e) {
        keycode = e.which;
    }

    if (keycode > 31 && (keycode < 48 || keycode > 57) && keycode!=190)    {
        return false;
    } else {
        return true
    }
}

function showChangeCatButton()
{
    $('changeCategory').show()
    return false;
}

var icount=0

function showMiniCategory()
{
    $('categoryitem').show()
    counter=0
}

var counter=0;

function hideMiniCategory()
{
  counter++;
  if(counter>=2)
    {
      $('categoryitem').hide()
      counter=0
    }
}

function loadyouritems()
{
${remoteFunction(action:'listyouritems', onSuccess:'viewyouritems(e)', params:'\'resident=\'+resident')}
    $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').show()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').hide()
    $('itemdisplay').hide()
    $('itemSearch').hide()
    $('searchOptions').hide()
}
function createitemshow()
{
transfer=""
$('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('categorySearchSpan').show()
    $('createbarteritem').show()
    $('listitem').hide()
    $('requestpanel').hide()
    $('itemdisplay').hide()
    $('itemPictureDiv').hide();
    $('itemPeekture').show();
    $('itemStartAction').selectedIndex=0
    $('itemName').value=""
    $('itemCategorySearch').value=""
    $('itemCategory').value
    $('itemCondition').selectedIndex=0
    $('itemValue').value=""
    $('itemDescription').value=""
    $('itemTime').selectedIndex=0
    $('itemPhoto').value=""
    $('itemSearch').hide()
    $('searchOptions').hide()
}

function viewitem(id)
{
${remoteFunction(action:'viewitem', onSuccess:'displayitem(e)', params:'\'id=\'+id')}
    $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').hide()
    $('itemdisplay').show()
    $('itemSearch').hide()
}

function displayitem(response)
{
  var results = eval( '(' + response.responseText + ')' )
                    var html=""

                    if(results[1].id==resident)
                      {


                                                var sDate = new Date();
  var eDate = new Date(results[0].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));

html+="            <form action=\"/ProjectAmity/barter/index\" method=\"post\" enctype=\"multipart/form-data\" >"
html+="<input type=\"hidden\" id=\"resident\" value=\""+results[1].id+"\" name=\"resident\">"
html+="<input type=\"hidden\" name=\"id\" value=\""+results[0].id+"\" id=\"id\" />"
html+="<table width=\"900\">"
html+="  <tr>"
html+="    <td width=\"301\" rowspan=\"7\">"
html+="<br/><img src=\"/../../ProjectAmity/images/database/"+results[0].itemPhoto+"\" width=\"250\"/><br/>"
html+="<input type=\"text\" name=\"itemPhoto\" id=\"itemPhoto2\" value=\""+results[0].itemPhoto+"\" />"
html+=" </td>"
html+="    <td colspan=\"2\"><h2><span id=\"itemName3\">"+results[0].itemName+"</span></h2>"
html+="<span id=\"itemName2\">"
html+="Name of item: <input type=\"text\" name=\"itemName\" id=\"itemName\" value=\""+results[0].itemName+"\" />"
html+="</span>"
html+="</td>"
html+="    <td width=\"142\" align=\"right\">"
html+="<input onclick=\"new Ajax.Request(\'/ProjectAmity/barter/edit\',{asynchronous:true,evalScripts:true,onSuccess:function(e){viewitem(\'"+results[0].id+"\')},onLoading:function(e){hideError();toggleObj(\'spinner1\')},onComplete:function(e){toggleObj(\'spinner1\')},parameters:Form.serialize(this.form)});return false\" type=\"button\" value=\"Done\" id=\"done2\"></input>"
html+="<span id=\"updater3\"><a href=\"#\" onclick=\"showEditPane();return false;\">Edit</a></span> | "
html+="<a href=\"#\">Delete</a>"
html+="    </td>"
html+="  </tr>"
html+="  <tr>"
html+="    <td colspan=\"3\"><a href=\"#\">"+results[1].name+"</a> is trading items."


html+="</td>"

html+="  </tr>"
html+="  <tr>"
html+="    <td width=\"199\">Item Category :</td>"
html+="    <td colspan=\"2\"><span id=\"itemCategory3\">"+results[0].itemCategory+"</span>"

html+="              <span id=\"categorySpan2\">"
html+="                <input type=\"text\" name=\"itemCategory\" onkeydown=\"return showChangeCatButton2();\" id=\"itemCategory2\" value=\""+results[0].itemCategory+"\" />"
html+="                <input id=\"changeCategory2\" type=\"button\" value=\"Change category?\" onclick=\"showSearchCategory2()\"/>"
html+="              </span>"
html+="              <span id=\"categorySearchSpan2\">"
html+="                <br/>Item category: <input type=\"text\" name=\"itemCategorySearch\" id=\"itemCategorySearch\" value=\""+results[0].itemCategory+"\" />"

html+="                <input onclick=\"new Ajax.Request(\'/ProjectAmity/barter/searchcategory\',{asynchronous:true,evalScripts:true,onSuccess:function(e){searchResults(e)},onLoading:function(e){hideError();toggleObj(\'spinner1\')},onComplete:function(e){toggleObj(\'spinner1\')},parameters:Form.serialize(this.form)});return false\" type=\"button\" value=\"Search\"></input>"
html+="                <a href=\"#\" onclick=\"showAllCategory2()\">Show all categories</a>"
html+="                <div id=\"searchResults\"></div>"
html+="              </span>"
html+="              <span id=\"categoryShow2\">"
html+="                <table border=\"1\">"
html+="                  <tr>"
html+="                    <td><h4>Automotive</h4></td>"

html+="                    <td><h4>Baby Care</h4></td>"
html+="                    <td><h4>Books &amp; Stationery</h4></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Vehicles\'); return false;\">Vehicles</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Baby Clothes\'); return false;\">Baby Clothes &amp; Shoes</a></td>"

html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Children\'s Books\'); return false;\">Children\'s Books</a></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Automotive Parts\'); return false;\">Automotive Parts</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Baby Food\'); return false;\">Baby Food</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Comics\'); return false;\">Comics</a></td>"
html+="                  </tr>"

html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Automotive Accessories\'); return false;\">Automotive Accessories</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Other Baby Care Products\'); return false;\">Other Baby Care Products</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Non-fiction\'); return false;\">Non-fiction</a></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td>&nbsp;</td>"

html+="                    <td>&nbsp;</td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Fiction Books\'); return false;\">Fiction Books</a></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Stationery\'); return false;\">Stationery</a></td>"
html+="                  </tr>"

html+="                  <tr>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><h4>Clothing, Shoes &amp; Accessories</h4></td>"

html+="                    <td><h4>Home &amp;Garden</h4></td>"
html+="                    <td><h4>Music &amp; Multimedia</h4></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Men\' Clothings\'); return false;\">Men\'s Clothes</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Furniture\'); return false;\">Furniture</a></td>"

html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Music\'); return false;\">Music</a></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Women\'s Clothings\'); return false;\">Women\'s Clothes</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Gardening\'); return false;\">Gardening &amp; Plants</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Video\'); return false;\">Video</a></td>"

html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Maternity Clothes\'); return false;\">Maternity Clothes</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Other Home & Gardening Items\'); return false;\">Other Home &amp; Gardening Items</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Musical Instruments\'); return false;\">Musical Instruments</a></td>"
html+="                  </tr>"

html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Clothing Accessories\'); return false;\">Clothing Accessories </a></td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Video Games\'); return false;\">Video Games</a></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Shoes\'); return false;\">Shoes</a></td>"

html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"

html+="                    <td><h4>Health &amp; Beauty</h4></td>"
html+="                    <td><h4>Sports</h4></td>"
html+="                    <td><h4>Miscellaneous</h4></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Bath & Body\'); return false;\">Bath &amp; Body</a></td>"

html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Sporting Goods\'); return false;\">Sporting Goods</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'All other Items\'); return false;\">All Other Items</a></td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Beauty Tools\'); return false;\">Beauty Tools</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"

html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Other Health & Beauty Items\'); return false;\">Other Health &amp; Beauty Items</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"

html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><h4>Electronics</h4></td>"
html+="                    <td><h4>Collectables</h4></td>"
html+="                    <td>&nbsp;</td>"

html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Television\'); return false;\">Television &amp; Monitors</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Plushies\'); return false;\">Plushies</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"

html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Mobile Devices\'); return false;\">Mobile Devices</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Coins\'); return false;\">Coins</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Household Appliances\'); return false;\">Household Appliances</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Antiques\'); return false;\">Antiques</a></td>"

html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Computers\'); return false;\">Computers</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Toys\'); return false;\">Toys</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"

html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Computer Peripherals\'); return false;\">Computer Peripherals</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Stamps\'); return false;\">Stamps</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Other electronic items\'); return false;\">Other electronic items</a></td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Art\'); return false;\">Art</a></td>"

html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                  <tr>"
html+="                    <td>&nbsp;</td>"
html+="                    <td><a href=\"#\" onclick=\"searchResultText2(\'Other Collectable items\'); return false;\">Other Collectable items</a></td>"
html+="                    <td>&nbsp;</td>"
html+="                  </tr>"
html+="                </table>"

html+="              </span>"

html+="</td>"

html+="  </tr>"
html+="  <tr>"
html+="    <td>Estimated Value :</td>"
html+="    <td colspan=\"2\"><span id=\"itemValue3\">"+results[0].itemValue+"</span>"
html+="              <input type=\"text\" name=\"itemValue\" onkeydown=\"return checkKeycode(event)\" id=\"itemValue2\" value=\""+results[0].itemValue+"\" />"
html+="</td>"
html+="  </tr>"
html+="  <tr>"
html+="    <td>Current Condition :</td>"
html+="    <td colspan=\"2\"><span id=\"itemCondition3\">"+results[0].itemCondition+"</span>"
html+="              <select name=\"itemCondition\" id=\"itemCondition2\" >"
if(results[0].itemCondition=="Completely new") {
html+="<option value=\"null\">-Choose item condition-</option>"
html+="<option value=\"Completely new\" SELECTED>Completely new</option>"
html+="<option value=\"Used before and everything working\" >Used before and everything working</option>"
html+="<option value=\"Used before and some parts not working\" >Used before and some parts not working</option>"
} else if(results[0].itemCondition=="Used before and everything working") {
html+="<option value=\"null\">-Choose item condition-</option>"
html+="<option value=\"Completely new\" >Completely new</option>"
html+="<option value=\"Used before and everything working\" SELECTED>Used before and everything working</option>"
html+="<option value=\"Used before and some parts not working\" >Used before and some parts not working</option>"
} else if(results[0].itemCondition=="Used before and some parts not working") {
html+="<option value=\"null\">-Choose item condition-</option>"
html+="<option value=\"Completely new\" >Completely new</option>"
html+="<option value=\"Used before and everything working\" >Used before and everything working</option>"
html+="<option value=\"Used before and some parts not working\" SELECTED>Used before and some parts not working</option>"
}

html+="</select>"

html+="</td>"

html+="  </tr>"
html+="  <tr>"
html+="    <td>Time left:</td>"
html+="    <td colspan=\"2\"><span id=\"itemTime3\">"+daysApart+" days left.</span>"
html+="<select name=\"itemTime\" id=\"itemTime2\" >"
if(daysApart==1) {
html+="<option value=\"1 day\" SELECTED>1 day</option>"
html+="<option value=\"2 days\" >2 days</option>"
html+="<option value=\"3 days\" >3 days</option>"
html+="<option value=\"4 days\" >4 days</option>"
html+="<option value=\"5 days\" >5 days</option>"
html+="<option value=\"6 days\" >6 days</option>"
html+="<option value=\"7 days\" >7 days</option>"
} else if(daysApart==2) {
html+="<option value=\"1 day\">1 day</option>"
html+="<option value=\"2 days\" SELECTED>2 days</option>"
html+="<option value=\"3 days\" >3 days</option>"
html+="<option value=\"4 days\" >4 days</option>"
html+="<option value=\"5 days\" >5 days</option>"
html+="<option value=\"6 days\" >6 days</option>"
html+="<option value=\"7 days\" >7 days</option>"
} else if(daysApart==3) {
html+="<option value=\"1 day\">1 day</option>"
html+="<option value=\"2 days\" >2 days</option>"
html+="<option value=\"3 days\" SELECTED>3 days</option>"
html+="<option value=\"4 days\" >4 days</option>"
html+="<option value=\"5 days\" >5 days</option>"
html+="<option value=\"6 days\" >6 days</option>"
html+="<option value=\"7 days\" >7 days</option>"
} else if(daysApart==4) {
html+="<option value=\"1 day\">1 day</option>"
html+="<option value=\"2 days\" >2 days</option>"
html+="<option value=\"3 days\" >3 days</option>"
html+="<option value=\"4 days\" SELECTED>4 days</option>"
html+="<option value=\"5 days\" >5 days</option>"
html+="<option value=\"6 days\" >6 days</option>"
html+="<option value=\"7 days\" >7 days</option>"
} else if(daysApart==5) {
html+="<option value=\"1 day\">1 day</option>"
html+="<option value=\"2 days\" >2 days</option>"
html+="<option value=\"3 days\" >3 days</option>"
html+="<option value=\"4 days\" >4 days</option>"
html+="<option value=\"5 days\" SELECTED>5 days</option>"
html+="<option value=\"6 days\" >6 days</option>"
html+="<option value=\"7 days\" >7 days</option>"
} else if(daysApart==6) {
html+="<option value=\"1 day\">1 day</option>"
html+="<option value=\"2 days\" >2 days</option>"
html+="<option value=\"3 days\" >3 days</option>"
html+="<option value=\"4 days\" >4 days</option>"
html+="<option value=\"5 days\" >5 days</option>"
html+="<option value=\"6 days\" SELECTED>6 days</option>"
html+="<option value=\"7 days\" >7 days</option>"
} else if(daysApart==7) {
html+="<option value=\"1 day\">1 day</option>"
html+="<option value=\"2 days\" >2 days</option>"
html+="<option value=\"3 days\" >3 days</option>"
html+="<option value=\"4 days\" >4 days</option>"
html+="<option value=\"5 days\" >5 days</option>"
html+="<option value=\"6 days\" >6 days</option>"
html+="<option value=\"7 days\" SELECTED>7 days</option>"
}

html+="</select>"

html+="</td>"
html+="  </tr>"
html+="  <tr>"
html+="    <td valign=\"top\">Description :</td>"
html+="    <td colspan=\"2\"><span id=\"itemDescription3\">"+results[0].itemDescription+"</span>"
html+=" <textarea name=\"itemDescription\" id=\"itemDescription2\" >"+results[0].itemDescription+"</textarea>"
html+="</td>"
html+="  </tr>"

html+="  </tr>"
html+="  </table>"
html+="            </form>"

                    $('itemdisplay').innerHTML=html

    $('categorySearchSpan2').hide()
    $('changeCategory2').hide()
    $('categoryShow2').hide()

    $('itemName2').hide()
    //$('itemStartAction2').hide()
    $('itemTime2').hide()
    $('itemDescription2').hide()
    
    $('categorySpan2').hide()
    $('itemPhoto2').hide()
    $('itemValue2').hide()
    $('itemCondition2').hide()
    $('done2').hide()
    

    

                      }

                      else
                        {



                          var sDate = new Date();
  var eDate = new Date(results[0].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));

html+="<form method=\"get\" id=\"createRequestHiddenForm\">"
html+="<input type=\"hidden\" name=\"itemName\" value=\""+results[0].itemName+"\"/>"
html+="<input type=\"hidden\" name=\"residentId\" value=\""+results[1].id+"\"/>"
html+="<input type=\"hidden\" name=\"residentName\" value=\""+results[1].name+"\"/>"
html+="<input type=\"hidden\" name=\"itemPhoto\" value=\""+results[0].itemPhoto+"\"/>"
html+="<input type=\"hidden\" name=\"itemCondition\" value=\""+results[0].itemCondition+"\"/>"
html+="<input type=\"hidden\" name=\"itemValue\" value=\""+results[0].itemValue+"\"/>"
html+="<input type=\"hidden\" name=\"itemDescription\" value=\""+results[0].itemDescription+"\"/>"
html+="<input type=\"hidden\" name=\"loggedInUserId\" value=\""+resident+"\"/>"
html+="<input type=\"hidden\" name=\"itemId\" value=\""+results[0].id+"\"/>"
html+="</form>"


html+="<table width=\"900\"> "
html+="  <tbody>"
html+="    <tr>"
html+="      <td width=\"301\" rowspan=\"7\"><img src=\"/../../ProjectAmity/images/database/"+results[0].itemPhoto+"\" width=\"290\"/><br/></td>"
html+="      <td colspan=\"2\"><h2>"+results[0].itemName+"</h2></td>  "

if(results[0].itemStartAction=='Trade with items') {
html+="      <td width=\"142\" align=\"right\"><a onclick=\"Modalbox.show(this.href, {title: this.title, width: 630, height: 440, overlayClose: false, params: Form.serialize(\'createRequestHiddenForm\')}); return false;\"  title=\"Send item trade request\" href=\"/../ProjectAmity/barter/createTradeRequest.gsp\">Make a request</a></td>  "
} else if(results[0].itemStartAction=='Selling') {
html+="      <td width=\"142\" align=\"right\"><a onclick=\"Modalbox.show(this.href, {title: this.title, width: 630, height: 100, overlayClose: false, params: Form.serialize(\'createRequestHiddenForm\')}); return false;\"  title=\"Send item buy request to "+results[1].name+"\" href=\"/../ProjectAmity/barter/createSellRequest.gsp\">Make a request</a></td>  "
} else if(results[0].itemStartAction=='Give away') {
html+="      <td width=\"142\" align=\"right\"><a onclick=\"Modalbox.show(this.href, {title: this.title, width: 630, height: 100, overlayClose: false, params: Form.serialize(\'createRequestHiddenForm\')}); return false;\"  title=\"Send item give away request to "+results[1].name+"\" href=\"/../ProjectAmity/barter/createGiveAwayRequest.gsp\">Make a request</a></td>  "
} else if(results[0].itemStartAction=='Create Wishlist') {
html+="      <td width=\"142\" align=\"right\"><a onclick=\"Modalbox.show(this.href, {title: this.title, width: 630, height: 100, overlayClose: false, params: Form.serialize(\'createRequestHiddenForm\')}); return false;\"  title=\"Send item sell offer to "+results[1].name+"\" href=\"/../ProjectAmity/barter/createWishlistRequest.gsp\">Make a request</a></td>  "
}

html+="    </tr>"
html+="    <tr>   "

if(results[0].itemStartAction=='Trade with items')
  {
    html+="      <td colspan=\"3\">"+results[1].name+" is looking to trade for items.</td> "
  } else if(results[0].itemStartAction=='Selling')
  {
    html+="      <td colspan=\"3\">"+results[1].name+" is selling away this item.</td> "
  } else if(results[0].itemStartAction=='Give away')
  {
    html+="      <td colspan=\"3\">"+results[1].name+" is giving this item away.</td> "
  } else if(results[0].itemStartAction=='Create a wishlist')
  {
    html+="      <td colspan=\"3\">"+results[1].name+" is looking to buy this item.</td> "
  }


html+="    </tr>  <tr>    <td width=\"199\">Item Category : </td> "
html+="      <td colspan=\"2\">"+results[0].itemCategory+" &gt;&gt; "+results[0].itemCategory2+"</td>  </tr> "
html+="    <tr>  "
html+="      <td>Estimated Value : </td>  "
html+="      <td colspan=\"2\">"+results[0].itemValue+"</td> "
html+="    </tr> "
html+="    <tr>   "
html+="      <td>Current Condition : </td> "
html+="      <td colspan=\"2\">"+results[0].itemCondition+"</td> "
html+="    </tr>  <tr>    <td>Time Left : </td> "
html+="      <td colspan=\"2\">"+daysApart+" days left.</td>"
html+="    </tr> "
html+="    <tr>  "
html+="      <td valign=\"top\">Description</td> "
html+="      <td colspan=\"2\">"+results[0].itemDescription+"</td>"
html+="    </tr>   "
html+="  </tbody>"
html+="</table>"
                    $('itemdisplay').innerHTML=html
                        }



}

function showEditPane()
{
  $('itemDescription2').show()
  $('itemName2').show()
    //$('itemStartAction2').show()
    $('itemTime2').show()
    //$('itemEndAction2').show()
    $('categorySpan2').show()
    //$('itemPhoto2').show()
    $('itemValue2').show()
    $('itemCondition2').show()
    $('done2').show()
    //$('itemEndActionText2').show()
$('updater3').hide()
  $('itemValue3').hide()
    $('itemCondition3').hide()
    $('itemName3').hide()
    //$('itemStartAction3').hide()
    $('itemTime3').hide()
    $('itemDescription3').hide()
    //$('itemEndAction3').hide()
    $('itemCategory3').hide()
    //$('itemPhoto3').hide()


    return false
}

function retrieveRequests()
{
${remoteFunction(action:'listRequest', onSuccess:'listRequests(e)', params:'\'resident=\'+resident')}
      $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').show()
    $('itemdisplay').hide()
}

function searchResultText2(catlvlone)
{
    $('itemCategory2').value=catlvlone
    $('categorySearchSpan2').hide()
    $('categorySpan2').show()
    $('categoryShow2').hide()
}


function showSearchCategory2()
{
    $('categorySearchSpan2').show()
    $('categorySpan2').hide()
    $('categoryShow2').hide()
    $('changeCategory2').hide()
}

function showAllCategory2()
{
    $('categorySearchSpan2').hide()
    $('categorySpan2').hide()
    $('categoryShow2').show()
}

function showChangeCatButton2()
{
    $('changeCategory2').show()
    return false;
}

function changeActionHide()
{
  if($('itemStartAction').value=='Sell to rag and bone man')
    {
      $('durationActionSpan').hide()
    }
    else
    {
      $('durationActionSpan').show()
    }
}

function searchedResults(response)
{
var results = eval( '(' + response.responseText + ')' )
var html=""

if(results[0].length==0)
  {
    html+="<i>No product found for "+$('search').value+".</i>"
  }
else
  {
    for(var i=0; i<results[0].length; i++)
                      {
                        html+="<div style=\"float: left;margin:0 10px 10px 0\">"
                        html+="<div style=\"background-color: #f0f0f0;border: 1px solid #d5d5d5;vertical-align: middle;width:120px;height:90px;padding: 5px 5px 5px 5px;margin:0 0 5px 0\">"
                        html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0][i].id+"\'); return false;\">"
                          html+="<img src=\"/../../ProjectAmity/images/database/"+results[0][i].itemPhoto+"\" height=\"90\" width=\"120\" border=\"0\"/><br/>"
                          html+="</a>"
                          html+="</div>"

                          if(results[0][i].itemName.length>=17)
                            {
                              html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0][i].id+"\'); return false;\">"
                              html+=results[0][i].itemName.substr(0,17) + "...</a><br/>"
                            } else {
                              html+="<a href=\"#\" onclick=\"viewitem(\'"+results[0][i].id+"\'); return false;\">"
                              html+=results[0][i].itemName + "</a><br/>"
                            }

                          html+= results[0][i].itemStartAction

                            var sDate = new Date();
  var eDate = new Date(results[0][i].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  html+="<br/>"+ daysApart + " days left"

                          html+="<br/>$"+ results[0][i].itemValue
                          if(results[1][i].name.length>=17)
                            {
                              html+="<br/><a href=\"#\">"+ results[1][i].name.substr(0,17) + "...</a>"
                            } else {
                              html+="<br/><a href=\"#\">"+ results[1][i].name + "</a>"
                            }

                          //html+="<br/><a href=\"#\" onclick=\"createRequest(\'"+results[0][i].id+"\',\'"+results[1][i].id+"\',\'"+results[0][i].itemStartAction+"\'); return false;\">Request</a>"
                          html+="</div>"
                      }
  }




    $('itemSearchResultsPanel').innerHTML=html

    if(results.length>0)
      {
        var heightpx=results.length/6
        heightpx=Math.ceil(heightpx)*220
        heightpx=heightpx+'px'
    $('itemSearchResultsPanel').setStyle({
  height: heightpx
});
      }
}

function showSearchPanel()
{
  $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('categorySearchSpan').hide()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').hide()
    $('itemdisplay').hide()
    $('itemPictureDiv').hide();
    $('itemPeekture').show();
    $('itemSearch').show()
    $('searchOptions').hide()

    $('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()

}

function allShow()
{
  $('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}

function showSearchOptions()
{
  $('searchOptions').show()
  return false;
}

function categorySearcher(cat)
{
  if(cat=="All Categories"){$('cattype').value="All Categories"}

else if(cat=="Automotive"){$('cattype').value="Automotive"}
else if(cat=="Vehicles"){$('cattype').value="Vehicles"}
else if(cat=="Automotive Parts"){$('cattype').value="Automotive Parts"}
else if(cat=="Automotive Accessories"){$('cattype').value="Automotive Accessories"}

else if(cat=="Baby Care"){$('cattype').value="Baby Care"}
else if(cat=="Baby Clothes & Shoes"){$('cattype').value="Baby Clothes & Shoes"}
else if(cat=="Baby Food"){$('cattype').value="Baby Food"}
else if(cat=="Other Baby Care Products"){$('cattype').value="Other Baby Care Products"}

else if(cat=="Books & Stationery"){$('cattype').value="Books & Stationery"}
else if(cat=="Children's Books"){$('cattype').value="Children's Books"}
else if(cat=="Comics"){$('cattype').value="Comics"}
else if(cat=="Non-fiction"){$('cattype').value="Non-fiction"}
else if(cat=="Fiction Books"){$('cattype').value="Fiction Books"}
else if(cat=="Stationery"){$('cattype').value="Stationery"}

else if(cat=="Clothing, Shoes & Accessories"){$('cattype').value="Clothing, Shoes & Accessories"}
else if(cat=="Men's Clothes"){$('cattype').value="Men's Clothes"}
else if(cat=="Women's Clothes"){$('cattype').value="Women's Clothes"}
else if(cat=="Maternity Clothes"){$('cattype').value="Maternity Clothes"}
else if(cat=="Clothing Accessories "){$('cattype').value="Clothing Accessories "}
else if(cat=="Shoes"){$('cattype').value="Shoes"}

else if(cat=="Home & Garden"){$('cattype').value="Home & Garden"}
else if(cat=="Furniture"){$('cattype').value="Furniture"}
else if(cat=="Gardening & Plants"){$('cattype').value="Gardening & Plants"}
else if(cat=="Other Home & Gardening Items"){$('cattype').value="Other Home & Gardening Items"}

else if(cat=="Music & Multimedia"){$('cattype').value="Music & Multimedia"}
else if(cat=="Music"){$('cattype').value="Music"}
else if(cat=="Video"){$('cattype').value="Video"}
else if(cat=="Musical Instruments"){$('cattype').value="Musical Instruments"}
else if(cat=="Video Games"){$('cattype').value="Video Games"}

else if(cat=="Health & Beauty"){$('cattype').value="Health & Beauty"}
else if(cat=="Bath & Body"){$('cattype').value="Bath & Body"}
else if(cat=="Beauty Tools"){$('cattype').value="Beauty Tools"}
else if(cat=="Other Health & Beauty Items"){$('cattype').value="Other Health & Beauty Items"}

else if(cat=="Sports"){$('cattype').value="Sports"}
else if(cat=="Sporting Goods"){$('cattype').value="Sporting Goods"}

else if(cat=="Miscellaneous"){$('cattype').value="Miscellaneous"}
else if(cat=="All Other Items"){$('cattype').value="All Other Items"}

else if(cat=="Electronics"){$('cattype').value="Electronics"}
else if(cat=="Television & Monitors"){$('cattype').value="Television & Monitors"}
else if(cat=="Mobile Devices"){$('cattype').value="Mobile Devices"}
else if(cat=="Household Appliances"){$('cattype').value="Household Appliances"}
else if(cat=="Computers"){$('cattype').value="Computers"}
else if(cat=="Computer Peripherals"){$('cattype').value="Computer Peripherals"}
else if(cat=="Other electronic items"){$('cattype').value="Other electronic items"}

else if(cat=="Collectables"){$('cattype').value="Collectables"}
else if(cat=="Plushies"){$('cattype').value="Plushies"}
else if(cat=="Coins"){$('cattype').value="Coins"}
else if(cat=="Antiques"){$('cattype').value="Antiques"}
else if(cat=="Toys"){$('cattype').value="Toys"}
else if(cat=="Stamps"){$('cattype').value="Stamps"}
else if(cat=="Art"){$('cattype').value="Art"}
else if(cat=="Other Collectable items"){$('cattype').value="Other Collectable items"}

}

function showAutomotives(){
$('sea3').show()
$('sea4').show()
$('sea5').show()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showBabyCare(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').show()
$('sea8').show()
$('sea9').show()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showBooks(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').show()
$('sea12').show()
$('sea13').show()
$('sea14').show()
$('sea15').show()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showClothes(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').show()
$('sea18').show()
$('sea19').show()
$('sea20').show()
$('sea21').show()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showGarden(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').show()
$('sea24').show()
$('sea25').show()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showMusic(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').show()
$('sea28').show()
$('sea29').show()
$('sea30').show()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showHealth(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').show()
$('sea33').show()
$('sea34').show()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showSports(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').show()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showMiscellaneous(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').show()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showElectronics(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').show()
$('sea41').show()
$('sea42').show()
$('sea43').show()
$('sea44').show()
$('sea45').show()
$('sea47').hide()
$('sea48').hide()
$('sea49').hide()
$('sea50').hide()
$('sea51').hide()
$('sea52').hide()
$('sea53').hide()
}
function showCollectables(){
$('sea3').hide()
$('sea4').hide()
$('sea5').hide()
$('sea7').hide()
$('sea8').hide()
$('sea9').hide()
$('sea11').hide()
$('sea12').hide()
$('sea13').hide()
$('sea14').hide()
$('sea15').hide()
$('sea17').hide()
$('sea18').hide()
$('sea19').hide()
$('sea20').hide()
$('sea21').hide()
$('sea23').hide()
$('sea24').hide()
$('sea25').hide()
$('sea27').hide()
$('sea28').hide()
$('sea29').hide()
$('sea30').hide()
$('sea32').hide()
$('sea33').hide()
$('sea34').hide()
$('sea36').hide()
$('sea38').hide()
$('sea40').hide()
$('sea41').hide()
$('sea42').hide()
$('sea43').hide()
$('sea44').hide()
$('sea45').hide()
$('sea47').show()
$('sea48').show()
$('sea49').show()
$('sea50').show()
$('sea51').show()
$('sea52').show()
$('sea53').show()
}

function updateHidden()
{
  $('searchOptionsCon').innerHTML="Current Options: <b>Results Type:</b> "+$('restype').value+" | <b>Sort By: </b>"+$('sorttype').value+" | <b>Condition:</b> "+$('contype').value+" | <b>Estimated Value:</b> "+$('valuetype').value+" | <b>Category:</b> "+$('cattype').value
}
    </script>

    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
    <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />

  </head>
  <body class="thrColFixHdr"  onload="loadHide()">

    <div class="wrapper">

      <div id="container">
        <a href="${createLink(controller: 'resident', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" border="0" id="logo"/></a>
        <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
        <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
        <a href="${createLink(controller: 'resident', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'home.png')}" border="0" id="home"/></a>
        <a href="${createLink(controller: 'report', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'report.png')}" border="0" id="report"/></a>
        <a href="${createLink(controller: 'carpoolListing', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'carpool.png')}" border="0" id="carpool"/></a>
        <a href="${createLink(controller: 'barter', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'barter.png')}" border="0" id="barter"/></a>
        <a href="${createLink(controller: 'barter', action:'index')}" >
          <img src="${resource(dir:'images/amity',file:'bbarter.png')}" border="0" id="pageTitle"/></a>

        <a href="#" onmouseover="$('tab1dark').appear({ duration: 0.2 });" onclick="loadyouritems(); return false;">
          <img src="${resource(dir:'images/amity',file:'tab1light.png')}" border="0" id="tab1light"/></a>
        <a href="#" onmouseover="$('tab2dark').appear({ duration: 0.2 });showMiniCategory();" onmouseout="hideMiniCategory()" onclick="">
          <img src="${resource(dir:'images/amity',file:'tab2light.png')}" border="0" id="tab2light"/></a>
        <a href="#" onmouseover="$('tab3dark').appear({ duration: 0.2 });" onclick="">
          <img src="${resource(dir:'images/amity',file:'tab3light.png')}" border="0" id="tab3light"/></a>
        <a href="#" onmouseover="$('tab4dark').appear({ duration: 0.2 });" onclick="">
          <img src="${resource(dir:'images/amity',file:'tab4light.png')}" border="0" id="tab4light"/></a>

        <a href="#" onmouseover="" onmouseout="$('tab1dark').fade({ duration: 0.2 });" onclick="loadyouritems(); return false;">
          <img src="${resource(dir:'images/amity',file:'tab1dark.png')}" border="0" id="tab1dark"/></a>
        <a href="#" onmouseover="" onmouseout="$('tab2dark').fade({ duration: 0.2 });hideMiniCategory();" onclick="return false;">
          <img src="${resource(dir:'images/amity',file:'tab2dark.png')}" border="0" id="tab2dark"/></a>
        <a href="#" onmouseover="" onmouseout="$('tab3dark').fade({ duration: 0.2 });" onclick="retrieveRequests(); return false;">
          <img src="${resource(dir:'images/amity',file:'tab3dark.png')}" border="0" id="tab3dark"/></a>
        <a href="#" onmouseover="" onmouseout="$('tab4dark').fade({ duration: 0.2 });" onclick="createitemshow(); return false;">
          <img src="${resource(dir:'images/amity',file:'tab4dark.png')}" border="0" id="tab4dark"/></a>


        <div id="categoryitem" onmouseout="$('categoryitem').hide()" onmouseover="showMiniCategory()">
          <table onmouseover="showMiniCategory()" width="470" height="150" style="margin:0px 0px 0px 40px;position:absolute;">
            <tr>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Automotive']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Automotive
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Baby Care']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Baby Care
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Books & Stationery']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Books &amp; Stationery
                  </g:remoteLink></h4></td>
            </tr>
            <tr>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Clothing, Shoes & Accessories']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Clothing, Shoes &amp; Accessories
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Home & Garden']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Home &amp; Garden
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Music & Multimedia']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Music &amp; Multimedia
                  </g:remoteLink></h4></td>
            </tr>
            <tr>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Health & Beauty']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Health &amp; Beauty
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Sports']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Sports
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Miscellaneous']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Miscellaneous
                  </g:remoteLink></h4></td>
            </tr>
            <tr>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Electronics']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Electronics
                  </g:remoteLink></h4></td>
              <td><h4><g:remoteLink value = "post"
                                    params="[items:'Collectables']"
                                    action="list"
                                    controller="barter"
                                    onSuccess = "viewallitems(e)"
                                    onLoading = "toggleObj('spinner2')"
                                    onComplete = "toggleObj('spinner2')">
                    Collectables
                  </g:remoteLink></h4></td>
              <td><a href="#" onclick="showSearchPanel();return false;" ><b>Search</b></a></td>
            </tr>
          </table>
        </div>

        <div id="header">
          <h1>test</h1>
          <!-- end #header --></div>
        <div id="banner">&nbsp;</div>
        <div id="navi">&nbsp; You are here: Testing
          <span id="navi2"><a href="${createLink(controller: 'message', action:'index')}"><img src="${resource(dir:'images/amity',file:'mail.png')}" border="0"/><span style="vertical-align:top;" >Message</span></a><a href="${createLink(controller: 'resident', action:'residentLogout')}" ><img src="${resource(dir:'images/amity',file:'logout.png')}" border="0"/><span style="vertical-align:top;" >Logout</span></a></span>
        </div>

        <div id="mainContent">

          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->

          <!--<h2>Barter Application</h2>
          <span onclick="loadyouritems(); return false;"><a href="">Your items</a></span>
          <span onmouseout="hideMiniCategory()" onmouseover="showMiniCategory();return false;"><a href="">Categories</a></span>
          <span onclick="retrieveRequests(); return false;"><a href="">Requests</a></span>
          <span onclick="createitemshow(); return false;"><a href="">Create items</a></span>-->

          <!--Create item here -->

          <div id="createbarteritem">


            <g:uploadForm id="createForm" >
              <g:hiddenField name="resident" value="${session.user.id}" id="resident"/>
              <g:hiddenField name="itemPhoto" value="" id="itemPhoto"/>

              <table width="900">
                <tr>
                  <td colspan="4" height="47"><h2>Add New Barter Listing</h2></td>
                </tr>
                <tr>
                  <td width="196" rowspan="7" align="center">
                    <div style="background-color: #f0f0f0; width:190px;height:190px;border: 1px solid #d5d5d5;line-height:40px;" id="itemPeekture" >
                      <br/><br/>

                      <a onclick="Modalbox.show('/../ProjectAmity/barter/uploadPhoto.gsp', {title: this.title, overlayClose: false, afterHide: function() { $('itemPictureDiv').show();$('itemPeekture').hide();$('itemPhoto').value=transfer; $('itemPicture').src='/../../ProjectAmity/images/database/'+transfer.replace('C:\\fakepath\\',''); if(transfer=='') {$('itemPictureDiv').hide();$('itemPeekture').show()} } });return false;" title="Upload a photo" href="#">Upload a photo</a>
                    </div>
                    <div id="itemPictureDiv" style="background-color: #f0f0f0; width:190px;height:190px;border: 1px solid #d5d5d5;line-height:40px;">
                      <img id="itemPicture" src="" width="190"  height="190" ></img>
                    </div>
                  </td>
                  <td width="240" align="right" valign="top"><!--What you want to do with the item :--></td>
                  <td width="304">&nbsp;<g:select style="width:268px;" onchange="changeActionHide()"  name="itemStartAction" from="${['Trade with items', 'Selling', 'Sell to rag and bone man', 'Give away', 'Create wishlist']}" noSelection="['Trade with items':'Trade with items']"/></td>
                  <td width="150">&nbsp;</td>
                </tr>
                <tr>
                  <td  align="right" valign="top">Name of item :</td>
                  <td>&nbsp;<g:textField name="itemName" style="width:263px;"/></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="right" valign="top">Category of item :</td>
                  <td colspan="2"><span id="categorySpan">
                      &nbsp;<g:textField style="width:263px;" name="itemCategory" onkeydown="return showChangeCatButton();"/>
                      <input id="changeCategory" type="button" value="Change category?" onclick="showSearchCategory()"/>
                    </span>
                    <span id="categorySearchSpan">
                      &nbsp;<g:textField name="itemCategorySearch"  style="width:193px;"/>
                      <g:submitToRemote value="Search" url="[controller: 'barter', action: 'searchcategory']"
                                        onSuccess="searchResults(e)"
                                        onLoading="hideError();toggleObj('spinner1')"
                                        onComplete="toggleObj('spinner1')" />
                      <br/><a href="#" onclick="showAllCategory(); return false;">Show all categories</a>
                      <div id="searchResults"></div>
                    </span>
                    <span id="categoryShow">
                      &nbsp;<table border="1">
                        <tr>
                          <td><h4>Automotive</h4></td>
                          <td><h4>Baby Care</h4></td>
                          <td><h4>Books &amp; Stationery</h4></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Vehicles'); return false;">Vehicles</a></td>
                          <td><a href="#" onclick="searchResultText('Baby Clothes & Shoes'); return false;">Baby Clothes &amp; Shoes</a></td>
                          <td><a href="#" onclick="searchResultText('Children\'s Books'); return false;">Children's Books</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Automotive Parts'); return false;">Automotive Parts</a></td>
                          <td><a href="#" onclick="searchResultText('Baby Food'); return false;">Baby Food</a></td>
                          <td><a href="#" onclick="searchResultText('Comics'); return false;">Comics</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Automotive Accessories'); return false;">Automotive Accessories</a></td>
                          <td><a href="#" onclick="searchResultText('Other Baby Care Products'); return false;">Other Baby Care Products</a></td>
                          <td><a href="#" onclick="searchResultText('Non-fiction'); return false;">Non-fiction</a></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td><a href="#" onclick="searchResultText('Fiction Books'); return false;">Fiction Books</a></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td><a href="#" onclick="searchResultText('Stationery'); return false;">Stationery</a></td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><h4>Clothing, Shoes &amp; Accessories</h4></td>
                          <td><h4>Home &amp;Garden</h4></td>
                          <td><h4>Music &amp; Multimedia</h4></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Men\' Clothes'); return false;">Men's Clothes</a></td>
                          <td><a href="#" onclick="searchResultText('Furniture'); return false;">Furniture</a></td>
                          <td><a href="#" onclick="searchResultText('Music'); return false;">Music</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Women\'s Clothes'); return false;">Women's Clothes</a></td>
                          <td><a href="#" onclick="searchResultText('Gardening & Plants'); return false;">Gardening &amp; Plants</a></td>
                          <td><a href="#" onclick="searchResultText('Video'); return false;">Video</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Maternity Clothes'); return false;">Maternity Clothes</a></td>
                          <td><a href="#" onclick="searchResultText('Other Home & Gardening Items'); return false;">Other Home &amp; Gardening Items</a></td>
                          <td><a href="#" onclick="searchResultText('Musical Instruments'); return false;">Musical Instruments</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Clothing Accessories'); return false;">Clothing Accessories </a></td>
                          <td>&nbsp;</td>
                          <td><a href="#" onclick="searchResultText('Video Games'); return false;">Video Games</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Shoes'); return false;">Shoes</a></td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><h4>Health &amp; Beauty</h4></td>
                          <td><h4>Sports</h4></td>
                          <td><h4>Miscellaneous</h4></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Bath & Body'); return false;">Bath &amp; Body</a></td>
                          <td><a href="#" onclick="searchResultText('Sporting Goods'); return false;">Sporting Goods</a></td>
                          <td><a href="#" onclick="searchResultText('All other Items'); return false;">All Other Items</a></td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Beauty Tools'); return false;">Beauty Tools</a></td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Other Health & Beauty Items'); return false;">Other Health &amp; Beauty Items</a></td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><h4>Electronics</h4></td>
                          <td><h4>Collectables</h4></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Television'); return false;">Television &amp; Monitors</a></td>
                          <td><a href="#" onclick="searchResultText('Plushies'); return false;">Plushies</a></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Mobile Devices'); return false;">Mobile Devices</a></td>
                          <td><a href="#" onclick="searchResultText('Coins'); return false;">Coins</a></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Household Appliances'); return false;">Household Appliances</a></td>
                          <td><a href="#" onclick="searchResultText('Antiques'); return false;">Antiques</a></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Computers'); return false;">Computers</a></td>
                          <td><a href="#" onclick="searchResultText('Toys'); return false;">Toys</a></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Computer Peripherals'); return false;">Computer Peripherals</a></td>
                          <td><a href="#" onclick="searchResultText('Stamps'); return false;">Stamps</a></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td><a href="#" onclick="searchResultText('Other electronic items'); return false;">Other electronic items</a></td>
                          <td><a href="#" onclick="searchResultText('Art'); return false;">Art</a></td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td>&nbsp;</td>
                          <td><a href="#" onclick="searchResultText('Other Collectable items'); return false;">Other Collectable items</a></td>
                          <td>&nbsp;</td>
                        </tr>
                      </table>
                    </span></td>

                </tr>
                <tr>
                  <td align="right" valign="top">Condition of item :</td>
                  <td>&nbsp;<g:select name="itemCondition" style="width:268px;" from="${['Completely new', 'Used before and everything working', 'Used before and some parts not working']}" noSelection="['null':'-Choose item condition-']"/></td>
                  <td>&nbsp;</td>
                </tr>

                <tr>
                  <td align="right" valign="top">Estimated value :</td>
                  <td>&nbsp;<g:textField style="width:263px;" name="itemValue" onkeydown="return checkKeycode(event)"/></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="right" valign="top">Short description of item :</td>
                  <td>&nbsp;<g:textArea style="width:263px;font: 14px 'Segoe UI'" name="itemDescription" /></td>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="right" valign="top">Duration of listing of item :</td>
                  <td>&nbsp;<g:select style="width:268px;" name="itemTime" from="${['2 days', '3 days', '4 days', '5 days', '6 days', '7 days']}" noSelection="['7 days':'7 days']"/></td>
                  <td>&nbsp;</td>
                </tr>
                <td colspan="3" align="left">
                  <div style="width:713px; text-align: right;">
                    <g:submitToRemote value="Create" url="[controller: 'barter', action: 'create']"
                                      onSuccess="viewyouritems(e);"
                                      onLoading="hideError();toggleObj('spinner1')"
                                      onComplete="toggleObj('spinner1')" />
                  </div>
                </td>
                <td>&nbsp;</td>
              </table>
            </g:uploadForm>
          </div>


          <div id="yourlistitem">
          </div>

          <div id="listitem">

          </div>

          <div id="requestpanel">


          </div>

          <div id="itemdisplay">

          </div>

          <div id="itemSearch">
            <form id="searchingForm">



              <table width="900">
                <tr>
                  <td colspan="5" align="left"><h2>Search for items</h2></td>
                </tr>
                <tr>
                  <td colspan="5">
                    <g:textField name="search"/>
                    <g:submitToRemote value="Search" url="[controller: 'barter', action: 'search']"
                                      onSuccess="searchedResults(e);"
                                      onLoading="hideError();toggleObj('spinner1')"
                                      onComplete="toggleObj('spinner1')" />
                  </td>
                </tr>
                <tr>
                  <td colspan="4"><a href="#" onclick="showSearchOptions(); updateHidden();return false;">Show Search Options</a></td>
                  <td align="right">&nbsp;</td>
                </tr>
<tr>
                  <td colspan="5"><hr/></td></tr>
                <tr>
                  <td colspan="5">

                <div id="searchOptions">
                  <table width="900">
<tr><td colspan="5">
<div id="searchOptionsCon" style="background-color: #f0f0f0;border: 1px solid #d5d5d5;padding:5px">
Current Options: <b>Results Type:</b> All | <b>Sort By:</b> Relevance | <b>Condition:</b> All | <b>Estimated Value:</b> All | <b>Category:</b> All Categories
</div></td></tr>
                <tr>
                  <td width="131">Result type: </td>
                  <td width="103">Sort by:</td>
                  <td width="208">Condition:</td>
                  <td width="189">Estimated value:</td>
                  <td width="245">Category: </td>
                </tr>
                <tr>
                  <td valign="top">
                    <span id="res1" onclick="$('restype').value='All';updateHidden();return false;"><a href="#">All</a></span>
                    <br/><span id="res2" onclick="$('restype').value='Trading';updateHidden();return false;"><a href="#">Trading</a></span>
                    <br/><span id="res3" onclick="$('restype').value='Selling';updateHidden();return false;"><a href="#">Selling</a></span>
                    <br/><span id="res4" onclick="$('restype').value='Give aways';updateHidden();return false;"><a href="#">Give aways</a></span>
                    <br/><span id="res5" onclick="$('restype').value='Wishlists';updateHidden();return false;"><a href="#">Wishlists</a></span>
                  </td>
                  <td valign="top">
                    <a href="#" onclick="$('sorttype').value='Relevance';updateHidden();return false;">Relevance</a>
                    <br/><a href="#" onclick="$('sorttype').value='Posted date';updateHidden();return false;">Posted date</a>
                    <br/><a href="#" onclick="$('sorttype').value='Estimated value';updateHidden();return false;">Estimated value</a>
                    <br/><a href="#" onclick="$('sorttype').value='Alphabetical';updateHidden();return false;">Alphabetical</a>
                    <br/><a href="#" onclick="$('sorttype').value='Reverse Alphabetical';updateHidden();return false;">Reverse Alphabetical</a>
                  </td>
                  <td valign="top">
<a href="#"onclick="$('contype').value='All';updateHidden();return false;">All</a>
                    <br/><a href="#"onclick="$('contype').value='Completely new';updateHidden();return false;">Completely new</a>
                    <br/><a href="#"onclick="$('contype').value='Used before and everything working';updateHidden();return false;">Used before and everything working</a>
                    <br/><a href="#"onclick="$('contype').value='Used before and some parts not working';updateHidden();return false;">Used before and some parts not working</a>
                  </td>
                  <td valign="top">
<a href="#"onclick="$('valuetype').value='All';updateHidden();return false;">All</a>
                    <br/>More than:<g:textField name="morethan" style="width:50px;margin:0 0 0 3px;"/> <a href="#"onclick="$('valuetype').value=''; $('valuetype').value+='More than ';$('valuetype').value+=$('morethan').value;updateHidden();return false;">Go</a>
                    <br/>Less than: <g:textField name="lessthan" style="width:50px;margin:0 0 0 5px;"/> <a href="#"onclick="$('valuetype').value=''; $('valuetype').value+='Less than ';$('valuetype').value=$('lessthan').value;updateHidden();return false;">Go</a>
<br/><i>(Fill in both boxes for <br/>a between value)</i>
                  </td>
                  <td valign="top">

<span id="sea1" onclick="categorySearcher('All Categories'); allShow(); updateHidden();return false;"><a href="#"/>All Categories</a></span>

<span id="sea2" onclick="categorySearcher('Automotive'); showAutomotives(); updateHidden();return false;"><br/><a href="#"/>Automotive</a></span>
<span id="sea3" onclick="categorySearcher('Vehicles'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Vehicles</a></span>
<span id="sea4" onclick="categorySearcher('Automotive Parts'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Automotive Parts</a></span>
<span id="sea5" onclick="categorySearcher('Automotive Accessories'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Automotive Accessories</a></span>

<span id="sea6" onclick="categorySearcher('Baby Care'); showBabyCare(); updateHidden();return false;"><br/><a href="#"/>Baby Care</a></span>
<span id="sea7" onclick="categorySearcher('Baby Clothes & Shoes'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Baby Clothes & Shoes</a></span>
<span id="sea8" onclick="categorySearcher('Baby Food'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Baby Food</a></span>
<span id="sea9" onclick="categorySearcher('Other Baby Care Products'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Other Baby Care Products</a></span>

<span id="sea10" onclick="categorySearcher('Books & Stationery'); showBooks(); updateHidden();return false;"><br/><a href="#"/>Books & Stationery</a></span>
<span id="sea11" onclick="categorySearcher('Children's Books'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Children's Books</a></span>
<span id="sea12" onclick="categorySearcher('Comics'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Comics</a></span>
<span id="sea13" onclick="categorySearcher('Non-fiction'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Non-fiction</a></span>
<span id="sea14" onclick="categorySearcher('Fiction Books'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Fiction Books</a></span>
<span id="sea15" onclick="categorySearcher('Stationery'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Stationery</a></span>

<span id="sea16" onclick="categorySearcher('Clothing, Shoes & Accessories'); showClothes(); updateHidden();return false;"><br/><a href="#"/>Clothing, Shoes & Accessories</a></span>
<span id="sea17" onclick="categorySearcher('Men's Clothes'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Men's Clothes</a></span>
<span id="sea18" onclick="categorySearcher('Women's Clothes'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Women's Clothes</a></span>
<span id="sea19" onclick="categorySearcher('Maternity Clothes'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Maternity Clothes</a></span>
<span id="sea20" onclick="categorySearcher('Clothing Accessories '); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Clothing Accessories </a></span>
<span id="sea21" onclick="categorySearcher('Shoes'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Shoes</a></span>

<span id="sea22" onclick="categorySearcher('Home & Garden'); showGarden(); updateHidden();return false;"><br/><a href="#"/>Home & Garden</a></span>
<span id="sea23" onclick="categorySearcher('Furniture'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Furniture</a></span>
<span id="sea24" onclick="categorySearcher('Gardening & Plants'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Gardening & Plants</a></span>
<span id="sea25" onclick="categorySearcher('Other Home & Gardening Items'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Other Home & Gardening Items</a></span>

<span id="sea26" onclick="categorySearcher('Music & Multimedia'); showMusic(); updateHidden();return false;"><br/><a href="#"/>Music & Multimedia</a></span>
<span id="sea27" onclick="categorySearcher('Music'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Music</a></span>
<span id="sea28" onclick="categorySearcher('Video'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Video</a></span>
<span id="sea29" onclick="categorySearcher('Musical Instruments'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Musical Instruments</a></span>
<span id="sea30" onclick="categorySearcher('Video Games'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Video Games</a></span>

<span id="sea31" onclick="categorySearcher('Health & Beauty'); showHealth(); updateHidden();return false;"><br/><a href="#"/>Health & Beauty</a></span>
<span id="sea32" onclick="categorySearcher('Bath & Body'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Bath & Body</a></span>
<span id="sea33" onclick="categorySearcher('Beauty Tools'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Beauty Tools</a></span>
<span id="sea34" onclick="categorySearcher('Other Health & Beauty Items'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Other Health & Beauty Items</a></span>

<span id="sea35" onclick="categorySearcher('Sports'); showSports(); updateHidden();return false;"><br/><a href="#"/>Sports</a></span>
<span id="sea36" onclick="categorySearcher('Sporting Goods'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Sporting Goods</a></span>

<span id="sea37" onclick="categorySearcher('Miscellaneous'); showMiscellaneous(); updateHidden();return false;"><br/><a href="#"/>Miscellaneous</a></span>
<span id="sea38" onclick="categorySearcher('All Other Items'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;All Other Items</a></span>

<span id="sea39" onclick="categorySearcher('Electronics'); showElectronics(); updateHidden();return false;"><br/><a href="#"/>Electronics</a></span>
<span id="sea40" onclick="categorySearcher('Television & Monitors'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Television & Monitors</a></span>
<span id="sea41" onclick="categorySearcher('Mobile Devices'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Mobile Devices</a></span>
<span id="sea42" onclick="categorySearcher('Household Appliances'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Household Appliances</a></span>
<span id="sea43" onclick="categorySearcher('Computers'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Computers</a></span>
<span id="sea44" onclick="categorySearcher('Computer Peripherals'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Computer Peripherals</a></span>
<span id="sea45" onclick="categorySearcher('Other electronic items'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Other electronic items</a></span>

<span id="sea46" onclick="categorySearcher('Collectables'); showCollectables(); updateHidden();return false;"><br/><a href="#"/>Collectables</a></span>
<span id="sea47" onclick="categorySearcher('Plushies'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Plushies</a></span>
<span id="sea48" onclick="categorySearcher('Coins'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Coins</a></span>
<span id="sea49" onclick="categorySearcher('Antiques'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Antiques</a></span>
<span id="sea50" onclick="categorySearcher('Toys'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Toys</a></span>
<span id="sea51" onclick="categorySearcher('Stamps'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Stamps</a></span>
<span id="sea52" onclick="categorySearcher('Art'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Art</a></span>
<span id="sea53" onclick="categorySearcher('Other Collectable items'); updateHidden();return false;"><br/><a href="#"/>&nbsp;&nbsp;&nbsp;&nbsp;&raquo;Other Collectable items</a></span>


</td>
                </tr>
<td colspan="5"><hr/>
<a href="#" onclick="$('searchOptions').hide(); updateHidden();return false;">Hide Options</a></td></tr>
                <tr>
                  <td colspan="5">
                    </table>
                </div>

                    </td>
                </tr>

                <tr>
                  <td colspan="5">&nbsp;
                    <div id="itemSearchResultsPanel"></div>
                  </td>
                </tr>
              </table>

              <g:hiddenField name="restype" value="All" />
<g:hiddenField name="cattype" value="All Categories" />
<g:hiddenField name="sorttype" value="Relevance" />
<g:hiddenField name="contype" value="All" />
<g:hiddenField name="valuetype" value="All" />

            </form>

          </div>


          <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->
        </div>
        <!-- This clearing element should immediately follow the #mainContent div in order to force the #container div to contain all child floats --><br class="clearfloat" />
        <!-- end #container --></div>

      <div class="push"></div>

    </div>

    <div class="footer">
      <p>Copyright &copy; 2010 Team Smiley Face</p>
    </div>
  </body>
</html>