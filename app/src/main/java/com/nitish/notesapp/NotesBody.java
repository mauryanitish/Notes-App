package com.nitish.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NotesBody extends AppCompatActivity {

    Toolbar toolbar;
    EditText etHeading, etContent;
    int position;
    private int itemPosition;
    private static final int FLAG = 200;
    private int getFlag;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_body);

        etContent = findViewById(R.id.etContentBody);
        etHeading = findViewById(R.id.etHeadingBody);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        if (!getSupportActionBar().equals("")) {
            getSupportActionBar().setTitle("New Note...");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setSubtitle("Write your wish");


        //    show notes


        itemPosition = getIntent().getIntExtra("id", -1);
         getFlag= getIntent().getIntExtra("flag",-1);
        id = getIntent().getIntExtra("id",0);
        showData();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplicationContext()).inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuId = item.getItemId();
        try {
            if (menuId == R.id.opt_save) {
                if (itemPosition != -1 && getFlag == FLAG ) {
                    String head = etHeading.getText().toString();
                    String cont = etContent.getText().toString();
                    // Replace with your updated data
                    Log.d("TAG", "id "+id);
                    // Pass the updated data and position back to YourActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("id", id);
                    resultIntent.putExtra("head",head);
                    resultIntent.putExtra("cont",cont);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
                else {
                    if (etHeading.getText().toString().equals("")){
                        etHeading.setError("Please enter the heading");
                    }
                    else {
                        setDataOnNote();
                        onBackPressed();
                    }
                }

            }
            else if (menuId == R.id.opt_delete) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("id", itemPosition);
                setResult(RESULT_CANCELED, resultIntent);
                finish();
                Toast.makeText(this, "Delete note ", Toast.LENGTH_LONG).show();
            }
            else if (menuId == android.R.id.home) {
                onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDataOnNote() {
        Bundle bundle = new Bundle();
        bundle.putString("heading", etHeading.getText().toString());
        bundle.putString("content", etContent.getText().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtras(bundle);
        setResult(RESULT_OK, resultIntent);
    }
    private void showData(){
                String heading = getIntent().getStringExtra("head");
                String content = getIntent().getStringExtra("des");
                if(!TextUtils.isEmpty(heading)) {
                    getSupportActionBar().setTitle(heading);
                }

                etHeading.setText(heading);
                etContent.setText(content);
    }

}