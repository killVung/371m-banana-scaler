package com.example.niclam.banana_scalerplayground;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import java.util.LinkedList;
import java.util.List;


public class SurfaceViewTest extends Activity implements View.OnTouchListener {
    FastRenderView renderView;
    Bitmap ba;
    Bitmap bg;
    List <PointF> points;
    Point display;

    public void onCreate(Bundle savedInstanceState) {
        setup();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        renderView = new FastRenderView(this);
        renderView.setOnTouchListener(this);
        setContentView(renderView);
    }

    private void setup() {
        /*  Banana and the Background  */
        ba = BitmapFactory.decodeResource(getResources(), R.drawable.banana);

        float ratio = Math.min(
                (float) 100 / ba.getWidth(),
                (float) 100 / ba.getHeight());

        ba = scaleImage(ba, ratio);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.test);

        display = new Point();
        points = new LinkedList<>();
    }

    private Bitmap scaleImage(Bitmap image, float ratio){
        int width = Math.round((float) ratio * ba.getWidth());
        int height = Math.round((float) ratio * ba.getHeight());
        return Bitmap.createScaledBitmap(image, width,
                height, false);
    }

    protected void onResume() {
        super.onResume();
        renderView.resume();
    }

    protected void onPause() {
        super.onPause();
        renderView.pause();
    }

    class FastRenderView extends SurfaceView implements Runnable {
        Thread renderThread = null;
        SurfaceHolder holder;
        volatile boolean running = false;

        public FastRenderView(Context context) {
            super(context);
            holder = getHolder();

            /** We may need this code to get our images **/

//            try {
//                AssetManager assetManager = context.getAssets();
//                InputStream inputStream = assetManager.open("bobrgb888.png");
////                bob565 = BitmapFactory.decodeStream(inputStream);
////                inputStream.close();
////                Log.d("BitmapText",
////                        "bobrgb888.png format: " + bob565.getConfig());
//
//                inputStream = assetManager.open("bobargb8888.png");
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
////                bob4444 = BitmapFactory
////                        .decodeStream(inputStream, null, options);
////                inputStream.close();
////                Log.d("BitmapText",
////                        "bobargb8888.png format: " + bob4444.getConfig());
//
//            } catch (IOException e) {
//                // silently ignored, bad coder monkey, baaad!
//            } finally {
//                // we should really close our input streams here.
//            }
        }

        public void resume() {
            running = true;
            renderThread = new Thread(this);
            renderThread.start();
        }

        public void run() {
            while (running) {

                if (!holder.getSurface().isValid())
                    continue;

                Canvas canvas = holder.lockCanvas();
                Display currentDisplay = getWindowManager().getDefaultDisplay();
                currentDisplay.getSize(display);
                canvas.drawBitmap(bg,
                        null,
                        new Rect(0,0,display.x,display.y),
                        null);

                for(int i = 0; i < points.size(); i++){
                    canvas.drawBitmap(ba,points.get(i).x,points.get(i).y,null);
                }

                holder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            running = false;
            while (true) {
                try {
                    renderThread.join();
                    return;
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                points.add(new PointF(event.getX(),event.getY()));

                return true;
        }
        return true;
    }
}