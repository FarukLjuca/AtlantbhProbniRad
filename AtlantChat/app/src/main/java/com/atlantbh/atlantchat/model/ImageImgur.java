package com.atlantbh.atlantchat.model;

/**
 * Created by Faruk on 29/06/16.
 */
public class ImageImgur {
    private String image;
    private String type;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageImgur(String image, String type) {
        this.image = image;
        this.type = type;
    }
}
