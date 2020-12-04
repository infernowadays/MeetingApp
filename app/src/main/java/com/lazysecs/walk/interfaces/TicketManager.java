package com.lazysecs.walk.interfaces;

public interface TicketManager extends BaseContentManager {

    void saveName(String name);

    String getName();

    void savePrice(int price);

    int getPrice();
}
