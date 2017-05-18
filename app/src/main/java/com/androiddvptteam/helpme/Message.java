package com.androiddvptteam.helpme;

public class Message {
    private int imageId;
    private String name;
    private String text;

    public Message(int imageId, String name, String text) {

        this.imageId = imageId;
        this.name = name;
        this.text = text;

    }

    public int getImageId() {

        return imageId;

    }

    public String getName() {

        return name;

    }

    public String getText(){

        return text;

    }

}
