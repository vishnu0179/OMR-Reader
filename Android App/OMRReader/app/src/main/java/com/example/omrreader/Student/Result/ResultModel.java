package com.example.omrreader.Student.Result;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultModel implements Serializable {
    @SerializedName("status")
    String status;
    @SerializedName("data")
    List<ResultDataModel> data;

    public ResultModel() {
    }

    public ResultModel(String status, List<ResultDataModel> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultDataModel> getData() {
        return data;
    }

    public void setData(List<ResultDataModel> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
