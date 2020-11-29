package com.example.omrreader.Details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetailsResponseModel implements Serializable {
    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    String data;

    @Override
    public String toString() {
        return "DetailsResponseModel{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
