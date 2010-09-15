/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

/**
 *
 * @author student
 */
public class LocateOfficerActivity extends MapActivity implements LocationListener {

    private String ipAddress = "10.0.2.2:8080";
    // private String ipAddress = "www.welovepat.com";
    private String userid, getOfficerServerMsg = "";
    private String logoutURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficerMobile/logoutAndroid";
    private String getOfficerURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficerMobile/getNearbyOfficer";
    private Double latitude = 0.0, longitude = 0.0;
    private MapController mc;
    private MapView mapView;
    private JSONArray jsonArray;
    private List<String> phoneNumberList, nameList;
    private List<Double> latitudeList, longitudeList;
    private  ProgressDialog myProgressDialog = null;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
        setContentView(R.layout.locateofficer);
        myProgressDialog = ProgressDialog.show(LocateOfficerActivity.this, "Retrieving GPS Coordinates.", "Please wait..", true,true);
        Criteria c = new Criteria();
        c.setAccuracy(1);
        c.setCostAllowed(true);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(lm.getBestProvider(c, false), (long) 1000, (float) 50.0, this);
        Log.i("Latitude", latitude + "");
        Log.i("Longitude", longitude + "");
        mapView = (MapView) findViewById(R.id.mapviewOfficer);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        mapView.setSatellite(true);
        mc = mapView.getController();

        if(latitude != 0.0 && longitude !=0.0)
        {
            myProgressDialog.dismiss();
        }
        // Toast.makeText(getApplicationContext(), "Retrieving Officers within 2.5KM radius...", Toast.LENGTH_SHORT).show();
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            myProgressDialog.dismiss();
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mapView.getOverlays().clear();
           
            List<Overlay> mapOverlays = mapView.getOverlays();
             GeoPoint point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
            Drawable drawable = this.getResources().getDrawable(R.drawable.mapredpin);
            ItemizedOverlay itemizedoverlay = new ItemizedOverlay(drawable, this);
            OverlayItem overlayitem = new OverlayItem(point, "", "You are here.");
            itemizedoverlay.addOverlay(overlayitem, true);
            mapOverlays.add(itemizedoverlay);
            mc.setZoom(17);
            mc.animateTo(point);
            getOfficer();

            try {
                jsonArray = new JSONArray(getOfficerServerMsg);
                nameList = new ArrayList<String>();
                phoneNumberList = new ArrayList<String>();
                latitudeList = new ArrayList<Double>();
                longitudeList = new ArrayList<Double>();
                for (int i = 0; i < jsonArray.length(); i++) {
//                    nameList.add(jsonArray.getJSONObject(i).getString("name"));
//                    nameList.add(jsonArray.getJSONObject(i).getString("phoneNumber"));
//                    latitudeList.add(jsonArray.getJSONObject(i).getDouble("latitude"));
//                    longitudeList.add(jsonArray.getJSONObject(i).getDouble("longitude"));

                    GeoPoint officerPoint = new GeoPoint((int) (jsonArray.getJSONObject(i).getDouble("latitude") * 1E6), (int) (jsonArray.getJSONObject(i).getDouble("longitude") * 1E6));
                    List<Overlay> mapOverlays2 = mapView.getOverlays();
                    Drawable drawableOfficer = this.getResources().getDrawable(R.drawable.mapbluepin);
                    ItemizedOverlay itemizedoverlayOfficer = new ItemizedOverlay(drawableOfficer, this);
                    OverlayItem overlayitemOfficer = new OverlayItem(officerPoint, jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getString("phoneNumber"));
                    itemizedoverlayOfficer.addOverlay(overlayitemOfficer, false);
                    mapOverlays2.add(itemizedoverlayOfficer);

                }
            } catch (Exception e) {
                Log.e("Error in ServerMsg", e.toString());
            }
        }
    }

    public void getOfficer() {
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(getOfficerURL);
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
            getOfficerServerMsg = serverMsg.toString().trim();
            Log.i("Get Officer Server Response", getOfficerServerMsg);
            is.close();

            if (getOfficerServerMsg.equalsIgnoreCase("[]")) {
                Toast.makeText(getApplicationContext(), "There are currently no officers located nearby.", Toast.LENGTH_SHORT).show();
            }
        } catch (ClientProtocolException e) {
            Log.e("Building List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Building List Exception", e.toString());
        }
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onProviderDisabled(String arg0) {
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

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
