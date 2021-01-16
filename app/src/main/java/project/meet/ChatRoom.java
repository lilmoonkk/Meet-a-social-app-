package project.meet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {
    private String chatID;
    private EditText currentMsg;
    private Button send;
    private FirebaseUser currentUser;
    private String currentUserID, oppositeUserID;
    private DocumentReference currentUserRef;
    private CollectionReference messageListRef;
    private ArrayList<MessageObject> messageList;
    private RecyclerView msgListRV;
    private RecyclerView.LayoutManager msgLayoutManager;
    private RecyclerView.Adapter msgAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        chatID=getIntent().getStringExtra("chatID");
        oppositeUserID = getIntent().getStringExtra("oppositeUserID");

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        currentUserID=currentUser.getUid();
        messageListRef = FirebaseFirestore.getInstance().collection("Chats").
                document(chatID).collection("message");

        send=findViewById(R.id.send);
        currentMsg=findViewById(R.id.textbox);

        messageList= new ArrayList<MessageObject>();
        msgListRV = (RecyclerView) findViewById(R.id.messagelist);
        msgLayoutManager = new LinearLayoutManager(ChatRoom.this);
        msgListRV.setLayoutManager(msgLayoutManager);
        msgAdapter = new MessagesRecyclerAdapter(messageList, ChatRoom.this);
        getEachMessage();
        msgListRV.setAdapter(msgAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage(){
        String currentMsgText=currentMsg.getText().toString();
        if(!currentMsgText.isEmpty()){
            /*messagelist = FirebaseFirestore.getInstance().collection("Chats").document(chatID).collection("message");
            messagelist.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().size() == 0){
                            addChatIDtoOUser();
                        }
                    }
                }
            });*/
            addChatIDtoOUser();
            currentUserRef=FirebaseFirestore.getInstance().collection("Chats").
                    document(chatID).collection("message").document();
            Map<String, Object> docData = new HashMap<>();
            docData.put("sentBy", currentUserID);
            docData.put("message", currentMsgText);
            docData.put("timeSent", Timestamp.now());
            currentUserRef.set(docData);
            currentMsg.getText().clear();
        }
    }

    private void addChatIDtoOUser(){
        DocumentReference reference=FirebaseFirestore.getInstance().collection("users").
                document(oppositeUserID).collection("matches").document(currentUserID);
        reference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if (!document.exists()){
                    Map<String, Object> docData = new HashMap<>();
                    docData.put("chatID", chatID);
                    reference.set(docData);
                }
            }
        });
    }

    private String messageID="";
    private boolean sentByCurrentUser=false;
    DocumentReference chatRef;
    private void getEachMessage(){
        messageListRef.orderBy("timeSent").addSnapshotListener((value, error) -> {
            if(error==null){
                DocumentReference matchUser;
                for (QueryDocumentSnapshot doc : value){
                    messageID=doc.getId();
                    chatRef=FirebaseFirestore.getInstance().collection("Chats").
                            document(chatID).collection("message").document(messageID);
                    chatRef.addSnapshotListener((value1, error1) -> {
                        if(error1 ==null){
                                MessageObject obj = new MessageObject(value1.getString("message"), value1.getString("sentBy").equals(currentUserID));
                                messageList.add(obj);
                                msgAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }


        });
    }

}