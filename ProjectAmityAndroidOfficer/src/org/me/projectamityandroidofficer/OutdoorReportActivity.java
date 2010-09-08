/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
public class OutdoorReportActivity extends MapActivity {

   // private String ipAddress = "10.0.2.2:8080";
     private String ipAddress = "www.welovepat.com";
    private String logoutURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/logoutAndroid";
    private String removeReportURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/removeReportsAndroid";
    private String acceptReportURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/acceptReportsAndroid";
    private String title = "", date = "", description = "", reportID = "", removeReportServerMsg = "", acceptReportServerMsg = "", userid = "", recommended = "",add="";
    private Double latitude = 0.0, longitude = 0.0;
    private TextView titleTV, dateTV, descriptionTV;
    private Button removeReport, getDirections;
    private MapController mc;
    private List<Overlay> mapOverlays;
    private Drawable drawable;
    private MyItemizedOverlay itemizedOverlay;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        // ToDo add your GUI initialization code here
        setContentView(R.layout.outdoorreport);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            title = extras.getString("Title");
            date = extras.getString("Date");
            description = extras.getString("Description");
            latitude = Double.parseDouble(extras.getString("Latitude"));
            longitude = Double.parseDouble(extras.getString("Longitude"));
            reportID = extras.getString("ReportID");
            recommended = extras.getString("Recommended");
        }

//        titleTV = (TextView) findViewById(R.id.TitleContent);
//        dateTV = (TextView) findViewById(R.id.DateContent);
//        descriptionTV = (TextView) findViewById(R.id.DescriptionContent);

        if (recommended.equalsIgnoreCase("false")) {
            removeReport = (Button) findViewById(R.id.outdoorRemove);
            removeReport.setText("Remove Report");
            removeReport.setOnClickListener(new ButtonClickHandler());
        } else if (recommended.equalsIgnoreCase("true")) {
            removeReport = (Button) findViewById(R.id.outdoorRemove);
            removeReport.setText("Accept Report");
            removeReport.setOnClickListener(new AcceptButtonClickHandler());
        }

        getDirections = (Button) findViewById(R.id.outdoorGetDirections);
        getDirections.setOnClickListener(new DirectionsClickHandler());


//        titleTV.setText(title);
        String datesplitted[] = date.split("T");
//        dateTV.setText(datesplitted[0]);
//        descriptionTV.setText(description);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
       // mapView.setStreetView(true);
        mapView.setSatellite(true);
        mc = mapView.getController();
//        mapView.invalidate();
        GeoPoint point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

        try {
            List<Address> addresses = geoCoder.getFromLocation(
                    latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(i).getMaxAddressLineIndex();
                        i++) {
                    add += addresses.get(0).getAddressLine(0) + "\n";
                    add += addresses.get(0).getCountryName();//+ " " + addresses.get(0).getPostalCode();
                }
            }
        } catch (Exception e) {
            Log.e("Outdoor Geocoder", e.toString());
        }
//        List<Overlay> mapOverlays = mapView.getOverlays();
//        Drawable drawable = this.getResources().getDrawable(R.drawable.markerpink);
//        ItemizedOverlay itemizedoverlay = new ItemizedOverlay(drawable, this);
//        OverlayItem overlayitem = new OverlayItem(point, "", add);
//        itemizedoverlay.addOverlay(overlayitem,true);
//        mapOverlays.add(itemizedoverlay);

        mapOverlays = mapView.getOverlays();
        drawable = getResources().getDrawable(R.drawable.marker);
        itemizedOverlay = new MyItemizedOverlay(drawable, mapView);
        OverlayItem overlayItem = new OverlayItem(point, title, datesplitted[0] + "\n" + description);
        itemizedOverlay.addOverlay(overlayItem, add);
        mapOverlays.add(itemizedOverlay);
        
        mc.setZoom(15);
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
                nameValuePairs.add(new BasicNameValuePair("category", "Outdoor"));
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
    public class DirectionsClickHandler implements View.OnClickListener {

        public void onClick(View view) {
            Log.i("Direction Button", latitude+", "+longitude);
            Log.i("Direction Button", add);
            //Apparently, Google Navigation is not supported in Singapore.
            //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latitude+" "+longitude));
          //  Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + add));
             Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+add));
            startActivity(i);
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
                nameValuePairs.add(new BasicNameValuePair("category", "Outdoor"));
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
