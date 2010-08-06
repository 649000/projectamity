/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author student
 */
public class ReportListActivity extends ListActivity {

    private ListView reportList;
    private String userid;
    private String ipAddress = "152.226.232.99";
    private String reportListURL = "http://" + ipAddress + ":8080/ProjectAmity/NEAOfficer/getReportsAndroid";
    private String reportListServerMsg = "";
   private JSONArray jsonArray;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            ipAddress = extras.getString("ipAddress");
        }
        Log.i("IP Address: ", ipAddress);
        getReports();
        //  setContentView(R.layout.report);
        // reportList = (ListView) findViewById(R.id.list);


        try {
           jsonArray = new JSONArray(reportListServerMsg);
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i).getString("title"));
            }
            reportList = getListView();
            reportList.setChoiceMode(1);
            setListAdapter(new ArrayAdapter<String>(this, R.layout.report, list));
            reportList.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      // When clicked, show a toast with the TextView text
      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
          Toast.LENGTH_SHORT).show();
                    Log.i("Selected Report Index: ", reportList.getCheckedItemPosition() + "");
                    Intent i = new Intent();
                    try {
                        if (jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("category").equalsIgnoreCase("Indoor")) {
                            i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.IndoorReportActivity");
                            i.putExtra("userid", userid);
                            i.putExtra("selectedReport", reportList.getCheckedItemPosition() + "");
                            i.putExtra("Title", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("title"));
                            i.putExtra("Date", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("datePosted"));
                            i.putExtra("Description", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("description"));
                            i.putExtra("PostalCode", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("postalCode"));
                              startActivity(i);
                        } else if (jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("category").equalsIgnoreCase("Outdoor")) {
                           // i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.OutdoorReportActivity");
                            i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.taboutdoor");
                           // i.setClass(this, taboutdoor.class);
                            i.putExtra("userid", userid);
                            i.putExtra("selectedReport", reportList.getCheckedItemPosition() + "");
                            i.putExtra("Title", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("title"));
                            i.putExtra("Date", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("datePosted"));
                            i.putExtra("Description", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("description"));
                            i.putExtra("Latitude", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("latitude"));
                            i.putExtra("Longitude", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("longitude"));
                            
                        startActivity(i);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(ReportListActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                  

                }
            });

        } catch (JSONException ex) {
            Log.e("JSON Exception", ex.toString());
        }

    }

    public void getReports() {
        // Create a new HttpClient and Post Header
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(reportListURL);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", userid));
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
            reportListServerMsg = serverMsg.toString().trim();
            Log.i("Server Response", reportListServerMsg);
            is.close();
        } catch (ClientProtocolException e) {
            Log.e("Report List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Report List Exception", e.toString());
        }
    }
}
