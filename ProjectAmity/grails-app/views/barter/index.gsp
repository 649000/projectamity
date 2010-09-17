<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
	<head>


        <title>Define Your Carpool Listing</title>

        <g:javascript library="scriptaculous" />
        <g:javascript library="prototype" />

<script type="text/javascript">
  var resident='hairiandy'
    function loadHide()
{
    $('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('createbarteritem').hide()
    $('listitem').hide()
    $('requestpanel').show()
    
    ${remoteFunction(action:'listRequest', onSuccess:'listRequests(e)', params:'\'resident=\'+resident')}
}
function viewyouritems(response)
{
var results = eval( '(' + response.responseText + ')' )
                    var html=""
                    for(var i=0; i<results.length; i++)
                      {
                        html+="<div style=\"border: 1px solid #000; width: 130px; float: left;\">"
                          html+="<img src=\"../images/database/"+results[i].itemPhoto+"\" height=\"90\" width=\"120\"/><br/>"
                          html+="<b><a href=\"#\" onclick=\"viewyouritem(\'"+results[i].id+"')\">"+ results[i].itemName+ "</a></b>"
                          html+="<br/><a href=\"#\" onclick=\"viewyouritem(\'"+results[i].id+"'); return false;\">Request</a>"
                          html+="<br/>"+ results[i].itemStartAction

                            var sDate = new Date();
  var eDate = new Date(results[i].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  html+="<br/>"+ daysApart + " days left"
                          html+="<br/>$"+ results[i].itemValue
                          html+="</div>"
                      }
                      $('yourlistitem').innerHTML=html
}

function viewallitems(response)
{
var results = eval( '(' + response.responseText + ')' )
                    var html=""
                    for(var i=0; i<results.length; i++)
                      {
                        html+="<div style=\"border: 1px solid #000; width: 130px; float: left; \">"
                          html+="<img src=\"../images/database/"+results[i].itemPhoto+"\" height=\"90\" width=\"120\"/><br/>"
                          html+="<b><a href=\"#\">"+ results[i].itemName+ "</a></b>"
                          html+="<br/>"+ results[i].itemStartAction

                            var sDate = new Date();
  var eDate = new Date(results[i].itemEndDate);
  var daysApart = Math.abs(Math.round((sDate-eDate)/86400000));
  html+="<br/>"+ daysApart + " days left"
                          html+="<br/>$"+ results[i].itemValue
                          html+="<br/><a href=\"#\">"+ results[i].resident + "</a>"
                          html+="<br/><a href=\"#\" onclick=\"createRequest(\'"+results[i].id+"\',\'"+results[i].resident+"\',\'"+results[i].itemStartAction+"\'); return false;\">Request</a>"
                          html+="</div>"
                      }


    $('listitem').innerHTML=html

    $('yourlistitem').hide()
    $('createbarteritem').hide()
    $('listitem').show()
}

function createRequest(itemid, traderid, startAction)
{
  //username here
  var a='1'
  var b=traderid
  var c=itemid
  var d=startAction
  ${remoteFunction(action:'createRequest', onSuccess:'asdfgasd(e)', params:'\'partyone=\'+a+\'&partytwo=\'+b+\'&involvedItems=\'+c+\'&barterAction=\'+d+\'\'')}
}

function listRequests(response)
{
  var results = eval( '(' + response.responseText + ')' )
                    var html=""
                    for(var i=0; i<results.length; i++)
                      {
                      html+=results[i].partyone
                      html+=" has requested to "
                      if(results[i].barterAction=="Trade with items")
                        {
                          html+=' trade items with you.'
                        }
                        else if(results[i].barterAction=="Give away")
                        {
                          html+=' receive your item.'
                        }
                        html+="<a href=\"#\">Accept</a> "
                        html+="<a href=\"#\">Reject</a> "
                        html+="<br/>"
                      }
                      $('requestpanel').innerHTML=html
}

function viewyouritem(asdf)
{
alert(asdf)
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

function showMiniCategory()
{
    $('categoryitem').show()
}

function hideMiniCategory()
{
    $('categoryitem').hide()
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
}
function createitemshow()
{
$('categorySpan').hide()
    $('categoryShow').hide()
    $('changeCategory').hide()
    $('categoryitem').hide()
    $('yourlistitem').hide()
    $('createbarteritem').show()
    $('listitem').hide()
    $('requestpanel').hide()
}

function viewyouritem()
{

}

  </script>

		<meta http-equiv="content-type" content="text/html; charset=utf-8" />

                <link rel="stylesheet" href="${resource(dir:'css',file:'layout.css')}" />
                <link rel="stylesheet" href="${resource(dir:'css',file:'style.css')}" />

	</head>
<body class="thrColFixHdr"  onload="loadHide()">

		<div class="wrapper">

			<div id="container">
            <img src="${resource(dir:'images/amity',file:'logo3.PNG')}" id="logo"/>
            <img src="${resource(dir:'images/amity',file:'header.png')}" id="headerIMG"/>
            <img src="${resource(dir:'images/amity',file:'bg.jpg')}" id="background"/>
            <img src="${resource(dir:'images/amity',file:'home.png')}" id="home"/>
            <img src="${resource(dir:'images/amity',file:'report.png')}" id="report"/>
            <img src="${resource(dir:'images/amity',file:'carpool.png')}" id="carpool"/>
            <img src="${resource(dir:'images/amity',file:'barter.png')}" id="barter"/>
            <img src="${resource(dir:'images/amity',file:'bcarpool.png')}" id="pageTitle"/>
  <div id="header">
    <h1>test</h1>
  <!-- end #header --></div>
  <div id="banner">&nbsp;</div>
  <div id="navi">&nbsp; You are here: Testing</div>
  <div id="mainContent" style="height: 400px;">

  <!--CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE CONTENT HERE  -->


<h2>Barter Application</h2>

  <span onclick="loadyouritems(); return false;"><a href="">Your items</a></span>
  <span onmouseout="hideMiniCategory()" onmouseover="showMiniCategory();return false;"><a href="">Categories</a></span>
  <span onmouseout="retrieveRequests()"><a href="">Requests</a></span>
  <span onclick="createitemshow(); return false;"><a href="">Create items</a></span>

  <!--Create item here -->

  <div id="createbarteritem" style="border: 1px solid #000;">
    <g:uploadForm>
      Item action: <g:select name="itemStartAction" from="${['Trade with items', 'Selling', 'Sell to rag and bone man', 'Give away', 'Create wishlist']}" noSelection="['null':'-Choose item action-']"/>
      <br/>Name of item: <g:textField name="itemName"/>
      <br/>Item value: <g:textField name="itemValue" onkeydown="return checkKeycode(event)"/>
      <br/>Item condition:  <g:select name="itemCondition" from="${['Completely new', 'Used before and everything working', 'Used before and some parts not working']}" noSelection="['null':'-Choose item condition-']"/>
      <span id="categorySpan">
        <br/>Item category: <g:textField name="itemCategory" onkeydown="return showChangeCatButton();"/>
        <input id="changeCategory" type="button" value="Change category?" onclick="showSearchCategory()"/>
      </span>
      <span id="categorySearchSpan">
        <br/>Item category: <g:textField name="itemCategorySearch"/>
        <g:submitToRemote value="Search" url="[controller: 'barter', action: 'searchcategory']"
                          onSuccess="searchResults(e)"
                          onLoading="hideError();toggleObj('spinner1')"
                          onComplete="toggleObj('spinner1')" />
        <a href="#" onclick="showAllCategory()">Show all categories</a>
        <div id="searchResults"></div>
      </span>
      <span id="categoryShow">
        <table border="1">
          <tr>
            <td><h4>Automotive</h4></td>
            <td><h4>Baby Care</h4></td>
            <td><h4>Books &amp; Stationery</h4></td>
          </tr>
          <tr>
            <td><a href="#" onclick="searchResultText('Vehicles'); return false;">Vehicles</a></td>
            <td><a href="#" onclick="searchResultText('Baby Clothes'); return false;">Baby Clothes &amp; Shoes</a></td>
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
            <td><a href="#" onclick="searchResultText('Computer, IT , Internet'); return false;">Computer, IT , Internet</a></td>
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
            <td><a href="#" onclick="searchResultText('Men\' Clothings'); return false;">Men's Clothes</a></td>
            <td><a href="#" onclick="searchResultText('Furniture'); return false;">Furniture</a></td>
            <td><a href="#" onclick="searchResultText('Music'); return false;">Music</a></td>
          </tr>
          <tr>
            <td><a href="#" onclick="searchResultText('Women\'s Clothings'); return false;">Women's Clothes</a></td>
            <td><a href="#" onclick="searchResultText('Garderning'); return false;">Gardening &amp; Plants</a></td>
            <td><a href="#" onclick="searchResultText('Video'); return false;">Video</a></td>
          </tr>
          <tr>
            <td><a href="#" onclick="searchResultText('Maternity Clothes'); return false;">Maternity Clothes</a></td>
            <td><a href="#" onclick="searchResultText('Other Home & Garderning Items'); return false;">Other Home &amp; Gardening Items</a></td>
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
      </span>
      <br/>Item description: <g:textArea name="itemDescription"/>
      <br/>Duration of item listing: <g:textField name="itemTime"/>
      <br/>Action to do after expiry: <g:select name="itemEndAction" from="${['Trade with items', 'Selling', 'Sell to rag and bone man', 'Give away', 'Create wishlist']}" noSelection="['null':'-Choose item action-']"/>
      <br/>Item photo: <g:textField name="itemPhoto"/>
      <br/><g:submitToRemote value="Create" url="[controller: 'barter', action: 'create']"
                             onSuccess="updateFields(e)"
                             onLoading="hideError();toggleObj('spinner1')"
                             onComplete="toggleObj('spinner1')" />
    </g:uploadForm>
  </div>

  <div id="categoryitem" style="border: 1px solid #000;float: left;position: absolute;left: 275px;top: 300px;background: white;" onmouseout="hideMiniCategory()" onmouseover="showMiniCategory()">
    |
    <table onmouseover="showMiniCategory()">
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
        <td>&nbsp;</td>
      </tr>
    </table>
  </div>

  <div id="yourlistitem" style="border: 1px solid #000;">
  </div>

  <div id="listitem" style="border: 1px solid #000;">

  </div>

  <div id="requestpanel" style="border: 1px solid #000;">

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