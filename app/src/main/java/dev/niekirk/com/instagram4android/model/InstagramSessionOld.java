package dev.niekirk.com.instagram4android.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString(callSuper=false, includeFieldNames=true)
public class InstagramSessionOld {

    @Getter
    @SerializedName("a")
    protected String deviceId;

    @Getter @Setter
    @SerializedName("d")
    private String username;

    @Getter @Setter
    @SerializedName("e")
    private String password;

    @Getter @Setter
    private String accessToken;

    @Getter
    @SerializedName("b")
    protected boolean isLoggedIn;

    @Getter
    @SerializedName("g")
    private String uuid;

    @Getter @Setter
    protected String rankToken;

    @Getter @Setter
    @SerializedName("h")
    private long userId;

    @Getter @Setter
    @SerializedName("i")
    private HashMap<String, CookieOld> cookieStore = new HashMap<>();



}
