/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function checkLogin(response)
{
    var nric = $F("nric");
    var password = $F("password");
    var temp = response.responseText

    if(nric != '' || password != '')
    {
        var loginStatus = temp.toString().split("|")
        if (loginStatus[0] =="Success Resident")
        {
        
            if(loginStatus[1] =="new")
            {
                window.location="/ProjectAmity/resident/new.gsp";
            }else if (loginStatus[1] == "existing")
            {
                window.location="/ProjectAmity/report/";
            }
        }
        else if (temp =="Success NEA")
        {
            //alert("Success")

            window.location="/ProjectAmity/NEAOfficer/";
        }
        else if (temp == "Invalid NRIC / Password Combination")
        {
            alert("Invalid NRIC / Password Combination")
        }
    }
    else
    {
        alert("Login ID or Password Cannot be blank")
    }
}
