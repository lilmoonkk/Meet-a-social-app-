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
        chatList = (RecyclerView) findViewById(R.id.chatlist);
        matchesLayoutManager = new LinearLayoutManager(ChatDisplay.this);
        chatList.setLayoutManager(matchesLayoutManager);
        matchObjectList=new ArrayList<MatchObject>();
        matchesAdapter = new recyclerAdapter(matchObjectList, ChatDisplay.this);
        getEachMatchInfo();
        chatList.setAdapter(matchesAdapter);



    }
    private String matchID="";
    private void getEachMatchInfo(){
        matches.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                String matchID="";
                DocumentReference matchUser;
                for (QueryDocumentSnapshot doc : value){
                    matchID=doc.getId();
                    matchUser = users.document(matchID);
                    matchUser.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error==null){
                                StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/" + value.getId() + "/profile.jpg");
                                MatchObject obj = new MatchObject(value.getId(), value.getString("name"),
                                        value.getString("tag"), imageRef);
                                matchObjectList.add(obj);
                                matchesAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                }
            }
        });
    }
}