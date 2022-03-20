package com.example.rubikscube;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScanActivity extends AppCompatActivity {
    TextView txtMain;
    Button btnScan;
    int facing = 1;
    public RCube rCube = new RCube();
    private CubeFaceView cubeFaceView;
    public LinearLayout cubeFaceContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        txtMain = findViewById(R.id.txtMain);
        btnScan = findViewById(R.id.btnScan);

        txtMain.setText("Face " + String.valueOf(facing));

        cubeFaceContainer = findViewById(R.id.cube_face_container);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        int dim = Math.min(displayHeight, displayWidth);

        cubeFaceView = new CubeFaceView(this, dim/5);
        cubeFaceView.setColors(1,5,3,6,2);
        cubeFaceView.setLayoutParams(new LinearLayout.LayoutParams(dim, dim));

        cubeFaceContainer.addView(cubeFaceView);

        btnScan.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), CameraActivity.class);
            startActivityForResult(intent, 1);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            Bundle bundle = data.getExtras();
            long[][] colorsArray = (long[][]) bundle.getSerializable("result");
            rCube.addFace(colorsArray);

            facing++;
            switch (facing){
                case 2:
                    cubeFaceView.setColors(2,5,1,6,4); break;
                case 3:
                    cubeFaceView.setColors(3,5,4,6,1); break;
                case 4:
                    cubeFaceView.setColors(4,5,2,6,3); break;
                case 5:
                    cubeFaceView.setColors(5,4,3,1,2); break;
                case 6:
                    cubeFaceView.setColors(6,1,3,4,2); break;
            }
//            public RCube(){
//                colorMap.put(0,'-');
//                colorMap.put(1,'W');
//                colorMap.put(2,'O');
//                colorMap.put(3,'R');
//                colorMap.put(4,'Y');
//                colorMap.put(5,'B');
//                colorMap.put(6,'G');
//            }
            if(facing > 6){
                rCube.configureColors();
                rCube.printFacesColors();

                // go to next activity

                Intent intent = new Intent(getBaseContext(), CorrectionActivity.class);
                intent.putExtra("rCube", rCube);
                startActivity(intent);


            }
            txtMain.setText("Face " + String.valueOf(facing));



        }
    }
}