/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
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
import java.util.List;
import java.util.Locale;

/**
 *
 * @author student
 */
public class OutdoorReportActivity extends MapActivity {

    private String title = "";
    private String date = "";
    private String description = "";
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private String reportID = "";
    private TextView titleTV;
    private TextView dateTV;
    private TextView descriptionTV;
    private MapController mc;
    private GeoPoint p;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
//        // ToDo add your GUI initialization code here
        setContentView(R.layout.outdoorreport);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("Title");
            date = extras.getString("Date");
            description = extras.getString("Description");
            latitude = Double.parseDouble(extras.getString("Latitude"));
            longitude = Double.parseDouble(extras.getString("Longitude"));
            reportID = extras.getString("ReportID");
        }

        titleTV = (TextView) findViewById(R.id.TitleContent);
        dateTV = (TextView) findViewById(R.id.DateContent);
        descriptionTV = (TextView) findViewById(R.id.DescriptionContent);
        titleTV.setText(title);
        String datesplitted[] = date.split("T");
        dateTV.setText(datesplitted[0]);
        descriptionTV.setText(description);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        mapView.setSatellite(true);
        mc = mapView.getController();
//        mapView.invalidate();
        GeoPoint point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
        String add = "";
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
        
        try {
           List<Address> addresses = geoCoder.getFromLocation(
                    latitude ,
                    longitude , 1);

            
            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(i).getMaxAddressLineIndex();
                        i++) {
                    add += addresses.get(0).getAddressLine(0) + "\n";
                    add += addresses.get(0).getCountryName() + " "+ addresses.get(0).getPostalCode();
                    

                }
            }
           
        } catch (Exception e) {
            Log.e("Geocoder", e.toString());
        }

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
