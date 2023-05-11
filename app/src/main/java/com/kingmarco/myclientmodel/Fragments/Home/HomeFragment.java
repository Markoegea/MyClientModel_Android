package com.kingmarco.myclientmodel.Fragments.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.ParcelableAdapter;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;
import java.util.Arrays;

/** The fragment responsible for show the recycler with the products*/
public class HomeFragment extends Fragment {

    private SetLabelName setLabelName;
    private RecyclerView rvProducts;
    private ParcelableAdapter parcelableAdapter;
    private ArrayList<Parcelable> products = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLabelName = (SetLabelName) getContext();


        parcelableAdapter = new ParcelableAdapter(this.getActivity(),this);
        parcelableAdapter.setProductActionId(R.id.action_homeFragment_to_productDetailsFragment);
        parcelableAdapter.setDatabase(products);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvProducts = view.findViewById(R.id.rvProducts);
        rvProducts.setAdapter(parcelableAdapter);
        rvProducts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        setLabelName.setLabelName("Home");
        // Inflate the layout for this fragment
        return view;
    }
}