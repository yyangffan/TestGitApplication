package com.example.yf.testgitapplication.input.keyboard;

/**
 * Created by shikh on 2016/12/8.
 */
public class KeyModel {
    private Integer code;
    private String label;

    public KeyModel(Integer code, String lable) {
        this.code = code;
        this.label = lable;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLable() {
        return label;
    }

    public void setLabel(String lable) {
        this.label = lable;
    }

}
