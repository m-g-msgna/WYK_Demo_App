package org.ntnu.wykdemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WYKAuthenticationReceiver extends BroadcastReceiver {
    private boolean authentication_status;

    @Override
    public void onReceive(Context context, Intent intent){
        //CHECK The broadcast receiver
        Bundle bundle = intent.getExtras();
        int uid = bundle.getInt("UID");
        int result = bundle.getInt("AUTHENTICATED");

        if (result == 1) {
            //User is successfully authenticated. Launch the main activity
            Intent mainIntent = new Intent(context,MainActivity.class);
            mainIntent.putExtra("UID", uid);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }
    }
}
