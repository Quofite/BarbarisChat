package com.barbaris.chat.models;

import com.barbaris.chat.ChatActivity;

public class TestClass {
    private ChatActivity activity;

    public TestClass(ChatActivity activity) {
        this.activity = activity;
    }

    public void test() {
        activity.getMessage("some message");
    }
}
