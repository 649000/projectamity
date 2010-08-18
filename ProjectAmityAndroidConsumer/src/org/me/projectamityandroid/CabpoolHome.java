// FINGERPRINT: 77:E8:9D:FC:32:A3:51:C1:38:11:E8:C6:90:D8:8D:4A

package org.me.projectamityandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class CabpoolHome extends MapActivity implements LocationListener
{

    MapView cMap;
    double lat, lng;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {

        super.onCreate(icicle);
        setContentView(R.layout.cabpool);
        // ToDo add your GUI initialization code here

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 1000, (float) 500.0, this);

        // Creating and initializing Map
        cMap = (MapView) findViewById(R.id.cabpoolMap);
        cMap.setSatellite(false);
        cMap.setBuiltInZoomControls(true);
        
    }

    public void updateMap()
    {
        GeoPoint p = new GeoPoint((int) (lat * 1000000), (int) (lng * 1000000));

        //get MapController that helps to set/get location, zoom etc.
        MapController mc = cMap.getController();
        mc.setCenter(p);
        mc.setZoom(16);
    }

    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
            updateMap();
        }
        else
        {
            showAlert(this.getCurrentFocus(), "No location");
        }
    }

    public void onProviderDisabled(String provider)
    {
    }

    public void onProviderEnabled(String provider)
    {
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }

    public void showAlert(View v, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(message)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id)
                   {
                        dialog.cancel();
                   }
               });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
