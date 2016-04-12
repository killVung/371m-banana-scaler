package niclam.banana_scaler_alpha;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    private File getTempFile(Context context){
        //it will return /sdcard/image.tmp
        final File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName() );
        if(!path.exists()){
            path.mkdir();
        }
        return new File(path, "image.tmp");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toast.makeText(this,"Please capture the image you want to draw",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(getBaseContext())));
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        File file = getTempFile(getBaseContext());
        Intent intent = new Intent(getBaseContext(), DrawingBananaActivity.class);
        intent.putExtra("filename", Uri.fromFile(file).toString());
        intent.putExtra("isCamera", true);
        if(resultCode == RESULT_CANCELED){
            finish();
        }else{
            fuckingCheckPermission();
            startActivity(intent);
            finish();
        }

    }
    /** Fucking check the permission to make sure the fucking READ DISK is accessible on fucking Android 6.0 **/
    private void fuckingCheckPermission() {

    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_CANCELED, mIntent);
        super.onBackPressed();

    }

}
