package com.example.notebooks;

import android.util.Patterns;

public class Utils {
    public static final String KEY_LIST_NOTES = "LIST_NOTES";
    public static final String KEY_LIST_SIGNED_IN = "LIST_SIGNED_IN";
    public static final String KEY_PROFILE = "PROFILE";
    public static final String KEY_LIST_TAGS = "LIST_TAGS";
    public static final String KEY_LIST_TRASH = "LIST_TRASH";

    public static final String KEY_NOTES = "NOTES";
    public static final String KEY_TAGS = "TAGS";
    public static final String KEY_TIMES = "TIMES";
    public static final String KEY_TRASH = "TRASH";

    public static final String KEY_LIST_NAME = "List of notes";
    public static final String KEY_TRASH_NAME = "Trash can";
    public static final String KEY_TAGS_NAME = "Tags";

    public static boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
