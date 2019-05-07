package com.libgdx.piggyrun;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

public class AlarmReceiver extends BroadcastReceiver {
    public static boolean inGame;
    public static NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!inGame && intent.getBooleanExtra("enable",true)) {
            Notification.Builder builder = new Notification.Builder(context);
            Intent intentActiv = new Intent(context, AndroidLauncher.class);
            intentActiv.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            builder
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(PendingIntent.getActivity(context, 0, intentActiv, PendingIntent.FLAG_CANCEL_CURRENT))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                    .setAutoCancel(true)
                    .setTicker("Piggy wants to run!")
                    .setContentTitle("Piggy Run")
                    .setContentText("Piggy wants to run!")
                    .setWhen(System.currentTimeMillis())
                    .setOnlyAlertOnce(true);
            Notification notification = builder.build();
            notification.defaults = Notification.DEFAULT_ALL;
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(1, notification);
        }
    }
}
