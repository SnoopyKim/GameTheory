package com.example.android.gametheory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText etID, etName, etPassword;
    Button btnLogin;

    FirebaseAuth mAuth;
    DatabaseReference userRef;

    boolean b_id = false, b_name = false, b_pw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            CustomUtils.displayToast(this, "자동 로그인: " + mAuth.getCurrentUser().getDisplayName());

            if (mAuth.getCurrentUser().getDisplayName().equals("manager")){
                Intent intent = new Intent(this, ManagerActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        userRef = FirebaseDatabase.getInstance().getReference("users");

        etID = findViewById(R.id.etID);
        etID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) { b_id = false; }
                else { b_id = true; }
                enableLoginButton();
            }
        });

        etName = findViewById(R.id.etName);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) { b_name = false; }
                else { b_name = true; }
                enableLoginButton();
            }
        });

        etPassword = findViewById(R.id.etPW);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) { b_pw = false; }
                else { b_pw = true; }
                enableLoginButton();
            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setEnabled(false);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(etID.getText().toString(), etPassword.getText().toString());
            }
        });

        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(etID.getText().toString(), etPassword.getText().toString(), etName.getText().toString());
            }
        });

        Button btnManager = findViewById(R.id.btnManager);
        btnManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createManager(etID.getText().toString(), etPassword.getText().toString());
            }
        });
    }

    public void enableLoginButton() {
        if (b_id && b_pw && b_name) {
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundColor(getColor(R.color.black));
        } else if (btnLogin.isEnabled()) {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(getColor(R.color.lightgray));
        }
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            CustomUtils.displayToast(getApplicationContext(), "로그인 성공");

                            FirebaseUser user = task.getResult().getUser();
                            if (user.getDisplayName().equals("manager")) {
                                Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            CustomUtils.displayToast(getApplicationContext(), "로그인 실패");
                        }
                    }
                });
    }

    public void createUser(String email, String password, final String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update display name & store in database
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            userRef.child(user.getUid()).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        CustomUtils.displayToast(getApplicationContext(), "정보 저장 성공");
                                    } else {
                                        CustomUtils.displayToast(getApplicationContext(), "정보 저장 실패");
                                        Log.d("setValue", "onFailed: " + task.getResult());
                                    }
                                }
                            });

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                CustomUtils.displayToast(getApplicationContext(), "회원가입 성공");
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            CustomUtils.displayToast(getApplicationContext(), "회원가입 실패");
                        }
                    }
                });

    }

    public void createManager(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update display name & store in database
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("manager")
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                CustomUtils.displayToast(getApplicationContext(), "관리자 추가 성공");
                                                Intent intent = new Intent(getApplicationContext(), ManagerActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            CustomUtils.displayToast(getApplicationContext(), "회원가입 실패");
                        }
                    }
                });

    }
}
