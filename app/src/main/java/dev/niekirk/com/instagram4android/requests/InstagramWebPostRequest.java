package dev.niekirk.com.instagram4android.requests;

import java.io.IOException;

import dev.niekirk.com.instagram4android.InstagramConstants;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class InstagramWebPostRequest<T> extends InstagramRequest<T> {

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public T execute() throws IOException {

        //String encodedUrl = URLEncoder.encode(getPayload(), "UTF-8");
        String encodedUrl = getPayload();

        Request request = new Request.Builder()
                .url(InstagramConstants.API_WEB_URL + getUrl())

                .addHeader("Connection", "close")
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Language", "en-US")
                .addHeader("User-Agent", InstagramConstants.USER_AGENT)
                .addHeader("X-IG-App-ID", "1217981644879628")
                .addHeader("X-Instagram-AJAX", "6f2ac705645c")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), encodedUrl))
                .build();

        Response response = api.getClient().newCall(request).execute();
        api.setLastResponse(response);

        int resultCode = response.code();
        String content = response.body().string();

        return parseResult(resultCode, content);
    }
}
