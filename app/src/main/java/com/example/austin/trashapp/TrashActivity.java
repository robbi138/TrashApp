package com.example.austin.trashapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TrashActivity extends AppCompatActivity {

    private static final String CREATE_CAN = "http://webdev.cse.msu.edu/~robbi138/classweb/trashApp/insertCan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
    }
}
