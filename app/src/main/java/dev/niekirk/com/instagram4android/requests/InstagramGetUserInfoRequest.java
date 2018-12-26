package dev.niekirk.com.instagram4android.requests;

import dev.niekirk.com.instagram4android.requests.payload.InstagramGetUserInfoResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InstagramGetUserInfoRequest extends InstagramGetRequest<InstagramGetUserInfoResult> {

    private String userId;

    @Override
    public String getUrl() {
        return "users/" + userId + "/info/";
    }

    @Override
    @SneakyThrows
    public InstagramGetUserInfoResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetUserInfoResult.class);
    }

}
