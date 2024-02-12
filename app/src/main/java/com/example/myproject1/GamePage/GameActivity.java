package com.example.myproject1.GamePage;

import static android.app.PendingIntent.getActivity;
import static com.example.myproject1.GamePage.Display.colorList;
import static com.example.myproject1.GamePage.Display.pathList;
import static com.example.myproject1.GamePage.Display.pathList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.MainScreenActivity;
import com.example.myproject1.R;
import com.example.myproject1.RankingScreenActivity;
import com.example.myproject1.WaitingRoomActivity;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;


public class GameActivity extends AppCompatActivity {

    public static Path path = new Path();
    public static Paint paintBrush = new Paint();

    TextView textView;

    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        textView = findViewById (R.id.textView3);

   //     Display display = new Display(this);



        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                textView.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                textView.setText("00:00");
                saveCanvasAsBitmap();
                Intent intent = new Intent(GameActivity.this, RankingScreenActivity.class);
                startActivity(intent);
            }
        }.start();

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




    public void saveCanvasAsBitmap() {
        View v = findViewById(R.id.include);

    //    View v = v1.findViewById(R.id.gmDisplay);


        // Measure and layout the view to ensure it's properly sized
        v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap myBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(myBitmap);
        v.draw(c);
       // Bitmap b  = BitmapFactory.decodeResource(getResources(),R.drawable.img);
    // myBitmap = b;
        String name = UUID.randomUUID().toString().substring(0,6);
        uploadDrawingToStorage(myBitmap, name+".png");
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
    /*
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override

            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    Log.d("FB STORAGE", "onComplete: upload success");
                }
                else
                    Log.d("FB STORAGE", "onComplete: upload fail " + task.getException().getMessage());

            }
        });

     */
        // This is required only if we want to get the image url
        // in https:...  type -> direct url to the image
        // not via Firebase references

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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