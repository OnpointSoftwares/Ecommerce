package nt.vn.ecommerce;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.UUID;

import nt.vn.ecommerce.admin.Product;

public class AddItemsFragment extends Fragment {
    private EditText editTextName, editTextPrice, editTextImageUrl;
    private String imageUrl;
    private String categorySelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_items, container, false);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:751833890258:android:9ad5a3de9da074edd15c0b") // Replace with your app's ID
                .setApiKey("AIzaSyAhnBDJEfd2XzZGZ4HvScSQSFpR5Wp5U5Q") // Replace with your API key
                .setDatabaseUrl("https://fingerprintverification-default-rtdb.firebaseio.com") // Replace with your database URL
                .setProjectId("fingerprintverification") // Replace with your project ID
                .setStorageBucket("fingerprintverification.appspot.com") // Replace with your storage bucket
                .build();

        categorySelected = "";

        // Initialize Firebase with the custom options
        Spinner categories = view.findViewById(R.id.spiner);
        String[] category = {
                "Shoes", "Clothes", "Food", "Electronics", "Books", "Accessories",
                "Home Appliances", "Toys", "Sports Equipment", "Beauty Products"
        };
        categories.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, category));
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        editTextName = view.findViewById(R.id.editTextName);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        editTextImageUrl = view.findViewById(R.id.editTextImageUrl);

        Button selectImage = view.findViewById(R.id.selectImage);
        selectImage.setOnClickListener(v -> selectImage());

        view.findViewById(R.id.buttonSaveProduct).setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            String imageUrl = editTextImageUrl.getText().toString();

            Product newProduct = new Product("new_id", name, price, imageUrl);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();
            String key = ref.push().getKey();
            ref.child("Products").child(key).child(categorySelected).setValue(newProduct).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Product added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void selectImage() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Uri imageSelected = data.getData();
            if (imageSelected != null) {
                ContentResolver contentResolver = requireContext().getContentResolver();
                try (Cursor myCursor = contentResolver.query(imageSelected, new String[]{MediaStore.Images.Media.DATA}, null, null, null)) {
                    if (myCursor != null && myCursor.moveToFirst()) {
                        int columnIndex = myCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        String picturePath = myCursor.getString(columnIndex);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageSelected);
                        uploadImage(imageSelected);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(Uri fileUri) {
        if (fileUri != null) {
            ProgressDialog progressDialog = new ProgressDialog(requireContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Uploading your image..");
            progressDialog.show();

            StorageReference ref = FirebaseStorage.getInstance().getReference().child(UUID.randomUUID().toString());
            ref.putFile(fileUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    editTextImageUrl.setText(imageUrl);
                });
                Toast.makeText(requireContext(), "Image Uploaded..", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                progressDialog.dismiss();
                Toast.makeText(requireContext(), "Failed to Upload Image..", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
