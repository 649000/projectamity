/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Nazri
 */
public class ResolveIndoorActivity extends Activity {

    //School's IP Address:
    // private String ipAddress = "152.226.232.16";
    //Home's IP Address:
    //  private String ipAddress = "10.0.1.3";
    // private String ipAddress = "10.0.2.2";
    // private String ipAddress = "172.10.20.2":8080;
    private String ipAddress = "www.welovepat.com";
    private String resolveURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/resolveIndoorAndroid";
    private String logoutURL = "http://" + ipAddress + "/ProjectAmity/NEAOfficer/logoutAndroid";
    private String resolveServerMsg = "";
    private EditText status, newDescription;
    private ImageButton image;
    private Button submit;
    private String reportID;
    protected Button _button;
    protected ImageView _image;
    protected String _path, imageName, userid;
    protected boolean _taken;
    protected static final String PHOTO_TAKEN = "photo_taken";
    private Uri imageURI;
    private File imageFile;
    private Bitmap b;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.resolveindoor);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            reportID = extras.getString("ReportID");
            userid = extras.getString("userid");
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Random r = new Random();

        imageName = "ProjectAmity_" + userid + "_" + sdf.format(cal.getTime()) + "_" + r.nextInt() + ".jpg";
        _path = Environment.getExternalStorageDirectory() + "/ProjectAmity/Indoor/" + "ProjectAmity_" + imageName;




        submit = (Button) findViewById(R.id.resolveInSubmit);
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i("ResolveIndoor Status", status.getText().toString());
                Log.i("ResolveOutIn", newDescription.getText().toString());
                if (status.getText().length() != 0 && newDescription.getText().length() != 0 && _image.getDrawable() != null) {
                    //execute transmission to server
                    submitResolvedReport();
                } else {
                    invalidInput("Empty fields detected.");
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
                temp(data);
                break;
        }
    }

    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
        _image.setImageBitmap(b);
        _image.setVisibility(View.GONE);

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

    public void submitResolvedReport() {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            //File f = new File(filename);

            HttpPost httpost = new HttpPost(resolveURL);
            MultipartEntity entity = new MultipartEntity();
            File file = new File(_path);
            ContentBody cbFile = new FileBody(imageFile, "image/jpeg");
            ContentBody cbDescription = new StringBody(newDescription.getText().toString());
            ContentBody cbStatus = new StringBody(status.getText().toString());
            ContentBody cbImageName = new StringBody(imageName);
            ContentBody cbreportID = new StringBody(reportID);


            entity.addPart("image", cbFile);
            entity.addPart("newdescription", cbDescription);
            entity.addPart("status", cbStatus);
            entity.addPart("imageName", cbImageName);
            entity.addPart("reportid", cbreportID);

            httpost.setEntity(entity);
            HttpResponse response;
            response = httpclient.execute(httpost);
            Log.i("submitResolvedReport", "Login form get: " + response.getStatusLine());
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
            resolveServerMsg = serverMsg.toString().trim();
            Log.i("Server Response", resolveServerMsg);
            is.close();

            if (resolveServerMsg.equalsIgnoreCase("T")) {
                Intent i = new Intent();
                i.setClassName("org.me.projectamityandroidofficer", "org.me.projectamityandroidofficer.ReportHomeActivity");
                i.putExtra("userid", userid);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Report has been successfully updated.", Toast.LENGTH_LONG).show();
            } else if (resolveServerMsg.equalsIgnoreCase("F")) {
                invalidInput("Unable to execute task on server.");
            }

        } catch (Exception ex) {
            Log.d("submitResolvedReport", "Upload failed: " + ex.getMessage() + " Stacktrace: " + ex.getStackTrace());
            invalidInput("Unable to execute task.");

        } finally {
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = new MenuInflater(this);
        //MenuItem item = menu.add(R.id.logoutMenu);
        inflater.inflate(R.menu.logout, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutMenu:
                setLogout();
                return true;
        }
        return false;
    }

    public void setLogout() {
        Log.i("Menu", "Logout Menu pressed Start");
        StringBuilder serverMsg = new StringBuilder("");
        InputStream is = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(logoutURL);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("userid", userid));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            is = response.getEntity().getContent();
            int ch = is.read();
            while (ch != -1) {
                serverMsg.append((char) ch);
                ch = is.read();
            }
            is.close();
        } catch (ClientProtocolException e) {
            Log.e("Building List Exception", e.toString());
        } catch (IOException e) {
            Log.e("Building List Exception", e.toString());
        }

        if (serverMsg.toString().trim().equalsIgnoreCase("T")) {
            Log.i("Menu", "Logout Success");
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(i);

        } else if (serverMsg.toString().trim().equalsIgnoreCase("F")) {
            Toast.makeText(getApplicationContext(), "Unable to execute task on server.", Toast.LENGTH_SHORT).show();
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
