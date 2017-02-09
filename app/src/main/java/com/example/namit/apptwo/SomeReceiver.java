package com.example.namit.apptwo;

import android.os.Vibrator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SomeReceiver extends BroadcastReceiver {
    public SomeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //Toast.makeText(context, "Charger disconnected", Toast.LENGTH_SHORT).show();
        if(intent.getAction()==Intent.ACTION_POWER_CONNECTED)
            Toast.makeText(context, "Charger disconnected", Toast.LENGTH_SHORT).show();
        else {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(600);
        }
    }
}
