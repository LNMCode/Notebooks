package com.example.notebooks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebooks.R;
import com.example.notebooks.activities.note.NoteDetailActivity;
import com.example.notebooks.activities.note.Status;
import com.example.notebooks.dialog.RemoveNoteDialog;
import com.example.notebooks.dialog.TrashNoteDialog;
import com.example.notebooks.model.Note;

import java.util.List;

public class AdapterTrash extends RecyclerView.Adapter<AdapterTrash.ViewHolder> {

    private Context context;
    private List<Note> listnote;

    public AdapterTrash(Context context, List<Note> listnote) {
        this.context = context;
        this.listnote = listnote;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View objview = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return new ViewHolder(objview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = listnote.get(position);
        Context context = holder.itemView.getContext();
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.createTime.setText(note.getTimeCreate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ((Activity)context).getParentActivityIntent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                RemoveNoteDialog removeNoteDialog = new RemoveNoteDialog(note, intent, context.getApplicationContext());
                removeNoteDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Remove dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listnote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, createTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            content = (TextView) itemView.findViewById(R.id.txt_content);
            createTime = (TextView) itemView.findViewById(R.id.text_create_time);
        }
    }
}
