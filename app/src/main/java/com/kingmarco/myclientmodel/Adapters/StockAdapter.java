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

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**Adapter to the Recycler View in the Home fragment, Promotion Fragment and Promotion Detail Fragment*/
public class StockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GetFireStoreDB {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private ArrayList<Parcelable> database;
    private final FragmentActivity fragmentActivity;
    private View contentView;
    private final Fragment fragment;
    private int actionId;

    public StockAdapter(FragmentActivity fragmentActivity,Fragment fragment) {
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;
        this.actionId = -1;
    }

    public void setDatabase(ArrayList<Parcelable> database) {
        this.database = database;
        notifyDataSetChanged();
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcelable_recicler_item,parent,false);
        return new ViewHolderParcelable(view);
    }

    /**Check if the object in the position is a Product or a PromoProduct*/
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderParcelable viewHolderParcelable = (ViewHolderParcelable) holder;
        if (database.get(position) instanceof Products) {
            withAProduct((Products) database.get(position), viewHolderParcelable);
        } else if (database.get(position) instanceof Promotions) {
            withAPPromotion((Promotions) database.get(position),viewHolderParcelable);
        }
    }

    /**Set the image, name, price and button action of the item in the recycler view*/
    private void withAProduct(Products product,ViewHolderParcelable holder){
        if(product.getUrl() != null){
            StorageReference gsReference = storage.getReferenceFromUrl(product.getUrl().get(0));
            GlideApp.with(fragment.getContext())
                    .load(gsReference)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
        }
        holder.txtParcelableName.setText(product.getName());
        String price = "$ "+ new DecimalFormat("###,###,###").format(product.getPrice())+" COP" ;
        holder.txtParcelablePrice.setText(price);
        if (actionId == -1){return;}
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("product",product);
                NavController nav = NavHostFragment.findNavController(fragment);
                nav.navigate(actionId,bundle);
            }
        });
    }

    /**Set the image, name, price and button action of the item in the recycler view*/
    private void withAPPromotion(Promotions promotions, ViewHolderParcelable holder){
        if(promotions.getUrl() != null){
            StorageReference gsReference = storage.getReferenceFromUrl(promotions.getUrl().get(0));
            GlideApp.with(fragment.getContext())
                    .load(gsReference)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
        }
        holder.txtParcelableName.setText(promotions.getName());
        String price = "$ "+ new DecimalFormat("###,###,###").format(promotions.getPrice())+" COP" ;
        holder.txtParcelablePrice.setText(price);
        if (actionId == -1){return;}
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = new Bundle();
                data.putParcelable("promotion", promotions);
                NavController nav = NavHostFragment.findNavController(fragment);
                nav.navigate(actionId,data);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (database == null){return 0;}
        return database.size();
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        if (contentView == null){return;}
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo, contentView, fragment.getContext(), fragmentActivity, false);
    }

    public class ViewHolderParcelable extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView imageView;
        private TextView txtParcelableName, txtParcelablePrice;

        public ViewHolderParcelable(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.parcelable_parent);
            imageView = itemView.findViewById(R.id.ivParcelable);
            txtParcelableName = itemView.findViewById(R.id.txtParcelableName);
            txtParcelablePrice = itemView.findViewById(R.id.txtParcelablePrice);
        }
    }
}
