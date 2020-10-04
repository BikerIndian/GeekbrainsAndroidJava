package ru.geekbrains.android.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import ru.geekbrains.android.R;

public class BatteryInfoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Broadcast", "BatteryInfoReceiver");
        Toast.makeText (context, R.string.broadcast_battery_low, Toast.LENGTH_LONG).show();
    }
}
