/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
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
 * @author Nazri
 */
public class GPSService extends Service implements LocationListener {
    //School's IP Address:
    //private String ipAddress = "152.226.232.16";
    //Home's IP Address:
    //  private String ipAddress = "10.0.1.3";

    private String ipAddress = "10.0.2.2:8080";
   // private String ipAddress = "www.welovepat.com";
    private double latitude = 0.0, longitude = 0.0;
    private String locationURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/setLocationAndroid", locationServerMsg = "";
    private String userid = "";

    @Override
    public IBinder onBind(Intent arg0) {


        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Criteria c = new Criteria();
        c.setAccuracy(1);
        c.setCostAllowed(true);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(lm.getBestProvider(c, false), (long) 1000, (float) 50.0, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
        Log.i("GPSService UserID", userid);
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        
            setLocation();
        }
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onProviderDisabled(String arg0) {
    }

    public void setLocation() {
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(locationURL);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", userid));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude + ""));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude + ""));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);
                ch = is.read();
            }
            locationServerMsg = serverMsg.toString().trim();
            Log.i("Server Response", locationServerMsg);
            is.close();
        } catch (ClientProtocolException e) {
            Log.e("Location Service Exception", e.toString());
        } catch (IOException e) {
            Log.e("Location Service Exception", e.toString());
        }
    }
}
