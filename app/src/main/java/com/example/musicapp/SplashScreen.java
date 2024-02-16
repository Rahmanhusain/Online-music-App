package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       Thread t=new Thread(){
           @Override
           public void run() {
               super.run();
               try {
                   sleep(1000);
               }catch (Exception e){
                   e.printStackTrace();
               }finally {
                   Intent i=new Intent(SplashScreen.this,MainActivity.class);
                   startActivity(i);
               }

           }
       };t.start();
    }
}