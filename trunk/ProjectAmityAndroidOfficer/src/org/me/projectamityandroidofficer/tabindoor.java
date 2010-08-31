/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 *
 * @author Nazri
 */
public class tabindoor extends TabActivity {

    /** Called when the activity is first created. */
    private String title = "", recommended="";
    private String date = "";
    private String description = "";
    private String postalCode = "";
    private String userid = "";
    private String reportID = "";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here

        setContentView(R.layout.tabindoor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            title = extras.getString("Title");
            date = extras.getString("Date");
            description = extras.getString("Description");
            postalCode = extras.getString("PostalCode");
            reportID = extras.getString("ReportID");
            recommended = extras.getString("Recommended");

        }
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent i;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        i = new Intent().setClass(this, IndoorReportActivity.class);
        i.putExtra("userid", userid);
        i.putExtra("Title", title);
        i.putExtra("Date", date);
        i.putExtra("Description", description);
        i.putExtra("PostalCode", postalCode);
        i.putExtra("ReportID", reportID);
        i.putExtra("Recommended", "false");
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("indoorreports").setIndicator("Indoor Report",
                res.getDrawable(R.drawable.ic_tab_artists)).setContent(i);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        i = new Intent().setClass(this, ResolveIndoorActivity.class);
        i.putExtra("userid", userid);
        i.putExtra("Title", title);
        i.putExtra("Date", date);
        i.putExtra("Description", description);
        i.putExtra("PostalCode", postalCode);
        i.putExtra("ReportID", reportID);
        i.putExtra("Recommended", "false");
        spec = tabHost.newTabSpec("resolveIndoor").setIndicator("Resolve it",
                res.getDrawable(R.drawable.ic_tab_artists)).setContent(i);
        tabHost.addTab(spec);

    }
}
