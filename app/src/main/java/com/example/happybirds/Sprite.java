package com.example.happybirds;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Sprite {

    private Bitmap bitmap; //ссылка на изображение с набором кадров
    private List<Rect> frames; // коллекция прямоугольников - кадров для анимации
    private int frameWidth; // ширина кадра
    private int frameHeight; // высота кадра
    private int currentFrame; // номер текущего кадра в анимации
    private double frameTime; // время показа одного кадра в анимации
    private double timeForCurrentFrame; // текущее время показа кадра

    private double x; // координата х верхнего левого угла спрайта
    private double y; // координата у верхнего левого угла спрайта

    private double velocityX; // скорость спрайта по оси Х
    private double velocityY; // скорость спрайта по оси Y

    private int padding; // внутренний отступ от границ спрайта

    //конструктор
    public Sprite(Bitmap bitmap, double x, double y, double velocityX, double velocityY, Rect initialFrame) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);
        this.timeForCurrentFrame = 0.0;
        this.frameTime = 0.1;
        this.currentFrame = 0;
        this.frameWidth = initialFrame.width();
        this.frameHeight = initialFrame.height();
        this.padding = 20;
    }

    //метод добавления кадра в анимационную последовательность
    public void addFrame (Rect frame){
        frames.add(frame);
    }

    //метод обновления спрайта
    public void update (int ms){
        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }

        x = x + velocityX * ms / 1000.0;
        y = y + velocityY * ms / 1000.0;
    }

    //метод рисования спрайта на холсте
    public void draw (Canvas canvas){
        Paint paint = new Paint();
        Rect destination = new Rect((int)x, (int)y, (int)(x + frameWidth), (int)(y + frameHeight));
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, paint);
    }

    //метод, возвращающий прямоугольник для определения столкновения
    public Rect getBoundingBoxRect() {
        return new Rect((int)x + padding, (int)y + padding, (int)(x+frameWidth - 2*padding), (int)(y+frameHeight - 2*padding));
    }

    //метод определения пересечения спрайтов:
    public boolean intersect (Sprite sprite){
        return getBoundingBoxRect().intersect(sprite.getBoundingBoxRect());
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame % frames.size();
    }

    public double getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(double frameTime) {
        this.frameTime = Math.abs(frameTime);
    }

    public double getTimeForCurrentFrame() {
        return timeForCurrentFrame;
    }

    public void setTimeForCurrentFrame(double timeForCurrentFrame) {
        this.timeForCurrentFrame = Math.abs(timeForCurrentFrame);
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

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public int getFramesCount(){
        return frames.size();
    }
}
