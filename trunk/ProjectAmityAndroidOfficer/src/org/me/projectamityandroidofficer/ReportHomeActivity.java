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
public class ReportHomeActivity extends TabActivity {

    private String userid = "";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.reportinghome);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            
        }
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent i;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        i = new Intent().setClass(this, ReportListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("userid", userid);
        
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("reportlist").setIndicator("Report List",
                res.getDrawable(R.drawable.ic_tab_reports)).setContent(i);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        i = new Intent().setClass(this, RecommendedReportHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("userid", userid);
        
        spec = tabHost.newTabSpec("recommendedreport").setIndicator("NearBy Reports",res.getDrawable(R.drawable.ic_tab_recommended)).setContent(i);

        tabHost.addTab(spec);
        
                // Do the same for the other tabs
        i = new Intent().setClass(this, LocateOfficerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("userid", userid);
        spec = tabHost.newTabSpec("locateofficer").setIndicator("Locate Officer",
                res.getDrawable(R.drawable.ic_tab_locate)).setContent(i);
        tabHost.addTab(spec);
           

    }
}
