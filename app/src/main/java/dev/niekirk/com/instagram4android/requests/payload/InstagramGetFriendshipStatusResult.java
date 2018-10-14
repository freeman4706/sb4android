package dev.niekirk.com.instagram4android.requests.payload;


import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramGetFriendshipStatusResult extends StatusResult {
    private Map<String, InstagramFriendshipStatus> friendship_statuses;
}
