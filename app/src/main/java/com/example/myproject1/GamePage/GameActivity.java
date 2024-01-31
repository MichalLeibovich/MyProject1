package com.example.myproject1.GamePage;

import static android.app.PendingIntent.getActivity;
import static com.example.myproject1.GamePage.Display.colorList;
import static com.example.myproject1.GamePage.Display.pathList;
import static com.example.myproject1.GamePage.Display.pathList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myproject1.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;


public class GameActivity extends AppCompatActivity {

    public static Path path = new Path();
    public static Paint paintBrush = new Paint();

    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }


    public void pencil(View view) {
        paintBrush.setColor(Color.BLACK);
        currentColor(paintBrush.getColor());
    }

    public void reset(View view) {
        pathList.clear();
        colorList.clear();
        path.reset();
    }


    public void redColor(View view) {
        paintBrush.setColor(Color.RED);
        currentColor(paintBrush.getColor());
    }

    public void yellowColor(View view) {
        paintBrush.setColor(Color.YELLOW);
        currentColor(paintBrush.getColor());
    }

    public void greenColor(View view) {
        paintBrush.setColor(Color.GREEN);
        currentColor(paintBrush.getColor());
    }

    public void pinkColor(View view) {
        paintBrush.setColor(Color.MAGENTA);
        currentColor(paintBrush.getColor());
    }

    public void blueColor(View view) {
        paintBrush.setColor(Color.BLUE);
        currentColor(paintBrush.getColor());
    }

    public void whiteColor(View view) {
        paintBrush.setColor(Color.WHITE);
        currentColor(paintBrush.getColor());
    }

    public void blackColor(View view) {
        paintBrush.setColor(Color.BLACK);
        currentColor(paintBrush.getColor());
    }

    public void greyColor(View view) {
        paintBrush.setColor(Color.GRAY);
        currentColor(paintBrush.getColor());
    }


    public void currentColor(int c) {
        Display.currentBrush = c;
        path = new Path();
    }

//    public void brightGrayColor(View view)
//    {
//        int color = R.color.brightGray;
//        String s = String.valueOf(color);
//        paintBrush.setColor(Color.parseColor(s));
//        currentColor(paintBrush.getColor());
//    }


    public void saveCanvasasBitmap(View view) {
        View v = findViewById(R.id.include);
        Bitmap myBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);

        Canvas c = new Canvas(myBitmap);

        v.draw(c);

        uploadDrawingToStorage(myBitmap, "hello.jpg");
    }

    public void uploadDrawingToStorage(Bitmap bitmap, String entryName) {
        // set the reference as follows:
        // "folder
        // " named entryname which is the id of the post
        // unique image name in case we have more than one image in the post...future
        StorageReference storageRef = firebaseStorage.getReference();
        // at the moment add random name
        StorageReference imageRef = storageRef.child(entryName);
        // bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        // This is required only if we want to get the image url
        // in https:...  type -> direct url to the image
        // not via Firebase references
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toast.makeText(GameActivity.this, "Uploading successes", Toast.LENGTH_SHORT);
                    Log.d("FIREBASE STORAGE", "STORAGE FIREBASE onSuccess: " + downloadUri);
                } else {
                    // Handle failures
                    Log.d("FIREBASE STORAGE", "STORAGE FIREBASE onComplete:  failed");
                    Toast.makeText(GameActivity.this, "Uploading failed", Toast.LENGTH_SHORT);
                }
            }
        });
//    }


    }
}