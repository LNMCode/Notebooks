package com.example.notebooks.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebooks.MainActivity;
import com.example.notebooks.R;
import com.example.notebooks.activities.note.NoteDetailActivity;
import com.example.notebooks.activities.note.Status;
import com.example.notebooks.model.Note;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolder> {

    private Context context;
    private List<Note> listnote;

    public AdapterNote(Context context, List<Note> listnote) {
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
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra("status", Status.READ);
                intent.putExtra("note", note);
                context.startActivity(intent);
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
