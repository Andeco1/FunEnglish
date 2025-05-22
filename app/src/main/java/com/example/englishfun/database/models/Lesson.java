package com.example.englishfun.database.models;

public class Lesson {
    public int lesson_id;
    public String title;
    public String content;
    public String level;
    public String levelDesription;

    public Lesson() {
    }

    public Lesson(String title, String level) {
        this.title = title;
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public String getTitle() {
        return title;
    }
}
