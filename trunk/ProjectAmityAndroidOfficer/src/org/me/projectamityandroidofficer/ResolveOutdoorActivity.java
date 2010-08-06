/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 *
 * @author student
 */
public class ResolveOutdoorActivity extends Activity {

    private EditText status;
    private EditText description;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here
        setContentView(R.layout.resolveoutdoor);
        status = (EditText) findViewById(R.id.resolveOutStatusContent);
        description = (EditText) findViewById(R.id.resolveOutDescriptionContent);
    }
}
