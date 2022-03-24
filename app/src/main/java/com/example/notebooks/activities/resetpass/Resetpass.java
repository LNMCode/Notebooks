package com.example.notebooks.activities.resetpass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.example.notebooks.R;
import com.example.notebooks.Utils;
import com.example.notebooks.activities.signin.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Resetpass extends AppCompatActivity {
    private ActionBar ab;
    private Button btnNext;
    private TextInputEditText edtEmailAddress;
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpass); // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbarReset);
        setSupportActionBar(myChildToolbar);
        myChildToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        initView();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmailAddress.getText().toString();
                if(!Utils.checkEmail(email))
                    Toast.makeText(getApplicationContext(),
                            "Invalid email",
                            Toast.LENGTH_LONG).show();
                else {
                    fbAuth.setLanguageCode("vi");
                    fbAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Please check your email", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else{
                                edtEmailAddress.requestFocus();
                                Toast.makeText(getApplicationContext(),
                                        "Email does not exist or has been deleted",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void initView(){
        edtEmailAddress = findViewById(R.id.edtEmailAddress);
        btnNext = findViewById(R.id.btnNext);
    }
}