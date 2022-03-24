package com.example.notebooks.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.notebooks.R;
import com.example.notebooks.Utils;
import com.example.notebooks.model.Note;
import com.example.notebooks.model.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class AddTagDialog extends AppCompatDialogFragment {
    private EditText nameTag;
    private Note note;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private DocumentReference docRef;

    private Context context;

    public AddTagDialog(@NonNull Note note, @NonNull Context context) {
        this.note = note;
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_tag, null);
        nameTag = view.findViewById(R.id.ed_name_tag);
        if (note.getTag() != null && !note.getTag().equals("")){
            nameTag.setText(note.getTag());
            nameTag.setSelection(note.getTag().length());
        }
        builder.setView(view)
                .setCancelable(false)
                .setTitle("")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!note.getDocumentId().equals("")){
                            String name = nameTag.getText().toString();
                            saveTag(name);
                        }
                    }
                });
        return builder.create();
    }

    private void saveTag(String tagName){
        String formatDocRef = String.format("%s/%s", fbAuth.getUid(), Utils.KEY_LIST_TAGS);
        docRef = db.document(formatDocRef);
        Tag tag = new Tag(tagName);
        docRef.collection(Utils.KEY_TAGS).add(tag).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Saved tag", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        note.setTag(tagName);
        formatDocRef = String.format("%s/%s", fbAuth.getUid(), Utils.KEY_LIST_NOTES);
        docRef = db.document(formatDocRef);
        docRef.collection(Utils.KEY_NOTES).document(note.getDocumentId()).set(note, SetOptions.merge());

    }

    public void dismiss(){
        this.dismiss();
    }
}
