package com.example.notebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.notebooks.adapters.AdapterTrash;
import com.example.notebooks.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrashActivity extends AppCompatActivity {

    private ArrayList<Note> notes = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvEmptyNote;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = null;
    private DocumentReference docRef;
    private AdapterTrash adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        userId = fbAuth.getUid();
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tag_toolbar_trash);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setTitle("Trash");
        myChildToolbar.setNavigationIcon(R.drawable.left_arrow);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        init();
        selectData();
    }

    public void selectData() {
        String formatDocRef = String.format("%s/%s", userId, Utils.KEY_LIST_TRASH);
        docRef = db.document(formatDocRef);
        notes.clear();
        docRef.collection(Utils.KEY_TRASH).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());
                        notes.add(note);
                    }
                    if (notes.size() != 0){
                        recyclerView.setVisibility(View.VISIBLE);
                        tvEmptyNote.setVisibility(View.GONE);
                        adapter = new AdapterTrash(TrashActivity.this, notes);
                        recyclerView.setAdapter(adapter);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        tvEmptyNote.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("error", task.getException().getMessage());
                }
            }
        });
    }

    private void init() {
        tvEmptyNote = findViewById(R.id.tv_empty_note);
        recyclerView = findViewById(R.id.recyclerView_trash);
        adapter = new AdapterTrash(this, notes);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        int numberCol = 2;
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(numberCol, LinearLayoutManager.VERTICAL));
    }
}