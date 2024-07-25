package nt.vn.ecommerce.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import nt.vn.ecommerce.R;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private OnProductActionListener listener;

    public interface OnProductActionListener {
        void onEditProduct(Product product);
        void onDeleteProduct(Product product);
    }

    public AdminProductAdapter(List<Product> productList, OnProductActionListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_product_item, parent, false);
        context = parent.getContext();
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getName());
        holder.textViewProductPrice.setText(String.valueOf(product.getPrice()));

        // Load image using Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground) // Optional placeholder image
                .error(R.drawable.ic_launcher_foreground) // Optional error image
                .into(holder.imageViewProduct);

        holder.buttonEditProduct.setOnClickListener(v -> listener.onEditProduct(product));
        holder.buttonDeleteProduct.setOnClickListener(v -> listener.onDeleteProduct(product));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewProductName, textViewProductPrice;
        public ImageView imageViewProduct;
        public Button buttonEditProduct, buttonDeleteProduct;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            buttonEditProduct = itemView.findViewById(R.id.buttonEditProduct);
            buttonDeleteProduct = itemView.findViewById(R.id.buttonDeleteProduct);
        }
    }
}
