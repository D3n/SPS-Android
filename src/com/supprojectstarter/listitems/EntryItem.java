package com.supprojectstarter.listitems;

public class EntryItem implements Item {
    public final int id;
    public final String title;
    public final String percentage;
    public final String description;

    public EntryItem(int id, String title, String percentage, String description) {
        this.id = id;
        this.title = title;
        this.percentage = percentage;
        this.description = description;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
