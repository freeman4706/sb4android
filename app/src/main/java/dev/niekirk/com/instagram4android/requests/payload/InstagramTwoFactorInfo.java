package dev.niekirk.com.instagram4android.requests.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class InstagramTwoFactorInfo {
    private String two_factor_identifier;
}