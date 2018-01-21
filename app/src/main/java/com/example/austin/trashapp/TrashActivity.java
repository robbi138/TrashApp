package com.example.austin.trashapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TrashActivity extends AppCompatActivity {

    private static final String CREATE_CAN = "http://webdev.cse.msu.edu/~robbi138/trashApp/insertCan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
    }


    public void onButton(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}

