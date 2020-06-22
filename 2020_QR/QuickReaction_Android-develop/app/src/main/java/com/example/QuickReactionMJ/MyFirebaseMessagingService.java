package com.example.QuickReactionMJ;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.QuickReactionMJ.db.SharedPreferenceController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String token = "";

    @Override
    public void onNewToken(String s) {
        Log.d("FCM Log","Refreshed token:" + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakeLock.acquire(3000);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceID failed", task.getException());
                            return;
                        }
                        // 현재 토큰 검색!
                        token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰:" + token);
                    }
                });

        Log.i("노티 접근 x", "Baaaaaaaaaaaaaaaaaaad");
        try {
            Map<String, String> data = remoteMessage.getData();
            String title1 = URLDecoder.decode(data.get("title"), "UTF-8");
            String body1 = URLDecoder.decode(data.get("body1"), "UTF-8");
            String body2 = URLDecoder.decode(data.get("body2"), "UTF-8");
            String body3 = URLDecoder.decode(data.get("body3"), "UTF-8");
            String body4 = URLDecoder.decode(data.get("body4"), "UTF-8");
            String message = body1 + body2 + body3 + body4;

                /*UserActivity userActivity = new UserActivity();
                userActivity.getMessage(title1, message)*/


            createNotification(title1, message);

            Log.i("title", title1);
            Log.i("text", message);


        } catch (Exception e) {
            e.getMessage();
        }


    }
    private void createNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.drawable.masklogo3);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_dialog_info));

        Uri soundUri= RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(soundUri);
        builder.setVibrate(new long[]{0,3000});

        builder.setColor(Color.RED);

        if(SharedPreferenceController.INSTANCE.getAuthorizationOfRole(MyFirebaseMessagingService.this).equals("USER")) {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("title", title);
            intent.putExtra("token", token);
            PendingIntent pending= PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pending);
        }
        else if(SharedPreferenceController.INSTANCE.getAuthorizationOfRole(MyFirebaseMessagingService.this).equals("ADMIN")){
            Intent intent = new Intent(this, ManagerActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("title", title);
            intent.putExtra("token", token);
            PendingIntent pending= PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pending);
        }

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        notificationManager.notify(1, builder.build());
    }
}
