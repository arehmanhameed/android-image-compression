package com.arehmanhameed.imagecompression;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Compress Any Image");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gallery_image: {
                intent = new Intent(this, CompressGalleryImage.class);
                break;
            }
            case R.id.camera_image: {
//                intent = new Intent(this, CompressCameraImage.class);
                break;
            }
            case R.id.gallery_image_store: {
//                intent = new Intent(this, CompressStoreGalleryImage.class);
                break;
            }
            case R.id.camera_image_store: {
//                intent = new Intent(this, CompressStoreCameraImage.class);
                break;
            }
            case R.id.gallery_image_crop: {
                Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                intent = null;
//                intent = new Intent(this, CompressStoreCameraImage.class);
                break;
            }
            case R.id.camera_image_crop: {
                Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                intent = null;
//                intent = new Intent(this, CompressStoreCameraImage.class);
                break;
            }
        }
        if (intent != null)
            startActivity(intent);
    }
}
