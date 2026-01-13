package com.example.model;

/**
 * 用户模型类，封装用户的核心信息
 */
public class UserModel {
    // 1. 定义用户相关属性（私有属性，符合封装原则）
    private Long userId;       // 用户ID
    private String userName;   // 用户名
    private String userPhone;  // 用户手机号
    private Integer userAge;   // 用户年龄

    // 2. 无参构造方法（必要，支持反射、框架实例化）
    public UserModel() {
    }

    // 3. 有参构造方法（快速创建用户实例）
    public UserModel(Long userId, String userName, String userPhone, Integer userAge) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userAge = userAge;
    }

    // 4. getter/setter方法（获取和修改私有属性）
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    // 5. 重写toString方法（方便打印用户信息，调试用）
    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userAge=" + userAge +
                '}';
    }
}