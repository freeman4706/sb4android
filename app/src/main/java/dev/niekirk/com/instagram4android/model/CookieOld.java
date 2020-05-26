package dev.niekirk.com.instagram4android.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Cookie;

@ToString(callSuper=false, includeFieldNames=true)
public class CookieOld implements Serializable {


    @Getter @Setter
    @SerializedName("e")
    private  String name;
    @Getter @Setter
    @SerializedName("f")
    private  String value;
    @Getter @Setter
    @SerializedName("g")
    private  long expiresAt;
    @Getter @Setter
    @SerializedName("h")
    private  String domain;
    @Getter @Setter
    @SerializedName("i")
    private  String path;
    @Getter @Setter
    @SerializedName("j")
    private  boolean secure;
    @Getter @Setter
    @SerializedName("k")
    private  boolean httpOnly;
    @Getter @Setter
    @SerializedName("l")
    private  boolean persistent; // True if 'expires' or 'max-age' is present.
    @Getter @Setter
    @SerializedName("m")
    private  boolean hostOnly; // True unless 'domain' is present.


    public Cookie toNew() {

        Cookie.Builder cookieBuilder = new Cookie.Builder();
        cookieBuilder.name(this.name).value(this.value).expiresAt(this.expiresAt).domain(this.domain).path(this.path);
        if (secure)
            cookieBuilder.secure();
        if (httpOnly)
            cookieBuilder.httpOnly();

        return cookieBuilder.build();
    }
}
