package org.me.projectamityandroid;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ReportingHome extends Activity
{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {

        super.onCreate(icicle);
        // ToDo add your GUI initialization code here
        TextView textview = new TextView(this);
        textview.setText("This is the Location-Based Reporting tab");
        setContentView(textview);
        
    }

}
