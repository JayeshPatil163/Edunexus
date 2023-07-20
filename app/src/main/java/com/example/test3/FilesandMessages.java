package com.example.test3;

public class FilesandMessages {

    String Title;
    String Message;
    String Type;

    public FilesandMessages(String title, String type, String message) {
        Title = title;
        Type = type;
        Message = message;
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

    public void setTitl(String title) {
        Title = title;
    }

    public void setType(String type) {
        Type = type;
    }
}
