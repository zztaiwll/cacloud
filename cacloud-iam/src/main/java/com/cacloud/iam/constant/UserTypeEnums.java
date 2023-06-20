package com.cacloud.iam.constant;

public enum UserTypeEnums {
    Root("Root", 0),

    SuperUser("SuperUser", 1),

    User("User", 2);


    //成员变量
    private String name;
    private int value;

    //构造方法
    UserTypeEnums(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
