package com.example.notebooks;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.notebooks.adapters.AdapterSearch;
import com.example.notebooks.adapters.AdapterTag;
import com.example.notebooks.model.Note;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText edSearch;
    private ArrayList<Note> notes;

    private AdapterSearch adapterSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tag_toolbar_search);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setTitle("");
        myChildToolbar.setNavigationIcon(R.drawable.left_arrow);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        notes = intent.getParcelableArrayListExtra("notes");
        Log.d("notes", notes.size() + "");
        init();
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<Note> notesSearch = new ArrayList<>();
                for (Note note : notes){
                    if (note.getTitle().contains(charSequence)){
                        notesSearch.add(note);
                    }
                }
                adapterSearch.search(notesSearch);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init(){
        edSearch = findViewById(R.id.ed_search);
        recyclerView = findViewById(R.id.recyclerView_search);
        adapterSearch = new AdapterSearch(this, notes);
        recyclerView.setAdapter(adapterSearch);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }
}