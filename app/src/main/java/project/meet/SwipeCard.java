package project.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeCard extends AppCompatActivity {
    private Card cardItem;
    private ArrayList<Card> Cards;
    private arrayAdapter arrayAdapter;
    private FirebaseUser currentUser;
    private String currentUserID;
    private DocumentReference singleChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        currentUserID=currentUser.getUid();


        Cards = new ArrayList<>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, Cards);
        CollectionReference users=FirebaseFirestore.getInstance().collection("users");
        users.addSnapshotListener((value, error) -> {
            if(error==null){
                for (QueryDocumentSnapshot doc : value) {
                    if(!doc.getId().equals(currentUserID)){
                        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").
                                document(currentUserID).collection("matches").document(doc.getId());
                        reference.get().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (!document.exists()){
                                    StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users/"+doc.getId()+"/profile.jpg");
                                    cardItem=new Card(doc.getId(),doc.getString("name"),doc.getString("age"),doc.getString("tag"),imageRef);
                                    Cards.add(cardItem);
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                Cards.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataobject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

                Toast.makeText(SwipeCard.this, "It's okay~", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(SwipeCard.this, "Let's chat!", Toast.LENGTH_SHORT).show();
                Card object = (Card) dataObject;
                //Add chatID to databse
                DocumentReference matchID=FirebaseFirestore.getInstance().collection("Chats").document();
                String chatID=matchID.getId();
                Map<String, Object> chatData = new HashMap<>();
                matchID.set(chatData);
                //Add chatID to matches
                DocumentReference reference =users.document(currentUserID).collection("matches").document(object.getUserID());
                Map<String, Object> docData = new HashMap<>();
                docData.put("chatID", chatID);
                reference.set(docData);
                //Add userIDs to respective chat
                singleChat=FirebaseFirestore.getInstance().collection("Chats").
                        document(chatID);
                Map<String, Object> data = new HashMap<>();
                data.put("user1", currentUserID);
                data.put("user2", object.getUserID());
                singleChat.set(data);
                switchToChatRoom(object.getUserID(),chatID);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener((itemPosition, dataObject) ->
                Toast.makeText(SwipeCard.this, "Swipe LEFT to QUIT\nSwipe RIGHT to CHAT", Toast.LENGTH_SHORT).show());

    }

    public void switchToChatRoom(String matchUserID, String chatID){
        Intent intent = new Intent(this, ChatRoom.class);
        intent.putExtra("oppositeUserID",matchUserID);
        intent.putExtra("chatID",chatID);

        startActivity(intent);
    }

}

