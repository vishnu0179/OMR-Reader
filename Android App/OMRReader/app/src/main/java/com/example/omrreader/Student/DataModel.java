package com.example.omrreader.Student;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataModel implements Serializable {
    @SerializedName("enrollment")
    String enrollment;
    @SerializedName("test_id")
    String test_id;
    @SerializedName("response")
    String response;

    public DataModel() {
    }

    public DataModel(String enrollment, String test_id, String response) {
        this.enrollment = enrollment;
        this.test_id = test_id;
        this.response = response;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "enrollment='" + enrollment + '\'' +
                ", test_id='" + test_id + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
