package com.example.myproject1.loginsignup_page;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject1.MainActivity;
import com.example.myproject1.R;
import com.google.firebase.auth.FirebaseAuth;


public class LoginTabFragment extends Fragment {

    FirebaseAuth fUser = FirebaseAuth.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_tab, container, false);
    }

    public void register(View view) {
        EditText etLoginEmail = findViewById(R.id.et_login_email);
        EditText etSignupEmail = findViewById(R.id.et_signup_email);
        EditText etLoginPassword = findViewById(R.id.et_login_password);
        EditText etSighUpPassword = findViewById(R.id.et_signup_password);
        EditText etSignUpConfirmPassword = findViewById(R.id.et_signup_confirmPassword);

        String loginEmail = etLoginEmail.getText().toString();
        String password = etPassword.getText().toString();
        String nickName = etName.getText().toString();

        fUser.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // can be done only register success
                            Log.d("FIREBASE", "onComplete: success");

                            // add user to firestore DB
                            // 1 create User Object
                            // 2 add the object to Users collection

                            User user = new User(email, nickName);
                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            firestore.collection("Users").add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("FIREBASE", "database success" + documentReference.toString());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("FIREBASE", "database fail " + e.getMessage());
                                        }
                                    });



                            Intent intent = new Intent(MainActivity.this, GameActivity.class);
                            startActivity(intent);
                        } else //register fail
                        {
                            Toast.makeText(MainActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}