package com.kingmarco.myclientmodel.Adapters;

import android.os.Bundle;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Enums.CartStatus;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Stock;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

/**Adapter to the Recycler View in the Cart fragment*/
public class CartsAdapter extends RecyclerView.Adapter<CartsAdapter.ViewHolder>{

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private ArrayList<Carts> database = new ArrayList<>();
    private FragmentActivity fragmentActivity;
    private Fragment fragment;
    private int actionId;

    public CartsAdapter(FragmentActivity fragmentActivity, Fragment fragment) {
        this.fragmentActivity = fragmentActivity;
        this.fragment = fragment;
        this.actionId = -1;
    }

    public void setDatabase(ArrayList<Carts> database) {
        this.database = database;
        notifyDataSetChanged();
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carts_recicler_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**Set the text for the TextsView in the layout*/
        Carts cart = database.get(position);

        StringBuilder name = new StringBuilder();
        boolean hasImage = false;

        if(cart == null){return;}

        if(cart.getPurchasedItemsId() == null){return;}

        holder.txtStatus.setText(getSpanishStatus(cart.getStatus()));

        String date;
        if(cart.getArrivedDate() == null){
            if (cart.getPurchasedDate() == null){
                date = "En Espera";
            } else {
                date = "Lo compraste el " + cart.getPurchasedDate().toString();
            }
        } else {
            date = "Te llego el " + cart.getArrivedDate().toString();
        }
        holder.txtDate.setText(date);

        iterateOverStock(holder,StockType.PRODUCT,cart,name,hasImage);
        iterateOverStock(holder,StockType.PROMOTION,cart,name,hasImage);

        holder.txtProductName.setText(name);

        /**Set the button lister to navigate to another fragment, with the details of the parcelable object*/
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController nav = NavHostFragment.findNavController(fragment);
                Bundle data = new Bundle();
                data.putLong("cartID",cart.getId());
                data.putString("cartName",name.toString());
                nav.navigate(actionId,data);
            }
        });
    }

    private void iterateOverStock(@NonNull ViewHolder holder, StockType stockType, Carts cart, StringBuilder name, boolean hasImage){
        ArrayList<Long> stockId = cart.getPurchasedItemsId().get(stockType.name());
        if (stockId != null){
            for(Long id: stockId){
                Stock stock = StockHolder.getSingleStockItem(stockType,id);
                if (stock == null){return;}
                if(!getNameAndPhoto(holder,stock,name,hasImage)){continue;}
                hasImage = true;
            }
        }
    }

    private boolean getNameAndPhoto(@NonNull ViewHolder holder, Stock stock, StringBuilder name, boolean hasImage){
        if(stock.getName() == null){return false;}
        name.append(stock.getName()).append(", ");

        if(stock.getUrl().get(0) == null || hasImage){return false;}
        StorageReference gsReference = storage.getReferenceFromUrl(stock.getUrl().get(0));
        GlideApp.with(fragment.getContext())
                .load(gsReference)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.ivCart);
        return true;
    }

    private String getSpanishStatus(CartStatus cartStatus){
        switch (cartStatus){
            case SENT:
                return "Enviado";
            case FAILED:
                return "Envio Fallido";
            case CANCELED:
                return "Envio Cancelado";
            case RECEIVED:
                return "Envio Recibido";
            case RETURNED:
                return "Envio en Regresado";
            case DELIVERED:
                return "Envio en Camino";
            case PROCESSED:
                return "Envio en Proceso";
            default:
                return "Borrador";
        }
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
