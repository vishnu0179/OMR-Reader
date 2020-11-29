package com.example.omrreader.Details;

import java.io.Serializable;

public class DetailsModel implements Serializable {
    String setId,name, correct,wrong,questions;

    public DetailsModel() {
    }

    public DetailsModel(String setId, String name, String correct, String wrong, String questions) {
        this.setId = setId;
        this.name = name;
        this.correct = correct;
        this.wrong = wrong;
        this.questions = questions;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public String getWrong() {
        return wrong;
    }

    public void setWrong(String wrong) {
        this.wrong = wrong;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "DetailsModel{" +
                "setId='" + setId + '\'' +
                ", name='" + name + '\'' +
                ", correct=" + correct +
                ", wrong=" + wrong +
                ", questions=" + questions +
                '}';
    }
}
