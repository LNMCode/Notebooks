package com.example.notebooks.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notebooks.NoteTagActivity;
import com.example.notebooks.R;
import com.example.notebooks.model.Note;
import com.example.notebooks.model.TagItemAdapter;

import java.util.ArrayList;

public class AdapterTagList extends RecyclerView.Adapter<AdapterTagList.ViewHolder> {
    private Context context;
    private ArrayList<TagItemAdapter> tags;
    private ArrayList<Note> notes;

    public AdapterTagList(Context context, ArrayList<TagItemAdapter> tags, ArrayList<Note> notes) {
        this.context = context;
        this.tags = tags;
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TagItemAdapter tag = tags.get(position);
        holder.textViewTag.setText(tag.getTagName());
        holder.textViewNumberTag.setText(tag.getNumberTag());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Note> notesContain = new ArrayList<>();
                for (Note note : notes){
                    if (note.getTag() != null && note.getTag().equals(tag.getTagName())){
                        notesContain.add(note);
                    }
                }
                Intent intent = new Intent(context, NoteTagActivity.class);
                intent.putParcelableArrayListExtra("notes", notesContain);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTag;
        TextView textViewNumberTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTag = itemView.findViewById(R.id.tv_tag);
            textViewNumberTag = itemView.findViewById(R.id.tv_number_tag);
        }
    }
}
