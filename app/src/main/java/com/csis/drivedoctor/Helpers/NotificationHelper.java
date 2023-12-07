package com.csis.drivedoctor.Helpers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.csis.drivedoctor.R;

public class NotificationHelper {

    private static NotificationHelper shared_instance = null ;
    private NotificationHelper() {}

    private  Context cntxt;

    public static NotificationHelper getInstance() {
        if(shared_instance == null) {
            shared_instance = new NotificationHelper();

        }
        return shared_instance;
    }
    public void sendAppointmentNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(cntxt, "Appointment")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Appointment")
                .setContentText("You have new scheduled appointment soon.")
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(cntxt);
        if (ActivityCompat.checkSelfPermission(cntxt, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        notificationManager.notify(10, builder.build());
    }

    public void registerForNotification(Context cntxt) {
        this.cntxt = cntxt;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Appointment", "Appointment", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = cntxt.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

