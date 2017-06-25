package com.j2ustc.example;

/**
 * Created by yenole on 2017/6/25.
 */

public class Example {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String name(String name){
        return String.format("hello %s",name);
    }

}
