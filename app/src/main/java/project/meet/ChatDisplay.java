package project.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatDisplay extends AppCompatActivity {

    private CollectionReference matches;
    private CollectionReference users;
    private FirebaseUser currentUser;
    private String currentUserID;
    StorageReference storageReference;
    private RecyclerView chatList;
    private RecyclerView.LayoutManager matchesLayoutManager;
    private RecyclerView.Adapter matchesAdapter;
    private ArrayList<MatchObject> matchObjectList;
    private String chatID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_display);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        currentUserID=currentUser.getUid();
        matches=FirebaseFirestore.getInstance().collection("users").
                document(currentUserID).collection("matches");
        users=FirebaseFirestore.getInstance().collection("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        matchObjectList= new ArrayList<MatchObject>();
        getEachMatchInfo();

        chatList = (RecyclerView) findViewById(R.id.chatlist);
        matchesLayoutManager = new LinearLayoutManager(ChatDisplay.this);
        chatList.setLayoutManager(matchesLayoutManager);
        matchesAdapter = new recyclerAdapter(matchObjectList, ChatDisplay.this);
        chatList.setAdapter(matchesAdapter);



    }
    private String matchID="";
    private void getEachMatchInfo(){
        matches.addSnapshotListener((value, error) -> {
            if(error==null){
                matchObjectList.clear();
                String matchID="";
                DocumentReference matchUser;
                for (QueryDocumentSnapshot doc : value){
                    matchID=doc.getId();
                    matchUser = users.document(matchID);
                    chatID = doc.getString("chatID");
                    matchUser.addSnapshotListener((value1, error1) -> {
                        if(error1 ==null){
                            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/" + value1.getId() + "/profile.jpg");
                            MatchObject obj = new MatchObject(value1.getId(), value1.getString("name"),
                                    value1.getString("tag"), imageRef, chatID);
                            matchObjectList.add(obj);
                            matchesAdapter.notifyDataSetChanged();
                        }

                    });

                }
            }


        });
    }
}