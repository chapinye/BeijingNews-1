package com.atguigu.ljt.switchbutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.suke.widget.SwitchButton;


public class MainActivity extends AppCompatActivity {
    private SwitchButton switchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchButton = (SwitchButton) findViewById(R.id.button);
        switchButton.setChecked(true);
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if(isChecked) {
                    Toast.makeText(MainActivity.this, "开", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "关", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
