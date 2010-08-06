/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import java.util.List;

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
        mc = mapView.getController();
        p = new GeoPoint(
                (int) (latitude * 1E6),
                (int) (longitude * 1E6));

        mc.animateTo(p);
        mc.setZoom(17);
        mapView.invalidate();
        //       List<Overlay> mapOverlays = mapView.getOverlays();
        // Drawable drawable = this.getResources().getDrawable(R.drawable.googlemarkerpink);
        // ItemizedOverlay itemizedoverlay = new ItemizedOverlay(drawable);
        //GeoPoint point = new GeoPoint(latitude, longitude);
        // OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        // itemizedoverlay.addOverlay(overlayitem);
        // mapOverlays.add(itemizedoverlay);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
