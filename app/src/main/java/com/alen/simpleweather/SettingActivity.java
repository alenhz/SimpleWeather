package com.alen.simpleweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.alen.simpleweather.util.Utility;

public class SettingActivity extends AppCompatActivity {
    private CheckBox setting_checkbox;
    private Button setting_button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
    }

    private void init(){
        Utility.statusBar(getWindow());
        setting_checkbox = (CheckBox) findViewById(R.id.setting_checkbox);
        String isGuide = Utility.getPrefe("setting", "isGuide");
        if (isGuide != null && isGuide.equals("0")){
            setting_checkbox.setChecked(false);
        }else {
            setting_checkbox.setChecked(true);
        }
        setting_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Utility.setPrefe("setting", "isGuide", "1");
                }else {
                    Utility.setPrefe("setting", "isGuide", "0");
                }
            }
        });
        setting_button_back = (Button) findViewById(R.id.setting_button_back);
        setting_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
