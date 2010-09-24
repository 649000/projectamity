/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroid;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

/**
 *
 * @author Nazri
 */
public class IndoorReportingLevel extends ListActivity {

   // private String ipAddress = "10.0.2.2:8080";
    private String ipAddress = "117.120.4.189";
   // private String ipAddress = "www.welovepat.com";
    private String userid, serverMessages[], buildingServerMsg;
    private String buildingURL = "http://" + ipAddress + "/ProjectAmity/reportMobile/getLevel";
    private JSONArray jsonArray;
    private ListView buildingList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            serverMessages = extras.getStringArray("serverMessages");
        }
        getLevel();

        try {

            jsonArray = new JSONArray(buildingServerMsg);
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            buildingList = getListView();
            buildingList.setChoiceMode(1);
            setListAdapter(new ArrayAdapter<String>(this, R.layout.reporting, list));
            buildingList.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.textView);
                    String text = textView.getText().toString();
                    Log.i("Selected Level Index: ", buildingList.getCheckedItemPosition() + "");
                    Log.i("Seleted Content:", text);
                    Intent i = new Intent();
                    i.setClassName("org.me.projectamityandroid", "org.me.projectamityandroid.IndoorReportingLocation");
                    i.putExtra("userid", userid);
                    i.putExtra("serverMessages", serverMessages);
                    i.putExtra("level", text);
                    startActivity(i);
                }
            });
        } catch (Exception e) {
            Log.e("IndoorReportLevelError", e.toString());
        }
    }

    public void getLevel() {
        // Create a new HttpClient and Post Header
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(buildingURL);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", userid));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            //Read in content from server
            is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);

                ch = is.read();
            }
            buildingServerMsg = serverMsg.toString().trim();
            Log.i("Server Response", buildingServerMsg);
            is.close();
        } catch (ClientProtocolException e) {
            Log.e("Building List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Building List Exception", e.toString());
        }
    }
}
