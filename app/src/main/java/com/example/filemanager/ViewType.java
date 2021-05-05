package com.example.filemanager;

public enum ViewType {

    ROW(1),GRID(2);

    private int value;
    ViewType(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
