package niclam.banana_scaler_alpha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DrawingBananaActivity extends Activity implements View.OnTouchListener {
    FastRenderView renderView;
    Bitmap ba;
    Bitmap bg;
    List <PointF> points;
    Point display;

    public void onCreate(Bundle savedInstanceState) {
        setup();
        Toast.makeText(DrawingBananaActivity.this, "TAP Screen to put banana", Toast.LENGTH_SHORT).show();
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
        Intent picture = getIntent();
        Uri myUri = Uri.parse(picture.getExtras().getString("filename"));
        boolean isCamera = picture.getExtras().getBoolean("isCamera");
        try {
            if(isCamera){
                bg = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
            }else{
                bg = BitmapFactory.decodeFile(myUri.toString());
            }

        } catch (FileNotFoundException e) {
            Log.e("someError", Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e("someError", Log.getStackTraceString(e));
        }

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
                    float x = points.get(i).x - (ba.getWidth()/2);
                    float y = points.get(i).y - (ba.getHeight()/2);
                    canvas.drawBitmap(ba,x,y,null);
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

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable(){
        public void run() {
            Toast.makeText(DrawingBananaActivity.this, "Long long time ago in a galaxy far far away", Toast.LENGTH_SHORT).show();
        }
    };

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                handler.removeCallbacks(mLongPressed);
                PointF touchedPoint = new PointF(event.getX(),event.getY());
                if(!isOverlap(points,touchedPoint)){
                    points.add(touchedPoint);
                }
                return true;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongPressed);
                touchedPoint = new PointF(event.getX(),event.getY());
                if(!isOverlap(points,touchedPoint)){
                    points.add(touchedPoint);
                }
                return true;
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(mLongPressed, 1000);
        }
        return true;
    }

    /** Return true if the points are overlapped **/
    private boolean isOverlap(List<PointF> points, PointF point) {
        /* Traverse the list of points, return true if the point */
        for(int i = 0; i < points.size(); i++){
            if(withinBound(points.get(i),point)){
                return true;
            }
        }
        return false;
    }


    /** A helper function to determine whether any given point is within any given point**/
    private boolean withinBound(PointF p1, PointF p2) {
        //Check if the point is within the boundaries of the point
        return p2.x >= p1.x-ba.getWidth() && p2.y >= p1.y - ba.getHeight() &&
                p2.x <= p1.x + ba.getWidth() && p2.y <= p1.y + ba.getHeight();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_CANCELED, mIntent);
        super.onBackPressed();
        finish();

    }
}