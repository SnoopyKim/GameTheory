package com.example.android.gametheory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText etID, etName, etPassword;
    Button btnLogin;

    boolean b_id = false, b_name = false, b_pw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
}
