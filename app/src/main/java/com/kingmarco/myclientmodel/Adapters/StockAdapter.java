package com.kingmarco.myclientmodel.Adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.FragmentAnimation;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.CartStatus;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetRealtimeDB;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.POJOs.Stock;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**Adapter to the Recycler View in the Home fragment, Promotion Fragment and Promotion Detail Fragment*/
public class StockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GetFireStoreDB {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private List<Stock> database;
    private final FragmentActivity fragmentActivity;
    private final Fragment fragment;
    private View contentView;
    private int actionId;
    private Carts carts;

    public StockAdapter(FragmentActivity fragmentActivity,Fragment fragment) {
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;
        this.actionId = -1;
    }

    public void setDatabase(List<Stock> database) {
        this.database = database;
        notifyDataSetChanged();
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public void setCarts(Carts carts) {
        this.carts = carts;
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
        organizeData(database.get(position), viewHolderParcelable);
    }

    private void organizeData(Stock stock, ViewHolderParcelable holder){
        if(stock.getUrl() != null){
            StorageReference gsReference = storage.getReferenceFromUrl(stock.getUrl().get(0));
            GlideApp.with(fragment.getContext())
                    .load(gsReference)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.imageView);
        }
        holder.txtParcelableName.setText(stock.getName());
        String price = "$ "+ new DecimalFormat("###,###,###").format(stock.getPrice())+" COP" ;
        holder.txtParcelablePrice.setText(price);
        if (actionId != -1){
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("stock",stock.getId());
                    NavController nav = NavHostFragment.findNavController(fragment);
                    nav.navigate(actionId,bundle, FragmentAnimation.navigateBehavior());
                }
            });
        }
        if (carts == null){return;}
        if (carts.getStatus() != CartStatus.DRAFT){return;}
        if (stock instanceof Products){
            setCartBehavior(carts,stock,StockType.PRODUCT,holder);
        } else if (stock instanceof Promotions){
            setCartBehavior(carts,stock,StockType.PROMOTION,holder);
        }
    }

    private void setCartBehavior(Carts carts, Stock stock, StockType stockType, ViewHolderParcelable holder){
        GetRealtimeDB getRealtimeDB = (GetRealtimeDB) fragment;
        if (carts.getPurchasedItemsId() == null){return;}
        ArrayList<Long> stockId = carts.getPurchasedItemsId().get(stockType.name());
        if (stockId == null){return;}

        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.btnDelete.setVisibility(View.VISIBLE);
        int quantity = 0;
        for (Long id : stockId){
            if (id.longValue() == stock.getId().longValue()){
                quantity += 1;
            }
        }
        holder.txtParcelableQuantity.setText(String.valueOf(quantity));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Long> stockId = carts.getPurchasedItemsId().get(stockType.name());
                if (stockId == null){return;}
                for (Long id : stockId){
                    if (id.longValue() == stock.getId().longValue()){
                        stockId.remove(id);
                        carts.setTotalPrice(carts.getTotalPrice() - stock.getPrice());
                        break;
                    }
                }
                SyncRealtimeDB.updateCartRequest(carts,getRealtimeDB);
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
        private TextView txtParcelableName, txtParcelablePrice, txtParcelableQuantity;
        private Button btnDelete;
        private LinearLayout linearLayout;


        public ViewHolderParcelable(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.parcelable_parent);
            imageView = itemView.findViewById(R.id.ivParcelable);
            txtParcelableName = itemView.findViewById(R.id.txtParcelableName);
            txtParcelablePrice = itemView.findViewById(R.id.txtParcelablePrice);
            txtParcelableQuantity = itemView.findViewById(R.id.txtParcelableQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            linearLayout = itemView.findViewById(R.id.ll1);
        }
    }
}
