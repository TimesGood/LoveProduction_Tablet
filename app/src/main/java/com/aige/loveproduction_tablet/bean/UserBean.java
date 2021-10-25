package com.aige.loveproduction_tablet.bean;

public class UserBean {
    private String userName;
    private String password;
    private String email;
    private String salt;
    private String nickName;
    private boolean isLocked;
    private boolean isDesigner;
    private String appUID;
    private String roleName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isDesigner() {
        return isDesigner;
    }

    public void setDesigner(boolean designer) {
        isDesigner = designer;
    }

    public String getAppUID() {
        return appUID;
    }

    public void setAppUID(String appUID) {
        this.appUID = appUID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isLocked=" + isLocked +
                ", isDesigner=" + isDesigner +
                ", appUID='" + appUID + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
