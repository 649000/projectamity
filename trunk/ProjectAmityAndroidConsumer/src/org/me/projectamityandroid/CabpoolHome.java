// FINGERPRINT: 77:E8:9D:FC:32:A3:51:C1:38:11:E8:C6:90:D8:8D:4A

package org.me.projectamityandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONArray;
import org.json.JSONException;

public class CabpoolHome extends MapActivity implements LocationListener
{

    String[] serverMessages;

    private String ipAddress = "10.0.2.2";
    private String updateLocationURL = "http://" + ipAddress + ":8080/ProjectAmity/cabpoolMobile/updateLocation";
    private String updateDestinationURL = "http://" + ipAddress + ":8080/ProjectAmity/cabpoolMobile/updateDestination";

    boolean getLocation = false;

    MapView cMap;
    MapController mc;
    double lat, lng;
    GeoPoint p;

    List<Overlay> mapOverlays;

    CabpoolYouItemizedOverlay youItemizedOverlay;
    CabpoolOthersItemizedOverlay othersItemizedOverlay;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {

        super.onCreate(icicle);
        setContentView(R.layout.cabpool);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            serverMessages = extras.getStringArray("serverMessages");
        }

        askForPermission();
        
    }

    public void askForPermission()
    {
        // Ask user for permission to use GPS location
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setMessage("Project Amity Mobile has to access your location in order to run this feature." +
                          "\nYour location may be made known to others." +
                          "\nDo you wish to use this feature?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id)
                   {
                        getLocation = true;
                        setUpMap();
                   }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                       public void onClick(DialogInterface dialog, int id)
                       {
                            getLocation = false;
                            dialog.cancel();
                            setUpMap();
                       }
               });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setUpMap()
    {
        // Creating and initializing Map
        cMap = (MapView) findViewById(R.id.cabpoolMap);
        p = new GeoPoint((int) (1.358744 * 1000000), (int) (103.822174 * 1000000));
        cMap.setSatellite(false);
        cMap.setBuiltInZoomControls(true);

        mc = cMap.getController();
        mc.setCenter(p);
        mc.setZoom(11);

        mapOverlays = cMap.getOverlays();

        if( getLocation )
        {
            showAlert2(this, "Please specify your intended destination.");

            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, Long.valueOf("1000"), Float.valueOf("500.0"), this);

            // Drawable drawable = this.getResources().getDrawable(R.drawable.cabpoolmapredpin);
            // youItemizedOverlay = new CabpoolYouItemizedOverlay(drawable);
            // Location lastKnown = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // GeoPoint lastKnownPoint = new GeoPoint((int) (lastKnown.getLatitude() * 1000000), (int) (lastKnown.getLongitude() * 1000000));
            // OverlayItem you = new OverlayItem(lastKnownPoint, "You are Here!", "This is your current location.");
            // youItemizedOverlay.addOverlay(you);
            // mapOverlays.add(youItemizedOverlay);
        }

        // Set up the onClickListener for the Update Location button
        Button reply = (Button) findViewById(R.id.btnupdatecabpooldestination);
        reply.setOnClickListener
        (
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        updateDestination();
                    }
                }
        );

    }

    public void onLocationChanged(Location location)
    {
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();

            p = new GeoPoint((int) (lat * 1000000), (int) (lng * 1000000));

            updateLocation();

            if( mapOverlays.contains(youItemizedOverlay) )
            {
                mapOverlays.remove(youItemizedOverlay);
            }
            else
            {
                Drawable drawable = this.getResources().getDrawable(R.drawable.cabpoolmapredpin);
                youItemizedOverlay = new CabpoolYouItemizedOverlay(drawable);
            }
            OverlayItem you = new OverlayItem(p, "You are Here!", "This is your current location.");
            youItemizedOverlay.removeAllOverlays();
            youItemizedOverlay.addOverlay(you);
            mapOverlays.add(youItemizedOverlay);

            mc.setCenter(p);
            mc.setZoom(14);
        }
        else
        {
            
        }
    }

    public void updateLocation()
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(updateLocationURL);

        try
        {
            EditText e = (EditText) findViewById( R.id.tbxcabpooldestination );

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", serverMessages[2]));
            nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
            nameValuePairs.add(new BasicNameValuePair("lng", String.valueOf(lng)));
            nameValuePairs.add(   new BasicNameValuePair( "dest", String.valueOf(e.getText()) )   );
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            StringBuilder serverMsg = new StringBuilder("");
            InputStream is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1)
            {
                serverMsg.append( (char) ch );
                ch = is.read();
            }

            if( mapOverlays.contains(othersItemizedOverlay) )
            {
                mapOverlays.remove(othersItemizedOverlay);
            }

            Drawable drawable = this.getResources().getDrawable(R.drawable.cabpoolmapbluepin);
            MobileHome parent = (MobileHome) this.getParent();

            // Geocode the user's current location into a human-readable address
           String add = "";
           Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

           try
           {
               List<Address> addresses = geoCoder.getFromLocation(
                       lat,
                       lng, 1);
               if (addresses.size() > 0)
               {
                   for (int i = 0; i < addresses.get(i).getMaxAddressLineIndex(); i++)
                   {
                       add += addresses.get(0).getAddressLine(0) + "\n";
                       add += addresses.get(0).getCountryName() + " " + addresses.get(0).getPostalCode();
                   }
               }
           }
           catch (Exception excep)
           {
               Log.e("Geocoder", excep.toString());
           }

            parent.setCurrentAddress( add );
                    
            othersItemizedOverlay = new CabpoolOthersItemizedOverlay(drawable, this, parent);
            othersItemizedOverlay.removeAllOverlays();
            JSONArray serverJ = new JSONArray( serverMsg.toString() );
            JSONArray nearbyPeople = serverJ.getJSONArray(0);
            JSONArray nearbyUserids = serverJ.getJSONArray(1);
            for(int i = 0 ; i < nearbyPeople.length() ; i++)
            {
                Double lati = Double.valueOf( nearbyPeople.getJSONObject(i).getString("latitude") );
                Double lngi = Double.valueOf( nearbyPeople.getJSONObject(i).getString("longitude") );
                String destination = nearbyPeople.getJSONObject(i).getString("destination");

                GeoPoint q = new GeoPoint((int) (lati * 1000000), (int) (lngi * 1000000));

                OverlayItem you = new OverlayItem(q, nearbyUserids.getString(i), "Heading to " + destination);
                othersItemizedOverlay.addOverlay(you);
            }
            mapOverlays.add(othersItemizedOverlay);
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
        }
        catch (IOException ex)
        {
            // TODO Auto-generated catch block
        }
        catch(JSONException exc)
        {
        }
    }

    public void updateDestination()
    {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(updateDestinationURL);

        try
        {
            EditText e = (EditText) findViewById( R.id.tbxcabpooldestination );

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", serverMessages[2]));
            nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(lat)));
            nameValuePairs.add(new BasicNameValuePair("lng", String.valueOf(lng)));
            nameValuePairs.add(   new BasicNameValuePair( "dest", String.valueOf(e.getText()) )   );
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            StringBuilder serverMsg = new StringBuilder("");
            InputStream is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1)
            {
                serverMsg.append( (char) ch );
                ch = is.read();
            }

            if( mapOverlays.contains(othersItemizedOverlay) )
            {
                mapOverlays.remove(othersItemizedOverlay);
            }

            MobileHome parent = (MobileHome) this.getParent();
            parent.setUserDestination( String.valueOf(e.getText()) );

            Drawable drawable = this.getResources().getDrawable(R.drawable.cabpoolmapbluepin);
            othersItemizedOverlay = new CabpoolOthersItemizedOverlay(drawable, this, parent);
            othersItemizedOverlay.removeAllOverlays();
            JSONArray serverJ = new JSONArray( serverMsg.toString() );
            JSONArray nearbyPeople = serverJ.getJSONArray(0);
            JSONArray nearbyUserids = serverJ.getJSONArray(1);
            for(int i = 0 ; i < nearbyPeople.length() ; i++)
            {
                Double lati = Double.valueOf( nearbyPeople.getJSONObject(i).getString("latitude") );
                Double lngi = Double.valueOf( nearbyPeople.getJSONObject(i).getString("longitude") );
                String destination = nearbyPeople.getJSONObject(i).getString("destination");

                GeoPoint q = new GeoPoint((int) (lati * 1000000), (int) (lngi * 1000000));

                OverlayItem you = new OverlayItem(q, nearbyUserids.getString(i), "Heading to " + destination);
                othersItemizedOverlay.addOverlay(you);
            }
            mapOverlays.add(othersItemizedOverlay);
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
        }
        catch (IOException ex)
        {
            // TODO Auto-generated catch block
        }
        catch(JSONException exc)
        {
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

    public void showAlert2(Context c, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
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
