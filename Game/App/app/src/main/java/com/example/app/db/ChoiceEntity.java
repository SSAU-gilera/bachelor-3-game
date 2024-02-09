package com.example.app.db;

import java.util.List;

public class ChoiceEntity {

    private int id;
    private List<String> choiceSentence;
    private List<Integer> transition;

    public ChoiceEntity(int id, List<String> choiceSentence, List<Integer> transition) {
        this.id = id;
        this.choiceSentence = choiceSentence;
        this.transition = transition;
    }

    public int getId() {
        return id;
    }

    public List<String> getChoiceSentence() {
        return choiceSentence;
    }

    public List<Integer> getTransition() {
        return transition;
    }
}
