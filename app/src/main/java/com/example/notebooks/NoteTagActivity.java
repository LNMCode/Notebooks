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
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notebooks.adapters.AdapterTag;
import com.example.notebooks.model.Note;

import java.util.ArrayList;

public class NoteTagActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    private RecyclerView recyclerView;
    private int numberCol = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_tag_list);
        // my_child_toolbar is defined in the layout file
        notes = getIntent().getParcelableArrayListExtra("notes");
        numberCol = notes.size() > 1 ? 2 : 1;
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tag_toolbar_tag_list);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setTitle(notes.get(0).getTag());
        myChildToolbar.setNavigationIcon(R.drawable.left_arrow);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView_list_tags);
        AdapterTag adapterTagList = new AdapterTag(this, notes);
        recyclerView.setAdapter(adapterTagList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(numberCol, LinearLayoutManager.VERTICAL));
    }

}