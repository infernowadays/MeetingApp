package com.lazysecs.walk.interfaces;

public interface NotificationListener {
    void addNotificationBadge(int number);

    void initNotificationBadge(int number);

    void subNotificationBadge(int number);

    void removeNotificationBadge();

    boolean isInit();
}
