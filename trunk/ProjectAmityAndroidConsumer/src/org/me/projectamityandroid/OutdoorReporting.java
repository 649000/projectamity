/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Nazri
 */
public class OutdoorReporting extends Activity implements LocationListener {

    private String ipAddress = "10.0.1.3";
    private String reportURL = "http://" + ipAddress + ":8080/ProjectAmity/reportMobile/outdoorReportAndroid";
    private String userid, _path, imageName, reportServerMsg, serverMessages[];
    private double longitude, latitude, altitude = 0.0;
    private EditText title, description;
    private TextView loc;
    private ImageView _image;
    private Button _button, submit;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN = "photo_taken";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            serverMessages = extras.getStringArray("serverMessages");
        }
        setContentView(R.layout.outdoorreporting);

        title = (EditText) findViewById(R.id.outdoorTitleContent);
        description = (EditText) findViewById(R.id.outdoorDescriptionContent);
        loc = (TextView) findViewById(R.id.outdoorLocationContent);
        _image = (ImageView) findViewById(R.id.outdoorImage);
        _button = (Button) findViewById(R.id.outdoorCamera);
        _button.setOnClickListener(new ButtonClickHandler());

        //Creating the neccessary folders to store the images.
        File mainFile = new File(Environment.getExternalStorageDirectory(), "ProjectAmity");
        if (mainFile.exists() == false) {
            mainFile.mkdir();
        }
        File fileOutdoor = new File(Environment.getExternalStorageDirectory() + "/ProjectAmity", "Outdoor");
        if (fileOutdoor.exists() == false) {
            fileOutdoor.mkdir();
        }

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) 1000, (float) 500.0, this);
        Log.i("Latitude",latitude+"");
        Log.i("Longitude",longitude+"");
        

        //Retrieving current time;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Random r = new Random();

        imageName = "ProjectAmity_" + userid + "_" + sdf.format(cal.getTime()) + "_" + r.nextInt() + ".jpg";
        _path = Environment.getExternalStorageDirectory() + "/ProjectAmity/Outdoor/" + "ProjectAmity_" + imageName;
        submit = (Button) findViewById(R.id.outdoorSubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (title.getText().length() != 0 && description.getText().length() != 0 && _image.getDrawable() != null && loc.getText().toString().length() != 0) {
                    //execute transmission to server
                    submitReport();
                } else {
                    invalidInput("Empty fields detected.");
                }
            }
        });
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            loc.setText("Latitude: " + latitude +"\nLonigtude: "+ longitude);
        }
        else
            invalidInput("Unable to get GPS Coordinates");
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
     
    }

    public void onProviderEnabled(String arg0) {
        
    }

    public void onProviderDisabled(String arg0) {
        
    }

    public class ButtonClickHandler implements View.OnClickListener {

        public void onClick(View view) {
            startCameraActivity();
        }
    }

    protected void startCameraActivity() {
        File file = new File(_path);
        Uri outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("OnActivityResult", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("OnActivityResult", "User cancelled");
                break;

            case -1:
                onPhotoTaken();
                break;
        }
    }

    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
        _image.setImageBitmap(bitmap);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(OutdoorReporting.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("onRestoreInstanceState", "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(OutdoorReporting.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }

    public void invalidInput(String message) {
        // prepare the alert box
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);

        // set the message to display
        alertbox.setMessage(message);

        // add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            // click listener on the alert box
            public void onClick(DialogInterface arg0, int arg1) {
                // the button was clicked
                //Toast.makeText(getApplicationContext(), "OK button clicked", Toast.LENGTH_LONG).show();
                return;
            }
        });
        // show it
        alertbox.show();

    }

    public void submitReport() {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            HttpPost httpost = new HttpPost(reportURL);
            MultipartEntity entity = new MultipartEntity();
            File file = new File(_path);
            ContentBody cbFile = new FileBody(file, "image/jpeg");
            ContentBody cbDescription = new StringBody(description.getText().toString());
            ContentBody cbTitle = new StringBody(title.getText().toString());
            ContentBody cbImageName = new StringBody(imageName);
            ContentBody cbLatitude = new StringBody(latitude + "");
            ContentBody cbLongitude = new StringBody(longitude + "");
            ContentBody cbAltitude = new StringBody(altitude + "");
            ContentBody cbUserid = new StringBody(userid + "");

            entity.addPart("image", cbFile);
            entity.addPart("imageName", cbImageName);
            entity.addPart("description", cbDescription);
            entity.addPart("title", cbTitle);
            entity.addPart("latitude", cbLatitude);
            entity.addPart("longitude", cbLongitude);
            entity.addPart("altitude", cbAltitude);
            entity.addPart("userid", cbUserid);

            httpost.setEntity(entity);
            HttpResponse response;
            response = httpclient.execute(httpost);
            Log.i("submitReport", "Login form get: " + response.getStatusLine());
            if (entity != null) {
                entity.consumeContent();
            }

            InputStream is = response.getEntity().getContent();
            StringBuilder serverMsg = new StringBuilder("");
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);
                ch = is.read();
            }
            reportServerMsg = serverMsg.toString().trim();
            Log.i("Server Response", reportServerMsg);
            is.close();

            if (reportServerMsg.equalsIgnoreCase("T")) {
                Intent i = new Intent();
                i.setClassName("org.me.projectamityandroid", "org.me.projectamityandroid.MobileHome");
                i.putExtra("userid", userid);
                i.putExtra("serverMessages", serverMessages);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Report has been successfully submitted.", Toast.LENGTH_LONG).show();
            } else if (reportServerMsg.equalsIgnoreCase("F")) {
                invalidInput("Unable to execute task on server.");
            }

        } catch (Exception ex) {
            Log.d("submitReport", "Upload failed: " + ex.getMessage() + " Stacktrace: " + ex.getStackTrace());
            invalidInput("Unable to execute task.");
        }
    }
}
