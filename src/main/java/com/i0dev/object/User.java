package com.i0dev.object;

import com.i0dev.R6API;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    String name, profileID, userID;
    R6API.PlatformType platformType;


}
