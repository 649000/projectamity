/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 *
 * @author student
 */
public class OutdoorReportActivity extends Activity {

    private String title = "";
    private String date = "";
    private String description = "";
    private TextView titleTV;
    private TextView dateTV;
    private TextView descriptionTV;

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
        }

        titleTV = (TextView) findViewById(R.id.TitleContent);
        dateTV = (TextView) findViewById(R.id.DateContent);
        descriptionTV = (TextView) findViewById(R.id.DescriptionContent);
        titleTV.setText(title);
        String datesplitted[] = date.split("T");
        dateTV.setText(datesplitted[0]);
        descriptionTV.setText(description);
    }
}
