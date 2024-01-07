package com.example.myproject1.loginsignup_page;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject1.GamePage.GameActivity;
import com.example.myproject1.MainActivity;
import com.example.myproject1.R;
import com.example.myproject1.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginTabFragment extends Fragment {

    FirebaseAuth fUser = FirebaseAuth.getInstance();
    View fragView;

    private ILoginRegister iLoginRegister;


    public LoginTabFragment(ILoginRegister iLoginRegister)
    {
        super();
        this.iLoginRegister = iLoginRegister;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView =  inflater.inflate(R.layout.fragment_login_tab, container, false);
        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void register(View view) {
        EditText etLoginEmail = fragView.findViewById(R.id.et_login_email);
        EditText etLoginPassword = fragView.findViewById(R.id.et_login_password);
        EditText etSignupEmail = fragView.findViewById(R.id.et_signup_email);
        EditText etSignUpPassword = fragView.findViewById(R.id.et_signup_password);
        EditText etSignUpConfirmPassword = fragView.findViewById(R.id.et_signup_confirmPassword);

        String loginEmail = etLoginEmail.getText().toString();
        String loginPassword = etLoginEmail.getText().toString();
        String signupEmail = etSignupEmail.getText().toString();
        String signupPassword = etSignUpPassword.getText().toString();
        String signupConfirmPassword = etSignUpConfirmPassword.getText().toString();

        fUser.createUserWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            // can be done only when register success
                            Log.d("FIREBASE", "onComplete: success");

                            // add user to firestore DB: create a User object, add the object to Users collection
                            User user = new User(loginEmail, loginPassword);
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