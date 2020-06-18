package com.example.weather;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Objects;

public class LowBatteryBroadcastReceiver extends BroadcastReceiver {

    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BATTERY_LOW)) {
            NotificationCompat.Builder builder = new
                    NotificationCompat.Builder(context, "2")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Заряд батареи очень низок")
                    .setContentText("Советую зарядить телефон");
            NotificationManager notificationManager =
                    (NotificationManager)
                            context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(messageId++, builder.build());
        }
    }

}
