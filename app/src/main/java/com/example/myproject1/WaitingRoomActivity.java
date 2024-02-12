package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myproject1.GamePage.GameActivity;

public class WaitingRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
    }

    public void StartClicked(View view)
    {
        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
        startActivity(intent);
    }
}