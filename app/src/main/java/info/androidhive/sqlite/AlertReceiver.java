package info.androidhive.sqlite;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Salim3DD on 7/2/2016.
 */
public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PendingIntent notification = PendingIntent.getActivity(context, 0, new Intent(context, Main3Activity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle("Reminder")
                .setContentText("Your task")
                .setColor(Color.rgb(90,148,228));

        builder.setContentIntent(notification);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        NotificationManager mm =( NotificationManager ) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mm.cancel(1);
        mm.notify(1, builder.build());

    }


}
