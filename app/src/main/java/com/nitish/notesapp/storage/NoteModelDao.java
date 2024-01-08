package com.nitish.notesapp.storage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteModelDao {

    @Query("SELECT * FROM notesAppTable")
    List<NotesModel> getAllNotes();

    @Insert
    public void addNote(NotesModel notesModel);

    @Update
    public void updateNote(NotesModel notesModel);

    @Query("Delete from notesAppTable where id = :id")
    public void deleteNote(int id);

    @Query("Select * From notesAppTable where id = :id ")
    public NotesModel readOneNote(int id);

}
