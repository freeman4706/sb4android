package dev.niekirk.com.instagram4android.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dev.niekirk.com.instagram4android.listener.InstagramWebLoginListener;

public class InstagramWebLoginBroadcastReceiver extends BroadcastReceiver {


    public InstagramWebLoginListener listener;
    public static final String KEY_ACTION_LOGIN_SUCCESSFUL = "KEY_ACTION_LOGIN_SUCCESSFUL";
    public static final String KEY_ACTION = "com.uniapps.KEY_ACTION";


    public InstagramWebLoginBroadcastReceiver(InstagramWebLoginListener listener)
    {
        super();
        this.listener = listener;
    }

    public InstagramWebLoginBroadcastReceiver()
    {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (listener == null)
            return;

        String action = intent.getStringExtra(KEY_ACTION);

        if (action == null)
            return;


        if(action.equalsIgnoreCase(KEY_ACTION_LOGIN_SUCCESSFUL))
        {
            listener.userLoggedSuccessfully();
        }


    }
}
