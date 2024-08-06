package nt.vn.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import nt.vn.ecommerce.BlankFragment;

public class ProductListActivity extends AppCompatActivity {

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
        FirebaseApp.initializeApp(ProductListActivity.this, options);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        BottomNavigationView bottomnav=findViewById(R.id.bottomnavLogin);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.signup) {
                    startActivity(new Intent(ProductListActivity.this,SignUpActivity.class));
                }
                if (item.getItemId() == R.id.login) {
                    startActivity(new Intent(ProductListActivity.this,LoginActivity.class));
                }

                if (item.getItemId() == R.id.chat) {
                    startActivity(new Intent(ProductListActivity.this,ChatActivity.class));
                }
                return false;
            }
        });
        viewPager.setAdapter(new ScreenSlidePagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Food");
                            break;
                        case 1:
                            tab.setText("Clothes");
                            break;
                        case 2:
                            tab.setText("Shoes");
                            break;
                    }
                }).attach();
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new BlankFragment();
                case 1:
                    return new ShoesFragment();
                case 2:
                    return new ClothesFragment();
                default:
                    return new BlankFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}