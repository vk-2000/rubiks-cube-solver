package com.example.rubikscube;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.rubikscube.CameraPreview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    public long[][] colorsArray = new long[3][3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        float density = getResources().getDisplayMetrics().density;

        mCamera = getCameraInstance();



        Camera.Parameters params = mCamera.getParameters();
        // set the focus mode
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        params.setFocusMode("continuous-picture");
        params.setPictureSize(800,600);
        // set Camera parameters
        mCamera.setParameters(params);


        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.cam_pre);
        int padding = (int)(50*density);
        imageView.setPadding(padding, 0, padding, 0);

        preview.addView(imageView);


        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                v -> {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();      // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
            final int maxSize = 300;
            int outWidth;
            int outHeight;
            int inWidth = bitmap.getWidth();
            int inHeight = bitmap.getHeight();
            if(inWidth > inHeight){
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            Bitmap resized = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
            bitmap.recycle();
            Log.d("width", "onPictureTaken: " + resized.getWidth());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 90, out);
            saveImage(out.toByteArray());
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            analyzeImage(decoded);
            sendScan();

        }
    };

    private void saveImage(byte[] data) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null){
            Log.d("aaa", "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            mCamera.startPreview();
        } catch (FileNotFoundException e) {
            Log.d("bbb", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("ccc", "Error accessing file: " + e.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void analyzeImage(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Color center = bitmap.getColor(width/2, height/2);
        int k = height/2;
        int centerDim = 0;

        // go up and down from center until color changes to get the dimensions of the center piece
        while(colorsAreClose(center, bitmap.getColor(width/2,k), 30)){
            k--;
            centerDim++;
        }
        k = height/2;
        while(colorsAreClose(center, bitmap.getColor(width/2,k), 30)){
            k++;
            centerDim++;
        }
        Log.d("centerDim", String.valueOf(centerDim));
        long cd = ((long)(center.red() * 255) << 16) + ((long)(center.green() * 255) << 8) + ((long)(center.blue() * 255));
        Log.d("cneter", String.valueOf(cd));

        int a = width/2 - centerDim;
        int b = height/2 - centerDim;

        for(int i=0;i<3;i++){
            int y = b + i*centerDim;
            for(int j=0;j<3;j++){
                int x = a + j*centerDim;
                Color c = bitmap.getColor(x, y);
                long dec = ((long)(c.red() * 255) << 16) + ((long)(c.green() * 255) << 8) + ((long)(c.blue() * 255));
                colorsArray[i][j] = dec;
//                Log.d(x + " " + y, String.valueOf(dec));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    boolean colorsAreClose(Color a, Color z, int threshold)
    {
        int r = (int)(a.red()*255) - (int)(z.red() * 255);
        int g = (int)(a.green()*255) - (int)(z.green() * 255);
        int b = (int)(a.blue()*255) - (int)(z.blue() * 255);
        return (r*r + g*g + b*b) <= threshold*threshold;
    }

    private void sendScan() {
        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        rotate90Clockwise(colorsArray);
        bundle.putSerializable("result", colorsArray);
        returnIntent.putExtras(bundle);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void rotate90Clockwise(long a[][])
    {
        for (int j = 0; j < 2; j++)
            {
                long temp = a[0][j];
                a[0][j] = a[2 - j][0];
                a[2 - j][0] = a[2][2 - j];
                a[2][2 - j] = a[j][2];
                a[j][2] = temp;
            }
    }

    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }

        File mediaStorageDir = new File(getExternalFilesDir(null) + "/" + "cubes");
        Log.d("fjile", "getOutputMediaFile: " + mediaStorageDir.getPath());
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
    }
}