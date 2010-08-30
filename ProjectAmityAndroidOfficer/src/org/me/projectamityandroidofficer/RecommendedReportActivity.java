/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

/**
 *
 * @author student
 */
public class RecommendedReportActivity extends ListActivity implements LocationListener {

    //School's IP Address:
    // private String ipAddress = "152.226.232.16";
    //Home's IP Address:
    //  private String ipAddress = "10.0.1.3";
    private String ipAddress = "10.0.2.2";
    private ListView reportList;
    private String userid = "", reportListServerMsg = "",indoorReportID = "", buildingPostalCode = "";
    private String reportListURL = "http://" + ipAddress + ":8080/ProjectAmity/NEAOfficer/getRecommendedReportsAndroid";
     private String buildingURL = "http://" + ipAddress + ":8080/ProjectAmity/NEAOfficer/getBuildingAndroid";
    private double longitude, latitude = 0.0;
     private JSONArray jsonArray;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 1000, (float) 500.0, this);
        Log.i("Latitude", latitude + "");
        Log.i("Longitude", longitude + "");
        getReports();
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
                            indoorReportID = jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("id");
                            getBuilding();
                            i.putExtra("userid", userid);
                            i.putExtra("selectedReport", reportList.getCheckedItemPosition() + "");
                            i.putExtra("Title", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("title"));
                            i.putExtra("Date", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("datePosted"));
                            i.putExtra("Description", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("description"));
                            i.putExtra("PostalCode", buildingPostalCode);
                            i.putExtra("ReportID", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("id"));

                            //   i.putExtra("PostalCode", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("postalCode"));
                            startActivity(i);
                        } else if (jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("category").equalsIgnoreCase("Outdoor")) {
                            // i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.OutdoorReportActivity");
                            i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.OutdoorReportActivity");
                            // i.setClass(this, taboutdoor.class);
                            i.putExtra("userid", userid);
                            i.putExtra("selectedReport", reportList.getCheckedItemPosition() + "");
                            i.putExtra("Title", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("title"));
                            i.putExtra("Date", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("datePosted"));
                            i.putExtra("Description", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("description"));
                            i.putExtra("Latitude", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("latitude"));
                            i.putExtra("Longitude", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("longitude"));
                            i.putExtra("ReportID", jsonArray.getJSONObject(reportList.getCheckedItemPosition()).getString("id"));
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

    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            invalidInput("Unable to get GPS Coordinates");
        }
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onProviderDisabled(String arg0) {
    }

    public void invalidInput(String message) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage(message);
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                // the button was clicked
                //Toast.makeText(getApplicationContext(), "OK button clicked", Toast.LENGTH_LONG).show();
                return;
            }
        });
        alertbox.show();
    }

    public void getReports() {
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(reportListURL);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude+""));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude+""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

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
            Log.e("Recommended Report List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Recommended Report List Exception", e.toString());
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
            Log.e("Report List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Report List Exception", e.toString());
        }
    }
}
