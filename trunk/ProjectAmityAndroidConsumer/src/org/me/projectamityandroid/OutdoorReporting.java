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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
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
import com.google.android.maps.GeoPoint;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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

    private String ipAddress = "10.0.2.2:8080";
    // private String ipAddress = "www.welovepat.com";
    private String reportURL = "http://" + ipAddress + "/ProjectAmity/reportMobile/outdoorReportAndroid";
    private String userid, _path, imageName, reportServerMsg, serverMessages[];
    private double longitude, latitude, altitude = 0.0;
    private EditText title, description;
    private TextView loc;
    private ImageView _image;
    private Button _button, submit;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN = "photo_taken";
    private Uri imageURI;
    private File imageFile;
    private Bitmap b;

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

        title.setText("An Example of a Report");
        description.setText("This is an example of a report being submitted via Project Amity's Location Based Reporting System.");

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

        Criteria c = new Criteria();
        c.setAccuracy(1);
        c.setCostAllowed(true);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(lm.getBestProvider(c, false), (long) 1000, (float) 50.0, this);
        Log.i("Latitude, Longitude", latitude + ", " + longitude);
        

        //Retrieving current time;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Random r = new Random();

        imageName = "ProjectAmity_" + userid + "_" + sdf.format(cal.getTime()) + "_" + r.nextInt() + ".jpg";
        _path = Environment.getExternalStorageDirectory() + "/ProjectAmity/Outdoor/" + imageName;
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
            GeoPoint point = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
            String add = "";
            Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

            try {
                List<Address> addresses = geoCoder.getFromLocation(
                        latitude,
                        longitude, 1);
                if (addresses.size() > 0) {
                    for (int i = 0; i < addresses.get(i).getMaxAddressLineIndex();
                            i++) {
                        add += addresses.get(0).getAddressLine(0) + "\n";
                        add += addresses.get(0).getCountryName() + " " + addresses.get(0).getPostalCode();
                    }
                }
            } catch (Exception e) {
                Log.e("Geocoder", e.toString());
            }
            //loc.setText("Latitude: " + latitude + "\nLonigtude: " + longitude);
            loc.setText(add);
        } else {
            invalidInput("Unable to get GPS Coordinates");
        }
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
                temp(data);
                break;
        }
    }

    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
        _image.setImageBitmap(b);
//          _image.setVisibility(View.GONE );

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

    public void temp(Intent data) {

        imageURI = data.getData();
        imageFile = convertImageUriToFile(imageURI, this);
        b = (Bitmap) data.getExtras().get("data");
        onPhotoTaken();
    }

    public static File convertImageUriToFile(Uri imageUri, Activity activity) {

        Cursor cursor = null;

        try {

            String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};

            cursor = activity.managedQuery(imageUri,
                    proj, // Which columns to return

                    null, // WHERE clause; which rows to return (all rows)

                    null, // WHERE clause selection arguments (none)

                    null); // Order-by clause (ascending by name)

            int file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

            if (cursor.moveToFirst()) {

                String orientation = cursor.getString(orientation_ColumnIndex);

                return new File(cursor.getString(file_ColumnIndex));

            }

            return null;

        } finally {

            if (cursor != null) {

                cursor.close();
            }

        }

    }

    public void submitReport() {

        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();

            HttpPost httpost = new HttpPost(reportURL);
            MultipartEntity entity = new MultipartEntity();
            File file = new File(_path);
            ContentBody cbFile = new FileBody(imageFile, "image/jpeg");
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
