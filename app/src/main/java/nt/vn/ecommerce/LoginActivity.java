package nt.vn.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import nt.vn.ecommerce.admin.AddProductActivity;
import nt.vn.ecommerce.admin.AdminProductActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:751833890258:android:9ad5a3de9da074edd15c0b") // Replace with your app's ID
                .setApiKey("AIzaSyAhnBDJEfd2XzZGZ4HvScSQSFpR5Wp5U5Q") // Replace with your API key
                .setDatabaseUrl("https://fingerprintverification-default-rtdb.firebaseio.com") // Replace with your database URL
                .setProjectId("fingerprintverification") // Replace with your project ID
                .setStorageBucket("fingerprintverification.appspot.com") // Replace with your storage bucket
                .build();

        // Initialize Firebase with the custom options
        FirebaseApp.initializeApp(this, options);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    public void login(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, AddProductActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
