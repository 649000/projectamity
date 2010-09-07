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

   // private String ipAddress = "10.0.2.2:8080";
    private String ipAddress = "10.0.2.2:8080";
   // private String ipAddress = "www.welovepat.com";
     
    private String loginURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/loginAndroid";
    private EditText loginID, password;
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
        loginID.setText("NEA01");
        password.setText("123");
        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i("Login", loginID.getText().toString());
                Log.i("Pass", password.getText().toString());
                if (loginID.getText().length() != 0 && password.getText().length() != 0) {
                    Intent i = new Intent();
                    i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.ReportHomeActivity");
                       login();

                    if (loginServerMsg.equals("T")) {
                    i.putExtra("userid", loginID.getText().toString());
                    i.putExtra("ipAddress", ipAddress);
                    startActivity(i);
                    Intent intent = new Intent(LoginActivity.this,GPSService.class);
                    intent.putExtra("userid", loginID.getText().toString());
                    startService(intent);

                    } else { invalidInput("Invalid Userid & Password Combination");
                        }

                } else {
                    invalidInput("Invalid Entry!");

                }
            }
        });


    }

    public void login() {
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(loginURL);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", loginID.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                //Read in content from server
                is = response.getEntity().getContent();
                int ch = is.read();
                while (ch != -1) {
                    serverMsg.append((char) ch);

                    ch = is.read();
                }
                loginServerMsg = serverMsg.toString().trim();
                Log.i("Server Response", loginServerMsg);
                is.close();
            } else {
                invalidInput("Unable to establish connection to server.");
            }
        } catch (ClientProtocolException e) {
            Log.e("Login Exception", e.toString());
        } catch (IOException e) {
            Log.e("Login Exception", e.toString());
        }
    }

    public void invalidInput(String message) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage(message);
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });
        alertbox.show();

    }
}
