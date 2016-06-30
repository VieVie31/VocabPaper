package edu.orissermaroix.vocabpaper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by VieVie31 on 30/06/2016.
 */
public class MyWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new MyWallPaperEngine();
    }

    private class MyWallPaperEngine extends Engine {
        private final Handler handler = new Handler();
        private Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };
        private int currectWordIndex = 0;
        private ArrayList<MyWord> myWords;
        private Paint paint = new Paint();
        private int width, height;
        private boolean visible = true;
        private boolean dispFrenchOnTouch, dispEnglishOnTouch;
        private boolean dispFr, dispEn;

        private float pX = 0f, pY = 0f;



        public MyWallPaperEngine() {
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(MyWallpaperService.this);

            dispFrenchOnTouch = prefs.getBoolean("fr_cb", false);
            dispEnglishOnTouch = prefs.getBoolean("en_cb", false);

            dispFr = !dispFrenchOnTouch;
            dispEn = !dispEnglishOnTouch;

            myWords = WordListFileManagement.getWordsToStudy();
            if (myWords.size() == 0) {
                myWords.add(new MyWord("English", "Anglais"));
                myWords.add(new MyWord("French", "Français"));
            }

            WordListFileManagement.writeToFile(myWords); // remove later...

            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);

            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;

            if (visible)
                handler.post(drawRunner);
            else
                handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    pX = x;
                    pY = y;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.pow(Math.pow(x - pX, 2.f) + Math.pow(y - pY, 2.f), .5) < 10) {
                        // display the world(s)
                        if (dispEnglishOnTouch)
                            dispEn = !dispEn;
                        if (dispFrenchOnTouch)
                            dispFr = !dispFr;
                    } else  {
                        // change the word
                        ++currectWordIndex;
                        currectWordIndex %= myWords.size();
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                default:
                    break;
            }


            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.DKGRAY);
                    drawWord(canvas, myWords.get(currectWordIndex));
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            super.onTouchEvent(event);
        }

        private void draw() {
            //actualize the word list to display..
            if (WordListFileManagement.actualiseWordList) {
                myWords = WordListFileManagement.getWordsToStudy();
                WordListFileManagement.actualiseWordList = false;
            }

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null)
                    drawWord(canvas, myWords.get(currectWordIndex));
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            handler.removeCallbacks(drawRunner);

            if (visible)
                handler.postDelayed(drawRunner, 5000);
        }

        private void drawWord(Canvas canvas, MyWord word) {
            canvas.drawColor(Color.DKGRAY);

            int maxTextSize = (int) (1.5 * width / Math.max(
                    word.getEnglish().length(),
                    word.getFrench().length()));

            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setStyle(Paint.Style.STROKE);
            paint.setTextSize(maxTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);

            if (dispEn)
                canvas.drawText(word.getEnglish(), width / 2, height / 2 - maxTextSize / 2, paint);
            if (dispFr)
                canvas.drawText(word.getFrench(), width / 2, height / 2 + maxTextSize / 2, paint);
        }
    }
}
