package com.kingmarco.myclientmodel.Fragments.Cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kingmarco.myclientmodel.Adapters.StockAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.CartHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.TimestampDeserializer;
import com.kingmarco.myclientmodel.Auxiliary.Enums.CartStatus;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.CartObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.POJOs.Stock;
import com.kingmarco.myclientmodel.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


/** The Fragment responsible to show the the detail of the cart*/
//TODO: When the user upload the cart, the buttons to erase and upload the cart have to disappear
public class CartDetailsFragment extends Fragment implements GetFireStoreDB, CartObserver {

    private SetLabelName setLabelName;
    private Carts cart;
    private View contentView;
    private TextView txtCartStatus, txtCartOwner, txtCartProducts,txtCartDateType, txtDate
            ,txtCartPrice,txtCartDirection;
    private FloatingActionButton btnUpload, btnDelete;
    private RecyclerView rvCartProducts;
    private StockAdapter stockAdapter;

    public CartDetailsFragment() {
        // Required empty public constructor
    }

    /**Get the data and create the parcelable adapter*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        if (getArguments() == null){return;}
        long cartID = getArguments().getLong("cartID");
        cart = CartHolder.getSingleCartItem(cartID);

        stockAdapter = new StockAdapter(getActivity(),this);
        stockAdapter.setCarts(cart);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_cart_details, container, false);
        setViews(contentView);
        onVariableChange(null);
        setLabelName.setLabelName("Detalles Compra");
        // Inflate the layout for this fragment
        return contentView;
    }

    /**Find the object by id and set them to the variables*/
    private void setViews(View view){
        txtCartStatus = view.findViewById(R.id.txtCartStatus);
        txtCartOwner = view.findViewById(R.id.txtCartOwner);
        txtCartProducts = view.findViewById(R.id.txtCartProducts);
        txtCartDateType = view.findViewById(R.id.txtCartDateType);
        txtDate = view.findViewById(R.id.txtDate);
        txtCartPrice = view.findViewById(R.id.txtCartPrice);
        txtCartDirection = view.findViewById(R.id.txtCartDirection);

        rvCartProducts = view.findViewById(R.id.rvCartProducts);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnDelete = view.findViewById(R.id.btnDelete);
    }

    /**Add the text to the Textview, and the adapter to the recicler view*/
    private void organizeInformation(){
        txtCartStatus.setText(cart.getStatus().toString());
        txtCartOwner.setText(getCartName());
        txtCartProducts.setText(getProductsName());
        txtCartDateType.setText(getCartDateType());
        txtDate.setText(getDate());
        String price = "$ "+ new DecimalFormat("###,###,###").format(cart.getTotalPrice())+" COP" ;
        txtCartPrice.setText(price);
        txtCartDirection.setText(getDirection());

        rvCartProducts.setAdapter(stockAdapter);
        rvCartProducts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setClickListeners(){
        if (cart.getStatus() != CartStatus.DRAFT){
            btnUpload.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            return;
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncFireStoreDB.deleteCartRequest(cart,CartDetailsFragment.this,CartDetailsFragment.this);
                if(getActivity() == null){return;}
                getActivity().onBackPressed();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.setStatus(CartStatus.SENT);
                cart.setPurchasedDate(TimestampDeserializer.generateNewTimestamp());
                SyncFireStoreDB.updateCartRequest(cart,CartDetailsFragment.this);
            }
        });
    }

    private String getCartName(){
        if (ClientHolder.getYouClient() == null){return "";}
        Clients clients = ClientHolder.getYouClient();
        return clients.getName() +" " + clients.getLastName();
    }

    private String getProductsName(){
        StringBuilder name = new StringBuilder();

        ArrayList<Long> productsId = cart.getPurchasedItemsId().get(StockType.PRODUCT.name());
        if (productsId != null){
            for(Long id: productsId){
                Stock stock = StockHolder.getSingleStockItem(StockType.PRODUCT,id);
                if (stock == null){continue;}
                name.append(stock.getName()).append(", ");
            }
        }

        ArrayList<Long> promotionsId = cart.getPurchasedItemsId().get(StockType.PROMOTION.name());
        if (promotionsId != null){
            for(Long id: promotionsId){
                Stock stock = StockHolder.getSingleStockItem(StockType.PROMOTION,id);
                if (stock == null){continue;}
                name.append(stock.getName()).append(", ");
            }
        }

        return name.toString();
    }

    private String getCartDateType(){
        if (cart.getArrivedDate() != null){
            return "Te llego el: ";
        }
        if (cart.getPurchasedDate() != null){
            return "Lo enviaste el: ";
        }
        return "Aun no lo has enviado, que esperas?";
    }

    private String getDate(){
        if (cart.getArrivedDate() != null){
            return cart.getArrivedDate().toString();
        }
        if (cart.getPurchasedDate() != null){
            return cart.getPurchasedDate().toString();
        }
        return "";
    }

    private String getDirection(){
        if (ClientHolder.getYouClient() == null){return "";}
        Clients clients = ClientHolder.getYouClient();
        if(clients.getDirections() == null){return "No tienes una direccion";}
        return clients.getDirections();
    }

    private void setAdapterDatabase(){
        ArrayList<Stock> cartStock = new ArrayList<>();

        ArrayList<Long> productsId = cart.getPurchasedItemsId().get(StockType.PRODUCT.name());

        if (productsId != null){
            for(Long id: productsId){
                Stock stock = StockHolder.getSingleStockItem(StockType.PRODUCT,id);
                if (stock == null){continue;}
                if (cartStock.contains(stock)){continue;}
                cartStock.add(stock);
            }
        }

        ArrayList<Long> promotionsId = cart.getPurchasedItemsId().get(StockType.PROMOTION.name());
        if (promotionsId != null){
            for(Long id: promotionsId){
                Stock stock = StockHolder.getSingleStockItem(StockType.PROMOTION,id);
                if (stock == null){continue;}
                if (cartStock.contains(stock)){continue;}
                cartStock.add(stock);
            }
        }

        stockAdapter.setDatabase(cartStock);
    }

    private boolean isEmptyCart(){
        if (cart.getPurchasedItemsId() == null){
            return true;
        }
        ArrayList<Long> productsId = cart.getPurchasedItemsId().get(StockType.PRODUCT.name());
        ArrayList<Long> promotionsId = cart.getPurchasedItemsId().get(StockType.PROMOTION.name());
        if (productsId == null || promotionsId == null){
            return true;
        }
        if (productsId.isEmpty() && promotionsId.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        CartHolder.addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        CartHolder.removeObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        CartHolder.removeObserver(this);
    }

    @Override
    public void onVariableChange(ArrayList<Carts> carts) {
        if (isEmptyCart()){
            CartHolder.removeObserver(this);
            SyncFireStoreDB.deleteCartRequest(cart,CartDetailsFragment.this,CartDetailsFragment.this);
            if(getActivity() == null){return;}
            getActivity().onBackPressed();
            return;
        }
        organizeInformation();
        setClickListeners();
        setAdapterDatabase();
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),false);
    }

}