package com.example.notebooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebooks.adapters.AdapterTagList;
import com.example.notebooks.model.Note;
import com.example.notebooks.model.TagItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NoteTagListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private ArrayList<Note> notes;
    private ArrayList<TagItemAdapter> tags = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_tag);
        init();
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.tag_toolbar_tag);
        setSupportActionBar(myChildToolbar);
        getSupportActionBar().setTitle("List of tags");
        myChildToolbar.setNavigationIcon(R.drawable.left_arrow);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        notes = intent.getParcelableArrayListExtra("notes");
        Map<String, Integer> map = getMapTags(notes);
        for (String key : map.keySet()){
            tags.add(new TagItemAdapter(key, String.valueOf(map.get(key))));
        }
        AdapterTagList adapterTag = new AdapterTagList(this, tags, notes);
        recyclerView.setAdapter(adapterTag);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView_tag);
    }

    private Map<String, Integer> getMapTags(ArrayList<Note> arrayList) {
        Map<String, Integer> map = new HashMap<>();
        for (Note note : arrayList) {
            String tag = note.getTag();
            if (tag != null){
                if (map.containsKey(tag)) {
                    int number = map.get(tag);
                    map.put(tag, ++number);
                } else {
                    map.put(tag, 1);
                }
            }
        }
        return map;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return true;
    }
}