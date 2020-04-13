package com.deeefoo.myappl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static int notification_id = 1;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        generateNotification(remoteMessage.getNotification().getBody(),
                remoteMessage.getNotification().getTitle());
    }

    private void generateNotification(String body, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notification_id > 1073741824) {
            notification_id = 0;
        }

        notificationManager.notify(notification_id,
                notificationBuilder.build());


    }


    @Override
    public void onNewToken(@NonNull String s) {

    }
}
