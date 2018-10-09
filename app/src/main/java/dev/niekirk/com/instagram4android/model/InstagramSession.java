package dev.niekirk.com.instagram4android.model;

import java.io.Serializable;
import java.util.HashMap;

import dev.niekirk.com.instagram4android.Instagram4Android;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Cookie;

public class InstagramSession implements Serializable {
    @Getter
    protected String deviceId;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String accessToken;

    @Getter
    protected boolean isLoggedIn;

    @Getter
    private String uuid;

    @Getter @Setter
    protected String rankToken;

    @Getter @Setter
    private long userId;

    @Getter @Setter
    private HashMap<String, Cookie> cookieStore = new HashMap<>();

    public void setSession(Instagram4Android instagram4Android)
    {
        this.deviceId = instagram4Android.getDeviceId();
        this.username = instagram4Android.getUsername();
        this.password = instagram4Android.getPassword();
        this.accessToken = instagram4Android.getAccessToken();
        this.isLoggedIn = instagram4Android.isLoggedIn();
        this.uuid = instagram4Android.getUuid();
        this.rankToken = instagram4Android.getRankToken();
        this.userId = instagram4Android.getUserId();
        this.cookieStore = instagram4Android.getCookieStore();

    }


}
