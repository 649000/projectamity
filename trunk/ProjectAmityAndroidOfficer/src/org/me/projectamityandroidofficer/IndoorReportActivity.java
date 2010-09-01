/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.json.JSONObject;

/**
 *
 * @author student
 */
public class IndoorReportActivity extends MapActivity {

       // private String ipAddress = "10.0.2.2";
    // private String ipAddress = "172.10.20.2":8080;
    private String ipAddress = "www.welovepat.com";
    private String logoutURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/logoutAndroid";
    private String acceptReportURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/acceptReportsAndroid";
    private String removeReportURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/removeReportsAndroid";
    private String userid = "", title = "", date = "", reportID = "", description = "", postalCode = "", removeReportServerMsg = "", acceptReportServerMsg = "", recommended = "";
    private TextView titleTV, dateTV, descriptionTV, postalCodeTV;
    private Button removeReport;
    private MapController mc;
    private Geocoder gc;
    private double longitude, latitude;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.indoorreport);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            title = extras.getString("Title");
            date = extras.getString("Date");
            description = extras.getString("Description");
            postalCode = extras.getString("PostalCode");
            reportID = extras.getString("ReportID");
            recommended = extras.getString("Recommended");

        }
        titleTV = (TextView) findViewById(R.id.IndoorTitleContent);
        dateTV = (TextView) findViewById(R.id.IndoorDateContent);
        descriptionTV = (TextView) findViewById(R.id.IndoorDescriptionContent);
        postalCodeTV = (TextView) findViewById(R.id.IndoorPostalContent);
        if (recommended.equalsIgnoreCase("false")) {
            removeReport = (Button) findViewById(R.id.indoorRemove);
            removeReport.setText("Remove Report");
            removeReport.setOnClickListener(new ButtonClickHandler());
        } else if (recommended.equalsIgnoreCase("true")) {
            removeReport = (Button) findViewById(R.id.indoorRemove);
            removeReport.setText("Accept Report");
            removeReport.setOnClickListener(new AcceptButtonClickHandler());
        }
        titleTV.setText(title);
        String datesplitted[] = date.split("T");
        dateTV.setText(datesplitted[0]);
        descriptionTV.setText(description);
        MapView mapView = (MapView) findViewById(R.id.Indoormapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        mapView.setSatellite(true);
        mc = mapView.getController();
        String add = "";

        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());


        try {
            Log.i("PostalCode", postalCode);
//            List<Address> addresses = geoCoder.getFromLocationName("Singapore " + postalCode, 1);
//            if (addresses.size() > 0) {
//                for (int i = 0; i < addresses.get(i).getMaxAddressLineIndex();
//                        i++) {
//                    longitude = addresses.get(0).getLongitude();
//
//                    latitude = addresses.get(0).getLatitude();
//                    Log.i("Longi, Lati", longitude + " " + latitude);
//                    add += addresses.get(i).getAddressLine(0) + "\n";
//                }
//            }
            URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=Singapore%20" + postalCode + "&sensor=false");
            URLConnection conn = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            InputStream in = httpConn.getInputStream();
            StringBuilder serverMsg = new StringBuilder("");
            String geo;
            int ch = in.read();
            while (ch != -1) {
                serverMsg.append((char) ch);

                ch = in.read();
            }
            geo = serverMsg.toString().trim();

//            JSONArray jsonArray = new JSONArray(geo);
            JSONObject jsonObject = new JSONObject(geo);
            List<String> list = new ArrayList<String>();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                list.add(jsonArray.getJSONObject(i).getString("results"));
//            }
            //    latitude =
            // Log.i("Longi, Lati", longitude + " " + jsonArray.getJSONObject(0).toString());


            JSONArray jsonArray = new JSONArray(jsonObject.getString("results"));
            Log.i("Latitude/Longitude", jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng") + " " + jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat"));
            latitude = Double.parseDouble(jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat"));
            longitude = Double.parseDouble(jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng"));
            in.close();
            postalCodeTV.setText(postalCode);
        } catch (Exception ex) {
            Logger.getLogger(IndoorReportActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log.i("Longi, Lati", longitude + " " + latitude);
        GeoPoint point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.markerpink);
        ItemizedOverlay itemizedoverlay = new ItemizedOverlay(drawable, this);

        OverlayItem overlayitem = new OverlayItem(point, "", postalCode);
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        mc.setZoom(17);
        mc.animateTo(point);


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

    public class ButtonClickHandler implements View.OnClickListener {

        public void onClick(View view) {

            StringBuilder serverMsg = new StringBuilder("");
            InputStream is = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(removeReportURL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("reportid", reportID));
                nameValuePairs.add(new BasicNameValuePair("category", "Indoor"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                is = response.getEntity().getContent();
                int ch = is.read();
                while (ch != -1) {
                    serverMsg.append((char) ch);
                    ch = is.read();
                }
                removeReportServerMsg = serverMsg.toString().trim();
                Log.i("Server Response", removeReportServerMsg);
                is.close();
                if (removeReportServerMsg.equalsIgnoreCase("T")) {
                    Intent i = new Intent();
                    i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.ReportHomeActivity");
                    i.putExtra("userid", userid);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Report has been successfully removed.", Toast.LENGTH_LONG).show();
                } else if (removeReportServerMsg.equalsIgnoreCase("F")) {
                    invalidInput("Unable to execute task on server.");
                }
            } catch (ClientProtocolException e) {
                Log.e("Report List Exception", e.toString());
            } catch (IOException e) {
                Log.e("Report List Exception", e.toString());
            }
        }
    }

    public class AcceptButtonClickHandler implements View.OnClickListener {

        public void onClick(View view) {

            StringBuilder serverMsg = new StringBuilder("");
            InputStream is = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(acceptReportURL);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("reportid", reportID));
                nameValuePairs.add(new BasicNameValuePair("category", "Indoor"));
                nameValuePairs.add(new BasicNameValuePair("userid", userid));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                is = response.getEntity().getContent();
                int ch = is.read();
                while (ch != -1) {
                    serverMsg.append((char) ch);
                    ch = is.read();
                }
                acceptReportServerMsg = serverMsg.toString().trim();
                Log.i("Server Response", acceptReportServerMsg);
                is.close();
                if (acceptReportServerMsg.equalsIgnoreCase("T")) {
                    Intent i = new Intent();
                    i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.ReportHomeActivity");
                    i.putExtra("userid", userid);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Report has been successfully accepted.", Toast.LENGTH_LONG).show();
                } else if (acceptReportServerMsg.equalsIgnoreCase("F")) {
                    invalidInput("Unable to execute task on server.");
                }
            } catch (ClientProtocolException e) {
                Log.e("Report List Exception", e.toString());
            } catch (IOException e) {
                Log.e("Report List Exception", e.toString());
            }
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
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
