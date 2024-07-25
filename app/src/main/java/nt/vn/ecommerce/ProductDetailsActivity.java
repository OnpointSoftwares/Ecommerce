package nt.vn.ecommerce;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView textViewName, textViewPrice, textViewDescription;
    private Product product;
    private ImageView imageView;
    private Button addToCart;
    private EditText edtQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        textViewName = findViewById(R.id.textViewName);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewDescription = findViewById(R.id.textViewDescription);
        addToCart=findViewById(R.id.addToCart);
        edtQuantity=findViewById(R.id.edtQuantity);
        imageView=findViewById(R.id.productImage);
        // Get product data from intent
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            textViewName.setText(product.getName());
            textViewPrice.setText(String.valueOf(product.getPrice()));
            textViewDescription.setText(product.getDescription());
            Glide.with(ProductDetailsActivity.this).load(product.getImageUrl()).into(imageView);
        }
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer qty=Integer.parseInt(edtQuantity.getText().toString());
                CartItem cart= new CartItem(product,qty);
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference ref=database.getReference();
                String key=ref.push().getKey().toString();
                ref.child("cart").child(key).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProductDetailsActivity.this,"Added to cart",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
