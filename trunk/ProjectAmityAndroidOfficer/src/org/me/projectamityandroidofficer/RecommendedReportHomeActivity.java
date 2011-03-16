/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
public class RecommendedReportHomeActivity extends ListActivity {

    private String userid = "", radius = "";
           // private String ipAddress = "10.0.2.2:8080";
       // private String ipAddress = "117.120.4.189";
    private String ipAddress="www.projectamity.info";
   // private String ipAddress = "www.welovepat.com";
    private String logoutURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficerMobile/logoutAndroid";
    private ListView distanceList;
    static final String[] distance = new String[]{"Radius of 1KM", "Radius of 3KM", "Radius of 5KM", "Radius of 7KM", "Radius of 9KM", "Entire Singapore"};

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
        distanceList = getListView();
        distanceList.setChoiceMode(1);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.distancelist, distance));
        distanceList.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                Log.i("Selected Distance Index: ", distanceList.getCheckedItemPosition() + "");
                if (distanceList.getCheckedItemPosition() == 0) {
                    radius = "1";
                } else if (distanceList.getCheckedItemPosition() == 1) {
                    radius = "3";
                } else if (distanceList.getCheckedItemPosition() == 2) {
                    radius = "5";
                } else if (distanceList.getCheckedItemPosition() == 3) {
                    radius = "7";
                } else if (distanceList.getCheckedItemPosition() == 4) {
                    radius = "9";
                } else if (distanceList.getCheckedItemPosition() == 5) {
                    radius = "All";
                }
                Intent i = new Intent();
                i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.RecommendedReportActivity");
                i.putExtra("userid", userid);
                i.putExtra("radius", radius);
                startActivity(i);
            }
        });
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
