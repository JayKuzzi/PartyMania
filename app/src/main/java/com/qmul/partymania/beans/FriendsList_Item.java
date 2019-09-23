package com.qmul.partymania.beans;

public class FriendsList_Item {
    private int friendsId;
    private String nickName;
    private String freeTime;
    private int sex;
    private String sign;
    private String business;
    private String hobby;
    private int age;
    private String headphoto;
    private String email;
    private String relationship;

    public FriendsList_Item(String nickName, String freeTime, int sex, String sign, String business, String hobby, int age, String headphoto, String email, String relationship) {
        this.nickName = nickName;
        this.freeTime = freeTime;
        this.sex = sex;
        this.sign = sign;
        this.business = business;
        this.hobby = hobby;
        this.age = age;
        this.headphoto = headphoto;
        this.email = email;
        this.relationship = relationship;
    }

    public FriendsList_Item(int friendsId, String nickName, String headphoto) {
        this.friendsId = friendsId;
        this.nickName = nickName;
        this.headphoto = headphoto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getFriendsId() {
        return friendsId;
    }

    public void setFriendsId(int friendsId) {
        this.friendsId = friendsId;
    }
}
