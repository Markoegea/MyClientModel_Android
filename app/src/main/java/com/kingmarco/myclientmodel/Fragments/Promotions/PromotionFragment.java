package com.kingmarco.myclientmodel.Fragments.Promotions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingmarco.myclientmodel.Adapters.ParcelableAdapter;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**The fragment responsible to show the promotion products in a recycler view*/
public class PromotionFragment extends Fragment {

    private SetLabelName setLabelName;

    private RecyclerView rvPromProducts;

    private ParcelableAdapter promotionAdapter;

    public PromotionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();


        //TODO: FOR TESTING PURPOSES, USE THE FIRESTORE INFORMATION
        ArrayList<Parcelable> promoProduct = new ArrayList<>();
        String[] category = new String[]{"category"};
        ArrayList<String> list = new ArrayList<>(){{ add("https://firebasestorage.googleapis.com/v0/b/first-model-client.appspot.com/o/Transform_this_image_into_a_dreamlike__fantastical_landscape__Visual_Novel__Colorful__by_Artgerm__En_Seed-1_Steps-30_Guidance-7.5%20(1).jpeg?alt=media&token=cefc0786-f516-426d-897d-f3cee5763bf5");}};
        promoProduct.add(new Promotions(list.get(0),"Two Bags for 15000",
                new ArrayList<>(){{ add(new Products(1,10000,4,"Bag","A Super Bag", list,null,new ArrayList<>(Arrays.asList(category))));}},
                15000, LocalDate.of(2023,4,1)));

        list.add("https://firebasestorage.googleapis.com/v0/b/first-model-client.appspot.com/o/Remove_Use_these_settingsEpic_Battle_field__with_dragons__wizards__soldiers_with_mountains__Digital__Seed-0_Steps-30_Guidance-7.5.jpeg?alt=media&token=d8947dff-5ac3-4d7b-81f4-baa0d98aa926");
        promoProduct.add(new Promotions(list.get(1),"Three skirts for 50000",
                new ArrayList<>(){{ add(new Products(2,20000,2,"Skirt", "A short skirt",list,null,new ArrayList<>(Arrays.asList(category))));}},
                50000,LocalDate.of(2023,4,1)));

        list.add("https://firebasestorage.googleapis.com/v0/b/first-model-client.appspot.com/o/Anime_dragon_with_a_fierce_expression__breathing_out_smoke___Anime__Infrared__Unreal_Engine_Seed-469113_Steps-25_Guidance-7.5.jpeg?alt=media&token=c6b7813a-9926-4f4e-8a41-2ddc71cdc1f3");
        promoProduct.add(new Promotions(list.get(2), "A skirt and a T-shirt for 42500",
                new ArrayList<>(){{ add(new Products(3,30000, 1,"T-shirt","A long t-shirt",list,null,new ArrayList<>(Arrays.asList(category))));
                                    add(new Products(2,20000,2,"Skirt", "A short skirt",list,null,new ArrayList<>(Arrays.asList(category))));}},
                42500,LocalDate.of(2023,4,1)));

        /**Set the promotion adapter*/
        promotionAdapter = new ParcelableAdapter(getActivity(),this);
        promotionAdapter.setPromoActionId(R.id.action_promotionFragment_to_promotionDetailsFragment);
        promotionAdapter.setDatabase(promoProduct);
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