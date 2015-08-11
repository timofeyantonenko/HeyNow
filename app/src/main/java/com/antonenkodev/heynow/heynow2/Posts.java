package com.antonenkodev.heynow.heynow2;

/**
 * Created by root on 19.04.15.
 */
public class Posts {
    private String userName;
    private String image;
    private int network;
    private int likes;
    private String standartIm;
    private String created_time;

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getStandartIm() {
        return standartIm;
    }

    public void setStandartIm(String standartIm) {
        this.standartIm = standartIm;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getNetwork() {
        return network;
    }

    public void setNetwork(int network) {
        this.network = network;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {

        return userName;
    }

    public String getImage() {
        return image;
    }
}
