/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author student
 */
public class IndoorReportActivity extends MapActivity {

    private String userid = "";
    private String title = "";
    private String date = "";
    private String description = "";
    private String postalCode = "";
    private TextView titleTV;
    private TextView dateTV;
    private TextView descriptionTV;
    private TextView postalCodeTV;
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

        }
        titleTV = (TextView) findViewById(R.id.IndoorTitleContent);
        dateTV = (TextView) findViewById(R.id.IndoorDateContent);
        descriptionTV = (TextView) findViewById(R.id.IndoorDescriptionContent);
        postalCodeTV = (TextView) findViewById(R.id.IndoorPostalContent);
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

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
