/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.me.projectamityandroidofficer;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 *
 * @author student
 */
public class taboutdoor extends TabActivity {

    /** Called when the activity is first created. */

        private String title = "";
    private String date = "";
    private String description = "";
    private String latitude = "";
    private String longitude = "";
    private String userid="";
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.taboutdoor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid= extras.getString("userid");
            title = extras.getString("Title");
            date = extras.getString("Date");
            description = extras.getString("Description");
            latitude = extras.getString("Latitude");
            longitude = extras.getString("Longitude");
        }
    Resources res = getResources(); // Resource object to get Drawables
    TabHost tabHost = getTabHost();  // The activity TabHost
    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
    Intent i;  // Reusable Intent for each tab

    // Create an Intent to launch an Activity for the tab (to be reused)
    i = new Intent().setClass(this, OutdoorReportActivity.class);
                            i.putExtra("userid", userid);                           
                            i.putExtra("Title", title);
                            i.putExtra("Date", date);
                            i.putExtra("Description", description);
                            i.putExtra("Latitude",latitude);
                            i.putExtra("Longitude", longitude);
    // Initialize a TabSpec for each tab and add it to the TabHost
    spec = tabHost.newTabSpec("outdoorreports").setIndicator("Outdoor Reports",
                      res.getDrawable(R.drawable.ic_tab_artists))
                  .setContent(i);
    tabHost.addTab(spec);

    // Do the same for the other tabs
    i = new Intent().setClass(this, ResolveOutdoorActivity.class);
                                i.putExtra("userid", userid);
                            i.putExtra("Title", title);
                            i.putExtra("Date", date);
                            i.putExtra("Description", description);
                            i.putExtra("Latitude",latitude);
                            i.putExtra("Longitude", longitude);
    spec = tabHost.newTabSpec("resolveOutdoor").setIndicator("Resolve it",
                      res.getDrawable(R.drawable.ic_tab_artists))
                  .setContent(i);
    tabHost.addTab(spec);


 //   tabHost.setCurrentTab(2);
    }

}
