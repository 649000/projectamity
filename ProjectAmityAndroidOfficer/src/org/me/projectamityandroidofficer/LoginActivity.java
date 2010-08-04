/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpConnection;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author student
 */
public class LoginActivity extends Activity {

    private String iPAddress = "152.226.232.99";
    private String loginURL = "http://" + iPAddress + ":8080/ProjectAmity/NEAOfficer/mLogin";
    private EditText loginID;
    private EditText password;
    private String loginServerMsg = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here
        setContentView(R.layout.main);
        // capture our View elements
        Button loginButton = (Button) findViewById(R.id.LoginButton);
        loginID = (EditText) findViewById(R.id.LoginIDExitText);
        password = (EditText) findViewById(R.id.PasswordExitText);

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i("Login", loginID.getText().toString());
                Log.i("Pass", password.getText().toString());
                if (loginID.getText().length() != 0 && password.getText().length() != 0) {
                    Intent i = new Intent();
                    i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.ReportListActivity");
                    login();

                    if (loginServerMsg.equals("T")) {
                        startActivity(i);
                    }

                } else {
                    invalidInput();

                }


            }
        });


    }

    public void login() {
        // Create a new HttpClient and Post Header
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(loginURL);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", loginID.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            //Read in content from server
            is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);

                ch = is.read();
            }
            loginServerMsg = serverMsg.toString().trim();
            Log.i("Server Response", loginServerMsg);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Log.e("Login Exception", e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("Login Exception", e.toString());
        }
    }

    public void invalidInput() {
        // prepare the alert box
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // set the message to display
        alertbox.setMessage("Invalid Entry!");

        // add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            // click listener on the alert box
            public void onClick(DialogInterface arg0, int arg1) {
                // the button was clicked
                //Toast.makeText(getApplicationContext(), "OK button clicked", Toast.LENGTH_LONG).show();
                return;
            }
        });

        // show it
        alertbox.show();

    }
}
