package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

/**The fragment responsible to show the promotion products in a recycler view*/
public class PromotionFragment extends Fragment implements ClientObserver {

    private SetLabelName setLabelName;
    private RecyclerView rvPromProducts;
    private StockAdapter stockAdapter;
    private ListenerRegistration listenerRegistration;

    public PromotionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        /**Set the promotion adapter*/
        stockAdapter = new StockAdapter(getActivity(),this);
        stockAdapter.setActionId(R.id.action_promotionFragment_to_promotionDetailsFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        stockAdapter.setContentView(view);

        rvPromProducts = view.findViewById(R.id.rvPromProducts);
        rvPromProducts.setAdapter(stockAdapter);
        rvPromProducts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setLabelName.setLabelName("Promociones");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ClientHolder.addObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        listenerRegistration = SyncFireStoreDB.newListenerRegistration(stockAdapter, Promotions.class,"Promociones");
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
        ClientHolder.removeObserver(this);
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClientHolder.removeObserver(this);
        if(listenerRegistration == null){return;}
        listenerRegistration.remove();
    }

    @Override
    public void onVariableChange(Clients client) {
        if (listenerRegistration != null){return;}
        listenerRegistration = SyncFireStoreDB.newListenerRegistration(stockAdapter, Promotions.class,"Promociones");
    }
}