package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        displayUsername();
    }


    public void displayUsername()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userId);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        TextView usernameTextView = findViewById(R.id.usernameTextView);
                        usernameTextView.setText(username);
                    } else {
                        Log.d("MainScreenActivity", "User document does not exist");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("MainScreenActivity", "Error fetching user document: " + e.getMessage());
                }
            });
        } else {
            Log.e("MainScreenActivity", "No user logged in");
        }

        //TextView usernameTextView = findViewById(R.id.usernameTextView);
        //String username = getIntent().getStringExtra("username");
        // get the username by the username's firebase
        //usernameTextView.setText(username);
    }


    public void showDialogBox(View view)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);

        Button b1 = dialog.findViewById(R.id.buttonCreateGameRoom);
        Button b2 = dialog.findViewById(R.id.buttonJoinGameRoom);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewGameRoom();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinAnExistingRoom();
            }
        });

        dialog.show();
    }

    public void MyGalleryClicked(View view)
    {
        Intent intent = new Intent(MainScreenActivity.this, MyGalleryActivity.class);
        startActivity(intent);
    }


    public void createNewGameRoom()
    {
        String username = "hello";
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        createNewGame(userId, username);
    }

    private void createNewGame(String userId, String username)
    {
        GameRoom gameRoom = new GameRoom(userId, username);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Games").add(gameRoom).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                String gameId = documentReference.getId();
                Intent intent = new Intent(MainScreenActivity.this, WaitingRoomActivity.class);
                intent.putExtra("gameId", gameId); // Pass the game code as an extra
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainScreenActivity.this, "creating a new game room failed: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("FIREBASE", e.getMessage());
            }
        });

//        FirebaseAuth fAuth = FirebaseAuth.getInstance();
//        String uid = fAuth.getCurrentUser().getUid();
//        DocumentReference ref = fbfs.collection("Users").document(uid);
//        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast.makeText(MainScreenActivity.this, "set successes", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void joinAnExistingRoom()
    {
        Dialog dialogJoin = new Dialog(this);
        dialogJoin.setContentView(R.layout.custom_dialog_box_join);
        dialogJoin.show();
    }

    public void accountsButtonClicked(View view)
    {
        Intent intent = new Intent(MainScreenActivity.this, MainActivity.class);
        startActivity(intent);
    }
}