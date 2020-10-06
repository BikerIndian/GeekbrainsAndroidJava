package ru.geekbrains.android.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import ru.geekbrains.android.R;

public class NetworkInfoReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Toast.makeText (context, R.string.broadcast_connect_off, Toast.LENGTH_LONG).show();
        }

    }

}
