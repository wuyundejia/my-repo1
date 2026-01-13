package com.example.model;

public class UserTest {
    public static void main(String[] args) {
        // 创建用户实例
        UserModel user = new UserModel(1L, "张三", "13800138000", 25);
        // 打印用户信息
        System.out.println(user);
        // 修改用户年龄
        user.setUserAge(26);
        System.out.println("修改后年龄：" + user.getUserAge());
    }
}