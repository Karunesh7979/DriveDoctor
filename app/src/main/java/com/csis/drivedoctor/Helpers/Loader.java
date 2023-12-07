package com.csis.drivedoctor.Helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.csis.drivedoctor.R;

public class Loader {

    private static ProgressDialog progressDialog;

        public static void show(Context context) {

            try {

                int messageResourceId = R.string.loading;

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                int style;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    style = android.R.style.Theme_Material_Light_Dialog;
                } else {
                    //noinspection deprecation
                    style = android.R.style.Theme_Material_Light_Dialog;
                }

                progressDialog = new ProgressDialog(context, style);
                progressDialog.setMessage(context.getResources().getString(messageResourceId));
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                Log.d("Loader-->>", "showloader method -->>"+ e.getLocalizedMessage());
            }

        }

        public static void dismiss() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

    }
