package com.example.rubikscube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.function.ObjDoubleConsumer;

import min2phase.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FBLLURRFBUUFBRFDDFUULLFRDDLRFBLDRFBLUUBFLBDDBUURRBLDDR

        String scrambledCube = Tools.fromScramble("U' R2 B2 L2 R U' R2 F2 R B' F' L2 U2 B2 R' B' F' D2 B2 U F' R2 F2 U2 F2");

        Log.e("scramble", scrambledCube);

        String result = new Search().solution("LDDDUULRURBRLRLBDLFBFUFFFFRLRDUDRBBUDFDDLFURUFBBUBLBLR", 21, 100000000, 0, 0);
        Log.e("tag", "onCreate: " + result);

        Log.e("verify", String.valueOf(Tools.verify("LDDDUULRURBRLRLBDLFBFUFFFFRLRDUDRBBUDFDDLFURUFBBUBLBLR")));


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ScanActivity.class);
                startActivity(intent);
            }
        });
    }
}