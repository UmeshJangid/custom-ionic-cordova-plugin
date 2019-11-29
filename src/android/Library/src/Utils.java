package com.synconset;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;

import io.ionic.starter.R;


public class Utils {


    public static long checkFileSize(String path) {
        long length = 0;
        try{
            File file = new File(path);
            if(file.exists()){
                Log.e("Error", "File Exist @Path "+path);
                 length = file.length();
                length = length / 1024;
                System.out.println("File Path : " + file.getPath() + ", File size : " + length + " KB");
            }else{
                Log.e("Error", "File Not Exist@Path"+path);
            }
        }catch (Exception e )
        {
            e.printStackTrace();
        }

        return length;

    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static void checkPermission(Fragment fragment, String permissionString, int permissionCode) {
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || fragment.getContext() == null)
            return;
        int existingPermissionStatus = ContextCompat.checkSelfPermission(fragment.getContext(),
                permissionString);
        if (existingPermissionStatus == PackageManager.PERMISSION_GRANTED) return;
        fragment.requestPermissions(new String[]{permissionString}, permissionCode);
    }

    public static void checkPermission(Activity fragment, String permissionString, int permissionCode) {
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || fragment.getApplicationContext() == null)
            return;
        int existingPermissionStatus = ContextCompat.checkSelfPermission(fragment.getApplicationContext(),
                permissionString);
        if (existingPermissionStatus == PackageManager.PERMISSION_GRANTED) return;
        fragment.requestPermissions(new String[]{permissionString}, permissionCode);
    }

    public static boolean isReadStorageGranted(Context context) {
        int storagePermissionGranted = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        return storagePermissionGranted == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWriteStorageGranted(Context context) {
        int storagePermissionGranted = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return storagePermissionGranted == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCameraGranted(Context context) {
        int cameraPermissionGranted = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        return cameraPermissionGranted == PackageManager.PERMISSION_GRANTED;
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    //Made: By Umesh Jangid
    public static void askForInput(Context context, String title, String message, String positiveButtonText, String negativeButtonText, boolean showOnlyOneButton, final AlertDialogCallback<String> callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage(message);
        alert.setPositiveButton(positiveButtonText != null ? positiveButtonText : "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.alertDialogCallback("1");
            }
        });
        if (!showOnlyOneButton) {
            alert.setNegativeButton(negativeButtonText != null ? negativeButtonText : "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                    callback.alertDialogCallback("0");
                }
            });
        }
        alert.setCancelable(false);
        alert.show();
    }


}