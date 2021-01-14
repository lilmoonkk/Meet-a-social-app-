package project.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    MaterialEditText nName, nAge, nEmail, nPhone, nTag;
    ImageView profileImage;
    Button saveBtn;
    FirebaseAuth auth;
    FirebaseFirestore store;
    FirebaseUser user;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        String name = data.getStringExtra("name");
        String age = data.getStringExtra("age");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");
        String tag = data.getStringExtra("tag");

        auth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        nName = findViewById(R.id.editName);
        nAge = findViewById(R.id.editAge);
        nEmail = findViewById(R.id.editEmail);
        nPhone = findViewById(R.id.editPhoneNo);
        nTag = findViewById(R.id.editTag);
        profileImage = findViewById(R.id.changeImage);
        saveBtn = findViewById(R.id.saveChange);

        //store the image url
        StorageReference imageRef = storageReference.child("users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(profileImage));

        nName.setText(name);
        nAge.setText(age);
        nEmail.setText(email);
        nPhone.setText(phone);
        nTag.setText(tag);

        Log.d(TAG, "onCreate: " + name + " " +age + " " + email + " " + phone + " " + tag);

        profileImage.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent,1000);
        });

        saveBtn.setOnClickListener(v -> {
            final String txt_username = nName.getText().toString();
            final String txt_age = nAge.getText().toString();
            final String txt_email = nEmail.getText().toString();
            final String txt_phone = nPhone.getText().toString();
            final String txt_tag = nTag.getText().toString();

            if (TextUtils.isEmpty(txt_username) ||TextUtils.isEmpty(txt_age)|| TextUtils.isEmpty(txt_email)
                    ||TextUtils.isEmpty(txt_phone)||TextUtils.isEmpty(txt_tag)){
                Toast.makeText(EditProfileActivity.this, "There are empty field", Toast.LENGTH_SHORT).show();
                return;
            }

            user.updateEmail(txt_email).addOnSuccessListener(aVoid -> {
                DocumentReference reference = store.collection("users").document(user.getUid());
                Map<String,Object> edited = new HashMap<>();
                edited.put("name",txt_username);
                edited.put("age",txt_age);
                edited.put("email", txt_email);
                edited.put("phone",txt_phone);
                edited.put("tag",txt_tag);
                reference.update(edited).addOnSuccessListener(aVoid1 ->
                        Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show());
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }).addOnFailureListener(e ->
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        final StorageReference fileRef = storageReference.child("users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(uri ->
                        Picasso.get().load(uri).into(profileImage))
        ).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show());
    }
}