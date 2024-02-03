package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private boolean passwordVisible = false;
    private TextView textForgotPassword;
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnSignIn;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initGUI();
        initListener();
        EditText editText = findViewById(R.id.textPassword);
        setIcon(editText);


        // Combine the two strings with "Password" clickable
        String combinedText = getString(R.string.forgot_password) + " " + getString(R.string.password) +"?";

        SpannableString spannableString = new SpannableString(combinedText);

        // Set ClickableSpan and color for "Password"
        int startIndex = combinedText.indexOf(getString(R.string.password));
        int endIndex = startIndex + getString(R.string.password).length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Navigate to forgot password action
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, 0);
        spannableString.setSpan(new android.text.style.ForegroundColorSpan(Color.rgb(12,46,223)), startIndex, endIndex, 0);

        // Set the modified SpannableString to the TextView
        textForgotPassword.setText(spannableString);
        textForgotPassword.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());

        TextView textCreateAccount = findViewById(R.id.textCreateAccount);

        // Combine the two strings with "Account" clickable
        String combinedTextCreateAccount = getString(R.string.create_account) + " " + getString(R.string.account) +"?";

        SpannableString spannableStringAccount = new SpannableString(combinedTextCreateAccount);

        // Set ClickableSpan and color for "Account"
        int startIndexAccount = combinedTextCreateAccount.indexOf(getString(R.string.account));
        int endIndexAccount = startIndexAccount + getString(R.string.account).length();

        ClickableSpan clickableSpanAccount = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                navigateToSignUp();
            }
        };

        spannableStringAccount.setSpan(clickableSpanAccount, startIndexAccount, endIndexAccount, 0);
        spannableStringAccount.setSpan(new android.text.style.ForegroundColorSpan(Color.rgb(12,46,223)), startIndexAccount, endIndexAccount, 0);

        // Set the modified SpannableString to the TextView
        textCreateAccount.setText(spannableStringAccount);
        textCreateAccount.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private void initGUI(){
        txtUsername = findViewById(R.id.textUsername);
        txtPassword = findViewById(R.id.textPassword);
        textForgotPassword = findViewById(R.id.textForgotPassword);
        btnSignIn = findViewById(R.id.sign_in_btn);

        progressDialog = new ProgressDialog(this);
    }
    private void initListener(){
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignIn();
            }
        });
    }

    private void onClickSignIn() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        String email = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();


        if(email.length() < 1 || password.length() < 1){
            Toast.makeText(SignInActivity.this,"Enter Email/Password !",Toast.LENGTH_LONG).show();
        } else{
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this,"signInWithEmail:success",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Toast.makeText(SignInActivity.this,"signInWithEmail:failure\n" + task.getException(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    public void navigateToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void setIcon(EditText editText) {
        Drawable icon = getResources().getDrawable(passwordVisible ? R.drawable.visible : R.drawable.hidden);
        int width = 60;
        int height = 60;
        icon.setBounds(0, 0, width, height);
        editText.setCompoundDrawablesRelative(null, null, icon, null);
    }

    private void togglePasswordVisibility(EditText editText) {
        int selection = editText.getSelectionEnd(); // Keep track of cursor position

        if (!passwordVisible) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        passwordVisible = !passwordVisible;
        setIcon(editText);

        // Restore cursor position
        editText.setSelection(selection);
    }


}