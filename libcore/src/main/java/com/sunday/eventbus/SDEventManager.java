package com.sunday.eventbus;

import de.greenrobot.event.EventBus;

public class SDEventManager {
    public SDEventManager() {
    }

    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    public static void postTag(String tag) {
        post(new SDBaseEvent((Object)null, tag));
    }

    public static void postTag(Object data, String tag) {
        post(new SDBaseEvent(data, tag));
    }
}
