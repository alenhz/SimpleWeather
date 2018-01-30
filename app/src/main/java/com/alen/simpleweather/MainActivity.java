package com.alen.simpleweather;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.alen.simpleweather.util.Utility;


public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            go();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utility.statusBar(getWindow());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            create();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Utility.toastUtil("此选项");
                    create();
                } else {
                    Utility.toastUtil("你拒绝此选项");
                }
                break;
            default:
        }
    }

    private void create(){
        handler.postDelayed(runnable, 1000);
        if (Utility.getPrefe("setting", "isGuide") != null && Utility.getPrefe("setting", "isGuide").equals("0")){
            go();
        }
        Button button = findViewById(R.id.main_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go();
            }
        });
    }
    private void go(){
        handler.removeCallbacks(runnable);
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
        finish();
    }
}


