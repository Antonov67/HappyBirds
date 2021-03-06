package com.example.happybirds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;

public class GameView extends View {

    private int viewWidth;
    private int viewHeight;
    private int points = 0;

    private Sprite playerBird;
    private Sprite enemyBird;

    private final int timerInterval = 30;

    //конструктор
    public GameView(Context context) {
        super(context);

        //создаем птицу-игрока
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.player);

        int w = b.getWidth()/5;
        int h = b.getHeight()/3;

        Rect firstFrame = new Rect(0,0,w,h);

        playerBird = new Sprite(b, 10,0,0,100,firstFrame);
        for (int i=0; i<3; i++){
            for (int j=0; j<4; j++){
                if (i ==0 && j == 0) continue;
                if (i ==2 && j == 3) continue;
                playerBird.addFrame(new Rect(j * w, i * h, j * w + w, i * w + w));
            }
        }

        //создаем птицу-врага
        b = BitmapFactory.decodeResource(getResources(),R.drawable.enemy);

        w = b.getWidth()/5;
        h = b.getHeight()/3;

        firstFrame = new Rect(4*w,0,5*w,h);

        enemyBird = new Sprite(b, 2000,250,-300,0,firstFrame);
        for (int i=0; i<3; i++){
            for (int j=4; j>=0; j--){
                if (i == 0 && j == 4) continue;
                if (i ==2 && j == 0) continue;
                enemyBird.addFrame(new Rect(j * w, i * h, j * w + w, i * w + w));
            }
        }

        Timer timer = new Timer();
        timer.start();
    }

    //обработаем нажатие на экран
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN) {
            //поднимем птичку вверх
            if (event.getY() < playerBird.getBoundingBoxRect().top) {
                playerBird.setVelocityY(-100);
                points--;
            } else if (event.getY() > playerBird.getBoundingBoxRect().bottom) {
                playerBird.setVelocityY(100);
                points--;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    //здесь рисуем
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawARGB(255,255,255,255);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(55.0f);
        paint.setColor(Color.BLACK);
        canvas.drawText(points + "", viewWidth - 100, 70, paint);

        playerBird.draw(canvas);
        enemyBird.draw(canvas);
    }

    //возвращение врага после пролета
    private void teleportEnemy(){
        enemyBird.setX(viewWidth + Math.random()*500);
        enemyBird.setY(Math.random() * (viewHeight - enemyBird.getFrameHeight()));
    }




    protected void update(){
        playerBird.update(timerInterval);
        enemyBird.update(timerInterval);
        if (enemyBird.getX() < - enemyBird.getFrameWidth()){
            teleportEnemy();
            points += 10;
        }

        //проверка столкновений
        if (enemyBird.intersect(playerBird)){
            teleportEnemy();
            points -=40;
        }

        //если птичка касается края экрана, то она меняет направление движения
        if (playerBird.getY() + playerBird.getFrameHeight() > viewHeight) {
            playerBird.setY(viewHeight - playerBird.getFrameHeight());
            playerBird.setVelocityY(-playerBird.getVelocityY());
            points--;
        }
        else if (playerBird.getY() < 0) {
            playerBird.setY(0);
            playerBird.setVelocityY(-playerBird.getVelocityY());
            points--;
        }

        invalidate();
    }

    //внутренний класс
    class Timer extends CountDownTimer{

        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {

        }
    }




}

