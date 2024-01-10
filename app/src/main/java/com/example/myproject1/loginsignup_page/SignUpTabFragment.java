package com.example.myproject1.loginsignup_page;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject1.MainScreenActivity;
import com.example.myproject1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpTabFragment extends Fragment {

    View fragView;
    FirebaseAuth fUser = FirebaseAuth.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up_tab, container, false);
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

    public void signUp()
    {
        EditText etSignupEmail = fragView.findViewById(R.id.et_signup_email);
        EditText etSignUpPassword = fragView.findViewById(R.id.et_signup_password);
        EditText etSignUpConfirmPassword = fragView.findViewById(R.id.et_signup_confirmPassword);

        String signupEmail = etSignupEmail.getText().toString();
        String signupPassword = etSignUpPassword.getText().toString();
        String signupConfirmPassword = etSignUpConfirmPassword.getText().toString();

        if (signupEmail.isEmpty() && signupPassword.isEmpty() && signupConfirmPassword.isEmpty()){
            Toast.makeText(getActivity(), "Fields cannon remain empty", Toast.LENGTH_SHORT);
        }
        else
        {
            if (signupPassword != signupConfirmPassword) {
                Toast.makeText(getActivity(), "Password and confirmation password have to be the same", Toast.LENGTH_SHORT);
            }
                else
                {
                    fUser.createUserWithEmailAndPassword(signupEmail, signupPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Signed up successfully", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(getActivity(), MainScreenActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Sign up failed" + task.getException().getMessage(), Toast.LENGTH_SHORT);
                            }
                    }
                });
            }
        }
    }
}