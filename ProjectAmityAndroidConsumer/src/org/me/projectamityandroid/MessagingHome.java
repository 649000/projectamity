package org.me.projectamityandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessagingHome extends Activity
{

          private String ipAddress = "10.0.2.2:8080";
   // private String ipAddress = "www.welovepat.com";

    // variables for PM module
    ArrayList<String> messageIDs;
    ArrayList<String> messageSubjects;
    ArrayList<String> messageTimeStamps = new ArrayList();
    private String checkMessageURL = "http://" + ipAddress + "/ProjectAmity/messageMobile/loadInbox";
    private String viewMessageURL = "http://" + ipAddress + "/ProjectAmity/messageMobile/viewMsg";
    JSONObject currentMsg;
    private String currentMsgSenderId;
    private String sendMessageURL = "http://" + ipAddress + "/ProjectAmity/messageMobile/sendFromAndroid";
    private String markAsReadURL = "http://" + ipAddress + "/ProjectAmity/messageMobile/markAsRead";

    String[] serverMessages;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        // ToDo add your GUI initialization code here

        setContentView(R.layout.messaging);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            serverMessages = extras.getStringArray("serverMessages");
        }

        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
        to.setText("");
        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
        subject.setText("");
        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);
        message.setText("");

        loadMessages();
    }

    @Override
    public void onResume()
    {
        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
        to.setText("");
        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
        subject.setText("");
        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);
        message.setText("");

        loadMessages();

        super.onResume();
    }

    // This method contains code that loads the user's inbox.
    // Also contains code that displays the Compose Message screen.
    public void loadMessages()
    {
        MobileHome parent = (MobileHome) this.getParent();
        if( parent.getPm() )
        {
            contactCabpooler();
            return;
        }

        ListView lv = (ListView) findViewById(R.id.messaginghome);
        showView( R.id.messaginghome );
        hideView( R.id.viewmessage );
        hideView( R.id.composemessage );

        TextView replyText = (TextView) findViewById(R.id.tbxreplymessage);
        replyText.setText("");

        messageIDs = new ArrayList();
        messageSubjects = new ArrayList();
        messageTimeStamps = new ArrayList();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(checkMessageURL);

        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user", serverMessages[2]));
            nameValuePairs.add(new BasicNameValuePair("timeStamp", serverMessages[1]));
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

            JSONArray msgs = new JSONArray( serverMsg.toString() );
            // JSONArray messages = msgs.getJSONArray("newMessages");
            for(int i = 0 ; i < msgs.length() ; i++)
            {
                if( msgs.getJSONObject(i).getString("subject").length() > 23 )
                {
                    messageSubjects.add( msgs.getJSONObject(i).getString("subject").substring(0, 23) + "..." );
                }
                else
                {
                     messageSubjects.add( msgs.getJSONObject(i).getString("subject") );
                }
            }

            for(int j = 0 ; j < msgs.length() ; j++)
            {
                messageTimeStamps.add( msgs.getJSONObject(j).getString("timeStamp") );
                messageIDs.add( msgs.getJSONObject(j).getString("id") );
            }

        }
        catch (ClientProtocolException e)
        {
            Log.e( "LOADING INBOX", e.toString() );
            // TODO Auto-generated catch block
        }
        catch (IOException ex)
        {
            Log.e( "LOADING INBOX", ex.toString() );
            // TODO Auto-generated catch block
        }
        catch(JSONException exc)
        {
            Log.e( "LOADING INBOX", exc.toString() );
        }

        /**
         *** START LOADING MENU AND LIST OF UNREAD MESSAGES ***
        */
        final String[] options = getResources().getStringArray(R.array.mainMenuOptions);
        String[] headersList = new String[ options.length + messageSubjects.size() ];
        for(int i = 0 ; i < options.length ; i ++)
        {
            headersList[i] = options[i];
        }
        if( messageSubjects.size() > 0 )
        {
            for(int i = options.length ; i < headersList.length ; i ++)
            {
                headersList[i] = messageSubjects.get( i - options.length );
            }
        }

        String[] captionsList = new String[ options.length + messageSubjects.size() ];
        captionsList[0] = "Check for new unread messages";
        captionsList[1] = "Write someone a message";
        if( messageSubjects.size() > 0 )
        {
            for(int i = options.length ; i < headersList.length ; i ++)
            {
                captionsList[i] = messageTimeStamps.get( i - options.length );
            }
        }

        String[] from = new String[] {"headers", "captions"};
        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };

        // prepare the list of all records
        List< HashMap<String, String> > fillMaps = new ArrayList< HashMap<String, String> >();
        for(int i = 0; i < headersList.length ; i++)
        {
        	HashMap<String, String> map = new HashMap<String, String>();
        	map.put("headers", headersList[i] );
        	map.put("captions", captionsList[i] );
        	fillMaps.add(map);
        }

        // setListAdapter(new ArrayAdapter<String> (this, android.R.layout.simple_list_item_2, android.R.id.text1, headersList));
        lv.setAdapter( new SimpleAdapter(lv.getContext(), fillMaps, android.R.layout.simple_list_item_2, from, to) );

        /**
         *** START LOADING MENU AND LIST OF UNREAD MESSAGES ***
        */

        // Event listeners for each item
        lv.setOnItemClickListener
        (
            new OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if( position == 0 )
                    {
                        // User wants to check for new messages
                        loadMessages();
                    }
                    else if( position == 1 )
                    {
                        // User wants to compose a message
                        showView( R.id.composemessage );
                        hideView( R.id.messaginghome );

                        // Set up the onClickListener for the Discard button
                        Button discard = (Button) findViewById(R.id.btndiscardnewmessage);
                        discard.setOnClickListener
                        (
                                new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                        builder.setMessage("Are you sure you want to discard this message and return to your inbox?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                   final View v = this.v;
                                                   public void onClick(DialogInterface dialog, int id)
                                                   {
                                                        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
                                                        to.setText("");
                                                        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
                                                        subject.setText("");
                                                        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);
                                                        message.setText("");
                                                        loadMessages();
                                                   }
                                                })
                                                .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                                                       public void onClick(DialogInterface dialog, int id)
                                                       {
                                                            dialog.cancel();
                                                       }
                                               });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                        );

                        // Set up the onClickListener for the Send button
                        Button send = (Button) findViewById(R.id.btnsendnewmessage);
                        send.setOnClickListener
                        (
                                new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                        builder.setMessage("Are you sure you want to send this message?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                   final View v = this.v;
                                                   public void onClick(DialogInterface dialog, int id)
                                                   {
                                                        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
                                                        String sendTo = to.getText().toString();
                                                        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
                                                        String sendSubject = subject.getText().toString();
                                                        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);
                                                        String sendMessage = message.getText().toString();
                                                        sendMessage(serverMessages[2], sendTo, sendSubject, sendMessage, true);
                                                   }
                                                })
                                                .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                                                       public void onClick(DialogInterface dialog, int id)
                                                       {
                                                           dialog.cancel();
                                                       }
                                               });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                        );
                    }
                    else
                    {
                        // User wants to view a message
                        hideView( R.id.messaginghome );
                        showMessage( messageIDs.get( position - options.length ) );
                    }
                }
            }
        );

    }

    public void sendMessage(String sender, String receiver, String subject, String message, boolean newMessage)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(sendMessageURL);

        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("sender", sender));
            nameValuePairs.add(new BasicNameValuePair("receiver", receiver));
            nameValuePairs.add(new BasicNameValuePair("subject", subject));
            nameValuePairs.add(new BasicNameValuePair("message", message));
            nameValuePairs.add(   new BasicNameValuePair( "newMessage", String.valueOf(newMessage) )   );
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

            if( serverMsg.toString().equalsIgnoreCase("T") )
            {
                // Message successfully sent
                if( !newMessage )
                {
                    showAlert(this.getCurrentFocus(), "Your reply has been successfully sent!");
                }
                else
                {
                    TextView tbxTo = (TextView) findViewById(R.id.tbxcomposeto);
                    tbxTo.setText("");
                    TextView tbxSubject = (TextView) findViewById(R.id.tbxcomposeto);
                    tbxSubject.setText("");
                    TextView tbxMessage = (TextView) findViewById(R.id.tbxcomposemessage);
                    tbxMessage.setText("");
                    showAlert(this.getCurrentFocus(), "Your new message has been successfully sent!");
                }
                loadMessages();
            }
            else if( serverMsg.toString().equalsIgnoreCase("F") )
            {
                // Error occured at server
                showAlert(this.getCurrentFocus(), "Something wrong happened while we were trying to send your message. Please try again later.");
            }
            else
            {
                // Invalid inputs from user
                String[] errors = split( serverMsg.toString(), "|" );
                String toDisplay = "The following inputs are not valid:\n\n";
                for(int i = 1 ; i < errors.length ; i++)
                {
                    toDisplay += errors[i] + "\n";
                }
                showAlert(this.getCurrentFocus(), toDisplay);
            }
        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
        }
        catch (IOException ex)
        {
            // TODO Auto-generated catch block
        }
    }

    public void showMessage( String messageID )
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(viewMessageURL);

        showView( R.id.viewmessage );

        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("messageID", messageID));
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

            // showAlert(this.getCurrentFocus(), serverMsg.toString());

            JSONArray messageAttributes = new JSONArray( serverMsg.toString() );
            currentMsg = messageAttributes.getJSONObject(0);
            String senderName = messageAttributes.getString(1);
            String receiverName = messageAttributes.getString(2);
            currentMsgSenderId = messageAttributes.getString(3);
            String receiverUserId = messageAttributes.getString(4);
            TextView t;
            t = (TextView) findViewById( R.id.viewmessagemain );
            t.setText( "From: \n" + senderName + " (" + currentMsgSenderId +")\n\n"
                        + "To: \n" + receiverName + " (" + receiverUserId +")\n\n"
                        + "Subject: \n" + currentMsg.getString("subject") + "\n\n"
                        + "On " + currentMsg.getString("timeStamp") + ", " + senderName + " wrote: " + "\n\n"
                        + currentMsg.getString("message") );
        }
        catch (ClientProtocolException e)
        {
            Log.e( "LOADING INBOX", e.toString() );
            // TODO Auto-generated catch block
        }
        catch (IOException ex)
        {
            Log.e( "LOADING INBOX", ex.toString() );
            // TODO Auto-generated catch block
        }
        catch(JSONException exc)
        {
            Log.e( "LOADING INBOX", exc.toString() );
        }

        // Set up the onClickListener for the Back to Inbox button
        Button back = (Button) findViewById(R.id.btnbacktoinbox);
        back.setOnClickListener
        (
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        loadMessages();
                    }
                }
        );

        // Set up the onClickListener for the Mark As Read button
        Button markAsRead = (Button) findViewById(R.id.viewmessagemarkasread);
        markAsRead.setOnClickListener
        (
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Are you sure you want to mark this message as read?\n\n" +
                                    "Once it's marked as read, it will no longer be shown on your mobile device.")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                       final View v = this.v;
                                       final String m = currentMsg.getString("id");
                                       public void onClick(DialogInterface dialog, int id)
                                       {
                                            markAsRead(m);
                                       }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                                           public void onClick(DialogInterface dialog, int id)
                                           {
                                               dialog.cancel();
                                           }
                                   });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        catch(JSONException e)
                        {

                        }
                    }
                }
        );

        // Set up the onClickListener for the Reply this Message button
        Button reply = (Button) findViewById(R.id.btnreplymessage);
        reply.setOnClickListener
        (
                new View.OnClickListener()
                {
                    final JSONObject msg = currentMsg;
                    @Override
                    public void onClick(View v)
                    {
                        TextView replyText = (TextView) findViewById(R.id.tbxreplymessage);
                        String replyMsg = replyText.getText().toString();

                        if( replyMsg.length() <= 1 )
                        {
                            showAlert(v, "You haven't seem to have typed a reply message that makes sense.");
                        }
                        else
                        {
                            try
                            {
                                sendMessage(serverMessages[2], currentMsgSenderId, msg.getString("subject"), replyMsg, false);
                            }
                            catch(JSONException e)
                            {
                            }
                        }
                    }
                }
        );
    }

    public void markAsRead( String messageID )
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(markAsReadURL);

        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("messageID", messageID));
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

            if( serverMsg.toString().equalsIgnoreCase("T") )
            {
                // Message successfully sent
                showAlert(this.getCurrentFocus(), "This message has been marked as read.\n\n" +
                        "While this message will not be displayed on your mobile phone any more, you can still access it from a browser.");
                loadMessages();
            }
            else if( serverMsg.toString().equalsIgnoreCase("F") )
            {
                // Error occured at server
                showAlert(this.getCurrentFocus(), "Something wrong happened while we were trying to mark this message as read. Please try again later.");
            }

        }
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
        }
        catch (IOException ex)
        {
            // TODO Auto-generated catch block
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

    public void contactCabpooler()
    {
        MobileHome parent = (MobileHome) this.getParent();

        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
        to.setText( parent.getPotentialCabpooler() );
        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
        if( parent.getUserDestination() != null )
        {
            if( !parent.getUserDestination().equalsIgnoreCase("") && !parent.getUserDestination().equalsIgnoreCase("Not Defined") )
            {
                subject.setText("Cabpool to " + parent.getUserDestination());
            }
            else
            {
                subject.setText("Cabpool");
            }
        }
        else
        {
            subject.setText("Cabpool");
        }

        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);

        if( parent.getCurrentAddress() != null )
        {
            if( !parent.getCurrentAddress().equalsIgnoreCase("") )
            {
                message.setText("I am currently around " + parent.getCurrentAddress() + ". I understand that you are looking for a cab too, and sharing a cab might work out. Would you like to share a cab with me?");
            }
            else
            {
                message.setText("I am currently somewhere near you. I understand that you are looking for a cab too, and sharing a cab might work out. Would you like to share a cab with me?");
            }
        }
        else
        {
            message.setText("I am currently somewhere near you. I understand that you are looking for a cab too, and sharing a cab might work out. Would you like to share a cab with me?");
        }
        
        // User wants to compose a message
        showView( R.id.composemessage );
        hideView( R.id.messaginghome );

        parent.setPm( false );

        // Set up the onClickListener for the Discard button
        Button discard = (Button) findViewById(R.id.btndiscardnewmessage);
        discard.setOnClickListener
        (
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure you want to discard this message and return to your inbox?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                   final View v = this.v;
                                   public void onClick(DialogInterface dialog, int id)
                                   {
                                        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
                                        to.setText("");
                                        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
                                        subject.setText("");
                                        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);
                                        message.setText("");
                                        loadMessages();
                                   }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                                       public void onClick(DialogInterface dialog, int id)
                                       {
                                            dialog.cancel();
                                       }
                               });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
        );

        // Set up the onClickListener for the Send button
        Button send = (Button) findViewById(R.id.btnsendnewmessage);
        send.setOnClickListener
        (
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure you want to send this message?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                   final View v = this.v;
                                   public void onClick(DialogInterface dialog, int id)
                                   {
                                        TextView to = (TextView) findViewById(R.id.tbxcomposeto);
                                        String sendTo = to.getText().toString();
                                        TextView subject = (TextView) findViewById(R.id.tbxcomposesubject);
                                        String sendSubject = subject.getText().toString();
                                        TextView message = (TextView) findViewById(R.id.tbxcomposemessage);
                                        String sendMessage = message.getText().toString();
                                        sendMessage(serverMessages[2], sendTo, sendSubject, sendMessage, true);
                                   }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener(){
                                       public void onClick(DialogInterface dialog, int id)
                                       {
                                           dialog.cancel();
                                       }
                               });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
        );
    }

}
