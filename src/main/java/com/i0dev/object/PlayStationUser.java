package com.i0dev.object;

import com.i0dev.R6API;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class PlayStationUser extends User {

    String psnId;

    public PlayStationUser(String psnId) {
        this.platformType = R6API.PlatformType.PLAY_STATION;
        this.psnId = psnId;
    }


}
