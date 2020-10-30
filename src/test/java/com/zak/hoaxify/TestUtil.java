package com.zak.hoaxify;

import com.zak.hoaxify.User.User;

public class TestUtil {

    public static User createValidUser() {
        User user = new User();
        user.setUsername("test-user");
        user.setDisplayName("test-dispaly");
        user.setPassword("P4ssword");
        user.setImage("profile-image.png");
        return user;
    }

}
