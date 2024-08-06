package nt.vn.ecommerce.admin;

import static nt.vn.ecommerce.R.id.addItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import nt.vn.ecommerce.AddItemsFragment;
import nt.vn.ecommerce.OrdersFragment;
import nt.vn.ecommerce.R;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        BottomNavigationView bottomnav = findViewById(R.id.bottomnav);
        replaceFragment(new OrdersFragment());
        bottomnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.addItem) {
                    replaceFragment(new AddItemsFragment());
                }
                if (item.getItemId() == R.id.orders) {
                    replaceFragment(new OrdersFragment());
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
