package com.arehmanhameed.imagecompression;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arehmanhameed.imagecompression.Utils.Compressing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.arehmanhameed.imagecompression.Utils.Storing.storeImage;

public class CompressStoreCameraImage extends AppCompatActivity {

    //Declaring Objects
    private int GET_IMAGE = 1234;
    private Uri URI;
    private InputStream inputStream;
    private Bitmap originalBitmap;
    private ImageView originalImageView, scaledImageView;
    private RelativeLayout progressLayout;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_store_camera_image);

        //Set activity title
        this.setTitle("Compress & Store Camera Image");

        //Get support action bar to enable back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialization of objects
        originalImageView = (ImageView) findViewById(R.id.original_image);
        scaledImageView = (ImageView) findViewById(R.id.scaled_image);
        progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
    }

    /**
     * Responsible for all click events on activity_compress_store_camera_image views
     *
     * @param view
     */
    public void onClickGetCameraImage(View view) {
        //Initialize the intent
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Create a temp file with name [current_time_stamp].jpg
        file = new File(getApplicationContext().getExternalCacheDir(), String.valueOf(System.currentTimeMillis()) + ".jpg");

        //Get a full path to the temp file created in above step
        URI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);

        //set URI to camera intent to store captured image on that path
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, URI);

        /**uncomment if you want to allow only Camera to be open**/
//        PackageManager packageManager = getApplicationContext().getPackageManager();
//        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
//        for (int n = 0; n < list.size(); n++) {
//            if ((list.get(n).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
//                if (list.get(n).loadLabel(packageManager).toString().equalsIgnoreCase("Camera")) {
//                    takePicture.setPackage(list.get(n).packageName);
//                    break;
//                }
//            }
//        }

        //Grant camera intent permission to read URI
        takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        //Start camera intent to capture image
        startActivityForResult(takePicture, GET_IMAGE);

        //Show progress bar
        progressLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Responsible for actions after camera intent is finished
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the requestCode is equal to GET_IMAGE = 1234,
        //and resultCode is equal to RESULT_OK (Which means we have a result as an image)
        if (requestCode == GET_IMAGE && resultCode == RESULT_OK) {
            try {

                //Initialize with the URI of temp file which we already have given to camera intent to store image on it.
                inputStream = getContentResolver().openInputStream(URI);

                //Get original image as bitmap from input stream
                originalBitmap = BitmapFactory.decodeStream(inputStream);

                //Wait for 1 sec
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        //Set original image to original image view
                        originalImageView.setImageBitmap(originalBitmap);

                        //Declare and initialize with compressed image
                        Bitmap tempBitmap = Compressing.letsCompress(getApplicationContext(), URI);

                        //Store compressed image and get path of stored image
                        String tempPath = storeImage(tempBitmap);

                        //Get and Set scaled image to scaled image view
                        scaledImageView.setImageURI(Uri.parse(tempPath));

                        //Hide the progress bar
                        progressLayout.setVisibility(View.GONE);

                        //Delete the temp file
                        file.delete();

                        //Show the path of compressed file so that user can go and access the file
                        Snackbar.make(progressLayout,"To See Compress Image Go To : "+tempPath,Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
                    }
                }.start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else
            progressLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
