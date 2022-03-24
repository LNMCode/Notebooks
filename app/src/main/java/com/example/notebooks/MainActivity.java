package com.example.notebooks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notebooks.adapters.AdapterNote;
import com.example.notebooks.activities.note.NoteDetailActivity;
import com.example.notebooks.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private Toolbar myChildToolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout rootView;
    private NavigationView navigationView;
    private ImageView img_setting;
    private View view;

    private RecyclerView recyclerView;
    private ImageView btnSearch;
    private static ArrayList<Note> arrayList = new ArrayList<>();
    private AdapterNote adapter;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId = null;

    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = fbAuth.getUid();
        init();
        navigation();
        setSupportActionBar(myChildToolbar);
        detail();
        selectData();

        view = navigationView.getHeaderView(0);
        img_setting = view.findViewById(R.id.img_setting);
        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putParcelableArrayListExtra("notes", arrayList);
                startActivity(intent);
            }
        });
    }

    private void navigation() {
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, myChildToolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        myChildToolbar.setNavigationIcon(R.drawable.navigation);
    }

    private void init() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        rootView = (DrawerLayout) findViewById(R.id.rootView);
        drawerLayout = findViewById(R.id.rootView);
        myChildToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        navigationView = findViewById(R.id.navigationView);
        btnSearch = findViewById(R.id.btn_search);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

    }

    private void detail() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void selectData() {
        String formatDocRef = String.format("%s/%s", userId, Utils.KEY_LIST_NOTES);
        docRef = db.document(formatDocRef);
        arrayList.clear();
        docRef.collection(Utils.KEY_NOTES).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());
                        arrayList.add(note);
                    }
                    adapter = new AdapterNote(MainActivity.this, arrayList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("error", task.getException().getMessage());
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.all_note:
                break;
            case R.id.tag:
                Intent intent = new Intent(this, NoteTagListActivity.class);
                intent.putParcelableArrayListExtra("notes", arrayList);
                startActivity(intent);
                break;
            case R.id.trash:
                startActivity(new Intent(this, TrashActivity.class));
                break;
            case R.id.profile:
                Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}
