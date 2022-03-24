package com.example.notebooks.activities.note;

public interface NoteActions {
    void addNote(String title, String content);
    void removeNote(String documentId);
    void updateNote(String title, String content);
}
