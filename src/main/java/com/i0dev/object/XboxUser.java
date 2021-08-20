package com.i0dev.object;

import com.i0dev.R6API;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class XboxUser extends User {

    String xboxID;

    public XboxUser(String xboxID) {
        this.platformType = R6API.PlatformType.XBOX;
        this.xboxID = xboxID;
    }


}

