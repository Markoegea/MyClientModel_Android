package com.kingmarco.myclientmodel.Adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;
import com.kingmarco.myclientmodel.Threads.ImageThreads;

import java.util.ArrayList;

/**Adapter to the Recycler View in the Home fragment, Promotion Fragment and Promotion Detail Fragment*/
public class ParcelableAdapter extends RecyclerView.Adapter<ParcelableAdapter.ViewHolder> {

    private ArrayList<Parcelable> database = new ArrayList<>();
    private FragmentActivity fragmentActivity;
    private Fragment fragment;
    private int promoActionId;
    private int productActionId;

    public ParcelableAdapter(FragmentActivity fragmentActivity, Fragment fragment) {
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;
        productActionId = -1;
        promoActionId = -1;
    }

    public void setDatabase(ArrayList<Parcelable> database) {
        this.database = database;
        notifyDataSetChanged();
    }

    public void setPromoActionId(int promoActionId) {
        this.promoActionId = promoActionId;
    }

    public void setProductActionId(int productActionId) {
        this.productActionId = productActionId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcelable_recicler_item,parent,false);
        ParcelableAdapter.ViewHolder viewHolder = new ParcelableAdapter.ViewHolder(view);
        return viewHolder;
    }

    /**Check if the object in the position is a Product or a PromoProduct*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (database.get(position) instanceof Products){
            withAProduct((Products) database.get(position),holder);
        } else if (database.get(position) instanceof Promotions) {
            withAPPromoProduct((Promotions) database.get(position),holder);
        }
    }

    /**Set the image, name, price and button action of the item in the recycler view*/
    private void withAProduct(Products product,ViewHolder holder){
        if(product.getUrl() != null){
            new ImageThreads(product.getUrl().get(0),holder.imageView,fragmentActivity).start();
        }
        holder.txtParcelableName.setText(product.getName());
        holder.txtParcelablePrice.setText("$ "+product.getPrice()+" COP");
        if (productActionId == -1){return;}
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("product",product);
                NavController nav = NavHostFragment.findNavController(fragment);
                nav.navigate(productActionId,bundle);
            }
        });
    }

    /**Set the image, name, price and button action of the item in the recycler view*/
    private void withAPPromoProduct(Promotions promotions, ViewHolder holder){
        if(promotions.getUrl() != null){
            //new ImageThreads(promotions.getUrl(),holder.imageView,fragmentActivity).start();
        }
        //holder.txtParcelableName.setText(promotions.getPromoName());
        //holder.txtParcelablePrice.setText(promotions.getNewPrice());
        if (promoActionId == -1){return;}
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putParcelable("promotion", promotions);
                NavController nav = NavHostFragment.findNavController(fragment);
                nav.navigate(promoActionId,data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return database.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView imageView;
        private TextView txtParcelableName, txtParcelablePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.parcelable_parent);
            imageView = itemView.findViewById(R.id.ivParcelable);
            txtParcelableName = itemView.findViewById(R.id.txtParcelableName);
            txtParcelablePrice = itemView.findViewById(R.id.txtParcelablePrice);

        }
    }
}
