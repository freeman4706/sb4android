package dev.niekirk.com.instagram4android;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import dev.niekirk.com.instagram4android.model.InstagramSession;
import dev.niekirk.com.instagram4android.requests.InstagramAutoCompleteUserListRequest;
import dev.niekirk.com.instagram4android.requests.InstagramFbLoginRequest;
import dev.niekirk.com.instagram4android.requests.InstagramGetInboxRequest;
import dev.niekirk.com.instagram4android.requests.InstagramGetRecentActivityRequest;
import dev.niekirk.com.instagram4android.requests.InstagramLoginRequest;
import dev.niekirk.com.instagram4android.requests.InstagramLoginTwoFactorRequest;
import dev.niekirk.com.instagram4android.requests.InstagramRequest;
import dev.niekirk.com.instagram4android.requests.InstagramSyncFeaturesRequest;
import dev.niekirk.com.instagram4android.requests.InstagramTimelineFeedRequest;
import dev.niekirk.com.instagram4android.requests.internal.InstagramFetchHeadersRequest;
import dev.niekirk.com.instagram4android.requests.payload.InstagramFbLoginPayload;
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoginPayload;
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoginResult;
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoginTwoFactorPayload;
import dev.niekirk.com.instagram4android.requests.payload.InstagramSyncFeaturesPayload;
import dev.niekirk.com.instagram4android.util.InstagramGenericUtil;
import dev.niekirk.com.instagram4android.util.InstagramHashUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.niekirk.com.instagram4android.util.SharedPreferenceUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by root on 08/06/17.
 */

public class Instagram4Android {

    @Getter
    protected String deviceId;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String accessToken;

    @Getter @Setter
    protected boolean isLoggedIn;

    @Getter
    private String uuid;

    @Getter @Setter
    protected String rankToken;

    @Getter @Setter
    private long userId;

    @Getter @Setter
    protected Response lastResponse;

    @Getter
    protected OkHttpClient client;

    @Getter @Setter
    protected Context context;

    @Getter @Setter
    private HashMap<String, Cookie> cookieStore = new HashMap<>();

    protected String identifier;


    @Builder
    public Instagram4Android(String username, String password, Context context) {
        super();
        this.username = username;
        this.password = password;
        this.context = context;
    }


