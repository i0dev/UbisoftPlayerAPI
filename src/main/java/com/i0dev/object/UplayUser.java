package com.i0dev.object;

import com.i0dev.R6API;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class UplayUser extends User {

    String uplayID;

    public UplayUser(String uplayID) {
        this.platformType = R6API.PlatformType.U_PLAY;
        this.uplayID = uplayID;
    }


}

