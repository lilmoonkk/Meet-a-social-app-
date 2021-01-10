package project.meet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    MaterialEditText username, age, email, password, phone, tag;
    RadioGroup radioGroup;
    Button btn_register;
    TextView signin;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        radioGroup = findViewById(R.id.radioGroup);
        username = findViewById(R.id.username);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        tag = findViewById(R.id.tag);
        btn_register = findViewById(R.id.btn_register);
        signin = findViewById(R.id.textSignIn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progressBar);

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }

        btn_register.setOnClickListener(view -> {

            int select = radioGroup.getCheckedRadioButtonId();
            final RadioButton radioButton = findViewById(select);

            if(radioButton.getText() == null){
                return;
            }

            final String gender=radioButton.getText().toString();
            final String txt_username = username.getText().toString();
            final String txt_age = age.getText().toString();
            final String txt_email = email.getText().toString();
            final String txt_password = password.getText().toString();
            final String txt_phone = phone.getText().toString();
            final String txt_tag = tag.getText().toString();

            if (TextUtils.isEmpty(txt_username) ||TextUtils.isEmpty(txt_age)|| TextUtils.isEmpty(txt_email) ||
                    TextUtils.isEmpty(txt_password)||TextUtils.isEmpty(txt_phone)||TextUtils.isEmpty(txt_tag)){
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (txt_password.length() < 6 ){
                Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            //show user the register is processing
            progressBar.setVisibility(View.VISIBLE);
            //register the user in firebase
            auth.createUserWithEmailAndPassword(txt_email, txt_password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){

                            //Send verification link to verify email
                            FirebaseUser current_user = auth.getCurrentUser();
                            current_user.sendEmailVerification().addOnSuccessListener(aVoid ->
                                    Toast.makeText(RegisterActivity.this, "Verification Email Successfully Sent.", Toast.LENGTH_SHORT).show()
                                    ).addOnFailureListener(e -> Log.d(TAG, "onFailure: Email not sent " + e.getMessage()));

                            Toast.makeText(RegisterActivity.this, "Successfully Register!", Toast.LENGTH_SHORT).show();
                            userID = auth.getCurrentUser().getUid();
                            DocumentReference reference = db.collection("users").document(userID);
                            // Create a new user with all the information
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", txt_username);
                            user.put("age", txt_age);
                            user.put("gender", gender);
                            user.put("email", txt_email);
                            user.put("phone", txt_phone);
                            user.put("tag", txt_tag);

                            // Add a new document with a generated ID
                            reference.set(user).addOnSuccessListener(aVoid ->
                                    Log.d(TAG, "onSuccess: User Profile is created for "+ userID)
                            ).addOnFailureListener(e ->
                                    Log.d(TAG, "onFailure: " + e.toString()));

                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

        });

        signin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LoginActivity.class)));
    }
}
