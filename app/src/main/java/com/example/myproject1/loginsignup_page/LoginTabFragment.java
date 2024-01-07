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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginTabFragment extends Fragment {

    FirebaseAuth fUser = FirebaseAuth.getInstance();
    View fragView;

    private ILoginRegister iLoginRegister;


//    public LoginTabFragment(ILoginRegister iLoginRegister)
//    {
//        super();
//        this.iLoginRegister = iLoginRegister;
//    }

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

    public void login(View view)
    {
        EditText etLoginEmail = fragView.findViewById(R.id.et_login_email);
        EditText etLoginPassword = fragView.findViewById(R.id.et_login_password);

        String loginEmail = etLoginEmail.getText().toString();
        String loginPassword = etLoginEmail.getText().toString();

        if (loginEmail.isEmpty() && loginPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Fields cannot remain empty", Toast.LENGTH_SHORT);
        }
        else
        {
            fUser.signInWithEmailAndPassword(loginEmail, loginPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getActivity(), "Logged in successfully", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(getActivity(), GameActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Log in failed", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }



}