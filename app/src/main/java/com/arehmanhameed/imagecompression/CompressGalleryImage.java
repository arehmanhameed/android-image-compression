package com.arehmanhameed.imagecompression;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arehmanhameed.imagecompression.Utils.Compressing;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CompressGalleryImage extends AppCompatActivity {
    //Declaring objects
    private int GET_IMAGE = 1234;
    private Uri URI;
    private InputStream inputStream;
    private Bitmap originalBitmap;
    private ImageView originalImageView, scaledImageView;
    private RelativeLayout progressLayout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_gallery_image);

        //Set title to activity
        this.setTitle("Compress Gallery Image");

        //Get support action bar to enable back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialization of objects
        originalImageView = findViewById(R.id.original_image);
        scaledImageView = findViewById(R.id.scaled_image);
        progressLayout = findViewById(R.id.progress_layout);
    }

    /**
     * Responsible for all click events on activity_compress_gallery_image views
     * @param view
     */
    public void onClickGetGalleryImage(View view) {
        //Initialize camera intent
        intent = new Intent();

        //Set action to intent to get content
        intent.setAction(Intent.ACTION_GET_CONTENT);

        //Set content type to be "Image"
        intent.setType("image/*");

        //Start activity for result
        startActivityForResult(Intent.createChooser(intent, "Select Source"), GET_IMAGE);

        //Show progress bar
        progressLayout.setVisibility(View.VISIBLE);
    }


    /**
     * Responsible for actions after gallery intent is finished
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the requestCode is equal to GET_IMAGE = 1234,
        //and resultCode is equal to RESULT_OK (Which means we have a result as an image)
        if (requestCode == GET_IMAGE && resultCode == RESULT_OK) {

            //Get image path
            URI = data.getData();
            try {
                //Set input Stream from URI
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

                        //Get and Set scaled image to scaled image view
                        scaledImageView.setImageBitmap(Compressing.letsCompress(getApplicationContext(), URI));

                        //Hide the progress bar
                        progressLayout.setVisibility(View.GONE);
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
