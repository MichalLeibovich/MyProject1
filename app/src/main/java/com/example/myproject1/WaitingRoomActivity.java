package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myproject1.GamePage.GameActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class WaitingRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        // Get the game code from the intent extras
        String gameCode = getIntent().getStringExtra("gameCode");

        TextView textView = findViewById (R.id.codeTextView);
        textView.setText(gameCode);


    }



    public void StartClicked(View view)
    {
        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
        startActivity(intent);
    }
}