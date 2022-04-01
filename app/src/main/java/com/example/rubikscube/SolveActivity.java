package com.example.rubikscube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import min2phase.*;

public class SolveActivity extends AppCompatActivity {

    private RCube rCube;
    LinearLayout cubeLayout;
    Cube2DView cube2DView;
    HorizontalScrollView scrollCubeContainer;
    private String[] solution;
    private TextView txtMove;
    private Button btnNext;
    private Button btnPrev;
    private int moveIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);

        cubeLayout = findViewById(R.id.cubeContainerLayout);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        txtMove = findViewById(R.id.txtMove);

        rCube = (RCube) getIntent().getSerializableExtra("rCube");

        String stringCube = rCube.getStringCube();
        Log.d("StringCube", stringCube);
        String result = new Search().solution(stringCube, 21, 100000000, 0, 0);
        Log.d("tag", "onCreate: " + result);
        String[] sol = result.split(" ", 0);
        Log.d("solution", Arrays.deepToString(solution));

        ArrayList<String> stringArrayList = new ArrayList<>();
        for(String s: sol){
            if(!s.equals("")){
                stringArrayList.add(s);
            }
        }
        solution = (String[]) stringArrayList.toArray(new String[0]);





        TextView txtResult = findViewById(R.id.txtResult);
        txtResult.setText(result);

        scrollCubeContainer = findViewById(R.id.scrollCubeContainer);
        scrollCubeContainer.post(() -> {
            int height = scrollCubeContainer.getHeight();
            Log.d("height", "run: " + height);

            int dim = height/11;

            create2DCube(dim);


        });

    }

    private void create2DCube(int dim) {
        cube2DView = new Cube2DView(getBaseContext(), dim);
        cube2DView.setrCube(rCube);
        cube2DView.setLayoutParams(new LinearLayout.LayoutParams(14 * dim, 11 * dim));

        cubeLayout.addView(cube2DView);


        solveMoves();



    }

    private void solveMoves() {

        txtMove.setText("Start");

        btnNext.setOnClickListener(view -> {
            moveIndex++;
            if(moveIndex >= solution.length){
                moveIndex = solution.length - 1;
                return;
            }
            txtMove.setText(solution[moveIndex]);
            cube2DView.makeMove(solution[moveIndex]);



        });

        btnPrev.setOnClickListener(view -> {


            if(moveIndex == -1){
                return;
            }
            cube2DView.makeMoveReverse(solution[moveIndex]);
            moveIndex--;
            if(moveIndex == -1){
                txtMove.setText("Start");
                return;
            }
            txtMove.setText(solution[moveIndex]);

        });
    }
}