package com.example.myproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myproject1.GamePage.GameActivity;
import com.example.myproject1.loginsignup_page.ILoginRegister;
import com.example.myproject1.loginsignup_page.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements ILoginRegister {

    FirebaseAuth fUser = FirebaseAuth.getInstance();
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tabLayout = findViewById(R.id.tab_layout);
        this.viewPager2 = findViewById(R.id.view_pager);

        this.tabLayout.addTab(this.tabLayout.newTab().setText("log In"));
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Sign Up"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        this.viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle(),this);
        this.viewPager2.setAdapter(this.viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                // TODO: find out why I can't write this. in listeners I suppose?
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position)
            {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }


    public void register(View view) {
        EditText etLoginEmail = findViewById(R.id.et_login_email);
        EditText etLoginPassword = findViewById(R.id.et_login_password);
        EditText etSignupEmail = findViewById(R.id.et_signup_email);
        EditText etSignUpPassword = findViewById(R.id.et_signup_password);
        EditText etSignUpConfirmPassword = findViewById(R.id.et_signup_confirmPassword);

        String loginEmail = etLoginEmail.getText().toString();
        String loginPassword = etLoginEmail.getText().toString();
        String signupEmail = etSignupEmail.getText().toString();
        String signupPassword = etSignUpPassword.getText().toString();
        String signupConfirmPassword = etSignUpConfirmPassword.getText().toString();

        if (signupPassword == signupConfirmPassword) {
            fUser.createUserWithEmailAndPassword(signupEmail, signupPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // can be done only when register success
                                //Log.d("FIREBASE", "onComplete: success");

                                // add user to firestore DB: create a User object, add the object to Users collection
                                User user = new User(loginEmail, loginPassword);
                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                firestore.collection("Users").add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                //Log.d("FIREBASE", "database success" + documentReference.toString());
                                                Toast.makeText(MainActivity.this, "Registration is complete", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Log.d("FIREBASE", "database fail " + e.getMessage());
                                                Toast.makeText(MainActivity.this, "Registration has failed", Toast.LENGTH_SHORT).show();
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
        else
            Toast.makeText(MainActivity.this, "Your password and confirmation password are not the same", Toast.LENGTH_SHORT);
    }

    @Override
    public void login(String email, String password) {

    }

    @Override
    public void register(String email, String password) {

    }
}