package com.example.omrreader.Student.Result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultDataModel implements Serializable {
    @SerializedName("score")
    String score;
    @SerializedName("enrollment")
    String enrollment;
    @SerializedName("test_id")
    String test_id;
    @SerializedName("answer_sheet_img")
    String answer_sheet_img;

    public ResultDataModel() {
    }

    public ResultDataModel(String score, String enrollment, String test_id, String answer_sheet_img) {
        this.score = score;
        this.enrollment = enrollment;
        this.test_id = test_id;
        this.answer_sheet_img = answer_sheet_img;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getAnswer_sheet_img() {
        return answer_sheet_img;
    }

    public void setAnswer_sheet_img(String answer_sheet_img) {
        this.answer_sheet_img = answer_sheet_img;
    }

    @Override
    public String toString() {
        return "ResultDataModel{" +
                "score='" + score + '\'' +
                ", enrollment='" + enrollment + '\'' +
                ", test_id='" + test_id + '\'' +
                ", answer_sheet_img='" + answer_sheet_img + '\'' +
                '}';
    }
}
