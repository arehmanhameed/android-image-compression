package com.arehmanhameed.imagecompression.Utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by admin on 23-Oct-17.
 */

public class Storing {

    private static FileOutputStream outputStream;
    private static String filePath;

    public static String storeImage(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "ProfilePic");
        if (!file.exists()) {
            file.mkdirs();
        }

        filePath = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");

        try {
            outputStream = new FileOutputStream(filePath);

            /**write the compressed bitmap at the destination specified by filename.
             * you can change quality to 100 or 55 (as 55 is good)**/
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filePath;
    }
}
