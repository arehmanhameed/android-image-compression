package com.arehmanhameed.imagecompression.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 22-Oct-17.
 */

public class Compressing {

    private static Bitmap scaledBitmap, originalBitmap;
    private static InputStream inputStream;
    private static float maxHeight, maxWidth, imgRatio, maxRatio, ratioX, ratioY, middleX, middleY;
    private static int actualHeight, actualWidth;
    private static Canvas canvas;
    private static Matrix scaleMatrix;

    public static Bitmap letsCompress(Context context, Uri uri) {
        try {
            //create bitmap factory options to get width and height.
            BitmapFactory.Options options = new BitmapFactory.Options();

            //set options decode in bounds true.
            options.inJustDecodeBounds = true;

            //get input stream from uri
            inputStream = context.getContentResolver().openInputStream(uri);

            //decode bitmap from input stream.
            originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);

            //get actual image height and width.
            actualHeight = options.outHeight;
            actualWidth = options.outWidth;

            //check if image is landscape or portrait.
            if (actualHeight > actualWidth) {
                //its portrait.
                maxHeight = 816;
                maxWidth = 612;
            } else {
                //its landscape.
                maxHeight = 612;
                maxWidth = 816;
            }

            //calculate image ratio of original image.
            imgRatio = actualWidth / actualHeight;

            //calculate image ratio for required image.
            maxRatio = maxWidth / maxHeight;

            //width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                //check if original image ratio is less , greater or equal.
                if (imgRatio < maxRatio) {
                    // if less then recalculate image ratio from max required height and actual height.
                    imgRatio = maxHeight / actualHeight;
                    //calculate and update actual width in int.
                    actualWidth = (int) (imgRatio * actualWidth);
                    //update actual height with max height in int.
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    //if greater then recalculate image ratio from max width and actual width.
                    imgRatio = maxWidth / actualWidth;
                    //calculate and update actual height in int.
                    actualHeight = (int) (imgRatio * actualHeight);
                    //update actual width with max width in int.
                    actualWidth = (int) maxWidth;
                } else {
                    //if neither less or greater then update actual height to max height and actual width to max width in int.
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;
                }
            }

            //set options sample size getting from calculateSampleSize() based on actual width and height calculated above.
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

            //set options decode in just bounds to false.
            options.inJustDecodeBounds = false;

            //set options temporary storage byte array size to 16 * 1024.
            options.inTempStorage = new byte[16 * 1024];

            //get input stream from uri again.
            inputStream = context.getContentResolver().openInputStream(uri);

            //decode bitmap from input stream based on bitmap factory options we just initialized with custom values.
            originalBitmap = BitmapFactory.decodeStream(inputStream, null, options);

            // calculate x-axis and y-axis ratios.
            ratioX = actualWidth / (float) options.outWidth;
            ratioY = actualHeight / (float) options.outHeight;

            // calculate x-axis and y-axis middle points.
            middleX = actualWidth / 2.0f;
            middleY = actualHeight / 2.0f;

            //declare Matrix
            scaleMatrix = new Matrix();

            //set matrix scale by ratios and middle point we calculated above.
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            //create bitmap by for required height and width we calculated.
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

            //initialize canvas on bitmap created above.
            canvas = new Canvas(scaledBitmap);

            //set canvas matrix to matrix we initialized above.
            canvas.setMatrix(scaleMatrix);

            //draw bitmap from original bitmap we have as per last calculated values so that it is perfectly drawn on canvas of required size.
            canvas.drawBitmap(originalBitmap, middleX - originalBitmap.getWidth() / 2, middleY - originalBitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

            //maintaining rotation of image as is was not correct in samsung and lg phones.
//            ExifInterface exif;
//            exif = new ExifInterface(uri.getPath());
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
//            Log.i("path ", uri.getPath());
//            Log.i("NORMAL ORIENTATION ", String.valueOf(ExifInterface.ORIENTATION_NORMAL));
//            Log.i("ROTATE 90 ORIENTATION ", String.valueOf(ExifInterface.ORIENTATION_ROTATE_90));
//            Log.i("ROTATE 180 ORIENTATION ", String.valueOf(ExifInterface.ORIENTATION_ROTATE_180));
//            Log.i("ROTATE 270 ORIENTATION ", String.valueOf(ExifInterface.ORIENTATION_ROTATE_270));
//
//            Matrix matrix = new Matrix();
//            if (orientation == 6) {
//                matrix.postRotate(90);
//            } else if (orientation == 3) {
//                matrix.postRotate(180);
//            } else if (orientation == 8) {
//                matrix.postRotate(270);
//            }
//
//            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            //create bitmap from bitmap we have drawn on canvas.
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), scaleMatrix, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //return scaled bitmap
        return scaledBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);

            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
}
