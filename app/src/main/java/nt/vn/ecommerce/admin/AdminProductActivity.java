package nt.vn.ecommerce.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nt.vn.ecommerce.R;

public class AdminProductActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private AdminProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        adapter = new AdminProductAdapter(productList, new AdminProductAdapter.OnProductActionListener() {
            @Override
            public void onEditProduct(Product product) {
                // Navigate to EditProductActivity
                Intent intent = new Intent(AdminProductActivity.this, EditProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onDeleteProduct(Product product) {
                // Delete product from Firebase
                deleteProduct(product);
            }
        });
        recyclerViewProducts.setAdapter(adapter);

        // Load products from Firebase
        loadProducts();

        // Add new product
        findViewById(R.id.buttonAddProduct).setOnClickListener(v -> {
            Intent intent = new Intent(AdminProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Products");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear(); // Clear existing list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        product.setId(snapshot.getKey()); // Set the ID manually
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminProductActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteProduct(Product product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Products").child(product.getId());

        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productList.remove(product);
                adapter.notifyDataSetChanged();
                Toast.makeText(AdminProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminProductActivity.this, "Failed to delete product.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
