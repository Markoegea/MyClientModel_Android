package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.StockAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.R;

/**The fragment responsible to show the promotion products in a recycler view*/
public class PromotionFragment extends Fragment {

    private SetLabelName setLabelName;

    private RecyclerView rvPromProducts;

    private StockAdapter promotionAdapter;

    public PromotionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();

        /**Set the promotion adapter*/
        promotionAdapter = new StockAdapter(getActivity(),this,StockAdapter.VIEW_TYPE_PARCELABLE);
        promotionAdapter.setActionId(R.id.action_promotionFragment_to_promotionDetailsFragment);
        //promotionAdapter.setDatabase(promoProduct);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        rvPromProducts = view.findViewById(R.id.rvPromProducts);
        rvPromProducts.setAdapter(promotionAdapter);
        rvPromProducts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setLabelName.setLabelName("Promociones");

        // Inflate the layout for this fragment
        return view;
    }
}