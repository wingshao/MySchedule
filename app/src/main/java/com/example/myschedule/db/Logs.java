package com.example.myschedule.db;

import org.litepal.crud.LitePalSupport;

public class Logs extends LitePalSupport {

    int id;

    String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