    public void setupIfNeeded(){
        if (SharedPreferenceUtil.getInstagramSession(context) != null)
        {
            InstagramSession instagramSession= SharedPreferenceUtil.getInstagramSession(context);

            if (!instagramSession.getUsername().equalsIgnoreCase(getUsername()))
            {
                SharedPreferenceUtil.deleteSharedPreferences(context);
                this.setup();
                return;
            }

            bindInstagramSession(instagramSession);

            client = new OkHttpClient.Builder()
                    .cookieJar(new CookieJar() {

                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            if (cookies != null) {
                                for (Cookie cookie : cookies) {
                                    cookieStore.put(cookie.name(), cookie);
                                }
                            }
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> validCookies = new ArrayList<>();
                            for (Map.Entry<String, Cookie> entry : cookieStore.entrySet()) {
                                Cookie cookie = entry.getValue();
                                if(cookie.expiresAt() >= System.currentTimeMillis()) {
                                    validCookies.add(cookie);
                                }
                            }
                            return validCookies;
                        }
                    })
                    .build();
        }
        else
        {
            this.setup();
        }
    }

    private void bindInstagramSession(InstagramSession session)
    {
        this.deviceId = session.getDeviceId();
        this.username = session.getUsername();
        this.password = session.getPassword();
        this.accessToken = session.getAccessToken();
        this.isLoggedIn = session.isLoggedIn();
        this.uuid = session.getUuid();
        this.rankToken = session.getRankToken();
        this.userId = session.getUserId();
        this.cookieStore = session.getCookieStore();
    }

    public void setup() {

        /*
        if (this.username.length() < 1) {
            throw new IllegalArgumentException("Username is mandatory.");
        }

        if (this.password.length() < 1) {
            throw new IllegalArgumentException("Password is mandatory.");
        }*/

        this.deviceId = InstagramHashUtil.generateDeviceId(this.username, this.password);
        this.uuid = InstagramGenericUtil.generateUuid(true);

        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        if (cookies != null) {
                            for (Cookie cookie : cookies) {
                                cookieStore.put(cookie.name(), cookie);
                            }
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> validCookies = new ArrayList<>();
                        for (Map.Entry<String, Cookie> entry : cookieStore.entrySet()) {
                            Cookie cookie = entry.getValue();
                            if(cookie.expiresAt() >= System.currentTimeMillis()) {
                                validCookies.add(cookie);
                            }
                        }
                        return validCookies;
                    }
                })
                .build();

    }

    public InstagramLoginResult loginFb() throws IOException {

        InstagramFbLoginPayload loginRequest = InstagramFbLoginPayload.builder()
                .dryrun(true)
                /*.username(username)*/
                .adid(InstagramGenericUtil.generateUuid(false))
                .device_id(deviceId)
                .fb_access_token(password)
                .phone_id(InstagramGenericUtil.generateUuid(false))
                .waterfall_id(InstagramGenericUtil.generateUuid(false))
                /*.allow_contacts_sync(true)
                .big_blue_token(password)*/
                .build();

        InstagramLoginResult loginResult = this.sendRequest(new InstagramFbLoginRequest(loginRequest));
        if (loginResult.getStatus().equalsIgnoreCase("ok")) {
            System.out.println(cookieStore.toString());
            this.userId = loginResult.getLogged_in_user().getPk();
            this.rankToken = this.userId + "_" + this.uuid;
            this.isLoggedIn = true;

            InstagramSyncFeaturesPayload syncFeatures = InstagramSyncFeaturesPayload.builder()
                    ._uuid(uuid)
                    ._csrftoken(getOrFetchCsrf(null))
                    ._uid(userId)
                    .id(userId)
                    .experiments(InstagramConstants.DEVICE_EXPERIMENTS)
                    .build();

            this.sendRequest(new InstagramSyncFeaturesRequest(syncFeatures));
            this.sendRequest(new InstagramAutoCompleteUserListRequest());
            //this.sendRequest(new InstagramTimelineFeedRequest());
            this.sendRequest(new InstagramGetInboxRequest());
            this.sendRequest(new InstagramGetRecentActivityRequest());
        }

        System.out.println("Hello! --> " + loginResult.toString());

        return loginResult;

    }

    public InstagramLoginResult login() throws IOException {

        //Log.d("LOGIN", "Logging with user " + username + " and password " + password.replaceAll("[a-zA-Z0-9]", "*"));

        InstagramLoginPayload loginRequest = InstagramLoginPayload.builder().username(username)
                .password(password)
                .guid(uuid)
                .device_id(deviceId)
                .phone_id(InstagramGenericUtil.generateUuid(true))
                .login_attempt_account(0)
                ._csrftoken(getOrFetchCsrf(null))
                .build();

        InstagramLoginResult loginResult = this.sendRequest(new InstagramLoginRequest(loginRequest));
        emulateUserLoggedIn(loginResult);

        if (loginResult.getTwo_factor_info() != null) {
            identifier = loginResult.getTwo_factor_info().getTwo_factor_identifier();
        } else if (loginResult.getChallenge() != null) {
            // logic for challenge
            //log.info("Challenge required: " + loginResult.getChallenge());
        }
        return loginResult;
    }

    public InstagramLoginResult login(String verificationCode) throws IOException {
        if (identifier == null) {
            login();
        }
        InstagramLoginTwoFactorPayload loginRequest = InstagramLoginTwoFactorPayload.builder().username(username)
                .verification_code(verificationCode)
                .two_factor_identifier(identifier)
                .password(password)
                .guid(uuid)
                .device_id(deviceId)
                .phone_id(InstagramGenericUtil.generateUuid(true))
                .login_attempt_account(0)
                ._csrftoken(getOrFetchCsrf(null))
                .build();
        InstagramLoginTwoFactorRequest req = new InstagramLoginTwoFactorRequest(loginRequest);
        InstagramLoginResult loginResult = this.sendRequest(req);
        emulateUserLoggedIn(loginResult);
        return loginResult;
    }

    private void emulateUserLoggedIn(InstagramLoginResult loginResult) throws IOException {
        if (loginResult.getStatus().equalsIgnoreCase("ok")) {
            this.userId = loginResult.getLogged_in_user().getPk();
            this.rankToken = this.userId + "_" + this.uuid;
            this.isLoggedIn = true;

            InstagramSyncFeaturesPayload syncFeatures = InstagramSyncFeaturesPayload.builder()
                    ._uuid(uuid)
                    ._csrftoken(getOrFetchCsrf(null))
                    ._uid(userId)
                    .id(userId)
                    .experiments(InstagramConstants.DEVICE_EXPERIMENTS)
                    .build();

            this.sendRequest(new InstagramSyncFeaturesRequest(syncFeatures));
            this.sendRequest(new InstagramAutoCompleteUserListRequest());
            //this.sendRequest(new InstagramTimelineFeedRequest());
            this.sendRequest(new InstagramGetInboxRequest());
            this.sendRequest(new InstagramGetRecentActivityRequest());

            InstagramSession session = new InstagramSession();

            session.setSession(this);

            SharedPreferenceUtil.saveInstagramSession(context, session);


        }
    }

    public void saveCurrentSession() {
        InstagramSession session = new InstagramSession();

        session.setSession(this);

        SharedPreferenceUtil.saveInstagramSession(context, session);
    }

    public String getOrFetchCsrf(HttpUrl url) throws IOException {

        Cookie cookie = getCsrfCookie(url);
        if(cookie == null) {
            sendRequest(new InstagramFetchHeadersRequest());
            cookie = getCsrfCookie(url);
        }

        return cookie.value();

    }

    public Cookie getCsrfCookie(HttpUrl url) {

        for(Cookie cookie: client.cookieJar().loadForRequest(url)) {

//            Log.d("GETCOOKIE", "Name: " + cookie.name());
            if(cookie.name().equalsIgnoreCase("csrftoken")) {
                return cookie;
            }

        }

        return null;

    }

    public <T> T sendRequest(InstagramRequest<T> request) throws IOException {

        if (!this.isLoggedIn
                && request.requiresLogin()) {
            throw new IllegalStateException("Need to login first!");
        }

        request.setApi(this);
        T response = request.execute();

        return response;
    }

}
