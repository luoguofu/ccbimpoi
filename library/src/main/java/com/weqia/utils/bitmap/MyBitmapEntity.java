package com.weqia.utils.bitmap;

public class MyBitmapEntity {
    private float x;
    private float y;
    private float width;
    private float height;
    private static int devide = 1;
    private int index = -1;

    @Override
    public String toString() {
        return "MyBitmap [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height
                + ", devide=" + devide + ", index=" + index + "]";
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public static int getDevide() {
        return devide;
    }

    public static void setDevide(int devide) {
        MyBitmapEntity.devide = devide;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    
}
