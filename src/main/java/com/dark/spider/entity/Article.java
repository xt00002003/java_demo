package com.dark.spider.entity;

/**
 * Created by darkxue on 23/11/16.
 */
public class Article {
    private String date;
    private String content;
    private String title;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Article{" + "date='" + date + '\'' + ", content='" + content + '\'' + ", title='"
                + title + '\'' + '}';
    }
}
