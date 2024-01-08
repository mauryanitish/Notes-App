package com.nitish.notesapp.storage;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Random;

@Entity (tableName = "notesAppTable")
public class NotesModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "heading")
    public String heading;

    @ColumnInfo(name = "description")
    public String description;

    public NotesModel(int id,String heading,String description){
        this.id = id;
        this.heading = heading;
        this.description = description;
    }
    @Ignore
    public NotesModel(String heading, String description){
        this.heading = heading;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
