package com.example.hompage.model;

public class Friend {
    private String friendId; // Firestore UID
    private String profileImageUrl;
    private String nickname;
    private String statusMessage;

    public Friend() {
    }

    public Friend(String friendId, String profileImageUrl, String nickname, String statusMessage) {
        this.friendId = friendId;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.statusMessage = statusMessage;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
