package com.qmul.partymania.beans;

public class Comment_Item {
    private String nickname,comment;

    public Comment_Item(String nickname, String comment) {
        this.nickname = nickname;
        this.comment = comment;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
