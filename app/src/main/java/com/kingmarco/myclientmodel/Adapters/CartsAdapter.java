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

import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

/**Adapter to the Recycler View in the Cart fragment*/
public class CartsAdapter extends RecyclerView.Adapter<CartsAdapter.ViewHolder>{

    private ArrayList<Carts> database = new ArrayList<>();
    private FragmentActivity fragmentActivity;
    private Fragment fragment;
    private int actionId;

    public CartsAdapter(FragmentActivity fragmentActivity, Fragment fragment, int actionId) {
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;
        this.actionId = actionId;
    }

    public void setDatabase(ArrayList<Carts> database) {
        this.database = database;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carts_recicler_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**Set the text for the TextsView in the layout*/
        Carts cart = database.get(position);

        StringBuilder name = new StringBuilder();
        boolean hasImage = false;

        if(cart == null){return;}

        if(cart.getPurchasedProducts() == null){return;}

        /**Check if the purchased product is a class of Product or PromoProduct*/
        /*for(Parcelable parcelable: cart.getPurchasedProducts()){
            if(parcelable instanceof Products){
                if(isAProduct(holder,parcelable,name,hasImage)){
                    hasImage = true;
                }
            } else if (parcelable instanceof Promotions){
                if(isAPromotion(holder,parcelable,name,hasImage)) {
                    hasImage = true;
                }
            }
        }*/
        holder.txtProductName.setText(name);

        String date;
        if(cart.getArrivedDate() == null){
            date = "Lo compraste el " + cart.getPurchasedDate().toString();
        } else{
            date = "Te llego el " + cart.getArrivedDate().toString();
        }
        holder.txtDate.setText(date);

        holder.txtStatus.setText(cart.getStatus());

        /**Set the button lister to navigate to another fragment, with the details of the parcelable object*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController nav = NavHostFragment.findNavController(fragment);
                Bundle data = new Bundle();
                data.putParcelable("cart",cart);
                data.putString("cartName",name.toString());
                nav.navigate(actionId,data);
            }
        });
    }

    /**If is a product append the name to the name variable and load the image*/
    private boolean isAProduct(@NonNull ViewHolder holder, Parcelable product, StringBuilder name, boolean hasImage){
        Products cartProduct = (Products) product;

        if(cartProduct.getName() == null){return false;}
        name.append(cartProduct.getName()+", ");

        if(cartProduct.getUrl().get(0) == null || hasImage){return false;}
        //new ImageThreads(cartProduct.getUrl().get(0),holder.ivCart,fragmentActivity).start();
        return true;
    }

    /**If is a promotion append the name to the name variable and load the image*/
    private boolean isAPromotion(@NonNull ViewHolder holder, Parcelable promotion, StringBuilder name, boolean hasImage){
        Promotions cartPromo = (Promotions) promotion;

        if(cartPromo.getProducts() == null){return false;}
        /*for(Products product : cartPromo.getProducts()){
            name.append(product.getName()+", ");
        }

        if(cartPromo.getUrl() == null || hasImage){return false;}
        new ImageThreads(cartPromo.getUrl(),holder.ivCart,fragmentActivity).start();*/
        return true;
    }

    @Override
    public int getItemCount() {
        return database.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView ivCart;
        private TextView txtStatus, txtDate, txtProductName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cart_parent);
            ivCart = itemView.findViewById(R.id.ivCart);
            txtStatus = itemView.findViewById(R.id.txtCartStatus);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtProductName = itemView.findViewById(R.id.txtCartProducts);
        }
    }
}
