package com.example.masterlogistica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash_inicio extends AppCompatActivity {

    int usu_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_inicio);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("shared_login_data", Context.MODE_PRIVATE);

        usu_id =  prefs.getInt("id",0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (usu_id == 0){
                    Intent intent = new Intent(getApplicationContext(),login.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },2000);

    }
}
