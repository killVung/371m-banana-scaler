package niclam.banana_scaler_alpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "Menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startCaptureFotos(View c){
        startActivity(new Intent(this, CameraActivity.class));
    }

    public void startViewFotos(View v){
        startActivity(new Intent(this, GalleryActivity.class));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
