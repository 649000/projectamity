/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.os.Bundle;

/**
 *
 * @author student
 */
public class LocateOfficerActivity extends Activity {

    /** Called when the activity is first created. */
    //School's IP Address:
    // private String ipAddress = "152.226.232.16";
    //Home's IP Address:
    //  private String ipAddress = "10.0.1.3";
     private String ipAddress = "10.0.2.2";
    private String userid;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
        }
    }
}
