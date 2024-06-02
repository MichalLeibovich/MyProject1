package com.example.myproject1.loginsignup_screen;

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
import android.widget.Toast;

import com.example.myproject1.MainScreenActivity;
import com.example.myproject1.MainScreenActivity2;
import com.example.myproject1.R;
import com.example.myproject1.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class SignUpTabFragment extends Fragment {

    View fragView;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonSignup = fragView.findViewById(R.id.signup_button);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signUp();
            }
        });
    }


    private String signupEmail;
    private String signupPassword;

    public void signUp()
    {
        EditText etSignupEmail = fragView.findViewById(R.id.et_signup_email);
        EditText etSignUpPassword = fragView.findViewById(R.id.et_signup_password);
        EditText etSignUpConfirmPassword = fragView.findViewById(R.id.et_signup_confirmPassword);

        signupEmail = etSignupEmail.getText().toString();
        signupPassword = etSignUpPassword.getText().toString();
        String signupConfirmPassword = etSignUpConfirmPassword.getText().toString();

        if (signupEmail.isEmpty() || signupPassword.isEmpty() || signupConfirmPassword.isEmpty()){
            Toast.makeText(getActivity(), "Fields cannon remain empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!signupPassword.equals(signupConfirmPassword)) {
                Toast.makeText(getActivity(), "Password and confirmation password must be the same", Toast.LENGTH_SHORT).show();
            }
                else
                {
                    fAuth.createUserWithEmailAndPassword(signupEmail, signupPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                showDialogBox(signupEmail, signupPassword);
//                                User user = new User(signupEmail, signupPassword);
//                                addUserToFirestore(user);
//                                Intent intent = new Intent(getActivity(), MainScreenActivity.class);
//                                startActivity(intent);
                            }
                            else
                            {
                                String msg = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Sign up failed: " + msg, Toast.LENGTH_SHORT).show();
                                Log.d("FIREBASE", task.getException().getMessage());
                            }
                    }
                });
            }
        }
    }

    private void addUserToFirestore(User user)
    {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        fb.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(), "Signed up successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), MainScreenActivity.class);
//                startActivity(intent);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FIREBASE", e.getMessage());
            }
        });
        String uid = fAuth.getCurrentUser().getUid();
        DocumentReference ref = fb.collection("Users").document(uid);
        ref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("FIREBASE", "set successes");
            }
        });

    }

    public void showDialogBox(String signupEmail, String signupPassword)
    {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_box_username);

        Button b = dialog.findViewById(R.id.buttonRegister);
        EditText et = dialog.findViewById(R.id.et_username);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String username = et.getText().toString();
                checkUsername(username);
            }
        });

//        User user = new User(signupEmail, signupPassword);
//        addUserToFirestore(user);
//        Intent intent = new Intent(getActivity(), MainScreenActivity.class);
//        startActivity(intent);

        dialog.show();
    }

    public void checkUsername(String username)
    {
    // check if there's another one in firebase

        FirebaseFirestore firebase = FirebaseFirestore.getInstance();
        firebase.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            if(task.getResult().size() > 0)
                            {
                                // getContext() = getActivity() | in this case they have no difference
                                Toast.makeText(getActivity(), "This username is already in use. Try another one", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            User user = new User(signupEmail, signupPassword, username);
                            addUserToFirestore(user);
                            //Intent intent = new Intent(getActivity(), MainScreenActivity.class);
                            //TO-
                            Intent intent = new Intent(getActivity(), MainScreenActivity2.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Log.d("SignUpTabFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}