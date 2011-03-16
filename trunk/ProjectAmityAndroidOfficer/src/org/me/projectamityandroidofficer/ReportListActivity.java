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
import java.util.Vector;
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

/**
 *
 * @author student
 */
public class ReportListActivity extends ListActivity {

    // private String ipAddress = "10.0.2.2:8080";
       // private String ipAddress = "117.120.4.189";
    private String ipAddress="www.projectamity.info";
    // private String ipAddress = "www.welovepat.com";
    private ListView reportList;
    private String userid, reportListServerMsg = "", indoorReportID = "", buildingPostalCode = "", buildingInfo[];
    private String reportListURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficerMobile/getReportsAndroid";
    private String buildingURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficerMobile/getBuildingAndroid";
    private String logoutURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficerMobile/logoutAndroid";
    private JSONArray jsonArray;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
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
//                    Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
                    Log.i("Selected Report Index: ", reportList.getCheckedItemPosition() + "");
                    Intent i = new Intent();
                    try {
                        if (jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("category").equalsIgnoreCase("Indoor")) {
                            // i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.IndoorReportActivity");
                            i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.tabindoor");
                            indoorReportID = jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("id");
                            getBuilding();
                            i.putExtra("userid", userid);
                            i.putExtra("selectedReport", reportList.getCheckedItemPosition() + "");
                            i.putExtra("Title", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("title"));
                            i.putExtra("Date", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("datePosted"));
                            i.putExtra("Description", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("description"));
                            i.putExtra("ReportID", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("id"));
                            i.putExtra("Recommended", "false");
                            buildingInfo = split(buildingPostalCode, "|");
//                            Log.i("PostalCode", buildingInfo[0]);
//                            Log.i("Level", buildingInfo[1]);
//                            Log.i("Stairwell", buildingInfo[2]);
                            String pCode = buildingInfo[0];
                            String lvl = buildingInfo[1];
                            String sWell = buildingInfo[2];
                            i.putExtra("PostalCode", pCode);
                            i.putExtra("Level", lvl);
                            i.putExtra("Stairwell", sWell);
                            //   i.putExtra("PostalCode", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("postalCode"));
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
                            i.putExtra("ReportID", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("id"));
                            i.putExtra("Recommended", "false");
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
            if (reportListServerMsg.equalsIgnoreCase("[]")) {
                Toast.makeText(getApplicationContext(), "There are currently no available reports.", Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            Log.e("Report List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Report List Exception", e.toString());
        }
    }

    public void getBuilding() {
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(buildingURL);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", indoorReportID));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);
                ch = is.read();
            }
            buildingPostalCode = serverMsg.toString().trim();
            Log.i("Server Response", buildingPostalCode);

            is.close();
        } catch (ClientProtocolException e) {
            Log.e("Building List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Building List Exception", e.toString());
        }
    }

    private String[] split(String original, String separator) {
        Vector nodes = new Vector();
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create split string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
                System.out.println(result[loop]);
            }

        }
        return result;
    }
}
