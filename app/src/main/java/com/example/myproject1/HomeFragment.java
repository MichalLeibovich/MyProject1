package com.example.myproject1;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public static String username;
    private View fragView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragView = view;
        displayUsername();


        Button buttonPlay = fragView.findViewById(R.id.play_button);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playClicked();
            }
        });




        Button buttonChangeAccount = fragView.findViewById(R.id.accounts_button);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accountsButtonClicked();
            }
        });


        ImageView ivQuestionMark = fragView.findViewById(R.id.imageView_questionMark);
        ivQuestionMark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { questionMarkClicked();}
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void displayUsername() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userId);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        username = documentSnapshot.getString("username");
                        TextView usernameTextView = fragView.findViewById(R.id.usernameTextView);
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
    }

    // Other methods here...







    public void playClicked()
    {
        Dialog dialog = new Dialog(getContext());
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
                joinExistingRoom();
            }
        });
        dialog.show();
    }




    public void createNewGameRoom()
    {
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
                Intent intent = new Intent(getActivity(), WaitingRoomActivity.class);
                intent.putExtra("gameId", gameId); // Pass the game code as an extra
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "creating a new game room failed: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void joinExistingRoom()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_box_join);

        Button b1 = dialog.findViewById(R.id.button_join);
        EditText et = dialog.findViewById(R.id.gamecode_editText);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String gameId = et.getText().toString();
                if (gameId.isEmpty())
                {
                    Toast.makeText(getContext(), "No code entered", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    DocumentReference documentRef = firestore.collection("Games").document(gameId);
                    documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Boolean started = documentSnapshot.getBoolean("started");
                                if (started != null) {
                                    if (started == true) {
                                        // game has started
                                        Toast.makeText(getContext(), "You cannot join the game- game has already started", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // game has not started
                                        // how to join the same game room of the gameId and not open a new one?
                                        Intent intent = new Intent(getActivity(), WaitingRoomActivity.class);
                                        intent.putExtra("gameId", gameId); // Pass the game code as an extra
                                        startActivity(intent);

                                    }
                                } else {
                                    // "started" field is missing or null
                                    // Handle this case
                                }
                            } else {
                                // Document does not exist
                                Toast.makeText(getContext(), "Game room isn't found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error fetching document
                            Log.e("TAG", "Error fetching document: " + e.getMessage());
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dialog.show();
    }

    public void accountsButtonClicked()
    {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public void questionMarkClicked()
    {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_box_instructions);

        dialog.show();

    }



}