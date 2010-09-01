package org.me.projectamityandroid;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import java.util.Vector;

public class MobileHome extends TabActivity implements OnTabChangeListener
{

    private String ipAddress = "www.welovepat.com";

    // variables for cabpooling
    private String potentialCabpooler;
    private String currentAddress;
    private String userDestination;
    private boolean pm = false;

    String[] serverMessages;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {

        super.onCreate(icicle);
        // ToDo add your GUI initialization code here

        setContentView(R.layout.main);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            serverMessages = extras.getStringArray("serverMessages");
        }
        this.setTitle( "Welcome, " + serverMessages[2] );

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MessagingHome.class);
        intent.putExtra("serverMessages", serverMessages);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("messaging").setIndicator("Messaging",
                          res.getDrawable(R.drawable.ic_tab_mail))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, ReportingHome.class);
        intent.putExtra("serverMessages", serverMessages);
        spec = tabHost.newTabSpec("reporting").setIndicator("Reporting",
                          res.getDrawable(R.drawable.ic_tab_artists))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CabpoolHome.class);
        intent.putExtra("serverMessages", serverMessages);
        spec = tabHost.newTabSpec("cabpool").setIndicator("Cabpooling",
                          res.getDrawable(R.drawable.ic_tab_car))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setOnTabChangedListener(this);

        tabHost.setCurrentTab(0);

    }

    @Override
    public void onTabChanged(String tabId)
    {
        if( tabId.equals("messaging") )
        {
        }
        else if( tabId.equals("reporting") )
        {
        }
        else if( tabId.equals("cabpool") )
        {
        }
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

    public void showView(int id)
    {
        View v = (View) findViewById(id);
        v.setVisibility(View.VISIBLE);
    }

    public void hideView(int id)
    {
        View v = (View) findViewById(id);
        v.setVisibility(View.GONE);
    }

    private String[] split(String original, String separator)
    {
        Vector nodes = new Vector();
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create split string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
                System.out.println(result[loop]);
            }

        }
        return result;
    }

    public void setPotentialCabpooler(String p)
    {
        this.potentialCabpooler = p;
    }

    public String getPotentialCabpooler()
    {
        return this.potentialCabpooler;
    }

    public void setUserDestination(String d)
    {
        this.userDestination = d;
    }

    public String getUserDestination()
    {
        return this.userDestination;
    }

    public void pmCabpoool()
    {
        TabHost tabHost = getTabHost();  // The activity TabHost
        tabHost.setCurrentTab(0);
    }

    public void setPm(boolean b)
    {
        this.pm = b;
    }

    public boolean getPm()
    {
        return this.pm;
    }

    public void setCurrentAddress(String c)
    {
        this.currentAddress = c;
    }

    public String getCurrentAddress()
    {
        return this.currentAddress;
    }

}
