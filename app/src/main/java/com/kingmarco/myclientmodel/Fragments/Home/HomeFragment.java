package com.kingmarco.myclientmodel.Fragments.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.ListenerRegistration;
import com.kingmarco.myclientmodel.Adapters.StockAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;

/** The fragment responsible for show the recycler with the products*/
public class HomeFragment extends Fragment implements ClientObserver {

    private SetLabelName setLabelName;
    private RecyclerView rvProducts;
    private StockAdapter stockAdapter;
    private ListenerRegistration listenerRegistration;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        ClientHolder.addObserver(this);
        stockAdapter = new StockAdapter(getActivity(),this,StockAdapter.VIEW_TYPE_PARCELABLE);
        stockAdapter.setActionId(R.id.action_homeFragment_to_productDetailsFragment);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        stockAdapter.setContentView(view);

        rvProducts = view.findViewById(R.id.rvProducts);
        rvProducts.setAdapter(stockAdapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setLabelName.setLabelName("Productos");
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        listenerRegistration = SyncFireStoreDB.newListenerRegistration(stockAdapter, Products.class,"Productos");
    }

    @Override
    public void onPause() {
        super.onPause();
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onVariableChange(Clients client) {
        if (listenerRegistration != null){return;}
        listenerRegistration = SyncFireStoreDB.newListenerRegistration(stockAdapter,Products.class,"Productos");
    }
}