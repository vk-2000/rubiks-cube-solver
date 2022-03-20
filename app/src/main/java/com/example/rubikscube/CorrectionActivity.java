package com.example.rubikscube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import min2phase.Tools;

public class CorrectionActivity extends AppCompatActivity {
    LinearLayout cubeLayout;
    Cube2DView cube2DView;
    HorizontalScrollView scrollCubeContainer;
    private LinearLayout colorsContainer;
    private RCube rCube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction);

        cubeLayout = findViewById(R.id.cubeContainerLayout);

        rCube = (RCube) getIntent().getSerializableExtra("rCube");

        scrollCubeContainer = findViewById(R.id.scrollCubeContainer);
        scrollCubeContainer.post(new Runnable() {
                       @Override
                       public void run() {
//                           int width = scrollCubeContainer.getWidth();
                           int height = scrollCubeContainer.getHeight();
                           Log.d("height", "run: " + height);

                           int dim = height/11;

                           create2DCube(dim);


                       }
                   });

        colorsContainer = findViewById(R.id.colorsContainer);
        for(int i=0;i<colorsContainer.getChildCount();i++){
            int finalI = i;
            colorsContainer.getChildAt(i).setOnClickListener(view -> cube2DView.selectedColor = finalI);
        }

        Button btnSolve = findViewById(R.id.btnSolve);
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Tools.verify(rCube.getStringCube()) == 0){
                    Intent intent = new Intent(getBaseContext(), SolveActivity.class);
                    intent.putExtra("rCube", rCube);
                    startActivity(intent);
                }
                else{
                    Toast toast = Toast.makeText(getBaseContext(), "Invalid cube! Please check the colors", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cube2DView.invalidate();
    }

    private void create2DCube(int dim) {
        cube2DView = new Cube2DView(getBaseContext(), dim);
        cube2DView.setrCube(rCube);
        cube2DView.setLayoutParams(new LinearLayout.LayoutParams(14 * dim, 11 * dim));

        cubeLayout.addView(cube2DView);

    }
}