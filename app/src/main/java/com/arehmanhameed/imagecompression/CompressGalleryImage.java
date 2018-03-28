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
    private int GET_IMAGE = 1234;
    private Uri URI;
    private InputStream inputStream;
    private Bitmap originalBitmap;
    private ImageView originalImageView, scaledImageView;
    private RelativeLayout progressLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_gallery_image);
        this.setTitle("Compress Gallery Image");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        originalImageView = (ImageView) findViewById(R.id.original_image);
        scaledImageView = (ImageView) findViewById(R.id.scaled_image);
        progressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
    }

    public void onClickGetGalleryImage(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Source"), GET_IMAGE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE && resultCode == RESULT_OK) {
            URI = data.getData();
            try {
                inputStream = getContentResolver().openInputStream(URI);
                originalBitmap = BitmapFactory.decodeStream(inputStream);
                new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        originalImageView.setImageBitmap(originalBitmap);
                        scaledImageView.setImageBitmap(Compressing.letsCompress(getApplicationContext(), URI));
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
