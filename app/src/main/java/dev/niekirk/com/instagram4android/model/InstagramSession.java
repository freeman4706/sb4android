package dev.niekirk.com.instagram4android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dev.niekirk.com.instagram4android.Instagram4Android;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Cookie;

@ToString(callSuper=false, includeFieldNames=true)
public class InstagramSession implements Serializable {
    @Getter
    @SerializedName("deviceId")
    protected String deviceId;

    @Getter @Setter
    @SerializedName("username")
    private String username;

    @Getter @Setter
    @SerializedName("password")
    private String password;

    @Getter @Setter
    @SerializedName("accessToken")
    private String accessToken;

    @Getter
    @SerializedName("isLoggedIn")
    protected boolean isLoggedIn;

    @Getter
    @SerializedName("uuid")
    private String uuid;

    @Getter @Setter
    @SerializedName("rankToken")
    protected String rankToken;

    @Getter @Setter
    @SerializedName("userId")
    private long userId;

    @Getter @Setter
    @SerializedName("cookieStore")
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


    public static InstagramSession convertOldToNew(InstagramSessionOld old) {
        InstagramSession session = new InstagramSession();
        session.deviceId = old.getDeviceId();
        session.username = old.getUsername();
        session.password = old.getPassword();
        session.accessToken = old.getAccessToken();
        session.isLoggedIn = old.isLoggedIn();
        session.uuid = old.getUuid();
        session.rankToken = old.getRankToken();
        session.userId = old.getUserId();

        if (old.getCookieStore() != null) {

            Iterator hmIterator = old.getCookieStore().entrySet().iterator();

            while (hmIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry)hmIterator.next();

                if (mapElement.getValue() != null) {
                    session.cookieStore.put((String) mapElement.getKey(),  ((CookieOld) mapElement.getValue()).toNew());
                }
            }


        }

        return session;


    }


}
