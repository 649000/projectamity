/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
public class IndoorReporting extends Activity {

    /** Called when the activity is first created. */
    private String ipAddress = "10.0.2.2:8080";
   // private String ipAddress = "www.welovepat.com";
    private String reportURL = "http://" + ipAddress + "/ProjectAmity/reportMobile/indoorReportAndroid";
    private String userid, _path, imageName, reportServerMsg, serverMessages[], level, location;
    private double longitude, latitude, altitude = 0.0;
    private EditText title, description;
    private ImageView _image;
    private TextView loc;
    private Button _button, submit;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN = "photo_taken";
    private ProgressDialog myProgressDialog = null;
    private Uri imageURI;
    private File imageFile;
    private Bitmap b;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userid = extras.getString("userid");
            serverMessages = extras.getStringArray("serverMessages");
            level = extras.getString("level");
            location = extras.getString("location");
        }
        setContentView(R.layout.indoorreporting);

        title = (EditText) findViewById(R.id.indoorTitleContent);
        description = (EditText) findViewById(R.id.indoorDescriptionContent);
        loc = (TextView) findViewById(R.id.indoorLocationContent);
        loc.setText("Level " + level + "\nLocation " + location);
        _image = (ImageView) findViewById(R.id.indoorImage);
        _button = (Button) findViewById(R.id.indoorCamera);
        _button.setOnClickListener(new ButtonClickHandler());

        //Creating the neccessary folders to store the images.
        File mainFile = new File(Environment.getExternalStorageDirectory(), "ProjectAmity");
        if (mainFile.exists() == false) {
            mainFile.mkdir();
        }
        File fileOutdoor = new File(Environment.getExternalStorageDirectory() + "/ProjectAmity", "Indoor");
        if (fileOutdoor.exists() == false) {
            fileOutdoor.mkdir();
        }

        //Retrieving current time
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Random r = new Random();

        imageName = "ProjectAmity_" + userid + "_" + sdf.format(cal.getTime()) + "_" + r.nextInt() + ".jpg";
        _path = Environment.getExternalStorageDirectory() + "/ProjectAmity/Indoor/" + imageName;
        submit = (Button) findViewById(R.id.indoorSubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (title.getText().length() != 0 && description.getText().length() != 0 && _image.getDrawable() != null) {
                    submitReport();
                } else {
                    invalidInput("Empty fields detected.");
                }
            }
        });
    }

    public void invalidInput(String message) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage(message);
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(getApplicationContext(), "OK button clicked", Toast.LENGTH_LONG).show();
                return;
            }
        });
        alertbox.show();
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
          //_image.setVisibility(View.GONE );

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
            ContentBody cbLevel = new StringBody(level + "");
            ContentBody cbLocation = new StringBody(location + "");
            ContentBody cbUserid = new StringBody(userid + "");

            entity.addPart("image", cbFile);
            entity.addPart("imageName", cbImageName);
            entity.addPart("description", cbDescription);
            entity.addPart("title", cbTitle);
            entity.addPart("level", cbLevel);
            entity.addPart("location", cbLocation);
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
}
