package com.kingmarco.myclientmodel.Fragments.Cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingmarco.myclientmodel.Adapters.ParcelableAdapter;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;


/** The Fragment responsible to show the the detail of the cart*/
public class CartDetailsFragment extends Fragment {

    private SetLabelName setLabelName;
    private Carts cart;
    private TextView txtCartStatus, txtCartOwner, txtCartProducts,txtPurchaseDate, txtArriveDate
            ,txtCartPrice,txtCartDirection;
    private RecyclerView rvCartProducts;
    private String cartName;
    private ParcelableAdapter parcelableAdapter;

    public CartDetailsFragment() {
        // Required empty public constructor
    }

    /**Get the data and create the parcelable adapter*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        cart = getArguments().getParcelable("cart");
        cartName = getArguments().getString("cartName");

        parcelableAdapter = new ParcelableAdapter(getActivity(),this);
        parcelableAdapter.setPromoActionId(R.id.action_cartDetailsFragment_to_promotionDetailsFragment);
        parcelableAdapter.setProductActionId(R.id.action_cartDetailsFragment_to_productDetailsFragment);
        parcelableAdapter.setDatabase(cart.getPurchasedProducts());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_details, container, false);

        setViews(view);

        setLabelName.setLabelName("Detalles Compra");
        // Inflate the layout for this fragment
        return view;
    }

    /**Find the object by id and set them to the variables*/
    private void setViews(View view){
        txtCartStatus = view.findViewById(R.id.txtCartStatus);
        txtCartOwner = view.findViewById(R.id.txtCartOwner);
        txtCartProducts = view.findViewById(R.id.txtCartProducts);
        txtPurchaseDate = view.findViewById(R.id.txtPurchaseDate);
        txtArriveDate = view.findViewById(R.id.txtArriveDate);
        txtCartPrice = view.findViewById(R.id.txtCartPrice);
        txtCartDirection = view.findViewById(R.id.txtCartDirection);

        rvCartProducts = view.findViewById(R.id.rvCartProducts);

        organizeInformation();
    }

    /**Add the text to the Textview, and the adapter to the recicler view*/
    private void organizeInformation(){
        txtCartStatus.setText(cart.getStatus());
        txtCartOwner.setText(cart.getOwner());
        txtCartProducts.setText(cartName);
        txtPurchaseDate.setText(cart.getPurchasedDate().toString());
        //TODO: CHECK IF THE ARRIVE DATE IS AVAILABLE AND USE IT
        //txtArriveDate.setText(cart.getArrivedDate().toString());
        txtCartPrice.setText(String.valueOf(cart.getPrice()));
        txtCartDirection.setText(cart.getDirection());

        rvCartProducts.setAdapter(parcelableAdapter);
        rvCartProducts.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}