package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText txtEmail;
    private Button btn_SendEmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initGUI();
        initListener();
    }

    private void initGUI(){
        btn_SendEmail = findViewById(R.id.btn_FP_sendEmailReset);
        txtEmail = findViewById(R.id.text_FP_Email);
    }
    private void initListener() {
        btn_SendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSendEmail();

            }
        });
    }

    private void onClickSendEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = txtEmail.getText().toString().trim();
        String newPassword = "";// cần tìm cách thêm mật khẩu mới để lưu vào db

        if(emailAddress.length() < 5){
            Toast.makeText(ForgotPasswordActivity.this,"Enter email! ",Toast.LENGTH_SHORT).show();
        } else{
            progressDialog.show();
            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this,"Email sent to " + emailAddress,Toast.LENGTH_SHORT).show();
                                updateDataUser(emailAddress,newPassword);
                            } else{
                                Toast.makeText(ForgotPasswordActivity.this,"Email error!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void updateDataUser(String user, String password){
        // push data vào db
        // ...
    }

}