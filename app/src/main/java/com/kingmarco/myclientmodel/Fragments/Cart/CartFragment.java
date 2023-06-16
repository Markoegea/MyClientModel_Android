package com.kingmarco.myclientmodel.Fragments.Cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.kingmarco.myclientmodel.Adapters.CartsAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.CartHolder;
import com.kingmarco.myclientmodel.Auxiliary.Enums.CartStatus;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.CartObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

/** The fragment that display a recycler view with the carts of the client*/
public class CartFragment extends Fragment implements CartObserver {
    
    private SetLabelName setLabelName;
    private View contentView;
    private RecyclerView rvCarts;
    private CartsAdapter cartsAdapter;
    private Spinner spStatusSearchBar;
    private CartStatus cartStatus;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        /**Create the adapter and set the database*/
        cartsAdapter = new CartsAdapter(getActivity(),this);
        cartsAdapter.setActionId(R.id.action_cartFragment_to_cartDetailsFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_cart, container, false);
        setViews(contentView);
        setInformation();
        setLabelName.setLabelName("Carrito");
        // Inflate the layout for this fragment
        return contentView;
    }

    private void setViews(View view){
        spStatusSearchBar = view.findViewById(R.id.spStatusSearchBar);
        rvCarts = view.findViewById(R.id.rvCarts);
        rvCarts.setAdapter(cartsAdapter);
        rvCarts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setInformation(){
        CartStatus[] cartStatuses = CartStatus.values();
        ArrayAdapter<CartStatus> adapter = new ArrayAdapter<>(getContext(),
                R.layout.cart_status_spinner_item,
                cartStatuses);
        spStatusSearchBar.setAdapter(adapter);
        spStatusSearchBar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                cartStatus = (CartStatus) item;
                onVariableChange(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        cartsAdapter.setDatabase(CartHolder.getCartList(cartStatus));
    }
}