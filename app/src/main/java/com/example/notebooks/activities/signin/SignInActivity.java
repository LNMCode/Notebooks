package com.example.notebooks.activities.signin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notebooks.MainActivity;
import com.example.notebooks.R;
import com.example.notebooks.Utils;
import com.example.notebooks.activities.resetpass.Resetpass;
import com.example.notebooks.activities.signup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {
    private TextView signUp;
    private Button btnSignIn;
    private TextInputEditText edEmail;
    private TextInputEditText edPass;
    private TextView forgetPass;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef;

    private Date currentTime = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd MMM, yyyy");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_signin);
        initView();

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String pass = edPass.getText().toString();
                if (email.equals("") || pass.equals(""))
                    Toast.makeText(getApplicationContext(),
                            "Can not be empty",
                            Toast.LENGTH_LONG).show();
                else if (!Utils.checkEmail(email))
                    Toast.makeText(getApplicationContext(),
                            "Invalid email",
                            Toast.LENGTH_LONG).show();
                else {
                    Task<AuthResult> task = signIn(email, pass);
                    task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Add last signed in
                                FirebaseUser user = fbAuth.getCurrentUser();
                                String userId = fbAuth.getUid();
                                assert userId != null;
                                docRef = db.document(String.format("%s/%s", userId, Utils.KEY_LIST_SIGNED_IN));
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Last Day", formatter.format(currentTime));
                                docRef.collection(Utils.KEY_TIMES).add(map);

                                docRef = db.document(String.format("%s/%s", userId, Utils.KEY_PROFILE));
                                map.clear();
                                assert user != null;
                                map.put("Email", user.getEmail());
                                map.put("Display name", user.getDisplayName());
                                map.put("Phone number", user.getPhoneNumber());
                                map.put("Photo url", user.getPhotoUrl());
                                docRef.set(map).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Note add profile", e.getMessage());
                                    }
                                });

                                Toast.makeText(SignInActivity.this,
                                        getResources().getString(R.string.head_line_welcome),
                                        Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else Toast.makeText(getApplicationContext(),
                                    "Login failed, please try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Resetpass.class);
                startActivity(intent);
            }
        });
    }

    private Task<AuthResult> signIn(String email, String pass) {
        return fbAuth.signInWithEmailAndPassword(email, pass);
    }

    private void initView() {
        signUp = findViewById(R.id.sign_up);
        btnSignIn = findViewById(R.id.btnSignUp);
        edEmail = findViewById(R.id.edtEmailAddress);
        edPass = findViewById(R.id.password);
        forgetPass = findViewById(R.id.textViewForgotPassword);
    }

    public void setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.LEFT);
            slide.setDuration(400);
            slide.setInterpolator(new AccelerateDecelerateInterpolator());
            getWindow().setExitTransition(slide);
            getWindow().setEnterTransition(slide);
        }
    }
}