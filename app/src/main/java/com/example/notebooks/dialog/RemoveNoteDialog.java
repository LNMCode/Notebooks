package com.example.notebooks.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RemoveNoteDialog extends AppCompatDialogFragment {
    private EditText nameTag;
    private Note note;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private DocumentReference docRef;

    private Context context;
    private Intent intentParent;

    public RemoveNoteDialog(@NonNull Note note, @NonNull Intent intentParent, @NonNull Context context) {
        this.note = note;
        this.context = context;
        this.intentParent = intentParent;
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
        View view = inflater.inflate(R.layout.layout_remove_tag, null);
        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Undo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        undo();
                    }
                })
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeNote();
                    }
                });
        return builder.create();
    }

    private void removeNote() {
        String formatDocRef = String.format("%s/%s", fbAuth.getUid(), Utils.KEY_LIST_TRASH);
        docRef = db.document(formatDocRef);
        docRef.collection(Utils.KEY_TRASH).document(note.getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Deleted!", Toast.LENGTH_LONG).show();
                    context.startActivity(intentParent);
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void undo() {
        String formatDocRef = String.format("%s/%s", fbAuth.getUid(), Utils.KEY_LIST_NOTES);
        docRef = db.document(formatDocRef);
        docRef.collection(Utils.KEY_NOTES).add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Has been undone!", Toast.LENGTH_LONG).show();
                    context.startActivity(intentParent);
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        formatDocRef = String.format("%s/%s", fbAuth.getUid(), Utils.KEY_LIST_TRASH);
        docRef = db.document(formatDocRef);
        docRef.collection(Utils.KEY_TRASH).document(note.getDocumentId()).delete();
    }


    public void dismiss() {
        this.dismiss();
    }
}
