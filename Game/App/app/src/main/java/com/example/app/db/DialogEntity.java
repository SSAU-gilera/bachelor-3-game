package com.example.app.db;

public class DialogEntity {

    private int id;
    private String sentence;
    private String image_id;
    private String fon_id;
    private int side;
    private int act;
    private int index;

    public DialogEntity(int id, String sentence, String image_id, String fon_id, int side, int act, int index) {
        this.id = id;
        this.sentence = sentence;
        this.image_id = image_id;
        this.fon_id = fon_id;
        this.side = side;
        this.act = act;
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public String getSentence() {
        return sentence;
    }

    public String getImage_id() {
        return image_id;
    }

    public String getFon_id() {
        return fon_id;
    }

    public int getSide() {
        return side;
    }

    public int getAct() {
        return act;
    }

    public int getIndex() {
        return index;
    }
}
