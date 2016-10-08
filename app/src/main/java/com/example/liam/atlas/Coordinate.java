package com.example.liam.atlas;

import android.graphics.Bitmap;


/**
 * Created by Liam on 10/8/2016.
 */

public class Coordinate {

    protected double x,y, points;
    protected Bitmap image;
    public Coordinate(double x, double y, Bitmap image){
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
