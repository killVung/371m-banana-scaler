package niclam.banana_scaler_alpha;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 3;
    private static final String TAG = "Menu";
    Animation.AnimationListener animationSlideInLeftListener =
            new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                TextView logo = (TextView) findViewById(R.id.logo);
                ImageView logoImage = (ImageView) findViewById(R.id.logoImage);
                Button capture = (Button) findViewById(R.id.capture);
                Button viewPhoto = (Button) findViewById(R.id.view);
                slideLeft(logoImage);
                slideLeft(logo);
                slideLeft(capture);
                slideLeft(viewPhoto);
            }
        }, 500);
        getPermissions();
        setContentView(R.layout.activity_menu);
    }

    private void slideLeft(View v) {

        Animation animationSlideInLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        animationSlideInLeft.setDuration(900);
        animationSlideInLeft.setAnimationListener(animationSlideInLeftListener);

        v.startAnimation(animationSlideInLeft);
        v.setVisibility(View.VISIBLE);
    }

    private void slideRight(View v) {

        Animation animationSlideInLeft = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        animationSlideInLeft.setDuration(300);
        animationSlideInLeft.setAnimationListener(animationSlideInLeftListener);

        v.startAnimation(animationSlideInLeft);
        v.setVisibility(View.VISIBLE);
    }

    public void startCaptureFotos(View c) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                TextView logo = (TextView) findViewById(R.id.logo);
                ImageView logoImage = (ImageView) findViewById(R.id.logoImage);
                Button capture = (Button) findViewById(R.id.capture);
                Button viewPhoto = (Button) findViewById(R.id.view);
                slideRight(logoImage);
                slideRight(logo);
                slideRight(capture);
                slideRight(viewPhoto);
            }
        }, 100);

        startActivity(new Intent(this, PreviewCameraActivity.class));
    }

    public void startViewFotos(View v) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                TextView logo = (TextView) findViewById(R.id.logo);
                ImageView logoImage = (ImageView) findViewById(R.id.logoImage);
                Button capture = (Button) findViewById(R.id.capture);
                Button viewPhoto = (Button) findViewById(R.id.view);
                slideRight(logoImage);
                slideRight(logo);
                slideRight(capture);
                slideRight(viewPhoto);
            }
        }, 100);
        startActivity(new Intent(this, GalleryActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    public void getPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            /// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        /// Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

}

