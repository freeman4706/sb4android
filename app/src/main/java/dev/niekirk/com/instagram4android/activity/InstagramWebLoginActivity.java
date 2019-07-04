package dev.niekirk.com.instagram4android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.util.HashMap;

import dev.niekirk.com.instagram4android.Instagram4Android;
import dev.niekirk.com.instagram4android.R;
import dev.niekirk.com.instagram4android.broadcastreceiver.InstagramWebLoginBroadcastReceiver;
import dev.niekirk.com.instagram4android.util.SharedPreferenceUtil;
import okhttp3.Cookie;

public class InstagramWebLoginActivity extends AppCompatActivity {


    WebView webViewLogin;
    Instagram4Android instagram4Android;
    Button btnClose;

    public static int REQUEST_CODE_LOGIN_INSTAGRAM = 1001;
    public static int RESULT_CODE_LOGIN_SUCCESSFUL = 2001;
    public static  int RESULT_CODE_UNSUCCESSFUL = 2002;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_web_login);


        this.webViewLogin = (WebView) findViewById(R.id.webViewLogin);
        this.btnClose = (Button) findViewById(R.id.btnClose);
        this.clearCookiesAndCache();
        SharedPreferenceUtil.deleteSharedPreferences(this);
        webViewLogin.getSettings().setJavaScriptEnabled(true);
        webViewLogin.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url){
                String cookies = CookieManager.getInstance().getCookie(url);
                String []cookieArray = cookies.split("; ");
                if (isUserLogged(cookieArray)) {
                    HashMap<String, Cookie> cookieHashMap = InstagramWebLoginActivity.this.convertCookies(cookieArray);
                    instagram4Android = Instagram4Android.builder().username("username").password("password").context(InstagramWebLoginActivity.this).build();
                    instagram4Android.setCookieStore(cookieHashMap);
                    instagram4Android.setLoggedIn(true);
                    long userId = getUserId(cookieArray);
                    instagram4Android.setUserId(userId);
                    instagram4Android.setupIfNeeded();
                    instagram4Android.saveCurrentSession();

                    Intent resultIntent = new Intent();

                    resultIntent.putExtra("username", "");
                    resultIntent.putExtra("pk", userId);


                    InstagramWebLoginActivity.this.setResult(InstagramWebLoginActivity.RESULT_CODE_LOGIN_SUCCESSFUL, resultIntent);
                    finish();


                }
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InstagramWebLoginActivity.this.setResult(InstagramWebLoginActivity.RESULT_CODE_UNSUCCESSFUL);
                InstagramWebLoginActivity.this.finish();
            }
        });

        webViewLogin.loadUrl("https://www.instagram.com/accounts/login/");
    }


    private Cookie createCookie(String name, String value) {
        return new Cookie.Builder()
                .domain("instagram.com")
                .path("/")
                .name(name)
                .value(value)
                .httpOnly()
                .secure()
                .build();
    }

    private HashMap<String, Cookie> convertCookies(String[] cookieArray) {
        HashMap<String, Cookie> cookiesMap = new HashMap();

        for (int i = 0; i< cookieArray.length; i++) {
            //cookiesMap.put(cookieArray)
            String[] nameValue = cookieArray[i].split("=");
            cookiesMap.put(nameValue[0], createCookie(nameValue[0], nameValue[1]));
        }

        return cookiesMap;
    }

    private boolean isUserLogged(String[] cookieArray) {
        for (String cookie : cookieArray) {
            if (cookie.contains("ds_user_id")) {
                return true;
            }
        }
        return false;
    }

    private long getUserId(String[] cookieArray) {
        for (String cookie : cookieArray) {
            if (cookie.contains("ds_user_id")) {
                String []cookiePair = cookie.split("=");
                return Long.parseLong(cookiePair[1]);
            }
        }
        return 0;
    }


    private void sendBroadcastEvent(String actionName, Boolean isSuccessful){
        Intent i = new Intent(InstagramWebLoginBroadcastReceiver.KEY_ACTION);
        i.putExtra(InstagramWebLoginBroadcastReceiver.KEY_ACTION, actionName);
        i.putExtra(InstagramWebLoginBroadcastReceiver.KEY_ACTION_LOGIN_SUCCESSFUL, isSuccessful);
        sendBroadcast(i);
    }

    private void clearCookiesAndCache() {
        this.webViewLogin.clearCache(true);
        this.webViewLogin.clearFormData();
        this.webViewLogin.clearHistory();
        this.webViewLogin.clearMatches();
        this.webViewLogin.clearSslPreferences();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {

            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            if (cookieManager != null) {
                cookieManager.removeAllCookie();
            }
            CookieSyncManager.getInstance().sync();
        }
    }


}
