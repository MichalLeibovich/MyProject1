package com.example.myproject1.GameScreen;

import static android.app.PendingIntent.getActivity;
import static com.example.myproject1.GameScreen.Display.colorList;
import static com.example.myproject1.GameScreen.Display.pathList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject1.GameRoom;
import com.example.myproject1.HomeFragment;
import com.example.myproject1.R;
import com.example.myproject1.RatingScreenActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {

    public static Path path = new Path();
    public static Paint paintBrush = new Paint();
    String gameId;
    TextView tvTimer;
    String subject;
    Bitmap myBitmap;

    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

    private Display drawingView;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        tvTimer = findViewById (R.id.tv_timer);
        drawingView = findViewById(R.id.include);

        gameId = getIntent().getStringExtra("gameId");

        setWord();
        displayPlayersName(gameId);

        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00 there
            public void onFinish() {
                tvTimer.setText("00:00");
                saveCanvasAsBitmap();
            }
        }.start();





    }

    public void setWord()
    {
        subject = getIntent().getStringExtra("subject");
        TextView tvSubject = findViewById(R.id.tv_subject);
        tvSubject.setText("Subject: " + subject);
    }


    public void currentColor(int c) {
        Display.currentBrush = c;
        path = new Path();
    }


    public void pencil(View view) {
        int color = ContextCompat.getColor(this, R.color.black);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void eraser(View view) {
        int color = ContextCompat.getColor(this, R.color.white);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }


    public void undoDrawing(View view)
    {
        drawingView.undo();
    }


    public void resetDrawing(View view)
    {
        drawingView.reset();
        path.reset();
    }
    public void resetDrawing()
    {
        drawingView.reset();
        path.reset();
    }


    public void whiteColor(View view) {
        int color = ContextCompat.getColor(this, R.color.white);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void blackColor(View view) {
        int color = ContextCompat.getColor(this, R.color.black);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brightGrayColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brightGray);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void darkGrayColor(View view) {
        int color = ContextCompat.getColor(this, R.color.darkGray);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brightRedColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brightRed);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }
    public void darkRedColor(View view) {
        int color = ContextCompat.getColor(this, R.color.darkRed);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brightOrangeColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brightOrange);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void darkOrangeColor(View view) {
        int color = ContextCompat.getColor(this, R.color.darkOrange);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brightYellowColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brightYellow);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void darkYellowColor(View view) {
        int color = ContextCompat.getColor(this, R.color.darkYellow);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brightGreenColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brightGreen);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void darkGreenColor(View view) {
        int color = ContextCompat.getColor(this, R.color.darkGreen);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brightBlueColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brightBlue);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void darkBlueColor(View view) {
        int color = ContextCompat.getColor(this, R.color.darkBlue);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void pinkColor(View view) {
        int color = ContextCompat.getColor(this, R.color.pink);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void purpleColor(View view) {
        int color = ContextCompat.getColor(this, R.color.purple);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void beigeColor(View view) {
        int color = ContextCompat.getColor(this, R.color.beige);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }

    public void brownColor(View view) {
        int color = ContextCompat.getColor(this, R.color.brown);
        paintBrush.setColor(color);
        currentColor(paintBrush.getColor());
    }






    public void saveCanvasAsBitmap() {
        View v = findViewById(R.id.include);

        // Measure and layout the view to ensure it's properly sized
        v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        // Create a new bitmap with white background
        myBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        myBitmap.eraseColor(Color.WHITE); // Fill bitmap with white color

        // Draw the content of the view onto the bitmap
        Canvas c = new Canvas(myBitmap);
        v.draw(c);


        String gameId = getIntent().getStringExtra("gameId");

        // Upload the bitmap to storage
        //String name = MainScreenActivity.username + gameId;
        String name = HomeFragment.username + gameId;
        uploadDrawingToStorage(myBitmap, name+".png");
    }

    private void displayPlayersName(String gameId) {
        FirebaseFirestore fb = FirebaseFirestore.getInstance();

        fb.collection("Games").document(gameId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GameRoom gr = documentSnapshot.toObject(GameRoom.class);
                ArrayList<String> arrNames = gr.getPlayersNames();

                ListView lv = findViewById(R.id.lvNames);

                ArrayAdapter adapter = new ArrayAdapter<String>(GameActivity.this,
                        android.R.layout.simple_list_item_1,
                        arrNames);

                lv.setAdapter(adapter);




            }
        });


    }


/*

    public void getUsername()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference userRef = firestore.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    username = documentSnapshot.getString("username");
                    userNameFromFB(username);
                }
                else
                {
                    Log.d("MainScreenActivity", "User document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.e("MainScreenActivity", "Error fetching user document: " + e.getMessage());
            }
        });
    }

 */


    public void uploadDrawingToStorage(Bitmap bitmap, String entryName){
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

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override

            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    Log.d("FB STORAGE", "onComplete: upload success");
                    resetDrawing();
                }
                else
                    Log.d("FB STORAGE", "onComplete: upload fail " + task.getException().getMessage());

            }
        });


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
                    Intent intent = new Intent(GameActivity.this, RatingScreenActivity.class);
                    //intent.putExtra(Intent.EXTRA_TEXT, gameId);
                    intent.putExtra("gameId", gameId);
                    startActivity(intent);
                    Log.d("FIREBASE STORAGE", "STORAGE FIREBASE onSuccess: " + downloadUri);
                } else {
                    // Handle failures
                    Log.d("FIREBASE STORAGE", "STORAGE FIREBASE onComplete:  failed");
                    Toast.makeText(GameActivity.this, "Uploading failed", Toast.LENGTH_SHORT);
                }
            }
        });




    }


}