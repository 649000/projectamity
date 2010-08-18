package org.me.projectamityandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class LogIn extends Activity
{

    private TextView tbxUserID;
    private TextView tbxPassword;
    // private String ipAddress = "172.27.155.230";
    private String ipAddress = "192.168.1.68";
    private String loginUserURL = "http://" + ipAddress + ":8080/ProjectAmity/resident/mLogin";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here
        setContentView(R.layout.login);

        final Button button = (Button) findViewById(R.id.btnLogIn);
        button.setOnClickListener
        (
            new View.OnClickListener()
            {
                // this event is called when the Log In button is clicked
                public void onClick(View v)
                {
                    tbxUserID = (TextView) findViewById(R.id.tbxUserID);
                    tbxPassword = (TextView) findViewById(R.id.tbxPassword);
                    String userID = tbxUserID.getText().toString();
                    String password = tbxPassword.getText().toString();

                    if( userID.length() == 0 || password.length() == 0 )
                    {
                        // Display an validation error alert if User ID
                        // or password is empty
                        showAlert(v, "You left out either your User ID or Password.");
                    }
                    else
                    {
                        // Log in with server

                        // Create a new HttpClient and Post Header
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(loginUserURL);

                        try
                        {
                            // Add your data
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("nric", userID));
                            nameValuePairs.add(new BasicNameValuePair("password", password));
                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                            // Execute HTTP Post Request
                            HttpResponse response = httpclient.execute(httppost);

                            StringBuilder serverMsg = new StringBuilder("");
                            InputStream is = response.getEntity().getContent();
                            int ch = is.read();
                            while (ch != -1)
                            {
                                serverMsg.append( (char) ch );
                                ch = is.read();
                            }

                            String[] serverMessages = split(serverMsg.toString(), "|");

                            // Successful Login
                            if( serverMessages[0].equalsIgnoreCase("T") )
                            {
                                Intent i = new Intent();
                                i.setClassName("org.me.projectamityandroid", "org.me.projectamityandroid.MobileHome");
                                i.putExtra("serverMessages", serverMessages);
                                startActivity(i);
                            }
                            else
                            {
                                showAlert(v, "Invalid User ID / Password combination.");
                            }
                        }
                        catch (ClientProtocolException e)
                        {
                            Log.e( "LOGGING IN", e.toString() );
                            // TODO Auto-generated catch block
                        }
                        catch (IOException ex)
                        {
                            Log.e( "LOGGING IN", ex.toString() );
                            // TODO Auto-generated catch block
                        }
                    }
                }
            }
        );
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

}