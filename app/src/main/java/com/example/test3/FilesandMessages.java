package com.example.test3;

public class FilesandMessages {

    String Title;
    String Message;
    String Type;
    String Time;

    public FilesandMessages(String title, String type, String message, String time) {
        Title = title;
        Type = type;
        Message = message;
        Time = time;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public String getType() {
        return Type;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTime() {
        return Time;
    }
}
