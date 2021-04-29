package org.ntnu.wykdemoapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends AppCompatActivity {

    private WYKAuthenticationReceiver wykAuthenticationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        //Register WYK authentication receiver here
        wykAuthenticationReceiver = new WYKAuthenticationReceiver();
        IntentFilter intentFilter = new IntentFilter("org.ntnu.wyk_service.AUTHENTICATE");
        if ( intentFilter != null ){
            getApplicationContext().registerReceiver(wykAuthenticationReceiver, intentFilter);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //DO NOTHING
            }
        }, 5000);

        //DO THE AUTHENTICATION
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("org.ntnu.wyk_service.AUTHENTICATE");
        serviceIntent.setComponent(new ComponentName("org.ntnu.wyk_service", "org.ntnu.wyk_service.WYKService"));
        ComponentName c = getApplicationContext().startService(serviceIntent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //Remove receiver here
        if( wykAuthenticationReceiver !=  null ){
            getApplicationContext().unregisterReceiver(wykAuthenticationReceiver);
        }
    }
}
