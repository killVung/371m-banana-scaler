package niclam.banana_scaler_alpha;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DrawingBananaActivity extends Activity implements View.OnTouchListener {
    final Handler handler = new Handler();
    FastRenderView renderView;
    Bitmap ba;
    Bitmap origin_ba;
    Bitmap bg;
    List<PointF> points;
    Point display;
    Bitmap mutableBg;
    Runnable mLongPressed = new Runnable() {
        public void run() {
            openContextMenu(renderView);
        }
    };
    private float ratio;

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
        registerForContextMenu(renderView);
        scaleBanana();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Save Foto");
        menu.add(0, v.getId(), 0, "Clear banana");
        menu.add(0, v.getId(), 0, "Scale banana");
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Save Foto") {
            CapturePhotoUtils.insertImage(getContentResolver(), mutableBg, Long.toString(System.currentTimeMillis()), "");
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "Clear banana") {
            points.clear();
            Toast.makeText(this, "cleared", Toast.LENGTH_SHORT).show();
        } else if (item.getTitle() == "Scale banana") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter the % of banana scale size");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    float scale_to = Float.parseFloat(input.getText().toString());
                    Log.e("Comeon", "1:" + Float.toString(scale_to));
                    Log.e("Comeon", "2:" + Float.toString(ratio));
                    ratio = ratio*scale_to/100;
                    Log.e("Comeon", "3:" + Float.toString(ratio));
                    ba = scaleImage(origin_ba, ratio);
                    points.clear();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            return false;
        }
        return true;
    }

    private void setup() {
        /*  Banana and the Background  */
        ba = BitmapFactory.decodeResource(getResources(), R.drawable.banana);

        Intent picture = getIntent();
        Uri myUri = Uri.parse(picture.getExtras().getString("filename"));
        boolean isCamera = picture.getExtras().getBoolean("isCamera");
        try {
            if (isCamera) {
                bg = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
            } else {
                Bitmap hardcopy = BitmapFactory.decodeFile(myUri.toString());
                bg = hardcopy.copy(Bitmap.Config.ARGB_8888, true);
            }

        } catch (FileNotFoundException e) {
            Log.e("someError", Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e("someError", Log.getStackTraceString(e));
        }

        display = new Point();
        points = new LinkedList<>();
    }

    private void scaleBanana() {
        Display currentDisplay = getWindowManager().getDefaultDisplay();
        currentDisplay.getSize(display);

        ratio = Math.max(
                (float) display.x/(ba.getWidth()*20),
                (float) display.y/(ba.getHeight()*20));

        ba = scaleImage(ba, ratio);
        ratio = 1.0f;
        origin_ba = ba.copy(Bitmap.Config.ARGB_8888, true);
    }

    private Bitmap scaleImage(Bitmap image, float ratio) {
        int width = Math.round((float) ratio * image.getWidth());
        int height = Math.round((float) ratio * image.getHeight());
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

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                handler.removeCallbacks(mLongPressed);
                PointF touchedPoint = new PointF(event.getX(), event.getY());
                if (!isOverlap(points, touchedPoint)) {
                    points.add(touchedPoint);
                }
                return true;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(mLongPressed);
                touchedPoint = new PointF(event.getX(), event.getY());
                if (!isOverlap(points, touchedPoint)) {
                    points.add(touchedPoint);
                } else {
                    displayImpossibleBanana();
                }
                return true;
            case MotionEvent.ACTION_DOWN:
                handler.postDelayed(mLongPressed, 1000);
        }
        return true;
    }

    /**
     * Notify user for unable to put banana on screen
     **/
    private void displayImpossibleBanana() {
        final Toast toast = Toast.makeText(getApplicationContext(), "Unable to put banana", Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
    }

    /**
     * Return true if the points are overlapped
     **/
    private boolean isOverlap(List<PointF> points, PointF point) {
        /* Traverse the list of points, return true if the point */
        for (int i = 0; i < points.size(); i++) {
            if (withinBound(points.get(i), point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * A helper function to determine whether any given point is within any given point
     **/
    private boolean withinBound(PointF p1, PointF p2) {
        //Check if the point is within the boundaries of the point
        double offset = 1;
        return p2.x >= (p1.x - ba.getWidth()) * offset && p2.y >= (p1.y - ba.getHeight()) * offset &&
                p2.x <= (p1.x + ba.getWidth()) * offset && p2.y <= (p1.y + ba.getHeight()) * offset;
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

    class FastRenderView extends SurfaceView implements Runnable {
        Thread renderThread = null;
        SurfaceHolder holder;
        volatile boolean running = false;

        public FastRenderView(Context context) {
            super(context);
            holder = getHolder();
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
                mutableBg = Bitmap.createScaledBitmap(bg, display.x, display.y, true);
                Canvas bgCanvas = new Canvas(mutableBg);
                canvas.drawBitmap(bg,
                        null,
                        new Rect(0, 0, display.x, display.y),
                        null);

                for (int i = 0; i < points.size(); i++) {
                    float x = points.get(i).x - (ba.getWidth() / 2);
                    float y = points.get(i).y - (ba.getHeight() / 2);
                    canvas.drawBitmap(ba, x, y, null);
                    bgCanvas.drawBitmap(ba, x, y, null);
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
}