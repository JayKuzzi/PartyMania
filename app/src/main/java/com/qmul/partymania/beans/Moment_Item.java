package com.qmul.partymania.beans;

public class Moment_Item {
    private int postId;
    private int postManId;
    private String postManHeadphoto;
    private String postManNickname;
    private String postPhoto;
    private int likes;
    private String content;
    private String time;
    private int type;

    public Moment_Item(String postManHeadphoto, String postManNickname, String postPhoto, int likes, String content, String time, int type) {
        this.postManHeadphoto = postManHeadphoto;
        this.postManNickname = postManNickname;
        this.postPhoto = postPhoto;
        this.likes = likes;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public Moment_Item(int postId, String postManHeadphoto, String postManNickname, String postPhoto, int likes, String content, String time, int type) {
        this.postId = postId;
        this.postManHeadphoto = postManHeadphoto;
        this.postManNickname = postManNickname;
        this.postPhoto = postPhoto;
        this.likes = likes;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public Moment_Item(int postId, int postManId, String postManHeadphoto, String postManNickname, String postPhoto, int likes, String content, String time, int type) {
        this.postId = postId;
        this.postManId = postManId;
        this.postManHeadphoto = postManHeadphoto;
        this.postManNickname = postManNickname;
        this.postPhoto = postPhoto;
        this.likes = likes;
        this.content = content;
        this.time = time;
        this.type = type;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPostManId() {
        return postManId;
    }

    public void setPostManId(int postManId) {
        this.postManId = postManId;
    }

    public String getPostManHeadphoto() {
        return postManHeadphoto;
    }

    public void setPostManHeadphoto(String postManHeadphoto) {
        this.postManHeadphoto = postManHeadphoto;
    }

    public String getPostManNickname() {
        return postManNickname;
    }

    public void setPostManNickname(String postManNickname) {
        this.postManNickname = postManNickname;
    }

    public String getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(String postPhoto) {
        this.postPhoto = postPhoto;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
