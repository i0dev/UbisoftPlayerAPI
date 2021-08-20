package com.i0dev;

import com.i0dev.object.User;

public class Main {


    public static void main(String[] args) {
        R6API api = R6API.connect(email, password);
        User user = api.getUser(args[0], R6API.PlatformType.U_PLAY);
        System.out.println(user.getUserID());
        System.out.println(user.getName());
        System.out.println(user.getPlatformType());
        System.out.println(user.getProfileID());
    }


}
