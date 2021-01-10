package project.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    TextView uName, uAge, uGender, uEmail, uPhone, uTag, verifyMsg;
    ImageView profileImage;
    FirebaseAuth auth;
    FirebaseFirestore store;
    String userId;
    Button resendLink, changeProfile, logOut;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uName = findViewById(R.id.profileName);
        uAge = findViewById(R.id.profileAge);
        uGender = findViewById(R.id.profileGender);
        uEmail = findViewById(R.id.profileEmail);
        uPhone = findViewById(R.id.profilePhone);
        uTag = findViewById(R.id.profileTag);
        verifyMsg = findViewById(R.id.verifyMsg);
        resendLink = findViewById(R.id.resendLink);
        profileImage = findViewById(R.id.profilePic);
        changeProfile = findViewById(R.id.editProfile);
        logOut = findViewById(R.id.logOut_btn);

        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        userId = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        if(!user.isEmailVerified()){
            verifyMsg.setVisibility(View.VISIBLE);
            resendLink.setVisibility(View.VISIBLE);

            resendLink.setOnClickListener(v ->
                    user.sendEmailVerification().addOnSuccessListener(aVoid -> {
                        Toast.makeText(v.getContext(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                        if (user.isEmailVerified()) {
                            verifyMsg.setVisibility(View.GONE);
                            resendLink.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(e -> Log.d("tag", "onFailure: Email not sent " + e.getMessage())));
        }
        else{
            verifyMsg.setVisibility(View.GONE);
            resendLink.setVisibility(View.GONE);
        }

        //display user profile image
        StorageReference imageRef = storageReference.child("users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        //Retrieve data from fireStore
        DocumentReference reference = store.collection("users").document(userId);
        reference.addSnapshotListener(this, (value, error) -> {
            uName.setText(value.getString("name"));
            uAge.setText(value.getString("age"));
            uGender.setText(value.getString("gender"));
            uEmail.setText(value.getString("email"));
            uPhone.setText(value.getString("phone"));
            uTag.setText(value.getString("tag"));
        });

        changeProfile.setOnClickListener(v -> {
            // open gallery
            Intent intent = new Intent(v.getContext(),EditProfileActivity.class);
            intent.putExtra("name",uName.getText().toString());
            intent.putExtra("age",uAge.getText().toString());
            intent.putExtra("email",uEmail.getText().toString());
            intent.putExtra("phone",uPhone.getText().toString());
            intent.putExtra("tag",uTag.getText().toString());
            startActivity(intent);
        });
    }

    public void logout(View view) {
        // log out
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void Match(View view){
        Intent intent = new Intent(this, SwipeCard.class);
        startActivity(intent);
    }
}