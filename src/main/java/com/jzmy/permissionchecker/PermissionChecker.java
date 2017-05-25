package com.jzmy.permissionchecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xuqinchao on 17/2/22.
 */

public class PermissionChecker {

    public static boolean lackPermission(Context context, String permission){
        return android.support.v4.content.PermissionChecker.checkSelfPermission(context, permission)
                == android.support.v4.content.PermissionChecker.PERMISSION_DENIED;
    }

    public static boolean lackPermissions(Context context, String... permissions){
        for (String permission : permissions) {
            if (lackPermission(context, permission)) return true;
        }
        return false;
    }

    public static void requestPermissions(final Activity activity, final int requestCode,
                                         HashMap<String, String> dialogParam,
                                         DialogInterface.OnClickListener cancelListener,
                                         final String... permissions){
        if (shouldShowRequestPermissionRationaleInner(activity, permissions)) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(dialogParam.get("title"))
                    .setMessage(dialogParam.get("message"))
                    .setNegativeButton(android.R.string.cancel, cancelListener)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissionsInner(activity, requestCode, permissions);
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            requestPermissionsInner(activity, requestCode, permissions);
        }
    }

    private static void requestPermissionsInner(Activity activity, int requestCode, String... permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, requestCode);
        }
    }

    private static boolean shouldShowRequestPermissionRationaleInner(Activity activity, String... permissions){
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) return true;
        }
        return false;
    }

    public static ArrayList<String> getPermissionsShouldShowRequest(Activity activity, String... permissions){
        ArrayList<String> result = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) result.add(permission);
        }
        return result;
    }

    public static boolean hasAllPermissionRequest(@NonNull int[] grantResults){
        if (grantResults == null || grantResults.length == 0) return false;
        for (int grant : grantResults) {
            if (grant == android.support.v4.content.PermissionChecker.PERMISSION_DENIED) return false;
        }
        return true;
    }
}
