package com.qmul.partymania.beans;

public class PostList_Item {
    private int postId;
    private String photo;
    private String postedTo;
    private String content;
    private String time;


    public PostList_Item(int postId, String photo, String postedTo, String content, String time) {
        this.postId = postId;
        this.photo = photo;
        this.postedTo = postedTo;
        this.content = content;
        this.time = time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPostedTo() {
        return postedTo;
    }

    public void setPostedTo(String postedTo) {
        this.postedTo = postedTo;
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

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
