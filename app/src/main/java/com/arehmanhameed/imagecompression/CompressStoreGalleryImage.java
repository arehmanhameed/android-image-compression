package com.arehmanhameed.imagecompression;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.arehmanhameed.imagecompression.Utils.Compressing;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.arehmanhameed.imagecompression.Utils.Permission.checkMultipleStoragePermissions;
import static com.arehmanhameed.imagecompression.Utils.Permission.requestMultipleStoragePermissions;
import static com.arehmanhameed.imagecompression.Utils.Storing.storeImage;

public class CompressStoreGalleryImage extends AppCompatActivity {

    //Declaring Objects
    private static int GET_PERMISSION = 100;
    private int GET_IMAGE = 2255;
    private RelativeLayout progressLayout;
    private ImageView originalImageView, scaledImageView;
    private Uri URI;
    private InputStream inputStream;
    private Bitmap originalBitmap;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_store_gallery_image);

        //Set activity title
        this.setTitle("Compress & Store Gallery Image");

        //Get support action bar to enable back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialization of objects
        progressLayout = findViewById(R.id.progress_layout);
        originalImageView = findViewById(R.id.original_image);
        scaledImageView = findViewById(R.id.scaled_image);

        //Check is storage permission is granted or not
        if (!checkMultipleStoragePermissions(this))
            //If not ask for the storage permission
            requestMultipleStoragePermissions(this);
    }

    /**
     * Reponsible for request permission result
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Check if request code is equal to GET_PERMISSION = 1000
        if (requestCode == GET_PERMISSION) {
            //Check if we received permission or not
            if (!checkMultipleStoragePermissions(this)) {
                Toast.makeText(this, "You have to allow storage permission", Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(this, "You have allowed storage permission", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Responsible for all click events on activity_compress_store_gallery_image views
     *
     * @param view
     */
    public void onClickGetGalleryImage(View view) {
        if(checkMultipleStoragePermissions(this)) {
            //Initialize the intent
            intent = new Intent();

            //Set action to intent as content
            intent.setAction(Intent.ACTION_GET_CONTENT);

            //Set content typoe to be 'Image'
            intent.setType("image/*");

            //Start activity for result
            startActivityForResult(Intent.createChooser(intent, "Select Source"), GET_IMAGE);

            //show progress bar
            progressLayout.setVisibility(View.VISIBLE);
        }else{
            requestMultipleStoragePermissions(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Check if the requestCode is equal to GET_IMAGE = 2255,
        //and resultCode is equal to RESULT_OK (Which means we have a result as an image)
        if (requestCode == GET_IMAGE && resultCode == RESULT_OK) {

            //Get URI from data
            URI = data.getData();
            try {

                //Get input stream from URI
                inputStream = getContentResolver().openInputStream(URI);

                //Get original image from input stream
                originalBitmap = BitmapFactory.decodeStream(inputStream);

                //Wait for 1sec
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                        //Declare and initialize with compressed image
                        Bitmap tempBitmap = Compressing.letsCompress(getApplicationContext(), URI);

                        //Store compressed image and get path of stored image
                        String tempPath = storeImage(tempBitmap);

                        //Set original image to original image view
                        originalImageView.setImageBitmap(originalBitmap);

                        //Get and Set scaled image to scaled image view
                        scaledImageView.setImageURI(Uri.parse(tempPath));

                        //Hide the progress bar
                        progressLayout.setVisibility(View.GONE);

                        //Show the path of compressed file so that user can go and access the file
                        Snackbar.make(progressLayout, "To See Compress Image Go To : " + tempPath, Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
                    }
                }.start();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else
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
