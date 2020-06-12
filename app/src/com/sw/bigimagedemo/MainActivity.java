package com.sw.bigimagedemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sw.img.SwImageView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    private SwImageView mSwImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwImageView = findViewById(R.id.swiv_main);
        init();
    }

    private void init(){
        try {
            mSwImageView.setImageResource(getAssets().open("large.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
