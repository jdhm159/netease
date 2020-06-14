package com.gene;

public class CommentInfo {
    private String nickname;
    private String content;
    private String likeCount;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public String toString(){
        return nickname + " :  " + content.replaceAll("\\r|\\n", " ") + "\n    点赞:" + likeCount;
    }
}
