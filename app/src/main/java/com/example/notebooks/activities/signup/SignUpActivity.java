package com.example.notebooks.activities.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebooks.R;
import com.example.notebooks.Utils;
import com.example.notebooks.activities.signin.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private TextView register;
    private TextInputEditText edEmail;
    private TextInputEditText edPass;
    private TextInputEditText edConfirmPass;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPass.getText().toString();
                String confirmPass = edConfirmPass.getText().toString();
                if(checkAccount(email, password, confirmPass)){
                    Task<AuthResult> task = createUser(email, password);
                    task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("SignUp", String.valueOf(task.getException()));
                                Toast.makeText(getApplicationContext(), task.getException()+"", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean checkAccount(String email, String password, String confirmPassword){
        if (email.equals("") || password.equals("") || confirmPassword.equals("")){
            Toast.makeText(getApplicationContext(), "Can not be empty", Toast.LENGTH_LONG).show();
            edEmail.requestFocus();
            return false;
        } else if(!Utils.checkEmail(email)){
            Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_LONG).show();
            edEmail.requestFocus();
            return false;
        } else if (!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(), "Please enter same password", Toast.LENGTH_LONG).show();
            edConfirmPass.requestFocus();
            return false;
        } else if (password.length() < 6){
            Toast.makeText(getApplicationContext(), "Minimum length of password should be 6", Toast.LENGTH_LONG).show();
            edPass.requestFocus();
            return false;
        }
        return true;
    }

    private Task<AuthResult> createUser(String email, String password){
        return fbAuth.createUserWithEmailAndPassword(email, password);
    }

    private void initView(){
        register = findViewById(R.id.sign_in);
        edEmail = findViewById(R.id.edtEmailAddress);
        edPass = findViewById(R.id.password);
        edConfirmPass = findViewById(R.id.confirm_password);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

}