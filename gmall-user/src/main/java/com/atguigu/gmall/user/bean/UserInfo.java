package com.atguigu.gmall.user.bean;

public class UserInfo {
    private String id;
    private String login_name;
    private String nick_name;
    private String passwd;
    private String name;
    private String phone_num;
    private String email;
    private String head_img;
    private String user_level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public UserInfo(String id, String login_name, String nick_name, String passwd, String name, String phone_num, String email, String head_img, String user_level) {
        this.id = id;
        this.login_name = login_name;
        this.nick_name = nick_name;
        this.passwd = passwd;
        this.name = name;
        this.phone_num = phone_num;
        this.email = email;
        this.head_img = head_img;
        this.user_level = user_level;
    }
}
