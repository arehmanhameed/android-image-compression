package com.arehmanhameed.imagecompression.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by admin on 23-Oct-17.
 */

public class Permission {

    private static String[] STORAGE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static int GET_PERMISSION = 100;

    public static void requestMultipleStoragePermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, STORAGE_PERMISSIONS, GET_PERMISSION);
    }

    public static boolean checkMultipleStoragePermissions(Activity activity) {
        for (String permissions : STORAGE_PERMISSIONS)
            if (ActivityCompat.checkSelfPermission(activity, permissions) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }

}
