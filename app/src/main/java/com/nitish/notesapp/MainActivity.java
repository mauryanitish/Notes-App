package com.nitish.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.nitish.notesapp.storage.DatabaseHelper;
import com.nitish.notesapp.storage.NoteModelDao;
import com.nitish.notesapp.storage.NotesModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SECOND_ACTIVITY = 100;
    private static final int ADD_NOTE_UNIQUE_CODE = 1;
    private static int defineFlag;
    private static int id;
    private int flag= 0;
    AppCompatImageButton btnAdd;
    NotesRecyclerAdapter notesRecyclerAdapter;
    RecyclerView showNotes;
    DatabaseHelper databaseHelper;
    ArrayList<NotesModel> arrNotes = new ArrayList<>();
    SearchView searchView;
    ImageView imageView;
    SharedPreferences storeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = DatabaseHelper.getDB(this);

        btnAdd = findViewById(R.id.btnAdd);
        showNotes = findViewById(R.id.showNotes);
        imageView = findViewById(R.id.imgHeading);
        searchView = findViewById(R.id.searchBar);

//        searchView
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

//        image changer
        storeImage = getSharedPreferences("img",MODE_PRIVATE);


            id = storeImage.getInt("img", -1);
            flag = storeImage.getInt("flag", 0);
        if (id != -1){
            imageView.setImageResource(id);
//            Log.d("TAG","f "+flag+"id "+id);
        }
        else {
            imageView.setImageResource(R.drawable.c);
        }

        imageView.setOnClickListener(v -> {

//            Log.d("TAG","f in"+flag+"id "+id);
            imgFetch();
        });

//        overlay the keyboard on the layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

//        Set grid layout manager
        notesRecyclerAdapter = new NotesRecyclerAdapter(this, arrNotes);
        showNotes.setLayoutManager(new GridLayoutManager(this, 2));
        showNotes.setAdapter(notesRecyclerAdapter);

        notesRecyclerAdapter.setOnItemClickListener(new NotesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int id) {
                // Handle item click, you can start a new activity, show a dialog, etc.
                // In this example, we'll start SecondActivity with the position of the clicked item
                Intent intent = new Intent(MainActivity.this, NotesBody.class);
                NotesModel oneNote = databaseHelper.noteModelDao().readOneNote(id);
                Log.d("TAG","p "+id+" head "+oneNote.heading);
                intent.putExtra("flag",200);
                intent.putExtra("id", oneNote.getId());
                intent.putExtra("head", oneNote.getHeading());
                intent.putExtra("des", oneNote.getDescription());
                startActivityForResult(intent, REQUEST_CODE_SECOND_ACTIVITY);
            }
        });

        notesRecyclerAdapter.setOnLongClickListener(new NotesRecyclerAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClick(int position, Context context) {
                    Log.d("TAG", "onItemClick: run");
                    AlertDialog builder = new AlertDialog.Builder(context)
                            .setTitle("Delete item")
                            .setIcon(R.drawable.ic_delete)
                            .setMessage("are you sure ? ")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d("TAG", "onClick: right");
                                    deleteItem(position);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
        });


//        add new content
        btnAdd.setOnClickListener(view -> {
            Intent iNext = new Intent(this, NotesBody.class);
            startActivityForResult(iNext, ADD_NOTE_UNIQUE_CODE);
        });

        getNotes();
    }

//    image changer method
    private void imgFetch(){
        if (flag==0){
            id = (R.drawable.a);
            flag = 1;
        } else if (flag == 1) {
            id = (R.drawable.b);
            flag = 2;
        } else if (flag == 2) {
            id = (R.drawable.c);
            flag = 0;
        }
        storeImage = getSharedPreferences("img",MODE_PRIVATE);
        SharedPreferences.Editor editor = storeImage.edit();
        editor.putInt("img",id);
        editor.putInt("flag",flag);
        editor.apply();
        imageView.setImageResource(id);
    }

    //    Add context result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_UNIQUE_CODE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String heading = bundle.getString("heading");
                String content = bundle.getString("content");
                if (heading != null && content != null) {
                    NotesModel notesModel = new NotesModel(heading, content);
                    databaseHelper.noteModelDao().addNote(notesModel);
                    getNotes();
                }
            }
        }
        else if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == RESULT_OK && data != null) {
            // Receive the updated data and position from the SecondActivity
            int id = data.getIntExtra("id", -1);

            String head = data.getStringExtra("head");
            String cont = data.getStringExtra("cont");

            // Check if position is valid and update the array
            if (id != -1 && head != null && cont != null) {
                NotesModel notesModel = new NotesModel(id, head, cont);
                databaseHelper.noteModelDao().updateNote(notesModel);
                getNotes();

                // Notify your adapter or update UI components if necessary
                // For example, if you are using a RecyclerView and an adapter:
                // recyclerViewAdapter.notifyItemChanged(position);
            }
        }
        if (requestCode == REQUEST_CODE_SECOND_ACTIVITY && resultCode == RESULT_CANCELED && data != null) {
            int id = data.getIntExtra("id", -1);

            if (id != -1) {
                deleteItem(id);
            }
        }
    }
    //get All notes

    private void getNotes() {
        arrNotes.clear();
        class GetNote extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                arrNotes.addAll(databaseHelper.noteModelDao().getAllNotes());
                return null;
            }

            @Override
            protected void onPostExecute(Void unused) {
                super.onPostExecute(unused);
                notesRecyclerAdapter.notifyDataSetChanged();
            }
        }
        new GetNote().execute();
    }
    //delete note

    private void deleteItem(int position) {
        databaseHelper.noteModelDao().deleteNote(position);
        getNotes();
    }

//    search filter list method
    private void filterList(String newText) {

        ArrayList<NotesModel> filterArray = new ArrayList<>();
        if (!newText.isEmpty()){
        for (NotesModel arrNote : arrNotes){
            if (arrNote.getHeading().toLowerCase().contains(newText.toLowerCase())){
                filterArray.add(arrNote);
            }
        }
        if (filterArray.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
            notesRecyclerAdapter.setSearchFilterArray(filterArray);
        }
        else {
            notesRecyclerAdapter.setSearchFilterArray(filterArray);
        }
        }
        else {
            notesRecyclerAdapter.setSearchFilterArray(arrNotes);
        }
    }
}