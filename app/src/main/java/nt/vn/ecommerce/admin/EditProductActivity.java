package nt.vn.ecommerce.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import nt.vn.ecommerce.R;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice, editTextImageUrl;
    private Product productToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextImageUrl = findViewById(R.id.editTextImageUrl);

        productToEdit = (Product) getIntent().getSerializableExtra("product");

        if (productToEdit != null) {
            editTextName.setText(productToEdit.getName());
            editTextPrice.setText(String.valueOf(productToEdit.getPrice()));
            editTextImageUrl.setText(productToEdit.getImageUrl());
        }

        findViewById(R.id.buttonSaveProduct).setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            String imageUrl = editTextImageUrl.getText().toString();

            productToEdit.setName(name);
            productToEdit.setPrice(price);
            productToEdit.setImageUrl(imageUrl);
            // Save updated product to your data source
            Toast.makeText(EditProductActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
