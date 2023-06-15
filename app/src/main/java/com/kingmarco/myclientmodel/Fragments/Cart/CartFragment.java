package com.kingmarco.myclientmodel.Fragments.Cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.CartsAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.CartHolder;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.CartObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

/** The fragment that display a recycler view with the carts of the client*/
public class CartFragment extends Fragment implements CartObserver {
    
    private SetLabelName setLabelName;
    private RecyclerView rvCarts;
    private CartsAdapter cartsAdapter;

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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCarts = view.findViewById(R.id.rvCarts);
        rvCarts.setAdapter(cartsAdapter);
        rvCarts.setLayoutManager(new LinearLayoutManager(getContext()));

        setLabelName.setLabelName("Carrito");
        // Inflate the layout for this fragment
        return view;
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
        cartsAdapter.setDatabase(carts);
    }
}