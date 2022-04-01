package com.example.rubikscube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.function.ObjDoubleConsumer;

import min2phase.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        LinearLayout btnContainer = findViewById(R.id.btnContainer);
        btnContainer.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), ScanActivity.class);
            startActivity(intent);
        });
    }
}