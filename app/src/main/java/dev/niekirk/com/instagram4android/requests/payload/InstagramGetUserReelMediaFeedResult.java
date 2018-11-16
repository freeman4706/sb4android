package dev.niekirk.com.instagram4android.requests.payload;


import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramGetUserReelMediaFeedResult extends StatusResult   {
    private String id;
    private List<InstagramItem> items; //item
    private InstagramUser user;
    private long expiring_at;
    private int seen; //boolean?
    private boolean can_reply; //boolean
    private String location;
    private String latest_reel_media;
    private int prefetch_count;
    private InstagramBroadcast broadcast;
}
