package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingmarco.myclientmodel.Adapters.ParcelableAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;
import com.kingmarco.myclientmodel.Threads.ImageThreads;

import java.util.ArrayList;


/** The fragment responsible to show the detail of the promotion*/
public class PromotionDetailsFragment extends Fragment {
    //TODO: A BUTTON TO ADD THE PROMOTION TO A NEW OR OLD CART
    private SetLabelName setLabelName;
    private ImageView ivPromotion;
    private TextView txtPromotionName, txtPromotionDate, txtPromotionPrice;
    private RecyclerView rvPromotedProducts;
    private Promotions promotions;
    private ParcelableAdapter productsAdapter;


    public PromotionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        Bundle data = getArguments();
        promotions = data.getParcelable("promotion");

        /** Create the parcelable adapter for the products in the purchased product of the promProduct class*/
        ArrayList<Parcelable> productsArrayList = new ArrayList<>();
        //productsArrayList.addAll(promotions.getProducts());
        productsAdapter = new ParcelableAdapter(getActivity(),this);
        productsAdapter.setProductActionId(R.id.action_promotionDetailsFragment_to_productDetailsFragment);
        productsAdapter.setDatabase(productsArrayList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_details, container, false);
        ivPromotion = view.findViewById(R.id.ivPromotion);
        txtPromotionName = view.findViewById(R.id.txtPromotionName);
        txtPromotionDate = view.findViewById(R.id.txtPromotionDate);
        txtPromotionPrice = view.findViewById(R.id.txtPromotionPrice);
        rvPromotedProducts = view.findViewById(R.id.rvPromotedProducts);

        /**Organize the information and set the adapter to the recycler view*/
        /*new ImageThreads(promotions.getUrl(),ivPromotion,getActivity()).start();
        txtPromotionName.setText(promotions.getPromoName());
        txtPromotionDate.setText(promotions.getFinishDate().toString());
        txtPromotionPrice.setText(promotions.getNewPrice());*/

        rvPromotedProducts.setAdapter(productsAdapter);
        rvPromotedProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        setLabelName.setLabelName("Detalles Promocion");
        // Inflate the layout for this fragment
        return view;
    }
}