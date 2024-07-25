package nt.vn.ecommerce.admin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

import nt.vn.ecommerce.R;

public class AddProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice, editTextImageUrl;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:751833890258:android:9ad5a3de9da074edd15c0b") // Replace with your app's ID
                .setApiKey("AIzaSyAhnBDJEfd2XzZGZ4HvScSQSFpR5Wp5U5Q") // Replace with your API key
                .setDatabaseUrl("https://fingerprintverification-default-rtdb.firebaseio.com") // Replace with your database URL
                .setProjectId("fingerprintverification") // Replace with your project ID
                .setStorageBucket("fingerprintverification.appspot.com") // Replace with your storage bucket
                .build();

        // Initialize Firebase with the custom options

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);
        Button selectImage=findViewById(R.id.selectImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        findViewById(R.id.buttonSaveProduct).setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            String imageUrl = editTextImageUrl.getText().toString();

            Product newProduct = new Product("new_id", name, price, imageUrl);
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference ref=database.getReference();
            String key=ref.push().getKey();
            ref.child("Products").child(key.toString()).setValue(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(AddProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        });
    }
    private void selectImage()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri imageSelected = data.getData();
            if (imageSelected != null) {
                String[] pathColumn = {MediaStore.Images.Media.DATA};
                ContentResolver contentResolver = this.getContentResolver();
                Cursor myCursor = contentResolver.query(imageSelected, pathColumn, null, null, null);

                // Setting the image to the ImageView
                if (myCursor != null) {
                    myCursor.moveToFirst();
                    int columnIndex = myCursor.getColumnIndex(pathColumn[0]);
                    String picturePath = myCursor.getString(columnIndex);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageSelected);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    uploadImage(imageSelected);
                    myCursor.close();
                }
            }
        }
    }

    private void uploadImage(Uri fileUri) {
        if (fileUri != null) {
            // Display progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Uploading your image..");
            progressDialog.show();

            // Create a reference to Firebase Storage and a child with a random UUID
            StorageReference ref = FirebaseStorage.getInstance().getReference()
                    .child(UUID.randomUUID().toString());

            // Upload the file
            ref.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
                // This method is called when the file is uploaded successfully
                progressDialog.dismiss();
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    editTextImageUrl.setText(imageUrl);
                });
                Toast.makeText(this, "Image Uploaded..", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                // This method is called when there is a failure in file upload
                progressDialog.dismiss();
                Toast.makeText(this, "Fail to Upload Image..", Toast.LENGTH_SHORT).show();
            }).addOnCompleteListener(task -> {
                // This block can be used for additional actions when the task completes
            });
        }
    }

}
