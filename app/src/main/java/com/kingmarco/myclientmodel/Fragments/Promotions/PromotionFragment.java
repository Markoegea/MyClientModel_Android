package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.StockAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Classes.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.StockObserver;
import com.kingmarco.myclientmodel.R;

/**The fragment responsible to show the promotion products in a recycler view*/
public class PromotionFragment extends Fragment implements StockObserver {

    private SetLabelName setLabelName;
    private RecyclerView rvPromProducts;
    private StockAdapter stockAdapter;

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
        onVariableChange();
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
    public void onResume() {
        super.onResume();
        StockHolder.addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        StockHolder.removeObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        StockHolder.removeObserver(this);
    }

    @Override
    public void onVariableChange() {
        stockAdapter.setDatabase(StockHolder.getList(StockType.PROMOTION));
    }
}