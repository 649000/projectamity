package org.me.projectamityandroid;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ReportingHome extends ListActivity {

    /** Called when the activity is first created. */
    private String[] serverMessages;
    private ListView lv;
    private String userid;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serverMessages = extras.getStringArray("serverMessages");
            //Number 2 is resident's userid
        }
        String[] list = new String[]{"Outdoor Reporting", "Indoor Reporting"};
        userid = serverMessages[2];
        setListAdapter(new ArrayAdapter<String>(this, R.layout.reporting, list));
        lv = getListView();
        lv.setChoiceMode(1);
        

        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent i = new Intent();
                Log.i("Checked Position", lv.getCheckedItemPosition()+"");
                i.putExtra("userid", userid);
                if (lv.getCheckedItemPosition() == 0) {
                    i.setClassName("org.me.projectamityandroid", "org.me.projectamityandroid.OutdoorReporting");
                    i.putExtra("userid", userid);
                    i.putExtra("serverMessages", serverMessages);
                     startActivity(i);

                } else if (lv.getCheckedItemPosition() == 1) {
                    i.setClassName("org.me.projectamityandroid", "org.me.projectamityandroid.IndoorReportingLevel");
                    i.putExtra("userid", userid);
                     i.putExtra("serverMessages", serverMessages);
                     startActivity(i);
                }
            }
        });




    }
}
