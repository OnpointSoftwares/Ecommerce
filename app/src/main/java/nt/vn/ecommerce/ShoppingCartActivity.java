package nt.vn.ecommerce;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerView = findViewById(R.id.recyclerView);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartItemList = new ArrayList<>();
        // Populate cartItemList with data
        cartAdapter = new CartAdapter(cartItemList);
        recyclerView.setAdapter(cartAdapter);

        // Calculate total price
        double totalPrice = 0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getProduct().getPrice() * item.getQuantity();
        }
        textViewTotalPrice.setText("Total: $" + totalPrice);
    }
}
