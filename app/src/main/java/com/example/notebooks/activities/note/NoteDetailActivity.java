package com.example.notebooks.activities.note;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notebooks.R;
import com.example.notebooks.Utils;
import com.example.notebooks.dialog.AddTagDialog;
import com.example.notebooks.dialog.TrashNoteDialog;
import com.example.notebooks.model.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NoteDetailActivity extends AppCompatActivity implements NoteActions {
    private EditText noteTitle;
    private EditText noteContent;

    private ActionBar ab;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private DocumentReference docRef;

    private Date currentTime = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

    private Status status = Status.ADD;
    private Note note;

    private Menu menu;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar myChildToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        myChildToolbar.setNavigationIcon(R.drawable.left_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        initView();

        Intent intent = getIntent();
        status = (Status) intent.getSerializableExtra("status");
        note = intent.getParcelableExtra("note");
        if (status != null && note != null) {
            readStatus();
            showDetailNote(note);
        } else editStatus();

        String userId = fbAuth.getUid();
        assert userId != null;
        String formatDocRef = String.format("%s/%s", userId, Utils.KEY_LIST_NOTES);
        docRef = db.document(formatDocRef);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String title = noteTitle.getText().toString();
                String content = noteContent.getText().toString();
                if (!content.equals("")) {
                    switch (status) {
                        case ADD:
                            Log.d("id", "" + item.getTitle());
                            readStatus();
                            addNote(title, content);
                            break;
                        case UPDATE:
                            updateNote(title, content);
                            break;
                        case READ:
                            backToParentActivity();
                    }
                } else backToParentActivity();
                return true;
            case R.id.ic_read:
                readStatus();
                return true;
            case R.id.ic_edit:
                editStatus();
                return true;
            case R.id.item_menu_edit_tag:
                if (note != null) {
                    openDialogAddTag(note.getDocumentId());
                }
                return true;
            case R.id.item_menu_remove:
                removeNote(note.getDocumentId());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void addNote(String title, String content) {
        note = new Note(null, title, content, formatter.format(currentTime), formatter.format(currentTime), null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", Utils.KEY_LIST_NAME);
        docRef.set(map).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("NoteDetail", e.getMessage());
            }
        });
        docRef.collection(Utils.KEY_NOTES).add(note);
    }

    @Override
    public void removeNote(String documentId) {
        openDialogTrash();
//        docRef.collection(Utils.KEY_NOTES).document(documentId).delete();

    }

    @Override
    public void updateNote(String title, String content) {
        String documentId = note.getDocumentId();
        if (!documentId.equals("")) {
            note.setTitle(title);
            note.setContent(content);
            note.setLastTimeUpdated(formatter.format(currentTime));
            docRef.collection(Utils.KEY_NOTES).document(documentId).set(note, SetOptions.merge());
            readStatus();
        }
    }

    private void showDetailNote(Note note) {
        noteTitle.setText(note.getTitle());
        noteContent.setText(note.getContent());
    }

    private void openDialogAddTag(String documentID) {
        AddTagDialog addTagDialog = new AddTagDialog(note, getApplicationContext());
        addTagDialog.setCancelable(false);
        addTagDialog.show(getSupportFragmentManager(), "Add tag dialog");
    }

    private void openDialogTrash() {
        Intent intent = getParentActivityIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TrashNoteDialog removeNoteDialog = new TrashNoteDialog(note, intent, getApplicationContext());
        removeNoteDialog.setCancelable(false);
        removeNoteDialog.show(getSupportFragmentManager(), "Trash dialog");
    }

    private void initView() {
        noteTitle = findViewById(R.id.note_title);
        noteContent = findViewById(R.id.edtNote);
    }

    private void backToParentActivity() {
        Intent intent = getParentActivityIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void readStatus() {
        status = Status.READ;
        noteTitle.setFocusable(false);
        noteTitle.setFocusableInTouchMode(false);
        noteContent.setFocusable(false);
        noteContent.setFocusableInTouchMode(false);
        hideSoftKeyboard(noteTitle);
        hideSoftKeyboard(noteContent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void editStatus() {
        status = noteTitle.getText().toString().equals("") ? Status.ADD : Status.UPDATE;
        noteTitle.setFocusable(true);
        noteTitle.setFocusableInTouchMode(true);
        noteContent.setFocusable(true);
        noteContent.setFocusableInTouchMode(true);
        noteContent.requestFocus();
    }

    private void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && input != null) imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
}