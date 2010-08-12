/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import java.io.IOException;
import java.util.List;
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
        postalCodeTV = (TextView)findViewById(R.id.IndoorPostalContent);
        titleTV.setText(title);
        String datesplitted[] = date.split("T");
        dateTV.setText(datesplitted[0]);
        descriptionTV.setText(description);
        gc = new Geocoder(this);
        try {
            List<Address> foundAddresses = gc.getFromLocationName("Singapore " + postalCode, 1);
             postalCodeTV.setText(foundAddresses.get(0).toString());
        } catch (IOException ex) {
            Logger.getLogger(IndoorReportActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
        MapView mapView = (MapView) findViewById(R.id.Indoormapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);

    }
        @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
