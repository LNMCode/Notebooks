package com.example.notebooks.model;

public class TagItemAdapter {
    private String tagName;
    private String numberTag;

    public TagItemAdapter(String tagName, String numberTag) {
        this.tagName = tagName;
        this.numberTag = numberTag;
    }

    public String getTagName() {
        return tagName;
    }

    public String getNumberTag() {
        return numberTag;
    }
}
