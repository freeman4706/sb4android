package dev.niekirk.com.instagram4android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.niekirk.com.instagram4android.model.InstagramSession;
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary;

public class SharedPreferenceUtil {



    public static InstagramSession getInstagramSession(Context context)
    {
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString("INSTAGRAM4ANDROID", null);
        if (json != null) {
            try {
                Gson gson = new Gson();
                InstagramSession result = gson.fromJson(json, InstagramSession.class);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static void saveInstagramSession(Context context, InstagramSession instagramSession)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(instagramSession);
        prefsEditor.putString("INSTAGRAM4ANDROID", json);
        prefsEditor.commit();
    }

    public static void deleteSharedPreferences(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }


    public static void saveFollowerListToPreferences(Context context, Collection<InstagramUserSummary> users)
    {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(users);
        prefsEditor.putString("INSTAGRAM4ANDROID_FOLLOWERS", json);
        prefsEditor.commit();
    }
    public static Collection<InstagramUserSummary> getFollowerListFromPreferences(Context context)
    {
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString("INSTAGRAM4ANDROID_FOLLOWERS", null);
        if (json != null) {
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<InstagramUserSummary>>(){}.getType();
                List<InstagramUserSummary> result = gson.fromJson(json, listType);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<InstagramUserSummary>();
    }
}
