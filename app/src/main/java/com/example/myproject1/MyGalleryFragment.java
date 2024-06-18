package com.example.myproject1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGalleryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    View fragView;
    GridView gvMyGallery;
    User user;
    ArrayList<MyGalleryCube> myGalleryCubes;



    public MyGalleryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyGalleryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyGalleryFragment newInstance(String param1, String param2) {
        MyGalleryFragment fragment = new MyGalleryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_my_gallery, container, false);
        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
        {
            String userId = currentUser.getUid();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            DocumentReference userRef = firestore.collection("Users").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot value) {
                    if (value.exists()) {
                        user = value.toObject(User.class);
                        displayGridView(user);
                    } else {
                        Log.d("MyGalleryFragment", "User document does not exist");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("MyGalleryFragment", "Error fetching user document: " + e.getMessage());
                }
            });
        }
        else
        {
            Log.e("MyGalleryFragment", "No user logged in");
        }

    }


    public void displayGridView(User user)
    {
        myGalleryCubes = new ArrayList<>();

        ArrayList<String> gameIdsList = user.getGameIdsList();
        String username = HomeFragment.username;

        for (int i = 0; i < gameIdsList.size(); i++)
        {
            String gameId = gameIdsList.get(i);
            getSubjectInGameRoom(gameId, username, gameIdsList.size());
            // Inside the func, I got the subject and then went to continue func
            // Inside continue func, I added the myGalleryCube with this subject, gameId and username4
            // Now, all myGalleryCubes are in the arrayList myGalleryCubes.
        }

    }

    private void getSubjectInGameRoom(String gameId, String username, int gameIdsListSize)
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference gameRoomRef = firestore.collection("Games").document(gameId);
        gameRoomRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot value) {
            if (value.exists()) {
                GameRoom gr = value.toObject(GameRoom.class);
                String grSubject = gr.getSubject();
                continueDisplayGridView(grSubject, gameId, username, gameIdsListSize);
            }
            else
            {
                Log.d("MyGalleryFragment", "GameRoom document does not exist");
            }
        }
        });
    }


    private void continueDisplayGridView(String subject, String gameId, String username, int gameIdsListSize)
    {
        MyGalleryCube myGalleryCube = new MyGalleryCube(username + gameId, subject);
        myGalleryCubes.add(myGalleryCube);
        if (gameIdsListSize == myGalleryCubes.size())
        {
            gvMyGallery = fragView.findViewById(R.id.gv_myGallery);
            MyGalleryCubeAdapter gvAdapter = new MyGalleryCubeAdapter(getContext(), myGalleryCubes);
            gvMyGallery.setAdapter(gvAdapter);
        }
    }

}