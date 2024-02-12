package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }


    public void PlayClicked(View view)
    {
        Intent intent = new Intent(MainScreenActivity.this, WaitingRoomActivity.class);
        startActivity(intent);
    }

}