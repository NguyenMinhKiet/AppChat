package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpActivity extends AppCompatActivity {
    private boolean passwordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText editText = findViewById(R.id.textPassword);
        setIcon(editText);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility(editText);
                        return true;
                    }
                }
                return false;
            }
        });

        // Find your TextView by its ID
        TextView textViewSignUp = findViewById(R.id.textViewAlreadyMember);

        // Combine the two strings with "Sign in" clickable
        String combinedText = getString(R.string.already_member) + " " + getString(R.string.sign_in);

        SpannableString spannableString = new SpannableString(combinedText);

        // Set ClickableSpan and color for "Sign in"
        int startIndex = combinedText.indexOf(getString(R.string.sign_in));
        int endIndex = startIndex + getString(R.string.sign_in).length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                navigateToSignIn();
            }
        };

        spannableString.setSpan(clickableSpan, startIndex, endIndex, 0);
        spannableString.setSpan(new android.text.style.ForegroundColorSpan(Color.rgb(12,46,223)), startIndex, endIndex, 0);

        // Set the modified SpannableString to the TextView
        textViewSignUp.setText(spannableString);
        textViewSignUp.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    public void navigateToSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
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