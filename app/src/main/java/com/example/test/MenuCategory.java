package com.example.test;

import java.util.ArrayList;

/**
 * Created by joyjitdaw on 1/12/15.
 */
public class MenuCategory {
    private ArrayList<MenuElement> items;
    private String name;

    public MenuCategory(String name) {
        this.name = name;
        items = new ArrayList<MenuElement>();
    }

    public void addItem(MenuElement item) {
        items.add(item);
    }

    public int itemCount() {
        return items.size();
    }

    public String getName() {
        return name;
    }

    public ArrayList<MenuElement> getItems() {
        return items;
    }
}
