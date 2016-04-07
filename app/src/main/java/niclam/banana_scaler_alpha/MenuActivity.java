package niclam.banana_scaler_alpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "Menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startCaptureFotos(View c){
        Log.d(TAG,"capture!");
        startActivity(new Intent(this, CameraActivity.class));
    }

    public void startViewFotos(View v){
        Log.d(TAG,"view!");
        Toast.makeText(this,"Fuck you", Toast.LENGTH_SHORT).show();
    }

}
