/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
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
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            List<Address> addresses = geoCoder.getFromLocationName("Singapore " + postalCode, 1);
            
            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(i).getMaxAddressLineIndex();
                        i++) {
                    longitude = addresses.get(i).getLongitude();

                     latitude = addresses.get(i).getLatitude();
                      Log.i("Longi, Lati", longitude + " " +latitude );
                    add += addresses.get(i).getAddressLine(i) + "\n";
                }
            }

            postalCodeTV.setText(postalCode);
        } catch (Exception ex) {
            Logger.getLogger(IndoorReportActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        GeoPoint point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.markerpink);
        ItemizedOverlay itemizedoverlay = new ItemizedOverlay(drawable, this);

        OverlayItem overlayitem = new OverlayItem(point, "", add);
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
