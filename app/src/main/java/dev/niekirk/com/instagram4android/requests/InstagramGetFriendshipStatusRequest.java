package dev.niekirk.com.instagram4android.requests;


import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

import dev.niekirk.com.instagram4android.requests.payload.InstagramGetFriendshipStatusResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

//@RequiredArgsConstructor
@AllArgsConstructor
public class InstagramGetFriendshipStatusRequest extends InstagramMPostRequest<InstagramGetFriendshipStatusResult> {

    @NonNull
    private String[] userIdArray;

    @Override
    public String getUrl() {
        String baseUrl = "friendships/show_many/";

        return baseUrl;
    }

    @Override
    @SneakyThrows
    public String getPayload() {

        String userIds = TextUtils.join(",", userIdArray);



        Map<String, Object> likeMap = new LinkedHashMap<>();
        likeMap.put("_uuid", api.getUuid());
        //likeMap.put("_uid", api.getUserId());
        likeMap.put("_csrftoken", api.getOrFetchCsrf(null));
        likeMap.put("user_ids", userIds);

        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = mapper.writeValueAsString(likeMap);

        String bodyPayload = "_uuid=" + api.getUuid() + "&_csrftoken=" + api.getOrFetchCsrf(null) + "&user_ids=" + userIds;

        return bodyPayload;


    }

    @Override
    @SneakyThrows
    public InstagramGetFriendshipStatusResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, InstagramGetFriendshipStatusResult.class);
    }

}
