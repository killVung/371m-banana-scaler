package niclam.banana_scaler_alpha;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by ET on 5/3/2016.
 */

public class PreviewCameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }
}