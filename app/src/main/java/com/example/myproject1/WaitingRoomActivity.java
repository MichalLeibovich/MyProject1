package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.GamePage.GameActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class WaitingRoomActivity extends AppCompatActivity {

    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        // Get the game code from the intent extras
        gameId = getIntent().getStringExtra("gameId");

        TextView textView = findViewById (R.id.codeTextView);
        textView.setText(gameId);
    }



    public void StartClicked(View view)
    {
        // Update the "started" field to true in the game document
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRef = firestore.collection("Games").document(gameId);

        gameRef
                .update("started", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Game started successfully
                        Intent intent = new Intent(WaitingRoomActivity.this, GameActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure to update document
                        Toast.makeText(WaitingRoomActivity.this, "Failed to start the game: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("FIREBASE", "Failed to start the game: " + e.getMessage());
                    }
                });
    }


    public void shareCode(View view)
    {
        // impilicit intent - אינטנט מרומז
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //this action indicates taht you want to send data
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, let's play! Here's the game code: " + gameId);
        startActivityForResult(Intent.createChooser(shareIntent, "share using"), 1);
    }
}