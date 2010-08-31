/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
public class LocateOfficerActivity extends Activity {

    /** Called when the activity is first created. */
    //School's IP Address:
    // private String ipAddress = "152.226.232.16";
    //Home's IP Address:
    //  private String ipAddress = "10.0.1.3";
    private String ipAddress = "10.0.2.2";
    private String userid;
    private String logoutURL = "http://" + ipAddress + ":8080/ProjectAmity/NEAOfficer/logoutAndroid";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
        setContentView(R.layout.locateofficer);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(this);
        //MenuItem item = menu.add(R.id.logoutMenu);
        inflater.inflate(R.menu.logout, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                setLogout();
                return true;
        }
        return false;
    }

    public void setLogout() {
        Log.i("Menu", "Logout Menu pressed Start");
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(logoutURL);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", userid));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);
                ch = is.read();
            }
            is.close();
        } catch (ClientProtocolException e) {
            Log.e("Building List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Building List Exception", e.toString());
        }

        if (serverMsg.toString().trim().equalsIgnoreCase("T")) {
            Log.i("Menu", "Logout Success");
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(i);

        } else if (serverMsg.toString().trim().equalsIgnoreCase("F")) {
            Toast.makeText(getApplicationContext(), "Unable to execute task on server.", Toast.LENGTH_SHORT).show();
        }
    }
}
