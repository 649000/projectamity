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
        if (temp =="Success Resident")
        {
            //alert("Success")
            
            window.location="/ProjectAmity/report/";
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