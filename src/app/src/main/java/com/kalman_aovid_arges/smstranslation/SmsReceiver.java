package com.kalman_aovid_arges.smstranslation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SmsReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "SMS_CHANNEL_ID";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String sender = smsMessage.getOriginatingAddress();
                LocalDateTime timeReceived = LocalDateTime.ofInstant(Instant.ofEpochMilli(smsMessage.getTimestampMillis()), ZoneId.systemDefault());
                int type = smsMessage.getStatusOnIcc();
    
                // Create an ExecutorService
                ExecutorService executor = Executors.newSingleThreadExecutor();
    
                // Run the operations in a background thread
                executor.execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                    List<SMSGroup> smsGroups = db.smsGroupDao().getAll();
    
                    boolean senderExists = false;
                    for (SMSGroup smsGroup : smsGroups) {
                        if (smsGroup.getSender().equals(sender)) {
                            senderExists = true;
                            smsGroup.getMessages().add(new SMSMessage(messageBody, type, timeReceived));
                            smsGroup.setLastMessageBody(messageBody);
                            smsGroup.setLastMessageDateTime(timeReceived);
                            smsGroup.setUnreadMessagesCount(smsGroup.getUnreadMessagesCount() + 1);
                            db.smsGroupDao().update(smsGroup);
                        }
                    }
    
                    if (!senderExists) {
                        SMSGroup newGroup = new SMSGroup();
                        newGroup.setContactName(sender);
                        newGroup.setContactNumber(sender);
                        newGroup.setSender(sender);
                        newGroup.setLastMessageBody(messageBody);
                        newGroup.setLastMessageDateTime(timeReceived);
                        newGroup.setUnreadMessagesCount(1);
                        db.smsGroupDao().insertAll(newGroup);
                    }
                });
    
                // Shutdown the executor
                executor.shutdown();
    
                // Check if notifications are enabled
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean notificationsEnabled = sharedPreferences.getBoolean("notifications", true);
                if (!notificationsEnabled) {
                    return;
                }
    
                // Create a notification channel
                CharSequence name = "SMS Channel";
                String description = "Channel for SMS notifications";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
    
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
    
                // Build the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("New SMS Message")
                        .setContentText("From: " + sender + ", Message: " + messageBody)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    
                // Show the notification
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(0, builder.build());
            }
        }
    }
}
