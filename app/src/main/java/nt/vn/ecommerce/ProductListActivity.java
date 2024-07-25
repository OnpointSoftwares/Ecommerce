package nt.vn.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:751833890258:android:9ad5a3de9da074edd15c0b") // Replace with your app's ID
                .setApiKey("AIzaSyAhnBDJEfd2XzZGZ4HvScSQSFpR5Wp5U5Q") // Replace with your API key
                .setDatabaseUrl("https://fingerprintverification-default-rtdb.firebaseio.com") // Replace with your database URL
                .setProjectId("fingerprintverification") // Replace with your project ID
                .setStorageBucket("fingerprintverification.appspot.com") // Replace with your storage bucket
                .build();

        // Initialize Firebase with the custom options
        FirebaseApp.initializeApp(this, options);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab=findViewById(R.id.chat);
        FloatingActionButton fabCart=findViewById(R.id.cart);
        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductListActivity.this,CartActivity.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductListActivity.this,ChatActivity.class));
            }
        });
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Fetch products from Firebase
        loadProducts();
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
                productAdapter.notifyDataSetChanged(); // Notify adapter about data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductListActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openCart(View view) {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}
