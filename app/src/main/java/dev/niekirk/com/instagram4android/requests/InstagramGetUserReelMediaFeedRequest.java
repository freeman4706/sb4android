package dev.niekirk.com.instagram4android.requests;


import dev.niekirk.com.instagram4android.requests.payload.InstagramGetUserReelMediaFeedResult;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class InstagramGetUserReelMediaFeedRequest extends InstagramGetRequest<InstagramGetUserReelMediaFeedResult> {

    private long userId;

    @Override
    public String getUrl() {
        return "feed/user/" + userId + "/reel_media/?";
    }
    @Override
    public String getPayload() {
        return null;
    }

    @Override
    @SneakyThrows
    public InstagramGetUserReelMediaFeedResult parseResult(int resultCode, String content) {
        return parseJson(resultCode, content, InstagramGetUserReelMediaFeedResult.class);
    }
}
