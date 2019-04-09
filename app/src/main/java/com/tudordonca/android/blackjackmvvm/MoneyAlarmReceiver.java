package com.tudordonca.android.blackjackmvvm;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tudordonca.android.blackjackmvvm.userdata.UserRepository;

import androidx.core.app.NotificationCompat;

public class MoneyAlarmReceiver extends BroadcastReceiver {

    NotificationManager notificationManager;
    UserRepository userRepository;
    // Notification ID.
    private static final int NOTIFICATION_ID = 1;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("Receiver", "Just Received Alarm, about to send notification and update money");
        increaseUserMoney(context);
        sendNotification(context);
    }

    private void sendNotification(Context context){
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MainActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_attach_money_black_24dp)
                .setContentTitle("Money Added!")
                .setContentText("You received more money to play!")
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void increaseUserMoney(Context context){
        userRepository = new UserRepository((Application) context.getApplicationContext());
        //TODO: replace with two rxjava operations to get user data and to update the money amount
        //userRepository.increaseMoney(101);
        Log.i("Broadcast Receiver", "Received broadcast message, pretending to update user money");

    }
}
