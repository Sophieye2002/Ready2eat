package com.example.picktheperfectwatermelon.AudioAnylysis;

import java.util.ArrayList;
import java.util.List;

public class CAlData {
    public CAlData() {
        this.dataList = new ArrayList<>();
    }

    public CAlData(List<byte[]> dataList) {
        this.dataList = dataList;
    }

    public List<byte[]> getDataList() {
        return dataList;
    }

    public void setDataList(List<byte[]> dataList) {
        this.dataList = dataList;
    }


    private List<byte[]> dataList;

    public void addDatas(byte[] data) {
        dataList.add(data);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataList.size(); i++) {
            byte[] dataIn = dataList.get(i);
            for (int j = 0; j < dataIn.length; j++) {
                sb.append(dataIn[j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
