package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myproject1.GamePage.GameActivity;

public class MainScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }


    public void PlayClicked(View view)
    {
        //Intent intent = new Intent(MainScreenActivity.this, WaitingRoomActivity.class);
        //startActivity(intent);
        showDialogBox();
    }

    public void showDialogBox()
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.show();
    }

    public void MyGalleryClicked(View view)
    {
        Intent intent = new Intent(MainScreenActivity.this, MyGalleryActivity.class);
        startActivity(intent);
    }

    public void createNewGameRoom()
    {
        Intent intent = new Intent(MainScreenActivity.this, WaitingRoomActivity.class);
        startActivity(intent);
    }

}