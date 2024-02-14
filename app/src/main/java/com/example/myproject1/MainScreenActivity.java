package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myproject1.GamePage.GameActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }


    public void PlayClicked(View view)
    {
        //Intent intent = new Intent(MainScreenActivity.this, WaitingRoomActivity.class);
        //startActivity(intent);
        showDialogBox();
    }

    public void showDialogBox()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);

        Button b1 = dialog.findViewById(R.id.button27);
        Button b2 = dialog.findViewById(R.id.button28);

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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        createNewGame(user);
        Intent intent = new Intent(MainScreenActivity.this, WaitingRoomActivity.class);
        startActivity(intent);
    }

    private void createNewGame(FirebaseUser user)
    {
        Game game = new Game(user);
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Games").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Signed up successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainScreenActivity.class);
                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FIREBASE", e.getMessage());
            }
        });
        FirebaseAuth fAuth = FirebaseAuth.getInstance(); // TODO: do I need this?
        String uid = fAuth.getCurrentUser().getUid();
        DocumentReference ref = fb.collection("Users").document(uid);
        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getActivity(), "set successes", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void joinAnExistingRoom()
    {
        Dialog dialogJoin = new Dialog(this);
        dialogJoin.setContentView(R.layout.custom_dialog_box_join);
        dialogJoin.show();
    }

}