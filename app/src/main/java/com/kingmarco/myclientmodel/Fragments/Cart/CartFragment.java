package com.kingmarco.myclientmodel.Fragments.Cart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.CartsAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.R;

/** The fragment that display a recycler view with the carts of the client*/
public class CartFragment extends Fragment {
    
    private SetLabelName setLabelName;
    private RecyclerView rvCarts;
    private CartsAdapter cartsAdapter;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: Testing Purposes delete it and recover this data from Firebase with authentication
        setLabelName = (SetLabelName) getContext();

        /**Create the adapter and set the database*/
        cartsAdapter = new CartsAdapter(getActivity(),this,R.id.action_cartFragment_to_cartDetailsFragment);
        //cartsAdapter.setDatabase(carts);
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
}