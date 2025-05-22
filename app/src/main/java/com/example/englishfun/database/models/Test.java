package com.example.englishfun.database.models;

public class Test {
    public int test_id;
    public String title;
    public String level;
    public String passedAt;
    public int score;

    public Test() {
    }

    public Test(String title, String level, int count) {
        this.title = title;
        this.level = level;
        this.score = count;
    }

    public String getTitle() {
        return title;
    }

    public String getLevel() {
        return level;
    }

    public int getQuestionScore() {
        return score;
    }
} 