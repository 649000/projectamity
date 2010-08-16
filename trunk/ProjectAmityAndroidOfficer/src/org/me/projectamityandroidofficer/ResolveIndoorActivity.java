/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Nazri
 */
public class ResolveIndoorActivity extends Activity {
    //  private String ipAddress = "152.226.232.99";

    private String ipAddress = "10.0.1.3";
    private String resolveURL = "http://" + ipAddress + ":8080/ProjectAmity/NEAOfficer/resolveIndoorAndroid";
    private String resolveServerMsg = "";
    private EditText status,newDescription;
    private ImageButton image;
    private Button submit;
    private String reportID;
    protected Button _button;
    protected ImageView _image;
    protected String _path;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN = "photo_taken";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.resolveindoor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            reportID = extras.getString("ReportID");
        }
        status = (EditText) findViewById(R.id.resolveInStatusContent);
        newDescription = (EditText) findViewById(R.id.resolveInDescriptionContent);
        _image = (ImageView) findViewById(R.id.resolveInImageContent);
        _button = (Button) findViewById(R.id.resolveInCamera);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        _path = Environment.getExternalStorageDirectory() + "/ProjectAmity/Outdoor/" + " ProjectAmity" + sdf.format(cal.getTime()) + ".jpg";


        submit = (Button) findViewById(R.id.resolveInSubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i("ResolveOut Status", status.getText().toString());
                Log.i("ResolveOutIn", newDescription.getText().toString());
                if (status.getText().length() != 0 && newDescription.getText().length() != 0 && image.getDrawable() != null) {
                    //execute transmission to server
                } else {
                    invalidInput("Invalid Entry!");
                }
            }
        });
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
        outState.putBoolean(ResolveOutdoorActivity.PHOTO_TAKEN, _taken);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("onRestoreInstanceState", "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(ResolveOutdoorActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }
}
